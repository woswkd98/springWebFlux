package com.project.backend.Model;

public class seller {
    private int sellerId;
    private String portfolio;
    private String ImageLink;
    private int imageCount;
    private int sellerGrade;
    private int reviewerCount;
    private int userIndexId;
    
    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getSellerGrade() {
        return sellerGrade;
    }

    public void setSellerGrade(int sellerGrade) {
        this.sellerGrade = sellerGrade;
    }

    public int getReviewerCount() {
        return reviewerCount;
    }

    public void setReviewerCount(int reviewerCount) {
        this.reviewerCount = reviewerCount;
    }
    
    public seller() {
    }

    public int getUserIndexId() {
        return userIndexId;
    }

    public void setUserIndexId(int userIndexId) {
        this.userIndexId = userIndexId;
    }

    public seller(int sellerId, String portfolio, String imageLink, int imageCount, int sellerGrade, int reviewerCount,
            int userIndexId) {
        this.sellerId = sellerId;
        this.portfolio = portfolio;
        ImageLink = imageLink;
        this.imageCount = imageCount;
        this.sellerGrade = sellerGrade;
        this.reviewerCount = reviewerCount;
        this.userIndexId = userIndexId;
    }
}