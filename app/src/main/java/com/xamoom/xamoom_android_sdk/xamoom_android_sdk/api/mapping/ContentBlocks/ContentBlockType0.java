package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Text ContentBlock
 */
public class ContentBlockType0 extends ContentBlock {

    private String text;

    public ContentBlockType0(String title, boolean publicStatus, int contentBlockType, String text) {
        super(title, publicStatus, contentBlockType);
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\ntext: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), text);
    }

    //getter
    public String getText() {
        return text;
    }
}
