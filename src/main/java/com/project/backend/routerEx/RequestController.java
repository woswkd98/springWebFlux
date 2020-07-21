package com.project.backend.routerEx;

import org.springframework.context.annotation.Configuration;
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
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;


import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.backend.Model.*;
import com.project.backend.handlers.RequestHandler;
import com.project.backend.jwt.JwtProduct;


import java.text.ParseException;
import java.util.List;

@Configuration
public class RequestController {
    
    @Autowired
    RequestHandler requestHandler;

    @Bean
    public RouterFunction<?> requestInsert() {
        return RouterFunctions.route(PUT("/Insert"), requestHandler::insert);
    }

    @Bean RouterFunction<?> requestDelete() {
        return RouterFunctions.route(DELETE("/delete"), requestHandler::insert);
    }
    
    @Bean RouterFunction<?> requestSelectByTags() {
        return RouterFunctions.route(GET("/delete"), requestHandler::insert);
    }
    
}