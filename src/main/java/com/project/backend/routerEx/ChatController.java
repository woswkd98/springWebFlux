package com.project.backend.routerEx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

import com.project.backend.handlers.RedisUserHandler;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
@Configuration
public class ChatController {

    @Autowired
    private RedisUserHandler redisUserHandler;

    @Bean
    public RouterFunction<?> test1234() {
        return RouterFunctions.route(GET("/test"), 
            redisUserHandler::test12354
        );
    }


    
}