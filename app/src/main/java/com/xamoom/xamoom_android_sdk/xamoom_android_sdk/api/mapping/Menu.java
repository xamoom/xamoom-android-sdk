package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.APICallback;

import java.util.List;

/**
 * Used for mapping menu responses from the xamoom-cloud-api.
 * Menu will have only a list of MenuItems.
 *
 * @author Raphael Seher
 *
 * @see MenuItem
 * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi#getContentById(String, boolean, boolean, String, APICallback)
 * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi#getContentbyIdFull(String, boolean, boolean, String, boolean, APICallback)
 * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi#getContentByLocationIdentifier(String, boolean, boolean, String, APICallback)
 */
public class Menu {

    private List<MenuItem> items;

    @Override
    public String toString() {
        String output = "items: ";

        for (MenuItem item : items) {
            output += item.toString();
        }

        return output;
    }
}

/**
 * Uses for mapping menuItems from the xamoom-cloud-api.
 * MenuItems will have a itemLabel (title) and contentId.
 *
 * Open menuItems by calling <code>getContentById()</code> method.
 *
 * @author Raphael Seher
 *
 * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi#getContentById(String, boolean, boolean, String, APICallback)
 * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi#getContentbyIdFull(String, boolean, boolean, String, boolean, APICallback)
 */
class MenuItem {

    private String itemLabel;
    private String contentId;

    @Override
    public String toString() {
        return String.format("\nitemLabel: %s, \ncontentId: %s}", itemLabel, contentId);
    }
}
