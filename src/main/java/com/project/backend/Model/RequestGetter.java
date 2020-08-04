package com.project.backend.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestGetter { // 리퀘스트 저장받을 떄 형식
    private int userId;
    private String context;
    private String category;
    private Long deadline;
    private String hopeDate;
    private String tags[];
}