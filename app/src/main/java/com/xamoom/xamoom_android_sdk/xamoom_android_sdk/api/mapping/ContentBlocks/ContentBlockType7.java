package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Soundcloud ContentBlock
 */
public class ContentBlockType7 extends ContentBlock {

    @SerializedName("soundcloud_url")
    private String soundcloudUrl;

    public ContentBlockType7(String title, boolean publicStatus, int contentBlockType, String soundcloudUrl) {
        super(title, publicStatus, contentBlockType);
        this.soundcloudUrl = soundcloudUrl;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\nsoundcloudUrl: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), soundcloudUrl);
    }

    //getter
    public String getSoundcloudUrl() {
        return soundcloudUrl;
    }
}
