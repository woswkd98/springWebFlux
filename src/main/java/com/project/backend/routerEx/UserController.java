
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

import com.project.backend.Model.*;

import com.project.backend.jwt.JwtProduct;

import java.text.ParseException;
import java.util.List;

import com.nimbusds.jose.*;

@Configuration
class UserController {
    @Autowired
    JwtProduct jwtProduct;

    private final DatabaseClient databaseClient;

    public UserController(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;

    }

    @Bean
    public RouterFunction<?> test() {
        return route(GET("/PP"), req -> ok()
                .body(databaseClient.execute("select * from User").as(User.class).fetch().all(), User.class));
    }

    @Bean
    public RouterFunction<?> join() {
        return route(POST("/join"), req -> {

            Mono<User> insertPub = req.bodyToMono(User.class);
            return ok().body(insertPub.flatMap(u -> {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                Mono<Integer> rs = databaseClient
                        .execute("insert into User(id, password, name, email) values( :id, :password, :name, :email)")
                        .as(User.class).bind("id", u.getId()).bind("password", u.getPassword())
                        .bind("name", u.getName()).bind("email", u.getEmail()).fetch().rowsUpdated().onErrorReturn(0); // 해결봤다
                return rs;
            }), Integer.class);
        });
    }

    @Bean
    public RouterFunction<?> login() {
        return route(POST("/login"), req -> {
            Mono<LoginModel> mUser = req.bodyToMono(LoginModel.class);
            Mono<ServerResponse> res;
            
            Mono<String> t = mUser.flatMap(u -> {
                return databaseClient.execute("select * from User where id = :id").bind("id", u.getId()).as(User.class)
                        .fetch().first().flatMap(nu -> {
                   
                            
                            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                            if (!passwordEncoder.matches(u.getPassword(), nu.getPassword())) {
                                return Mono.just("");
                                
                            }
                           
                            return Mono.just(jwtProduct.getKey(u.getId()));
                        });         
            });
       
            return ok().cookie(
                ResponseCookie.from("token", t.block())
                .maxAge(JwtProduct.EXPIRATION_TIME)
                .httpOnly(true) // 보안 취약점을 막기위해
                .build()).body(Mono.just("asdf") ,String.class);
        });
    }

    @Bean
    public RouterFunction<?> logout() {
        return route(POST("/test2"), req -> {
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
        return route(POST("/verify"), req -> {
            Mono<LoginModel> mUser = req.bodyToMono(LoginModel.class);
            Mono<String> rs = mUser.flatMap(u -> {
                System.out.println(u.getId());
                System.out.println(u.getPassword());
                try {
                    if (!jwtProduct.verify(u.getId(), u.getPassword())) {
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
    
}