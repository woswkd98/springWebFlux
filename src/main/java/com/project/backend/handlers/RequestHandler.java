package com.project.backend.handlers;

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


import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import net.minidev.json.JSONObject;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.MultiValueMap;

import com.project.backend.Configurations.GetTimeZone;
import com.project.backend.Model.*;

import com.project.backend.jwt.JwtProduct;
import com.project.backend.repositories.RequestRepository;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

// 이제 controller 안에 덕지덕지 붙이는게 싫으니 따로 만들자
@Component
public class RequestHandler {
    
    private final DatabaseClient databaseClient;
    private final RequestRepository requestRepository;
    public RequestHandler(DatabaseClient databaseClient
    ,RequestRepository requestRepository
    ) {
        this.databaseClient = databaseClient;
        this.requestRepository = requestRepository;
    }
    
    
    
    public Mono<ServerResponse> insert(ServerRequest req) {

        Mono<RequestGetter> mbody = req.bodyToMono(RequestGetter.class);
      
        RequestGetter body = mbody.block();

        return ok().body(
            requestRepository.requestInsert( 
                body.getCategory(),
                body.getContext(),
                GetTimeZone.getSeoulDate(),
                body.getDeadline(),
                body.getHopeDate(),
                body.getUserId()
                ), Integer.class);
    }

    @Transactional //요 부분에서 요청을 삭제하면 그 요청에 대한 입찰도 같이 삭제해야함 따라서 두개의 쿼리문을 하나로
    public Mono<ServerResponse> delete(ServerRequest req) {
        
        Mono<HashMap<String, Object>> productRequest = req.bodyToMono(HashMap.class);
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

    public Mono<ServerResponse> selectAll(ServerRequest req) {
        Flux<Request> rs =
            databaseClient.execute("select * from request")
            .as(Request.class)
            .fetch()
            .all();
        return ok().body(rs, Request.class);
    }

    public Mono<ServerResponse> selectRequestsByTagContext(ServerRequest req) {
 
        Flux<Request> temp = requestRepository
        .selectRequestsByTagContext(req.bodyToMono(String.class).block());
  
        return ok().body(
            temp,Request.class);
    }

}