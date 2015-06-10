package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Content ContentBlock
 */
public class ContentBlockType6 extends ContentBlock {

    @SerializedName("content_id")
    private String contentId;

    public ContentBlockType6(String title, boolean publicStatus, int contentBlockType, String contentId) {
        super(title, publicStatus, contentBlockType);
        this.contentId = contentId;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\ncontentId: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), contentId);
    }

    //getter
    public String getContentId() {
        return contentId;
    }
}
