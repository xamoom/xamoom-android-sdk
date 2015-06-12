package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping Image contentBlocks received from the xamoom-cloud-api.
 *
 * When displaying images you should use the correct width/height-ratio.
 * The images can be jpg, png, gifs and SVGs.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 */
public class ContentBlockType3 extends ContentBlock {

    @SerializedName("file_id")
    private String fileId;

    public ContentBlockType3(String title, boolean publicStatus, int contentBlockType, String fileId) {
        super(title, publicStatus, contentBlockType);
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\nfileId: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), fileId);
    }

    //getter
    public String getFileId() {
        return fileId;
    }
}
