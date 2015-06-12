package com.xamoom.android.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping Soundcloud contentBlocks received from the xamoom-cloud-api.
 *
 * For displaying soundcloud contentBlocks you have to implement a soundcloud player.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
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
