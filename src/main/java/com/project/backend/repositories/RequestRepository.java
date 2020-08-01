package com.project.backend.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

import reactor.core.publisher.Flux;

import java.util.Map;

import com.project.backend.Model.*;

public interface RequestRepository extends ReactiveRepositoryBase<Request> {
    // https://www.baeldung.com/spring-data-jpa-stored-procedures

  

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
    
    // 입찰 성사 때
    @Query(
        "UPDATE tag"+ 
		"SET bidCount = bidCount + 1"+
		"WHERE tagId IN ("+
		"	SELECT tagId FROM (SELECT t.tagId FROM request r "+
		"	INNER JOIN request_has_tag rht "+
		"	ON r.requestId = rht.request_requestId"+
		"	INNER JOIN tag t "+
		"	ON t.tagId = rht.tag_tagId "+
		"	WHERE r.requestId = :requestId "+
		"	) AS tagIds "+
		");"
    )
    public Mono<Integer> updateTagWhenSuccessBidding(int requestId); 

    // bidding성공했을 때 요청 아이디와 같고 biddingId가 다른 모든애들을 삭제한다
    @Query("delete from 
    
    bidding where request_requestId = :requestId and biddingId != :biddingId")
    public Mono<Integer> deleteBiddingWhenSuccess(int requestId, int biddingId);
  
   
    
    @Query("delete from request_has_tag where request_requestId = :requestId")
    public Mono<Integer> deleteReqHasTag(int requestId); 

    //취소 
    @Query(
        "UPDATE tag"+ 
		"SET requestCount = requestCount - 1"+
		"WHERE tagId IN ("+
		"	SELECT tagId FROM (SELECT t.tagId FROM request r "+
		"	INNER JOIN request_has_tag rht "+
		"	ON r.requestId = rht.request_requestId"+
		"	INNER JOIN tag t "+
		"	ON t.tagId = rht.tag_tagId "+
		"	WHERE r.requestId = :requestId "+
		"	) AS tagIds "+
		");"
    )
    public Mono<Integer> updateTagWhenCancel(int requestId);

    @Query("delete from tag  where requestCount = 0")
    public Mono<Integer> deleteTagRequestCountIsZero();

    @Query("delete from request where requestId = :requestId")
    public Mono<Integer> deleteRequest(int requestId);


}