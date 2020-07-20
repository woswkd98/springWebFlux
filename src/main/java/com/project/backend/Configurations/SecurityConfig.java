
package com.project.backend.Configurations;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.security.SecureRandom;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

/*
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
   
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
  
        http.and()
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/admin/**").hasRole("ADMIN") // 경로 안의 모든권한은 어드민에게만 있다
        .antMatchers("/user/**").hasRole("User") // 경로안의 모든 권한은 어드민 + 유저에게 있다
        .antMatchers("/**").permitAll() //나머지는 모든사람이 접근 가능
        
        .and()
        .formLogin()
        .loginPage("/login") // 세큐리티에서 제공하지않는 커스텀 로그인 페이지의 경로이다
        .loginProcessingUrl("/authenticate") // 로그인 버튼을 눌렀을 때 요청받는 경로
        .usernameParameter("id") //유저 아이디 파라미터이름
        .passwordParameter("password") // 유저 비밀번호 파라미터 이름
        
        .and()
        .logout()
        .logoutSuccessUrl("/")
        
        .and()
        .csrf()//restApi기준에서는 안쓰는게 맞다 
        .disable();
      
    }  

}*/

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange()
        .anyExchange()
        .permitAll();
        http.csrf().disable();
        return http.build();
    }  
}