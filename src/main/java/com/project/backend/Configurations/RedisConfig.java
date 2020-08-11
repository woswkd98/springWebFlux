package com.project.backend.Configurations;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.embedded.RedisServer;


@Configuration
public class RedisConfig {

    /* 
    
    @Bean
    ReactiveRedisOperations<String, String> redisOperations(ReactiveRedisConnectionFactory factory) {
      Jackson2JsonRedisSerializer<String> serializer = new Jackson2JsonRedisSerializer<>(String.class);
  
      RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder =
          RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
  
      RedisSerializationContext<String, String> context = builder.value(serializer).build();
  
      return new ReactiveRedisTemplate<>(factory, context);
    }
   
    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializer<String> serializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(String.class);
        RedisSerializationContext serializationContext = RedisSerializationContext
                .<String, String>newSerializationContext()
                .key(serializer)
                .value(jackson2JsonRedisSerializer)
                .hashKey(serializer)
                .hashValue(jackson2JsonRedisSerializer)
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }*/
    /*
    @Bean
    ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
      Jackson2JsonRedisSerializer<String> serializer = new Jackson2JsonRedisSerializer<>(String.class);
  
      RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder =
          RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
  
      RedisSerializationContext<String, String> context = builder.value(serializer).build();
  
      return new ReactiveRedisTemplate<>(factory, context);
    }
*/
    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializer<String> serializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(String.class);
        RedisSerializationContext serializationContext = RedisSerializationContext
                .<String, String>newSerializationContext()
                .key(serializer)
                .value(jackson2JsonRedisSerializer)
                .hashKey(serializer)
                .hashValue(jackson2JsonRedisSerializer)
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

   
}