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

import java.util.Set;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.security.SecureRandom;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.time.ZoneId;

// 이제 controller 안에 덕지덕지 붙이는게 싫으니 따로 만들자
@Component
public class RequestHandler {

    // autowired가 스프링 4부터 이렇게 쓰면 자동으로 된다
    private final DatabaseClient databaseClient;
    private final PublicRepository publicRepository;


    public RequestHandler(
        DatabaseClient databaseClient, 
        PublicRepository publicRepository
    ) {
        this.databaseClient = databaseClient;
        this.publicRepository = publicRepository;
    }

    public Mono<ServerResponse> insert(ServerRequest req) {
    
    return ok().body(req.bodyToMono(RequestGetter.class).flatMap(map -> {
        return publicRepository.insertThenReturnId(
            map.getCategory(), 
            map.getContext(),
            GetTimeZone.getSeoulDate(),
            map.getDeadline(),
            map.getHopeDate(),
            map.getUserId()).flatMap(requestId ->{
                for(int i =0 ; i < map.getTags().length; ++i) {
                    publicRepository.insertTag(map.getTags()[i], requestId).subscribe();
                }
                return Mono.just(requestId);
            });   
        }),Integer.class);
    }


    // bloc고쳐
    public Mono<ServerResponse> selectByCategory(ServerRequest req) {
        Mono<String> rs = req.bodyToMono(String.class);

        return  ok().body(databaseClient.execute("select * from request where category = :category")
        .bind("category", rs.block())
        .fetch()
        .all().collectList() // dl
         , List.class);
       
    }


    /*
    this.requestId = requestId;
        this.context = context;
        this.category = category;
        this.uploadAt = uploadAt;
        this.deadLine = deadLine;
        this.hopeDate = hopeDate;
        this.user_indexId = user_indexId;
    */

    public Mono<ServerResponse> selectAll(ServerRequest req) {
        Flux<Request> rs = publicRepository.selectAllOrderByDeadLine();
        return ok().body(rs, Request.class);
    }

    public Mono<ServerResponse> findByPK(ServerRequest req) {
        int i = Integer.valueOf(req.queryParam("requestId").get());
        Mono<Request> rs = publicRepository.findRequestByPk(i);
        return ok().body(rs, Request.class);
    }

    @Transactional
    public Mono<ServerResponse> selectRequestsByTagContext(ServerRequest req) {
        return ok().body(publicRepository
        .selectRequestsByTagContext(req.queryParam("tag").get()).collectList(), Request.class);
    }

    
    @Transactional
    public Mono<ServerResponse> deleteRequestWhenCancel(ServerRequest req) {
        int i = Integer.valueOf(req.queryParam("requestId").get());
        return ok().body(
            publicRepository.updateTagWhenCancel(i)//  태그 업데이트된거 취소시키고(requestCount + 1 된것을 다시 되돌리고)
            .then(publicRepository.deleteTagRequestCount(0)) // 0이면 얘가 삽입되고 생성된거 그래서 삭제 
            .then(publicRepository.deleteReqHasTag(i)) // request_has_tag 삭제 
            .then(publicRepository.deleteBiddingByRequestId(i))
            .then(publicRepository.deleteRequest(i)) // request  삭제 
        , Integer.class);
    }

    public Mono<ServerResponse> getRequestsPaging(ServerRequest req) {
        int i = Integer.valueOf(req.queryParam("requestId").get());
        return ok().body(
            publicRepository.updateTagWhenCancel(i)//  태그 업데이트된거 취소시키고(requestCount + 1 된것을 다시 되돌리고)
            .then(publicRepository.deleteTagRequestCount(0)) // 0이면 얘가 삽입되고 생성된거 그래서 삭제 
            .then(publicRepository.deleteReqHasTag(i)) // request_has_tag 삭제 
            .then(publicRepository.deleteBiddingByRequestId(i))
            .then(publicRepository.deleteRequest(i)) // request  삭제 
        , Integer.class);
    }

}