package com.project.backend.jwt;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import com.nimbusds.jwt.JWTClaimsSet;
/*
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    public static final String SECRET = "jinheung";
    public static final Long EXPIRATION_TIME = 30 * 60 * 1000L; // 1초에 1000 * 60초 * 30 즉 30분
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return false;
    }
    
 
}   */