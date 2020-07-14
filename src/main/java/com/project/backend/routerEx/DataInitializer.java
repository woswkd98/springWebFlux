
package com.project.backend.routerEx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.data.domain.Sort.Order.desc;

/**
 * @author hantsy
 */
@Component
@Slf4j
class DataInitializer {

    
    private final DatabaseClient databaseClient;

    
    public DataInitializer(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }
    /*
    @EventListener(value = ContextRefreshedEvent.class)
    public void init() {
      
        this.databaseClient.insert()
                .into("posts")
                //.nullValue("id", Integer.class)
                .value("title", "First post title")
                .value("content", "Content of my first post")
                // see: https://github.com/spring-projects/spring-data-r2dbc/issues/251
                // .map((r, m) -> r.get("id", Integer.class))
                .map((r, m) -> r.get(0, Integer.class))
                .all()
                .log()
                .thenMany(
                        this.databaseClient.select()
                                .from("posts")
                                .orderBy(Sort.by(desc("id")))
                                .as(Post.class)
                                .fetch()
                                .all()
                                .log()
                )
                .subscribe(null, null, () -> System.out.println("initialization is done..."));
    }*/

}