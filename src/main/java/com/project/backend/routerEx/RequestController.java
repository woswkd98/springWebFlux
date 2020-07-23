package com.project.backend.routerEx;

import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import com.project.backend.handlers.RequestHandler;

@Configuration
public class RequestController {
    
    @Autowired
    RequestHandler requestHandler;

    @Bean
    public RouterFunction<?> requestInsert() {
        return RouterFunctions.route(PUT("/user"), requestHandler::insert);
    }

    @Bean 
    public RouterFunction<?> requestDelete() {
        return RouterFunctions.route(DELETE("/delete"), requestHandler::delete);
    }
    
    @Bean 
    public RouterFunction<?> requestSelectByCategory() {
        return RouterFunctions.route(GET("/selectByCategory"), requestHandler::selectByCategory);
    }
    
    @Bean 
    public RouterFunction<?> selectAll() {
        return RouterFunctions.route(GET("selectAll"),requestHandler::selectAll);
    }


}