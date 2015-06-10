package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import java.util.List;

/**
 *
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
