package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class ContentBlockType5 extends ContentBlock {

    private String fileId;
    private String artist;

    public ContentBlockType5(String title, String publicStatus, int contentBlockType, String fileId, String artist) {
        super(title, publicStatus, contentBlockType);
        this.fileId = fileId;
        this.artist = artist;
    }

    //getter
    public String getFileId() {
        return fileId;
    }

    public String getArtist() {
        return artist;
    }
}
