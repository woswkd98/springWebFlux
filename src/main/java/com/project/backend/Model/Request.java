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

    @Column("context")
    private String context;
    
    @Column("cateory")
    private String category;

    @Column("uploadAt")
    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime uploadAt;
    
    @Column("deadLine")
    private LocalDateTime deadLine;
    
    @Column("hopeDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hopeDate;
 
    private int user_indexId;

}


