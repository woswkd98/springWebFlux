package com.project.backend.Model;

public class Review {
    int reviewId;
    float grade;
    String context;
    int sellerId;
    int userId;

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Review(int reviewId, float grade, String context, int sellerId, int userId) {
        this.reviewId = reviewId;
        this.grade = grade;
        this.context = context;
        this.sellerId = sellerId;
        this.userId = userId;
    }

    public Review() {
    }
}