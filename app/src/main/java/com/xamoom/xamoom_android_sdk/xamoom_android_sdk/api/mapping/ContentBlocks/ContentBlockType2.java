package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class ContentBlockType2 extends ContentBlock {

    private String youtubeUrl;

    public ContentBlockType2(String title, String publicStatus, int contentBlockType, String youtubeUrl) {
        super(title, publicStatus, contentBlockType);
        this.youtubeUrl = youtubeUrl;
    }

    //getter
    public String getYoutubeUrl() {
        return youtubeUrl;
    }
}
