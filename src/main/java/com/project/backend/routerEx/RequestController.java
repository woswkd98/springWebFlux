package com.project.backend.routerEx;

import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
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


    //checked
    @Bean
    public RouterFunction<?> requestInsert() {
        return RouterFunctions.route(PUT("/requests"), requestHandler::insert);
    }

    //checked
    @Bean 
    public RouterFunction<?> selectRequestByCategory() {
        return RouterFunctions.route(GET("/requests/:category"), requestHandler::selectByCategory);
    }
    
    //checked
    @Bean 
    public RouterFunction<?> selectAll() {
        return RouterFunctions.route(GET("/requests"),requestHandler::selectAll);
    }
    
    //checked
    @Bean 
    public RouterFunction<?> selectRequestsByTagContext() {
        return RouterFunctions.route(GET("/requests/:tag"),requestHandler::selectRequestsByTagContext);
    }
    // 여기서 부터 
    @Bean
    public RouterFunction<?> deleteRequestWhenCancel() {
        return RouterFunctions.route(DELETE("/requests/:requestId"),requestHandler::deleteRequestWhenCancel);
    }
    @Bean
    public RouterFunction<?> findByPk() {
        return RouterFunctions.route(GET("/requests/:requestId"),requestHandler::findByPK);
    }

    @Bean
    public RouterFunction<?> paging() {
        return RouterFunctions.route(GET("/requests/:start/:size"),requestHandler::findByPK);
    }

}