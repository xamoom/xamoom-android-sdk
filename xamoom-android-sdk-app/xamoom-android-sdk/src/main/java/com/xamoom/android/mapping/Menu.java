package com.xamoom.android.mapping;

import com.xamoom.android.APICallback;

import java.util.List;

/**
 * Used for mapping menu responses from the xamoom-cloud-api.
 * Menu will have only a list of MenuItems.
 *
 * @author Raphael Seher
 *
 * @see MenuItem
 * @see com.xamoom.android.XamoomEndUserApi#getContentbyId(String, boolean, boolean, String, boolean, boolean, APICallback)
 * @see com.xamoom.android.XamoomEndUserApi#getContentByLocationIdentifier(String, String, boolean, boolean, String, APICallback)
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

    public List<MenuItem> getItems() {
        return items;
    }
}
