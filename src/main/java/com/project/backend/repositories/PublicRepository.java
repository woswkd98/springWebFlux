package com.project.backend.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

import reactor.core.publisher.Flux;

import java.util.Date;


import com.project.backend.Model.*;

public interface PublicRepository extends ReactiveCrudRepository<Request,Long> {
    // https://www.baeldung.com/spring-data-jpa-stored-procedures
    
    @Query("select UUID()")
    public Mono<String> genUuid();

    // 요청 삽입
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


    // 요청 삽입후 리턴 id
    // 도저히 LAST_INSERT_ID() 이거 못가져와서 그냥 프로시져에 넣어버렸다
    @Query("call insertThenReturnId(:category, :context , :uploadAt, :deadLine, :hopeDate, :user_indexId)")
    public Mono<Integer> insertThenReturnId(
        String category, 
        String context, 
        String uploadAt,
        String deadLine,
        String hopeDate,
        int user_indexId
    );

    // 태그로 검색한 요청들
    @Query(
        "SELECT r.* FROM request r " + 
        "INNER JOIN request_has_tag rht " +
        "ON r.requestId = rht.request_requestId " +
        "INNER JOIN tag t " +
        "ON t.tagId = rht.tag_tagId " +
        "WHERE t.context = :context "
    )
    public Flux<Request> selectRequestsByTagContext(String context);



    // 입찰 성사 때 tag업데이트 
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


    // request_has_Tag를 requestId로 검색해서 삭제 
    @Query("delete from request_has_tag where request_requestId = :requestId")
    public Mono<Integer> deleteReqHasTag(int requestId); 

    //요청 취소했을 때 태그 업데이트 
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

    // requestCount로 검색해서 삭제 
    @Query("delete from tag  where requestCount = :count")
    public Mono<Integer> deleteTagRequestCount(int count);

    // requestId로 request 삭제 
    @Query("delete from request where requestId = :requestId")
    public Mono<Integer> deleteRequest(int requestId);

    // category로 검색
    @Query("select r.* from request r where r.category = :category")
    public Flux<Request> selectByCateory(String category);

    // 마감순 검색
    @Query("select r.* from request r order by r.deadLine")
    public Flux<Request> selectAllOrderByDeadLine();

    // bidding 삽입
    @Query("insert into bidding (uploadAt,price,request_requestId, user_userId)"
    + " values (uploadAt,price, request_requestId, user_userId)")
    public Mono<Integer> insertBidding(Date uploadAt, int price, int request_requestId, int user_uerId);
    
    // bidding성공했을 때 요청 아이디와 같고 biddingId가 다른 모든애들을 삭제한다
    @Query("delete from bidding where request_requestId = :requestId and biddingId != :biddingId")
    public Mono<Integer> deleteBiddingWhenSuccess(int requestId, int biddingId);
    
    // 요청에 대한 입찰들 검색
    @Query("select b.* from bidding b where b.request_requestId = :requestId")
    public Flux<Bidding>  selectBiddingByUserIndexId(int indexId);

    // requestId로 삭제 
    @Query("delete from bidding where request_requestId = :requestId")
    public Mono<Integer> deleteBiddingByRequestId(int requestId);



    // 태그 id로 검색
    @Query("Select t.* from tag t where t.tagId = :tagId")
    public Mono<Tag> selectByTagId(int tagId);
    // 태그 내용으로 검색
    @Query("select t.* from tag t where t.context = :context")
    public Mono<Tag> selectByTagContext(String context);
    // 많이 성사된 순으로 태그 정렬
    @Query("select t.bidCount, t.context from tag t order by bidCount desc")
    public Flux<Tag> sortTagByBidCount();
    
    // 요청에 대한 태그들 검색
    @Query(
        "SELECT r.* FROM request r" + 
        "INNER JOIN request_has_tag rhtt" +
        "ON r.requestId = rht.request_requestId" +
        "INNER JOIN tag t" +
        "ON t.tagId = rht.tag_tagId" +
        "WHERE r.requestId = :id"
    )
    public Flux<Request> selectTagsByRequestFromRequestId(int id);

    // tag 삽입후 id 리턴
    @Query("call insertTag(:inContext, :requestId)")
    public Mono<Integer> insertTag(String inContext, int requestId);

    // 판매자 삽입
    @Query(
        "insert into seller (portfolio,imageLink,imageCount)" +
        "values (:portfolio, :imageLink, :imageCount)"
    )
    public Mono<Integer> insertSeller(String portfolio,String imageLink, int imageCount);


    // 유저 아이디로 찾기 
    @Query("select u from User u where u.indexId = :id")
    Mono<User> findByUserId(String id);

    // 유저에 판매자 설정
    @Query(
        "update set user seller_sellerId = :sellerId where indexId = :indexId"
    )
    public Mono<Integer> updateUserToSeller(int sellerId, int indexId);
    
    /*
    IN inPortfolio VARCHAR(100),
	IN inImageLink VARCHAR(45),
	IN inImageCount INT,
	IN inSellerGrade INT,
	IN inReviewerCount INT
    */
    @Query("call insertSellerThenReturnId(:portfolio,:imageLink,:imageCount)")
    public Mono<Integer> insertSellerThenReturnId(String portfolio, String imageLink,int imageCount);

    // 유저의 회원 상태 결정 (0이면 일반 탈퇴의 경우 1이면 회원 상태 2면 블랙리스트 추가 등등)
    @Query("update set user isWithdraw = :isWithdraw  where indexId = :indexId")
    public Mono<Integer> updateUserWidthdraw(int isWithdraw);


    // 리뷰 삽입
    @Query(
        "insert into review (grade, context, seller_sellerId, user_indexId)"+
        "values (:grade, :context, :sellerId, :userId)"
    )
    public Mono<Integer> insertReview(
        float grade,
        String context,
        String uploadAt,
        int sellerId,
        int userId
    );
    
    //리뷰 삭제
    @Query("delete from review where reviewId = :reviewId")
    public Mono<Integer> delete(int reviewId);
    

}