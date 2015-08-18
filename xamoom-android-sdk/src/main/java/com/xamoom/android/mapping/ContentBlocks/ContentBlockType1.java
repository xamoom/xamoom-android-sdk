package com.xamoom.android.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping Audio contentBlocks received from the xamoom-cloud-api.
 *
 * You have to implement your own music player to stream the file.
 * The fileId is only a URL pointing to the file in our cloud.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 */
public class ContentBlockType1 extends ContentBlock {

    @SerializedName("file_id")
    private String fileId;
    @SerializedName("artists")
    private String artist;

    public ContentBlockType1(String title, boolean publicStatus, int contentBlockType, String fileId, String artist) {
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
