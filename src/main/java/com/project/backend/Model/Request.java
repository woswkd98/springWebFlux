package com.project.backend.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("request")
public class Request {

    @Column("requestId")
    private int requestId;
    @Column("category")
    private String category;
    @Column("context")
    private String context;
    


    @Column("uploadAt")
    private String uploadAt;
    
    @Column("deadLine")
    private Long deadLine;
    
    @Column("hopeDate")
    private String hopeDate;
    @Column("user_indexId")
    private int user_indexId;

}


