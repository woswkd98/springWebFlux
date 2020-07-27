package com.project.backend.Model;


import java.util.Date;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table("request")
public class Request {

    private int requestId;

    private String context;

    private String category;

    private String uploadAt;

    private String deadLine;
    
    private String hopeDate;
 
    private int user_indexId;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(String uploadAt) {
        this.uploadAt = uploadAt;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getHopeDate() {
        return hopeDate;
    }

    public void setHopeDate(String hopeDate) {
        this.hopeDate = hopeDate;
    }

    public int getUser_indexId() {
        return user_indexId;
    }

    public void setUser_indexId(int user_indexId) {
        this.user_indexId = user_indexId;
    }

    public Request(int requestId, String context, String category, String uploadAt, String deadLine, String hopeDate,
            int user_indexId) {
        this.requestId = requestId;
        this.context = context;
        this.category = category;
        this.uploadAt = uploadAt;
        this.deadLine = deadLine;
        this.hopeDate = hopeDate;
        this.user_indexId = user_indexId;
    }

    public Request() {
    }

}


