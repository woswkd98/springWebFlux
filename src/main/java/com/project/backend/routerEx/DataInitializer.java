
package com.project.backend.routerEx;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;


/**
 * @author jinheung
 */
@Component

class DataInitializer {

    
    private final DatabaseClient databaseClient;
    
    public DataInitializer(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }
    


}