package com.project.backend.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import com.project.backend.Model.*;

@Component
public class RedisChat implements MessageListener {
    @Autowired
    ReactiveRedisTemplate reactiveRedisTemplate;

    @Autowired
    ReactiveRedisConnectionFactory factory;

    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        
    }
 
}