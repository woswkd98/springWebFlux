package com.project.backend.handlers;

import com.nimbusds.jose.JOSEException;
import com.project.backend.Model.LoginModel;
import com.project.backend.Model.User;
import com.project.backend.repositories.PublicRepository;

import com.project.backend.repositories.UserRepository;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.http.HttpCookie;
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

            System.out.println(u.getUserId());
            System.out.println(u.getUserPassword());

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            u.setUserPassword(passwordEncoder.encode(u.getUserPassword()));
            Mono<User> rs = databaseClient
                    .execute("INSERT INTO USER(userId, userPassword, userName, userEmail) VALUES (:userId,:userPassword,:userName,:userEmail)")
                    .as(User.class)
                    .bind("userId", u.getUserId())
                    .bind("userPassword", u.getUserPassword())
                    .bind("userName", u.getUserName())
                    .bind("userEmail", u.getUserEmail()).fetch().first(); // 해결봤다
            return rs;
        }), User.class);
    }


    public Mono<ServerResponse> login(ServerRequest req) {
        Mono<LoginModel> mLogin = req.bodyToMono(LoginModel.class);

        Mono<User> user = mLogin.flatMap(lm -> { 
            return publicRepository.findByUserId(lm.getId())
                .flatMap(nu -> {

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
                if (!passwordEncoder.matches(lm.getPassword(), nu.getUserPassword())) {
                    return Mono.just(nu);
                }
                return null;
            });
            
        });        
        
        User bUser = user.block();

        if(bUser == null) return ok().body(Mono.just(-1),Integer.class);   
  
        return ok().cookie(
            ResponseCookie
            .from("token",jwtProduct.getKey(bUser.getUserId())) 
            .maxAge(JwtProduct.EXPIRATION_TIME)
            .httpOnly(true)
            .build()).body(Mono.just(bUser.getIndexId()),Integer.class);   
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
                "userEmail = : email userName : name where indexId = :id")
                .bind("id", mUser.get("id"))
                .bind("pwd", mUser.get("userPassword"))
                .bind("email", mUser.get("email"))
                .bind("name", mUser.get("name"))
                .fetch().rowsUpdated()
                .onErrorReturn(0);
        }), Integer.class);
    }

   
    public Mono<ServerResponse> delete(ServerRequest req) { 
 
        Mono<HashMap> userRequest = req.bodyToMono(HashMap.class);
        return ok().body(userRequest.flatMap(mUser -> {
            return databaseClient.execute(
                "DELETE FROM user WHERE indexId = :id")
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
        .execute("select * from user where indexId = :id")
        .bind("id",req.bodyToMono(Map.class).block().get("id"))
        .fetch()
        .first(), User.class);

    }

    public  Mono<ServerResponse> findByUserId(ServerRequest req) { 
        return ok().body( databaseClient
        .execute("select * from user where userId = :userId")
        .bind("id",req.bodyToMono(Map.class).block().get("userId"))
        .fetch()
        .first(), User.class);

    }

    public  Mono<ServerResponse> setUserState(ServerRequest req) {
        Mono<Map<String, Object>> mMap = req.bodyToMono(Map.class);
        return ok().body(mMap.flatMap(u -> publicRepository.updateUserWidthdraw((int)u.get("userState"))), Integer.class);
    }

    public Mono<ServerResponse> setSeller(ServerRequest req) {
        Mono<Map<String, Object>> mMap = req.bodyToMono(Map.class);
        return ok().body(
            mMap.flatMap(map -> {
                Mono<Integer> mId =  publicRepository.insertSellerThenReturnId(
                    map.get("portfolio").toString(),
                    map.get("imageLink").toString(),
                    (int)map.get("imageCount")
                );
                
                return mId.flatMap(id -> publicRepository.updateUserToSeller(id,(int)map.get("indexId")));
            }), Integer.class);

    }

}