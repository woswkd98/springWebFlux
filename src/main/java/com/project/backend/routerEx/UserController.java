package com.project.backend.routerEx;

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

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST; // static 붙이는 이유가 함수형을 import 시키기 위해
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.backend.Configurations.GetTimeZone;
import com.project.backend.Model.*;

import com.project.backend.jwt.JwtProduct;
import com.project.backend.repositories.UserRepository;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.nimbusds.jose.*;

@Configuration
class UserController {
    @Autowired
    JwtProduct jwtProduct;

    private final DatabaseClient databaseClient;
    private final UserRepository userRepository;

    public UserController(DatabaseClient databaseClient,
    UserRepository userRepository) {
        this.databaseClient = databaseClient;
        this.userRepository = userRepository;
    }

    // 
    

    @Bean
    public RouterFunction<?> join() {
        return route(POST("/user/join"), req -> {

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
        });
    }

    @Bean
    public RouterFunction<?> login() {
        return route(POST("/user/login"), req -> {
            Mono<LoginModel> mLogin = req.bodyToMono(LoginModel.class);

            Mono<User> user = mLogin.flatMap(lm -> { 
            return databaseClient.execute("select * from user where userId = :id")
            .as(User.class)
            .bind("id", lm.getId())
            .fetch().first().flatMap(nu -> {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

                if (!passwordEncoder.matches(lm.getPassword(), nu.getUserPassword())) {
                    return Mono.just(nu);
                }
                return null;
            });        
        });
        


        return ok().cookie(
            user.flatMap(u -> { 
                return ResponseCookie
                .from("token",jwtProduct.getKey(u.getUserId())) 
                .maxAge(JwtProduct.EXPIRATION_TIME)
                .httpOnly(true)
                .build();}).body(user.flatMap(u -> u.getIndexId()),Integer.class);
   
           
    }

    @Bean
    public RouterFunction<?> logout() {
        return route(POST("/user/logout"), req -> {
            // 쿠키는 강제로 삭제는 못시킨다 하지만 만료를 시킬수 있다 
          return ok().cookie(
                ResponseCookie.from("token","") // 일단 
                .maxAge(0) // 시간 0
                .httpOnly(true) 
                .build()).body(Mono.just("asdf") ,String.class);
        });
    }
    @Bean
    public RouterFunction<?> verify() {
        return route(POST("/user/verify"), req -> {
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
        });
    }
 
    @Bean 
    public RouterFunction<?> update() {
        return route(POST("/user/update"), req -> {
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
                
        });
    }
    @Bean
    public RouterFunction<?> delete() {
        
        return route(POST("/user/delete"), req -> {
            Mono<HashMap> userRequest = req.bodyToMono(HashMap.class);
            return ok().body(userRequest.flatMap(mUser -> {
                return databaseClient
                .execute(
                    "DELETE FROM user WHERE indexId = :id")
                    .bind("id", mUser.get("id"))
                    .fetch().rowsUpdated()
                    .onErrorReturn(0);
            }), Integer.class);
        });
    }
    @Bean
    public RouterFunction<?> getAll() {
        return route(GET("/user/userList"), req -> {
                return ok().body(databaseClient
                .execute("select * from user ")
                .fetch()
                .all()
              , User.class);
        });
    }
    @Bean
    public RouterFunction<?> findById() {
        return route(POST("/user/findByPk"), req -> {
           return ok().body( databaseClient
           .execute("select * from user where indexId = :id")
           .bind("id",req.bodyToMono(Map.class).block().get("id"))
           .fetch()
           .first(), User.class);
        });

        
            
    }

    @Bean
    public RouterFunction<?> findByUserId() {
        return route(POST("/user/findByUserId"), req -> {
           return ok().body( databaseClient
           .execute("select * from user where userId = :userId")
           .bind("id",req.bodyToMono(Map.class).block().get("userId"))
           .fetch()
           .first(), User.class);
        });    
    }
}