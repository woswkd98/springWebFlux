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
        return route(POST("/user/join"), userHandler::join);
    }

    @Bean
    public RouterFunction<?> login() {
        return route(POST("/user/login"), userHandler::login);
    }
    

    @Bean
    public RouterFunction<?> logout() {
        return route(POST("/user/logout"), userHandler::logout);
    }


    @Bean
    public RouterFunction<?> verify() {
        return route(POST("/user/verify"),userHandler::verify);
    }
 
    @Bean 
    public RouterFunction<?> update() {
        return route(POST("/user/update"),userHandler::update);
    }
    @Bean
    public RouterFunction<?> delete() {
        
        return route(POST("/user/delete"), userHandler::delete);
    }
    @Bean
    public RouterFunction<?> getAll() {
        return route(POST("/user/getAll"), userHandler::getAll);
    }
    @Bean
    public RouterFunction<?> findByPK() {
        return route(POST("/user/delete"), userHandler::findByPK);
    }

    @Bean
    public RouterFunction<?> findByUserId() {
        return route(POST("/user/findByUserId"), userHandler::findByUserId);    
    }

    @Bean
    public RouterFunction<?> setUserState() {
        return route(POST("/user/setUserState"), userHandler::setUserState);    
    }
    @Bean
    public RouterFunction<?> setSeller() {
        return route(POST("/user/setSeller"), userHandler::setSeller);    
    }
    
}