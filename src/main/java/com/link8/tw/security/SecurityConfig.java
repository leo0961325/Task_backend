package com.link8.tw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.link8.tw.controller.advice.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Configuration
    @Order(1)
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    public static class ApiConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        AclAuthProvider aclAuthProvider;

        private ObjectMapper mapper = new ObjectMapper();

        /**
         * 初始化 security 控制規則及認證機制配置檔
         *
         * @param http
         * @throws Exception
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            /**
             *   headers():添加 Security HTTP Response Headers , frameOptions():設定iframe設定 , sameOrigin():同源政策
             */
            http.headers().frameOptions().sameOrigin();
            /**
             *   cors():允許跨域訪問 , antMatcher():設定HttpSecurity套用路徑
             */
            http.cors().and().antMatcher("/api/**")

                    /**
                     *   authorizeRequests():設定url權限
                     */
                    .authorizeRequests()

                    /**
                     *   antMatchers():Apache Ant路徑
                     */
                    .antMatchers("/api/**")

                    /**
                     *   authenticated():必須具有權限
                     */
                    .authenticated()
                    .and()

                    /**
                     *   http.requestCache().requestCache(new NullRequestCache()):spring security 提供了NullRequestCache， 该类实现了 RequestCache 接口，但是没有任何操作。
                     *   https://blog.coding.net/blog/Explore-the-cache-request-of-Security-Spring
                     */
                    .requestCache()
                    .requestCache(new NullRequestCache())
                    .and()

                    /**
                     *   logout():登出設定 , logoutUrl():設定登出url , deleteCookies():清除cookies "JSESSIONID","XSRF-TOKEN" 為cookies的名字
                     */
                    .logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID", "XSRF-TOKEN")

                    /**
                     *   logoutSuccessHandler():成功登出後續處理 , 也可以 new LogoutSuccessHandler() 然後在實作方法 onLogoutSuccess
                     */
                    .logoutSuccessHandler((HttpServletRequest var1, HttpServletResponse response, Authentication var3) -> {
                        CommonResponse logoutOk = new CommonResponse(true, null, "logout ok");
                        response.setHeader("Content-type", "application/json;charset=UTF-8");
                        response.getWriter().print(mapper.writeValueAsString(logoutOk));
                    });

            /**
             *   ignoringAntMatchers():設定忽略的url
             */
            http.csrf().ignoringAntMatchers("/api/login*");
            http.csrf().ignoringAntMatchers("/api/logout*");

            /**
             *   csrfTokenRepository():建立CsrfToken 工廠
             */
            http.csrf().csrfTokenRepository(new CookieCsrfTokenRepository());

//            單元測試用
//            http.csrf().disable();

            /**
             *   addFilterAt():增加指定的FilterClass
             */
            http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);

            /**
             *   exceptionHandling():權限例外處理器
             */
            http.exceptionHandling()

                    /**
                     *   authenticationEntryPoint():處理匿名用户(未登入)訪問無權限資源時的異常
                     *   等於 new AuthenticationEntryPoint() 然後在實作方法 commence
                     */
//                    .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                        @Override
//                        public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//                            CommonResponse mustLogin = new CommonResponse(AuthenticationErrorCode.MUST_LOGIN, exception.getMessage());
//                            response.setStatus(401);
//                            response.setHeader("Content-type", "application/json;charset=UTF-8");
//                            response.getWriter().print(mapper.writeValueAsString(mustLogin));
//                        }
//                    })
                    .authenticationEntryPoint((request, response, exception) -> {
                        CommonResponse mustLogin = new CommonResponse(false, exception.getMessage());
                        response.setStatus(401);
                        response.setHeader("Content-type", "application/json;charset=UTF-8");
                        response.getWriter().print(mapper.writeValueAsString(mustLogin));
                    })

                    /**
                     *   accessDeniedHandler():處理認證用户訪問無權限資源時的異常
                     *   等於 new AccessDeniedHandler () 然後在實作方法 commence
                     */
//                    .accessDeniedHandler(new AccessDeniedHandler() {
//                        @Override
//                        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//                            CommonResponse permission_denied = new CommonResponse(AuthenticationErrorCode.PERMISSION_DENIED, accessDeniedException.getMessage());
//                            response.setStatus(403);
//                            response.setHeader("Content-type", "application/json;charset=UTF-8");
//                            response.getWriter().print(mapper.writeValueAsString(permission_denied));
//                        }
//                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        CommonResponse permission_denied = new CommonResponse(false, accessDeniedException.getMessage());
                        response.setStatus(403);
                        response.setHeader("Content-type", "application/json;charset=UTF-8");
                        response.getWriter().print(mapper.writeValueAsString(permission_denied));
                    });
        }

        /**
         * 設定 AuthenticationManagerBuilder 配置檔
         *
         * @param auth
         * @throws Exception
         */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            /**
             *   authenticationProvider():設定驗證器
             */
            auth.authenticationProvider(aclAuthProvider);
        }

        @Bean
        public LoginFilter loginFilter() throws Exception {
            LoginFilter filter = new LoginFilter("/api/login");
            filter.setAuthenticationManager(authenticationManager());
            return filter;
        }
    }
}
