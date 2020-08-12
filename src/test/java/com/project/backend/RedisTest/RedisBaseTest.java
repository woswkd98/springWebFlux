package com.project.backend.RedisTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import com.project.backend.Configurations.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;



@Slf4j
@SpringBootTest
public class RedisBaseTest {

    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    
 
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
     

    @Test 
    public void opsHashUser() {
     
        ReactiveHashOperations<String, String, String> hashOps = reactiveRedisTemplate.opsForHash();
        String cacheKey = "valueHash";
        Map<String, String> setDatas = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            setDatas.put("key_" + i, "value_" + i);
        }

        // previous key delete - sync
        reactiveRedisTemplate.delete(cacheKey);
            
        // async process
        StepVerifier.create(hashOps.putAll(cacheKey, setDatas)).expectNext(true).verifyComplete();
        StepVerifier.create(reactiveRedisTemplate.type(cacheKey)).expectNext(DataType.HASH).verifyComplete();
        StepVerifier.create(hashOps.size(cacheKey)).expectNext(10L).verifyComplete();
        StepVerifier.create(hashOps.get(cacheKey, "key_5")).expectNext("value_5").verifyComplete();
        StepVerifier.create(hashOps.remove(cacheKey, "key_5")).expectNext(1L).verifyComplete();

        
    }

    @Test
    public void opsList() {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        String cacheKey = "valueList";
        for (int i = 0; i < 10; i++)
            listOps.leftPush(cacheKey, String.valueOf(i));

        assertSame(DataType.LIST, redisTemplate.type(cacheKey));
        assertSame(10L, listOps.size(cacheKey));
        log.info("##### opsList #####");
        log.info("{}", listOps.range(cacheKey, 0, 10));
        assertEquals("0", listOps.rightPop(cacheKey));
        assertEquals("9", listOps.leftPop(cacheKey));
        assertEquals(true, redisTemplate.delete(cacheKey));
    }

}