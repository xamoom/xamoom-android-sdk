package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping Spotmap contentBlocks received from the xamoom-cloud-api.
 *
 * @author Raphael Seher
 * @version 0.1
 *
 * @see ContentBlock
 */
public class ContentBlockType9 extends ContentBlock {

    @SerializedName("spot_map_tag")
    private String spotMapTag;

    public ContentBlockType9(String title, boolean publicStatus, int contentBlockType, String spotMapTag) {
        super(title, publicStatus, contentBlockType);
        this.spotMapTag = spotMapTag;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\nspotMapTag: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), spotMapTag);
    }

    //getter
    public String getSpotMapTag() {
        return spotMapTag;
    }
}
