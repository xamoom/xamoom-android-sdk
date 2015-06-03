package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class ContentBlockType0 extends ContentBlock {

    private String text;

    public ContentBlockType0(String title, String publicStatus, int contentBlockType, String text) {
        super(title, publicStatus, contentBlockType);
        this.text = text;
    }

    //getter
    public String getText() {
        return text;
    }
}
