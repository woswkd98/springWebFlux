package com.project.backend.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

import reactor.core.publisher.Flux;
import com.project.backend.Model.*;

public interface RequestRepository extends ReactiveCrudRepository<Request, Long> {
    
    @Query(
        "insert into Request(category, context , uploadAt, deadLine, hopeDate, user_indexId )" +
        "values ( :category, :context , :uploadAt, :deadLine, :hopeDate, :user_indexId)"
        )
    public Mono<Integer> requestInsert(
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