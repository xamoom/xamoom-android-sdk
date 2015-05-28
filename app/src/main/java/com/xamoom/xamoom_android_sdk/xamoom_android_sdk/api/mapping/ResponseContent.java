package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import java.lang.reflect.Array;

/**
 * Created by raphaelseher on 28.05.15.
 */
public class ResponseContent {

    public String contentId;
    public String imagePublicUrl;
    public String descriptionOfContent;
    public String language;
    public String title;
    public Array contentBlocks;

    @Override
    public String toString() {
        return (String.format("{contentId: %s, imagePublicUrl: %s, descriptionOfContent: %s, language: %s, title: %s, contentBlocks: %s}", contentId, imagePublicUrl, descriptionOfContent, language, title, contentBlocks.toString()));
    }
}
