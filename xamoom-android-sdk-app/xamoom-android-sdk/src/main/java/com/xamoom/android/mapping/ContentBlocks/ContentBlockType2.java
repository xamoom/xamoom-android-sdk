package com.xamoom.android.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping Video contentBlocks received from the xamoom-cloud-api.
 *
 * You have to implement your own youtube player.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 */
public class ContentBlockType2 extends ContentBlock {

    @SerializedName("video_url")
    private String videoUrl;

    public ContentBlockType2(String title, boolean publicStatus, int contentBlockType, String videoUrl) {
        super(title, publicStatus, contentBlockType);
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\nvideoUrl: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), videoUrl);
    }

    //getter
    public String getVideoUrl() {
        return videoUrl;
    }
}
