package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class ContentBlock {

    @SerializedName("public")
    private String publicStatus;
    private String title;
    private int contentBlockType;

    public ContentBlock(String title, String publicStatus, int contentBlockType) {
        this.title = title;
        this.publicStatus = publicStatus;
        this.contentBlockType = contentBlockType;
    }

    @Override
    public String toString() {
        return String.format("{title: %s, publicStatus: %s, contentBlockType: %s}", title, publicStatus, Integer.toString(contentBlockType));
    }

    //getter
    public String getTitle() {
        return title;
    }

    public String getPublicStatus() {
        return publicStatus;
    }

    public int getContentBlockType() {
        return contentBlockType;
    }
}