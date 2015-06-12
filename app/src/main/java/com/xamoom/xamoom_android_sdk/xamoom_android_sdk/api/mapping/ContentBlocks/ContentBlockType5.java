package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping Ebook contentBlocks received from the xamoom-cloud-api.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 */
public class ContentBlockType5 extends ContentBlock {

    @SerializedName("file_id")
    private String fileId;
    @SerializedName("artists")
    private String artist;

    public ContentBlockType5(String title, boolean publicStatus, int contentBlockType, String fileId, String artist) {
        super(title, publicStatus, contentBlockType);
        this.fileId = fileId;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\nfileId: %s" +
                "\nartist: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), fileId, artist);
    }

    //getter
    public String getFileId() {
        return fileId;
    }

    public String getArtist() {
        return artist;
    }
}
