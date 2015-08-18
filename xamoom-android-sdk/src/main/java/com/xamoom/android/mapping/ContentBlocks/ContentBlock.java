package com.xamoom.android.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping contentBlocks received from the xamoom-cloud-api.
 *
 * Display the text and set the font-style to the systemfont.
 * The text is html formatted. So you have to display it right and you
 * have to take care of different font-sizes and other text-stylings.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 * @see com.xamoom.android.mapping.Style
 */
public class ContentBlock {

    @SerializedName("public")
    private boolean publicStatus;
    private String title;
    @SerializedName("content_block_type")
    private int contentBlockType;

    public ContentBlock(String title, boolean publicStatus, int contentBlockType) {
        this.title = title;
        this.publicStatus = publicStatus;
        this.contentBlockType = contentBlockType;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s", title, publicStatus, contentBlockType);
    }

    //getter
    public String getTitle() {
        return title;
    }

    public boolean getPublicStatus() {
        return publicStatus;
    }

    public int getContentBlockType() {
        return contentBlockType;
    }
}