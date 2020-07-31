package com.project.backend.Model;

public class Bidding {
    private int biddingId;
    private String uploadAt;
    private int price;
    private int request_requestId;
    private int seller_sellerId;

    public int getBiddingId() {
        return biddingId;
    }

    public void setBiddingId(int biddingId) {
        this.biddingId = biddingId;
    }

    public String getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(String uploadAt) {
        this.uploadAt = uploadAt;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRequest_requestId() {
        return request_requestId;
    }

    public void setRequest_requestId(int request_requestId) {
        this.request_requestId = request_requestId;
    }

    public int getSeller_sellerId() {
        return seller_sellerId;
    }

    public void setSeller_sellerId(int seller_sellerId) {
        this.seller_sellerId = seller_sellerId;
    }

    public Bidding(int biddingId, String uploadAt, int price, int request_requestId, int seller_sellerId) {
        this.biddingId = biddingId;
        this.uploadAt = uploadAt;
        this.price = price;
        this.request_requestId = request_requestId;
        this.seller_sellerId = seller_sellerId;
    }

    public Bidding() {
    }
}