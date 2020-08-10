package com.project.backend.handlers;

import com.project.backend.Model.RequestGetter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class RedisUserHandler {
    
    @Autowired
    private ReactiveRedisTemplate<String,String> reactiveRedisTemplate;

    public Mono<ServerResponse> test12354(ServerRequest req) {
        System.out.println("________________________________-------------------------------------------------------------------------------------------------------------------------");
        ReactiveHashOperations<String, String, String> hashOps = reactiveRedisTemplate.opsForHash();
        String cacheKey = "valueHash";
        Map<String, String> setDatas = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            setDatas.put("key_" + i, "value_" + i);

            
        }

        //reactiveRedisTemplate.delete(cacheKey);
        hashOps.putAll(cacheKey, setDatas);
        System.out.println("awetawete" +  hashOps.size(cacheKey).block());
       
        return ok().body(
            hashOps.get(cacheKey,"key_" + 1
        ).toString(),String.class);

    }



        
  
}