package com.project.backend.RedisChat;

import com.project.backend.Model.ChattingMsg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;


@Component
public class RedisPublisher {



    public void publish(ChannelTopic topic, ChattingMsg msg) {
        
        
    }

}