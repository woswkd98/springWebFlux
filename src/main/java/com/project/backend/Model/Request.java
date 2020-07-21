package com.project.backend.Model;

import org.springframework.data.relational.core.mapping.Table;

@Table("Request")
public class Request {
    private int id;
    private String author;
    private String detail;
    private String category;
    private String uploadAt;
    private int deadline;
    private String hopeDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getHopeDate() {
        return hopeDate;
    }

    public void setHopeDate(String hopeDate) {
        this.hopeDate = hopeDate;
    }


    public Request(int id, String author, String detail, String category, String uploadAt, int deadline,
            String hopeDate) {
        this.id = id;
        this.author = author;
        this.detail = detail;
        this.category = category;
        this.uploadAt = uploadAt;
        this.deadline = deadline;
        this.hopeDate = hopeDate;
     
    }

    public Request() {
    }
}