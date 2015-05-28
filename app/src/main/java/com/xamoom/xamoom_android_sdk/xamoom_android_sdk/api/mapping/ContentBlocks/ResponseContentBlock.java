package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 28.05.15.
 */
public class ResponseContentBlock {

    public String title;
    public String publicStatus;
    public int contentBlockType;

    @Override
    public String toString() {
        return (String.format("title: %s, publicStatus: %s, contentBlockType: %i", title, publicStatus, contentBlockType));
    }
}
