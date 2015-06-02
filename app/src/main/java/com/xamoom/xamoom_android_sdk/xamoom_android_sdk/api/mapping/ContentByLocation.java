package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import java.util.List;

/**
 *
 */
public class ContentByLocation {

    private List<ContentByLocationItem> items;

    @Override
    public String toString() {
        String output = "items: ";

        for (ContentByLocationItem item : items) {
            output += "{" + item.toString() + "},";
        }

        return output;
    }

    //getter & setter

    public List<ContentByLocationItem> getItems() {
        return items;
    }
}
