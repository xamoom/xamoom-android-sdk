package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 * Created by raphaelseher on 28.05.15.
 */
public class ResponseContentBlockType0 extends ResponseContentBlock {

    public String text;

    @Override
    public String toString() {
        return (String.format("title: %s, publicStatus: %s, contentBlockType: %i, text: %s", title, publicStatus, contentBlockType, text));
    }
}
