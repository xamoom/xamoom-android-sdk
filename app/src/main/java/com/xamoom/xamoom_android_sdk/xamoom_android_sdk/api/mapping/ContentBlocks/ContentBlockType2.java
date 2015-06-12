package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping Youtube contentBlocks received from the xamoom-cloud-api.
 *
 * You have to implement your own youtube player.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 */
public class ContentBlockType2 extends ContentBlock {

    @SerializedName("youtube_url")
    private String youtubeUrl;

    public ContentBlockType2(String title, boolean publicStatus, int contentBlockType, String youtubeUrl) {
        super(title, publicStatus, contentBlockType);
        this.youtubeUrl = youtubeUrl;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\nyoutubeUrl: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), youtubeUrl);
    }

    //getter
    public String getYoutubeUrl() {
        return youtubeUrl;
    }
}
