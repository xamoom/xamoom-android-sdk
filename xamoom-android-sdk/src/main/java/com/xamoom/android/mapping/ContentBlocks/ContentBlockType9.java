package com.xamoom.android.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;
import com.xamoom.android.APICallback;

/**
 * Used for mapping Spotmap contentBlocks received from the xamoom-cloud-api.
 *
 * To display a spotmap you have to call the <code>getSpotMap()</code> method.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 * @see com.xamoom.android.XamoomEndUserApi#getSpotMap(String, String[], String, APICallback)
 */
public class ContentBlockType9 extends ContentBlock {

    @SerializedName("spot_map_tag")
    private String spotMapTag;

    public ContentBlockType9(String title, boolean publicStatus, int contentBlockType, String spotMapTag) {
        super(title, publicStatus, contentBlockType);
        this.spotMapTag = spotMapTag;
    }

    @Override
    public String toString() {
        return String.format("\ntitle: %s" +
                "\npublicStatus: %s" +
                "\ncontentBlockType: %s" +
                "\nspotMapTag: %s", this.getTitle(), this.getPublicStatus(), this.getContentBlockType(), spotMapTag);
    }

    //getter
    public String getSpotMapTag() {
        return spotMapTag;
    }
}
