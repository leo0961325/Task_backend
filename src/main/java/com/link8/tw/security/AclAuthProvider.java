package com.link8.tw.security;

import com.link8.tw.model.User;
import com.link8.tw.repository.UserRepository;
import com.tgfc.acl.request.LoginRequest;
import com.tgfc.acl.response.LoginVerifyGeneralResponse;
import com.tgfc.acl.service.AclService;
import com.tgfc.acl.service.exception.ACLLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotAuthorizedException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class AclAuthProvider implements AuthenticationProvider {

    @Autowired
    AclService aclService;

    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        try {
            aclService.loginVerify(new LoginRequest(account, password));
        } catch (ACLLoginException | NotAuthorizedException e) {
            throw new BadCredentialsException("登入失敗 請檢查帳號密碼");
        }
        Optional<User> user = userRepository.findByAccount(account);
        UserLoginResponse response = new UserLoginResponse(user.get());
        List<String> permissions = Arrays.asList("NORMAL");
        return new UsernamePasswordAuthenticationToken(response, null, AuthorityUtils.createAuthorityList(permissions.toArray(new String[permissions.size()])));
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == UsernamePasswordAuthenticationToken.class;
    }
}
