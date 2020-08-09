
package com.project.backend.Configurations;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.context.annotation.Bean;

import org.springframework.security.web.server.SecurityWebFilterChain;


@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http.cors();
  
        http
        .httpBasic()
        .and()
        .authorizeExchange()
        .pathMatchers("/admin/**").hasRole("ADMIN") // 경로 안의 모든권한은 어드민에게만 있다
        .pathMatchers("/users/:id/**").hasAnyRole("users")// 경로안의 모든 권한은 어드민 + 유저에게 있다 (삽입삭제)
        .pathMatchers("/**").permitAll() //나머지는 모든사람이 접근 가능        
        .and()
        .csrf()//restApi기준에서는 안쓰는게 맞다 
        .disable();
      
        return http.build();

    }   
}