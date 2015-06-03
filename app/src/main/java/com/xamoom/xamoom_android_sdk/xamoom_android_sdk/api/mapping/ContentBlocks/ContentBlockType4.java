package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class ContentBlockType4 extends ContentBlock {

    private String linkUrl;
    private String linkType;
    private String text;

    public ContentBlockType4(String title, String publicStatus, int contentBlockType, String linkUrl, String linkType, String text) {
        super(title, publicStatus, contentBlockType);
        this.linkUrl = linkUrl;
        this.linkType = linkType;
        this.text = text;
    }

    //getter
    public String getLinkUrl() {
        return linkUrl;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getText() {
        return text;
    }
}
