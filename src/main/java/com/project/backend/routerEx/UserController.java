package com.project.backend.routerEx;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST; // static 붙이는 이유가 함수형을 import 시키기 위해
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.backend.Configurations.GetTimeZone;
import com.project.backend.Model.*;
import com.project.backend.handlers.UserHandler;
import com.project.backend.jwt.JwtProduct;
import com.project.backend.repositories.UserRepository;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.nimbusds.jose.*;

@Configuration
class UserController {
    @Autowired
    JwtProduct jwtProduct;
    @Autowired
    UserHandler userHandler;
    private final DatabaseClient databaseClient;
    private final UserRepository userRepository;

    public UserController(DatabaseClient databaseClient,
    UserRepository userRepository) {
        this.databaseClient = databaseClient;
        this.userRepository = userRepository;
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
        return route(POST("/user/delete"), userHandler::getAll);
    }
    @Bean
    public RouterFunction<?> findByPK() {
        return route(POST("/user/delete"), userHandler::findByPK);
    }

    @Bean
    public RouterFunction<?> findByUserId() {
        return route(POST("/user/findByUserId"), userHandler::findByUserId);    
    }
}