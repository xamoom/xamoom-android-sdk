package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class ContentBlockType6 extends ContentBlock {

    private String contentId;

    public ContentBlockType6(String title, String publicStatus, int contentBlockType, String contentId) {
        super(title, publicStatus, contentBlockType);
        this.contentId = contentId;
    }

    //getter
    public String getContentId() {
        return contentId;
    }
}
