package com.xamoom.android.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping Download contentBlocks received from the xamoom-cloud-api.
 *
 * The download contentBlock can have 2 different types.
 * 0 VCF
 * 1 ICAL
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 */
public class ContentBlockType8 extends ContentBlock {

    @SerializedName("file_id")
    private String fileId;
    @SerializedName("download_type")
    private int downloadType;
    private String text;

    public ContentBlockType8(String title, boolean publicStatus, int contentBlockType, String fileId, int downloadType, String text) {
        super(title, publicStatus, contentBlockType);
        this.fileId = fileId;
        this.downloadType = downloadType;
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\nfileId: %s" +
                "\ndownloadType: %d" +
                "\ntext: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), fileId, downloadType, text);
    }

    //getter
    public String getFileId() {
        return fileId;
    }

    public int getDownloadType() {
        return downloadType;
    }

    public String getText() {
        return text;
    }
}
