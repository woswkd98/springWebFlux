package com.project.backend.handlers;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.data.r2dbc.core.DatabaseClient;

import org.springframework.stereotype.Component;


import com.project.backend.Model.*;
import com.project.backend.repositories.PublicRepository;




@Component
public class TagHandler {
    private final PublicRepository publicRepository;
    private final DatabaseClient databaseClient;
    
    public TagHandler(DatabaseClient databaseClient,
    PublicRepository publicRepository) {
        this.databaseClient = databaseClient;
        this.publicRepository = publicRepository;
    }

    public Mono<ServerResponse> sortByBiddingCount(ServerRequest req) {
        return ok().body(publicRepository.sortTagByBidCount(),Tag.class);
    }

    public Mono<ServerResponse> selectByTagContext(ServerRequest req) {
        Mono<String> mMap = req.bodyToMono(String.class);
     
        return ok().body(
            mMap.flatMap(
                m -> publicRepository.selectByTagContext(m)
            ), Tag.class);
    }

    public Mono<ServerResponse> selectByTagId(ServerRequest req) {
        Mono<Integer> mMap = req.bodyToMono(Integer.class);
        return ok().body(
            mMap.flatMap(
                m -> publicRepository.selectByTagId(m)
            ), Tag.class);
    }
}