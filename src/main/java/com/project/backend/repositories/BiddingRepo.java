package com.project.backend.repositories;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.project.backend.Model.*;

public interface BiddingRepo extends ReactiveCrudRepository<Bidding,Long> {
    /*
    "insert into Request(requestId, category, context , uploadAt, deadLine, hopeDate, user_userId )" +
        "values (:requestId, :category, :context , :uploadAt, :deadLine, :hopeDate, :user_userId)" 
    
    */


    @Query("insert into bidding(uploadAt, price, request_requestId, user_userId)" + 
        " values(:u;loadAt, :price, :requestId, :userId)")
    public Mono<Integer> insert(String uploadAt, int price, int requestId,int userId); 

    @Query(
        "SELECT count(*) FROM " + 
        "INNER JOIN request_has_tag rhtt" +
        "ON r.requestId = rht.request_requestId" +
        "INNER JOIN tag t" +
        "ON t.tagId = rht.tag_tagId" +
        "WHERE r.requestId = :requestId"
    )
    public Mono<Integer> biddingCountByRequestId(int requestId);

    

}