package com.project.backend.routerEx;


import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST; // static 붙이는 이유가 함수형을 import 시키기 위해

import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.r2dbc.core.DatabaseClient;

import com.project.backend.handlers.UserHandler;
import com.project.backend.jwt.JwtProduct;
import com.project.backend.repositories.UserRepository;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.HEAD;

@Configuration
class UserController {
    @Autowired
    JwtProduct jwtProduct;
    @Autowired
    UserHandler userHandler;

    public UserController() {
       
    }

    // 
    

    @Bean
    public RouterFunction<?> join() {
        return route(PUT("/users"), userHandler::join);
    }

    @Bean
    public RouterFunction<?> login() {
        return route(GET("/users/:id/:password"), userHandler::login);
    }
    

    @Bean
    public RouterFunction<?> logout() {
        return route(HEAD("/users"), userHandler::logout);
    }



    @Bean
    public RouterFunction<?> verify() {
        return route(GET("/token"),userHandler::verify);
    }
 
    @Bean 
    public RouterFunction<?> update() {
        return route(PATCH("/users"),userHandler::update);
    }
    @Bean
    public RouterFunction<?> delete() {
        
        return route(DELETE("/users"), userHandler::delete);
    }
    @Bean
    public RouterFunction<?> getAll() {
        return route(GET("/users"), userHandler::getAll);
    }
    @Bean
    public RouterFunction<?> findByPK() {
        return route(GET("/users/:pk"), userHandler::findByPK);
    }

    @Bean
    public RouterFunction<?> findByUserId() {
        return route(GET("/users/:userId"), userHandler::findByUserId);    
    }

    @Bean
    public RouterFunction<?> setUserState() {
        return route(POST("/users/:userState"), userHandler::setUserState);    
    }
    @Bean
    public RouterFunction<?> setSeller() {
        return route(PUT("/sellers"), userHandler::setSeller);    
    }
    
}