package com.project.backend.Configurations;


public class JwtProperties {
    public static final String SECRET = "jinheung";
    public static final Long EXPIRATION_TIME = 30 * 60 * 1000L; // 1초에 1000 * 60초 * 30 즉 30분
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}