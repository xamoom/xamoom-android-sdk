package com.xamoom.android.mapping.ContentBlocks;

import com.google.gson.annotations.SerializedName;

/**
 * Uses for mapping menuItems from the xamoom-cloud-api.
 * MenuItems will have a itemLabel (title) and contentId.
 *
 * Open menuItems by calling <code>getContentById()</code> method.
 *
 * @author Raphael Seher
 *
 */
public class MenuItem {
    @SerializedName("item_label")
    private String itemLabel;
    @SerializedName("content_id")
    private String contentId;
    @SerializedName("category")
    private String category;

    @Override
    public String toString() {
        return String.format("\nitemLabel: %s, \ncontentId: %s}", itemLabel, contentId);
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public String getContentId() {
        return contentId;
    }

    public String getCategory() {
        return category;
    }
}