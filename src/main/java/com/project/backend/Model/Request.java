package com.project.backend.Model;


import java.util.Date;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.temporal.Temporal;

@Table("request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    private int requestId;

    private String context;

    private String category;

    private String uploadAt;

    private String deadLine;
    
    private String hopeDate;
 
    private int user_indexId;

}


