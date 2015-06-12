package com.xamoom.android.mapping;


import com.xamoom.android.APICallback;

import java.util.List;

/**
 * Used for mapping contentByLocation responses from the xamoom-cloud-api.
 * ContentByLocation will only store the ContentByLocationItems.
 *
 * @author Raphael Seher
 *
 * @see ContentByLocationItem
 * @see com.xamoom.android.XamoomEndUserApi#getContentByLocation(double, double, String, APICallback)
 */
public class ContentByLocation {

    private List<ContentByLocationItem> items;

    @Override
    public String toString() {
        return String.format("items: %s", items);
    }

    //getter & setter
    public List<ContentByLocationItem> getItems() {
        return items;
    }
}
