package com.link8.tw.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.link8.tw.controller.advice.CommonResponse;
import com.link8.tw.controller.advice.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper mapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);

    public LoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        this.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                LoginResponse response = new LoginResponse(true, authentication.getPrincipal(), getXsfToken(httpServletRequest, httpServletResponse));
                httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
                httpServletResponse.getWriter().println(mapper.writeValueAsString(response));
                HttpSession session = httpServletRequest.getSession();
                session.setMaxInactiveInterval(-1);
            }

        });
        this.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                CommonResponse response = new CommonResponse(false, e.getMessage());
                httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
                httpServletResponse.setStatus(400);
                httpServletResponse.getWriter().println(mapper.writeValueAsString(response));
            }
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (httpServletRequest.getMethod().equals("POST") && httpServletRequest.getContentType().startsWith("application/json")) {
            JsonNode root = mapper.readTree(httpServletRequest.getInputStream());
            return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(root.get("account").asText(), root.get("password").asText()));
        } else {
            throw new BadCredentialsException("method not support");
        }

    }

    private String getXsfToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String result = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("XSRF-TOKEN")) {
                    result = cookie.getValue();
                    break;
                }
            }
        }
        if (result == null) {
            Collection<String> headers = httpServletResponse.getHeaders("Set-Cookie");
            for (String header : headers) {
                if (header.contains("XSRF-TOKEN")) {
                    result = header.substring(header.indexOf("=") + 1, header.indexOf(";"));
                    break;
                }
            }
        }

        return result;
    }

}
