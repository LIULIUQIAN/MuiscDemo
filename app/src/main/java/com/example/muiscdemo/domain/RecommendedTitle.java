package com.example.muiscdemo.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.muiscdemo.adapter.RecommendAdapter;

public class RecommendedTitle implements MultiItemEntity {
    @Override
    public int getItemType() {
        return RecommendAdapter.TYPE_TITLE;
    }

    public RecommendedTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
