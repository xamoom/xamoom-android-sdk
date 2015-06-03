package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class ContentBlockType7 extends ContentBlock {

    private String soundcloudUrl;

    public ContentBlockType7(String title, String publicStatus, int contentBlockType, String soundcloudUrl) {
        super(title, publicStatus, contentBlockType);
        this.soundcloudUrl = soundcloudUrl;
    }

    //getter
    public String getSoundcloudUrl() {
        return soundcloudUrl;
    }
}
