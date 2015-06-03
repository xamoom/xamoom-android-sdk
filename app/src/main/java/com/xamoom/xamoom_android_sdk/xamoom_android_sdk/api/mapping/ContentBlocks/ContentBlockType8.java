package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class ContentBlockType8 extends ContentBlock {

    private String fileId;
    private String downloadType;
    private String text;

    public ContentBlockType8(String title, String publicStatus, int contentBlockType, String fileId, String downloadType, String text) {
        super(title, publicStatus, contentBlockType);
        this.fileId = fileId;
        this.downloadType = downloadType;
        this.text = text;
    }

    //getter
    public String getFileId() {
        return fileId;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public String getText() {
        return text;
    }
}
