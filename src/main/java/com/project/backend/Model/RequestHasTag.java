package com.project.backend.Model;

public class RequestHasTag {
    int tagId;
    int requestHId;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getRequestHId() {
        return requestHId;
    }

    public void setRequestHId(int requestHId) {
        this.requestHId = requestHId;
    }

    public RequestHasTag() {
    }

    public RequestHasTag(int tagId, int requestHId) {
        this.tagId = tagId;
        this.requestHId = requestHId;
    }
}