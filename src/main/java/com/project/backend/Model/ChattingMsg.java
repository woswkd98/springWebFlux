package com.project.backend.Model;

import org.springframework.data.redis.core.RedisHash;
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
@Table("chat")
public class ChattingMsg {
    @Column("roomId")
    private int roomId;
    @Column("userId")
    private int userId;
    @Column("chat")
    private String chat;
    @Column("isRead")
    private int isRead;
    @Column("uploadAt")
    private String uploadAt;
}