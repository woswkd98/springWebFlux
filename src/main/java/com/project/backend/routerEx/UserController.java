
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

import lombok.extern.java.Log;

import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static reactor.core.publisher.Mono.just;
import org.springframework.data.r2dbc.core.DatabaseClient;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.project.backend.Model.*;
import com.project.backend.Repository.UserRepository;
@Configuration
class UserController {
    private final UserRepository userRepository;

    private final DatabaseClient databaseClient;
    
    public UserController(DatabaseClient databaseClient,UserRepository userRepository) {
        this.databaseClient = databaseClient;
        this.userRepository = userRepository;
    }

    @Bean
    public RouterFunction<?> all() {

        // select *from all
        Flux<User> selectPub 
        = databaseClient.execute("select * from User")
            .as(User.class)
            .fetch()
            .all();

        return route(
            GET("/getUser"), 
            req -> {
                selectPub.subscribe(); // 구독을 시켜주는것 (여기서 dispatch해주는 것 과 같다)
                return ok().body(selectPub,User.class);
        });  
    }

     // insert into
    @Bean
    public RouterFunction<?> insert() {
        return route(POST("/insertUser"), req -> {
    
            Mono<User> insertPub = req.bodyToMono(User.class);
           
            return ok().body(insertPub.flatMap(u -> {
                Mono<Integer> rs = databaseClient.execute(
                    "insert into User(id,password,name, email) values( :id, :password, :name, :email)"
                ).as(User.class).bind("id",u.getId())
                .bind("password",u.getPassword())
                .bind("name",u.getName())
                .bind("email",u.getEmail())
                .fetch()
                .rowsUpdated()
                .onErrorReturn(-1); // 나같은 경우는 -1로 해결봤다
                return rs;
            }) , Integer.class);
        });
    }
    
    //select by id
    @Bean
    public RouterFunction<?> selectById() {
        return route(POST("/selectById"), req -> {
            Flux<User> mUser = req.bodyToFlux(User.class);
            return ok().body(
                mUser.flatMap(user -> {
                    return databaseClient.execute("select * from User where id = :id")
                    .bind("id",user.getId())
                    .as(User.class)
                    .fetch()
                    .all();
                })
            , User.class);
        });
    }
    //update


    @Bean
    public RouterFunction<?> updateByIdAndPwd() {
        return route(POST("/updateByIdAndPwd"), req -> {
            Flux<User> fUser = req.bodyToFlux(User.class);
            
            return ok().body(
                fUser.flatMap(user -> {   
                return databaseClient.execute(
                    "select * from User where id = :id and password =:password"
                ).as(User.class)
                .bind("id", user.getId())
                .bind("password", user.getPassword())
                .fetch().all();
            }), User.class);
        });
    }

}