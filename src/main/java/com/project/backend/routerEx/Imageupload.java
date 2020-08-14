package com.project.backend.routerEx;

import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import com.project.backend.handlers.RequestHandler;
import com.project.backend.handlers.Fileupload;
@Configuration
@RequiredArgsConstructor
public class Imageupload {
    
    
    private final Fileupload fileupload;

    @Bean
    public RouterFunction<?> imgUpload() {
        return RouterFunctions.route(POST("/fileIo"), fileupload::fileupload);
    }
}