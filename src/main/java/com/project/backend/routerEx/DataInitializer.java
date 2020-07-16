
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
 * @author jinheung
 */
@Component
@Slf4j
class DataInitializer {

    
    private final DatabaseClient databaseClient;
    
    public DataInitializer(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }
    

}