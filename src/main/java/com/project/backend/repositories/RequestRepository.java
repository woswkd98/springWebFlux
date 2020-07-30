package com.project.backend.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

import reactor.core.publisher.Flux;

import java.util.Map;

import com.project.backend.Model.*;

public interface RequestRepository extends ReactiveRepositoryBase<Request> {
    // https://www.baeldung.com/spring-data-jpa-stored-procedures

    @Query
    ("call testPro(:temp)")
    public Mono<Integer> inserttest(String[] temp);

    @Query(
        "insert into Request(requestId, category, context , uploadAt, deadLine, hopeDate, user_indexId )" +
        "values (:requestId, :category, :context , :uploadAt, :deadLine, :hopeDate, :user_indexId)" 
        )
    public Mono<Integer> requestInsert(
        String requestId,
        String category, 
        String context, 
        String uploadAt,
        String deadLine,
        String hopeDate,
        int user_indexId 
    );
    @Query("LAST_INSERT_ID()")
    public Mono<Integer> getLastId();

    // 도저히 LAST_INSERT_ID() 이거 못가져와서 그냥 프로시져에 넣어버렸다
    @Query("call insertThenReturnId(:category, :context , :uploadAt, :deadLine, :hopeDate, :user_indexId)")
    public Mono<Integer> insertThenRetrunId(
        String category, 
        String context, 
        String uploadAt,
        String deadLine,
        String hopeDate,
        int user_indexId
    );


    @Query(
        "SELECT r.* FROM request r " + 
        "INNER JOIN request_has_tag rht " +
        "ON r.requestId = rht.request_requestId " +
        "INNER JOIN tag t " +
        "ON t.tagId = rht.tag_tagId " +
        "WHERE t.context = :context "
    )
    public Flux<Request> selectRequestsByTagContext(String context);

    @Query(
        "SELECT r.* FROM request r where context = :context order by r.deadLine" 
    )
    public Flux<Request> selectRequestByContextSortByDeadLine(String context);
    
}