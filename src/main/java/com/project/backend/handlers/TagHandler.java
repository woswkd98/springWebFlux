package com.project.backend.handlers;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.data.r2dbc.core.DatabaseClient;

import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

import com.project.backend.Configurations.GetTimeZone;
import com.project.backend.Model.*;
import com.project.backend.repositories.PublicRepository;


import java.util.Map;
import java.util.HashMap;
import java.util.List;




@Component
public class TagHandler {
    private final PublicRepository publicRepository;
    private final DatabaseClient databaseClient;
    
    public TagHandler(DatabaseClient databaseClient,
    PublicRepository publicRepository) {
        this.databaseClient = databaseClient;
        this.publicRepository = publicRepository;
    }
    /*
    
    // 태그 id로 검색
    @Query("Select t.* from tag t where t.tagId = :tagId")
    public Mono<Tag> selectByTagId(int tagId);
    // 태그 내용으로 검색
    @Query("select t.* from tag t where t.context = :context")
    public Mono<Tag> selectByTagContext(String context);
    // 많이 성사된 순으로 태그 정렬
    @Query("select t.bidCount, t.context from tag t order by bidCount desc")
    public Flux<Tag> sortTagByBidCount();
    
    */
    public Mono<ServerResponse> sortByBiddingCount(ServerRequest req) {
        return ok().body(publicRepository.sortTagByBidCount(),Tag.class);
    }

    public Mono<ServerResponse> selectByTagContext(ServerRequest req) {
        Mono<Map<String, Object>> mMap = req.bodyToMono(Map.class);
     
        return ok().body(
            mMap.flatMap(
                m -> publicRepository.selectByTagContext(m.get("context").toString())
            ), Tag.class);
    }

    public Mono<ServerResponse> selectByTagId(ServerRequest req) {
        Mono<Map<String, Object>> mMap = req.bodyToMono(Map.class);
        return ok().body(
            mMap.flatMap(
                m -> publicRepository.selectByTagId((int)m.get("tagId"))
            ), Tag.class);
    }

    
}