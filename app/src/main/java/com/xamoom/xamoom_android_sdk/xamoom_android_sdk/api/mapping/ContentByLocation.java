package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.APICallback;

import java.util.List;

/**
 * Used for mapping contentByLocation responses from the xamoom-cloud-api.
 * ContentByLocation will only store the ContentByLocationItems.
 *
 * @author Raphael Seher
 *
 * @see ContentByLocationItem
 * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi#getContentByLocation(double, double, String, APICallback)
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
