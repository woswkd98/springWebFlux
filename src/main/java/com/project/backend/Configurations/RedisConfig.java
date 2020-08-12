package com.project.backend.Configurations;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;



@Configuration
public class RedisConfig {

  
    @PostConstruct
    public void init() {

    }
    @PreDestroy
    public void onDestroy() {
        
    }


    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        RedisSerializer<String> serializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(String.class);
        RedisSerializationContext serializationContext = RedisSerializationContext
                .<String, String>newSerializationContext()
                .key(serializer)
                .value(jackson2JsonRedisSerializer)
                .hashKey(serializer)
                .hashValue(jackson2JsonRedisSerializer)
                .build();

        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
    }

   
}