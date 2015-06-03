package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class ContentBlockType9 extends ContentBlock {

    private String spotMapTag;

    public ContentBlockType9(String title, String publicStatus, int contentBlockType, String spotMapTag) {
        super(title, publicStatus, contentBlockType);
        this.spotMapTag = spotMapTag;
    }

    //getter
    public String getSpotMapTag() {
        return spotMapTag;
    }
}
