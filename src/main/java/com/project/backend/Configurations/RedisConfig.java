package com.project.backend.Configurations;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

/*
@Configuration
public class RedisConfig {
    
    private RedisServer redisServer;

    @PostConstruct 
    public void redisServer() {
        redisServer = new RedisServer(5678);
        redisServer.start();
    }
    
    @PreDestroy
    public void stopRedis() {
        

    }
}*/