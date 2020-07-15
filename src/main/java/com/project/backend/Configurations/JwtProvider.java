package com.project.backend.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import java.util.Base64;
import javax.annotation.PostConstruct;
/*
@Configuration
public class JwtProvider  {
    private String secretKey = null;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(JwtProperties.SECRET.getBytes());
    }
}*/