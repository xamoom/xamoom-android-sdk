package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping Link contentBlocks received from the xamoom-cloud-api.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 */
public class ContentBlockType4 extends ContentBlock {

    @SerializedName("link_url")
    private String linkUrl;
    @SerializedName("link_type")
    private int linkType;
    private String text;

    public ContentBlockType4(String title, boolean publicStatus, int contentBlockType, String linkUrl, int linkType, String text) {
        super(title, publicStatus, contentBlockType);
        this.linkUrl = linkUrl;
        this.linkType = linkType;
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\nlinkUrl: %s" +
                "\nlinkType: %d" +
                "\ntext: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), linkUrl, linkType, text);
    }

    //getter
    public String getLinkUrl() {
        return linkUrl;
    }

    public int getLinkType() {
        return linkType;
    }

    public String getText() {
        return text;
    }
}
