package com.xamoom.android.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;
import com.xamoom.android.APICallback;

/**
 * Used for mapping Content contentBlocks received from the xamoom-cloud-api.
 *
 * A content contentBlock is a "link" to another xamoom content.
 * To get additional informations of a contentBlock, you have to call the getContentById()
 * method. So you can for example display title, excerpt and image.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 * @see com.xamoom.android.XamoomEndUserApi#getContentById(String, boolean, boolean, String, APICallback)
 */
public class ContentBlockType6 extends ContentBlock {

    @SerializedName("content_id")
    private String contentId;

    public ContentBlockType6(String title, boolean publicStatus, int contentBlockType, String contentId) {
        super(title, publicStatus, contentBlockType);
        this.contentId = contentId;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\ncontentId: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), contentId);
    }

    //getter
    public String getContentId() {
        return contentId;
    }
}
