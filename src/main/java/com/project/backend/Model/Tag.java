package com.project.backend.Model;

import org.springframework.data.relational.core.mapping.Table;

@Table("tags")
public class Tag {
    int tagId;
    String conText;
    int bidCount;
    int clickCount;

 

    public Tag(int tagId, String conText, int bidCount, int clickCount) {
        this.tagId = tagId;
        this.conText = conText;
        this.bidCount = bidCount;
        this.clickCount = clickCount;
    }

    public Tag() {
    }
}