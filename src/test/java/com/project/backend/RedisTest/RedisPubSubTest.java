package com.project.backend.RedisTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

import com.project.backend.Configurations.ToByteArray;
import com.project.backend.Model.*;
import org.springframework.data.redis.connection.ReactivePubSubCommands;
import org.springframework.data.redis.connection.ReactiveSubscription.ChannelMessage;
import reactor.test.StepVerifier;
@Slf4j
@SpringBootTest
@Component

public class RedisPubSubTest {


    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    @Autowired
    private ReactiveRedisConnectionFactory reactiveRedisConnectionFactory;
    


    // redis 퍼블리셔를 리액티브하게 쓰기위해 커넥션 팩토리에서 씀
    @Test
    public void publish() {
        RedisChatMsg testMessage = new RedisChatMsg();
        testMessage.setChat("awetawet");
        testMessage.setUserId(1);
        testMessage.setIsRead(1);
        testMessage.setUploadAt("setgestte");
        StepVerifier.create(reactiveRedisConnectionFactory
        .getReactiveConnection()
        .pubSubCommands()
        .publish(
            ByteBuffer.wrap("setg".getBytes()),
            ToByteArray.ToByteBuffer(testMessage)
        )).expectNext(1L).verifyComplete();        

        reactiveRedisConnectionFactory
        .getReactiveConnection()
        .pubSubCommands()
        .subscribe(
            ByteBuffer.wrap("setg".getBytes())).cache().flatMap(test-> {
                return Mono.just(test);
            }).subscribe();
        
    }
}