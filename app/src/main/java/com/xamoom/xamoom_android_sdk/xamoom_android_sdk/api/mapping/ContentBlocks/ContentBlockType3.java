package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class ContentBlockType3 extends ContentBlock {

    private String fileId;

    public ContentBlockType3(String title, String publicStatus, int contentBlockType, String fileId) {
        super(title, publicStatus, contentBlockType);
        this.fileId = fileId;
    }

    //getter
    public String getFileId() {
        return fileId;
    }
}
