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

import com.project.backend.repositories.RequestRepository;

import java.util.Map;
import java.util.HashMap;
import java.util.List;


// 이제 controller 안에 덕지덕지 붙이는게 싫으니 따로 만들자
@Component
public class RequestHandler {
    
    // autowired가 스프링 4부터 이렇게 쓰면 자동으로 된다 
    private final DatabaseClient databaseClient;
    private final RequestRepository requestRepository;

    public RequestHandler(
        DatabaseClient databaseClient,
        RequestRepository requestRepository
    ) {
        this.databaseClient = databaseClient;
        this.requestRepository = requestRepository;
    }
    
    public Mono<ServerResponse> insert(ServerRequest req) {

        Mono<RequestGetter> mbody = req.bodyToMono(RequestGetter.class);
        Mono<Integer> request = mbody.flatMap(mBody-> {
            return requestRepository.requestInsert( 
                mBody.getCategory(),
                mBody.getContext(),
                GetTimeZone.getSeoulDate(),
                mBody.getDeadline(),
                mBody.getHopeDate(),
                mBody.getUserId()
            ).flatMap(mRequest -> {
                System.out.println(mRequest);
                return Mono.just(mRequest);
            });
        });
        
        System.out.println(request.block());
        return ok().body(
            request, Request.class);
    }

    @Transactional //요 부분에서 요청을 삭제하면 그 요청에 대한 입찰도 같이 삭제해야함 따라서 두개의 쿼리문을 하나로
    public Mono<ServerResponse> delete(ServerRequest req) {
        
        Mono<HashMap> productRequest = req.bodyToMono(HashMap.class);
        HashMap<String, Object> mRequest = productRequest.block();
 
        Mono<Integer> rs = databaseClient
        .execute(
            "delete from bidding where request_requestId = :requestId")
        .bind("requestId",  mRequest.get("requestId"))
        .fetch()
        .rowsUpdated()
        .then(databaseClient
            .execute(
                "delete from request where requestId = :requestId" )
            .bind("requestId",  mRequest.get("requestId"))
            .fetch()
            .rowsUpdated().onErrorReturn(0))
        .onErrorReturn(0);;
    
        return ok().body(rs, Integer.class);
    }

    public Mono<ServerResponse> selectByCategory(ServerRequest req) {
        Flux<Request> rs = 
            databaseClient.execute("select * from request where category = :category")
            .bind("category", req.bodyToMono(Map.class).block().get("category"))
            .as(Request.class)
            .fetch()
            .all();
        return ok().body(rs, Request.class);
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
        Flux<Request> rs =
            databaseClient.execute("select r.* from request r order by r.deadLine desc")
            .fetch()
            .all()
            .flatMap(map -> {
                return Flux.just(
                 new Request(
                    (int)map.get("requestId"),
                     map.get("category").toString(),
                     map.get("context").toString(),
                     map.get("uploadAt").toString(),
                     map.get("deadLine").toString(),
                     map.get("hopeDate").toString(),
                     (int)map.get("user_indexId")
                ));
            });

        return ok().body(rs, Request.class);
    }

    public Mono<ServerResponse> selectRequestsByTagContext(ServerRequest req) {
 
        Flux<Request> temp = requestRepository
        .selectRequestsByTagContext(req.bodyToMono(String.class).block());
  
        return ok().body(
            temp,Request.class);
    }

}