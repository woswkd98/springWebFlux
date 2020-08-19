package com.project.backend.handlers;

import com.nimbusds.jose.JOSEException;
import com.project.backend.Configurations.Email;
import com.project.backend.Configurations.GetTimeZone;
import com.project.backend.Model.Bidding;
import com.project.backend.Model.LoginModel;
import com.project.backend.Model.User;
import com.project.backend.repositories.PublicRepository;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.HashMap;
import java.util.Map;

import java.text.ParseException;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.backend.jwt.JwtProduct;
import com.project.backend.repositories.BiddingRepo;
@Component
public class BiddingHandler {

    private final DatabaseClient databaseClient;
    private final BiddingRepo biddingRepo;

    public BiddingHandler(
        DatabaseClient databaseClient, 
        BiddingRepo biddingRepo
    ) {
        this.databaseClient = databaseClient;
        this.biddingRepo = biddingRepo;
    }
    public Mono<ServerResponse> insertBidding(ServerRequest req) {
        Mono<String> rs = req.bodyToMono(Bidding.class).flatMap(bidding -> {
            return biddingRepo.biddingCountByRequestId(bidding.getRequest_requestId()).flatMap(count -> {
                if(count >= 10) {
                    return Mono.just("경매 마감");
                }
                else {
                    biddingRepo.insert(
                        GetTimeZone.getSeoulDate(),
                        bidding.getPrice(),
                        bidding.getRequest_requestId(),
                        bidding.getUser_userId()
                    );
                    return Mono.just("입찰 완료");
                }
            });
        });
        return ok().body(rs, String.class);
    }


    public Mono<ServerResponse> biddingCountByRequestId(ServerRequest req) {
        Mono<Integer> rs = req.bodyToMono(Bidding.class).flatMap(bidding -> {
            return biddingRepo.biddingCountByRequestId(Integer.valueOf(req.queryParam("requestId").toString()));
        });
        return ok().body(rs, Integer.class);
    }

   

    
}