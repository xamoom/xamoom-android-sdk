package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping contentBlocks received from the xamoom-cloud-api.
 *
 * @author Raphael Seher
 * @version 0.1
 */
public class ContentBlock {

    @SerializedName("public")
    private boolean publicStatus;
    private String title;
    @SerializedName("content_block_type")
    private int contentBlockType;

    public ContentBlock(String title, boolean publicStatus, int contentBlockType) {
        this.title = title;
        this.publicStatus = publicStatus;
        this.contentBlockType = contentBlockType;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s", title, publicStatus, contentBlockType);
    }

    //getter
    public String getTitle() {
        return title;
    }

    public boolean getPublicStatus() {
        return publicStatus;
    }

    public int getContentBlockType() {
        return contentBlockType;
    }
}