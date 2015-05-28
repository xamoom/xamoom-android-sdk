package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

/**
 * Created by raphaelseher on 28.05.15.
 */
public class ResponseContent {

    public String contentId;
    public String imagePublicUrl;
    public String descriptionOfContent;
    public String language;
    public String title;
    //public Array <ContentBlock> contentBlocks;

    public String toString() {
        return (String.format("{contentId: %s, imagePublicUrl: %s, descriptionOfContent: %s, language: %s, title: %s, contentBlocks: }", contentId, imagePublicUrl, descriptionOfContent, language, title));
    }
}
