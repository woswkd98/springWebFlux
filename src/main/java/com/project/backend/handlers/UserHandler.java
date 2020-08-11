package com.project.backend.handlers;

import com.nimbusds.jose.JOSEException;
import com.project.backend.Configurations.Email;
import com.project.backend.Model.LoginModel;
import com.project.backend.Model.User;
import com.project.backend.repositories.PublicRepository;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.HashMap;
import java.util.Map;

import java.text.ParseException;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.backend.jwt.JwtProduct;
@Component
public class UserHandler  {

    
    @Autowired
    JwtProduct jwtProduct;

    private final DatabaseClient databaseClient;
    private final PublicRepository publicRepository;



    @Autowired
    ReactiveRedisTemplate<String,String> reactiveRedisTemplate;

    @Autowired
    Email email;

    public UserHandler(
        DatabaseClient databaseClient,
        PublicRepository publicRepository

    ) {
        this.databaseClient = databaseClient;
        this.publicRepository = publicRepository;
       
    }

    public Mono<ServerResponse> join(ServerRequest req) { 
        Mono<User> insertPub = req.bodyToMono(User.class);
        return ok().body(insertPub.flatMap(u -> {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            u.setUserPassword(passwordEncoder.encode(u.getUserPassword()));
            Mono<User> rs = databaseClient
                    .execute("INSERT INTO USER(userPassword,userEmail,  userName) VALUES (:userPassword,:userEmail,:userName)")
                    .as(User.class)
                    .bind("userEmail", u.getUserEmail())
                    .bind("userPassword", u.getUserPassword())
                    .bind("userName", u.getUserName())
                    .fetch().first(); // 해결봤다
            return rs;
        }), User.class);
    }


    public Mono<ServerResponse> login(ServerRequest req) { 
        Mono<User> user = 
             publicRepository.findByUserPk(Integer.valueOf(req.queryParam("id").get()))
                .flatMap(nu -> {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
              
                if (!passwordEncoder.matches(req.queryParam("password").get(), nu.getUserPassword())) {
                    return Mono.just(nu);
                }
                return null;
            });
            
       
        
        User bUser = user.block();

        if(bUser == null) return ok().body(Mono.just(-1),Integer.class);   
  
        return ok().cookie(
            ResponseCookie
            .from("token",jwtProduct.getKey(bUser.getUserEmail()))
            .maxAge(JwtProduct.EXPIRATION_TIME)
            .httpOnly(true)
            .build()).body(Mono.just(bUser.getUserId()),Integer.class);   
    } 


    public Mono<ServerResponse> logout(ServerRequest req) {
        return ok().cookie(
            ResponseCookie.from("token","") // 일단 
            .maxAge(0) // 시간 0
            .httpOnly(true) 
            .build()).body(Mono.just("asdf") ,String.class);
    }
    
    public Mono<ServerResponse> verify(ServerRequest req) { 
        Mono<Map> mUser = req.bodyToMono(Map.class);
        Mono<String> rs = mUser.flatMap(u -> {
            try {
                if (!jwtProduct.verify(u.get("id").toString(), req.cookies().get("token").toString())) {
                    return Mono.just("failure");
                }
            } catch (ParseException | JOSEException e) {
                e.printStackTrace();
            }
            return Mono.just("Success");
        });
        return ok().body(rs, String.class);
    }
    public Mono<ServerResponse> update(ServerRequest req) { 
        Mono<Map> userRequest = req.bodyToMono(Map.class);
            
        return ok().body(userRequest.flatMap(mUser -> {
            return databaseClient
            .execute(
                "update user set userPassword = : pwd" + 
                ",userName = : name where userId = :id")
                .bind("id", mUser.get("id"))
                .bind("pwd", mUser.get("userPassword"))
                .bind("name", mUser.get("name"))
                .fetch().rowsUpdated()
                .onErrorReturn(0);
        }), Integer.class);
    }

   
    public Mono<ServerResponse> delete(ServerRequest req) { 
        Mono<HashMap> userRequest = req.bodyToMono(HashMap.class);
        return ok().body(userRequest.flatMap(mUser -> {
            return databaseClient.execute(
                "DELETE FROM user WHERE userId = :id")
                .bind("id", mUser.get("id"))
                .fetch().rowsUpdated()
                .onErrorReturn(0);
        }), Integer.class);
    }

