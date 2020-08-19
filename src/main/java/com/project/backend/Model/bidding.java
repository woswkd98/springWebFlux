package com.project.backend.Model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("Bidding")
public class Bidding {

    @Column("biddingId")
    private int biddingId;
    @Column("uploadAt")
    private String uploadAt;
    @Column("price")
    private int price;
    @Column("request_requestId")
    private int request_requestId;
    @Column("user_userId")
    private int user_userId;

}