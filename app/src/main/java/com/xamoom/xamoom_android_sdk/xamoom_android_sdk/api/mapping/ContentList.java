package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import java.util.List;

/**
 * Created by raphaelseher on 02.06.15.
 */
public class ContentList {
    private List<Content> items;
    private String cursor;
    private boolean more;


    @Override
    public String toString() {
        String output = "";
        for (Content content : items) {
            output += content + "\n";
        }

        return output + "cursor: " + cursor + ", more: " + more;
    }
}
