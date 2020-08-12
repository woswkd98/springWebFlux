package com.project.backend.Model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Serializable이 필요한 이유는 얘를 나중에 bytebuffer로 바꿔야하는데 그럴려면 implements
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RedisChatMsg implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1627628322240789236L;

    private int userId;

    private String chat;

    private int isRead;

    private String uploadAt;
}