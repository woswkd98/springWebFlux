package com.project.backend.repositories;

import com.project.backend.Model.Request;
import com.project.backend.Model.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.HashMap;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TagRepository extends ReactiveCrudRepository<Tag, Long> {
    

    // select들 
    @Query("Select t.* from tag t where t.tagId = :id")
    public Mono<Tag> findByTagId(int tagId);
    
    @Query("select t.* from tag t where t.context = :context")
    public Mono<Tag> findByTagContext(String context);

    @Query("select t.count, t.context from tag t order by bidCount desc")
    public Flux<HashMap<String, Object>> sortTagByBidCount();
    
 
    @Query(
        "SELECT r.* FROM request r" + 
        "INNER JOIN request_has_tag rhtt" +
        "ON r.requestId = rht.request_requestId" +
        "INNER JOIN tag t" +
        "ON t.tagId = rht.tag_tagId" +
        "WHERE r.requestId = :id"
    )
    public Flux<Request> selectTagsByRequestFromRequestId(int id);

    @Query("call insertTag(:inContext, :requestId)")
    public Mono<Integer> insertTag(String inContext, int requestId);


}