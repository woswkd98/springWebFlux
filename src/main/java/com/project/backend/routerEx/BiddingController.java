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
import com.project.backend.handlers.BiddingHandler;

@Configuration
public class BiddingController {


    @Autowired
    BiddingHandler biddingHandler;



    @Bean
    public RouterFunction<?> biddingInsert() {
        return RouterFunctions.route(PUT("/biddings"), biddingHandler::insertBidding);
    }

    

}