    public  Mono<ServerResponse> getAll(ServerRequest req) { 
 
        return ok().body(databaseClient
            .execute("select * from user")
            .fetch()
            .all()
            ,User.class);   
    }

    public  Mono<ServerResponse> findByPK(ServerRequest req) { 
        return ok().body( databaseClient
        .execute("select * from user where userId = :userId")
        .bind("userId",req.queryParam("pk").get())
        .fetch()
        .first(), User.class);

    }


 
    public  Mono<ServerResponse> findByUserId(ServerRequest req) { 
        return ok().body( databaseClient
        .execute("select * from user where userEmail = :userEmail")
        .bind("userEmail",req.queryParam("userId").get())
        .fetch()
        .first(), User.class);
    }

    public  Mono<ServerResponse> setUserState(ServerRequest req) {
        Mono<Map> mMap = req.bodyToMono(Map.class);
 
        return ok().body(mMap.flatMap(u -> publicRepository.updateUserWidthdraw((int)u.get("userState"))), Integer.class);
    }

    public Mono<ServerResponse> setSeller(ServerRequest req) {
        Mono<Map> mMap = req.bodyToMono(Map.class);
        mMap.block().get("userId");
        return ok().body(
            mMap.flatMap(map -> {
                System.out.println("-------------------------------------------");
       
               return publicRepository.findByUserPk((int)map.get("userId")).cache()
                .flatMap(user-> {
                    System.out.println(123);
                    if(user.getUserId() == -1) return Mono.just(user.getUserId() );

                    return publicRepository.insertSeller(
                        map.get("portfolio").toString(),
                        map.get("imageLink").toString(),
                        (int)map.get("imageCount"),
                        (int)map.get("userId")
                    );                     
                });
            }), Integer.class);

    }

    public Mono<ServerResponse> emailAuth(ServerRequest req) {
        return ok().body(databaseClient
        .execute("select * from user where userEmail = :userEmail")
        .bind("userEmail",req.queryParam("email").get())
        .fetch().first().flatMap(u -> {
            email.sendMail(req.queryParam("email").get());
            return Mono.just("이메일 보냄");
        }).switchIfEmpty(Mono.defer(() -> {
            return Mono.just("이메일 없다");
        })),String.class);
    }

    public Mono<ServerResponse> emailAuthVerify(ServerRequest req) { 
        return ok().body(Mono.defer(() -> {
            ReactiveHashOperations<String, String, String> hashOps = reactiveRedisTemplate.opsForHash();
            
            System.out.println(hashOps.size( Email.redisKey).block());
            return hashOps.get( Email.redisKey, "woswkd98@naver.com").flatMap(m -> {
                System.out.println(m);
                if(m.equals(req.queryParam("authId").get())) {
                    hashOps.remove( Email.redisKey, req.queryParam("authId").get());
                    return Mono.just("인증 완료");
                }
                
                return Mono.just("인증실패");
                
           });            
        }), String.class);
    }
    public Mono<ServerResponse> updatePwd(ServerRequest req) {
        
        System.out.println(req.queryParam("userId").get());
        System.out.println(req.queryParam("newPassword").get());
        return ok().body(databaseClient
        .execute(  "update user set userPassword = :pwd" + 
        " where userId = :userId")
        .bind("userId",req.queryParam("userId").get())
        .bind("pwd", (new BCryptPasswordEncoder()).encode(req.queryParam("newPassword").get()))
        .fetch()
        .first(), User.class);
    }


     
}