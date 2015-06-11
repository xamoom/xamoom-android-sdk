package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlock;

import java.util.List;

/**
 *
 */
public class Content {

    private String contentId;
    private String imagePublicUrl;
    private String description;
    private String language;
    private String title;
    private List<ContentBlock> contentBlocks;

    @Override
    public String toString() {
        return (String.format("\ncontentId: %s, " +
                "\nimagePublicUrl: %s, " +
                "\ndescription: %s, " +
                "\nlanguage: %s, " +
                "\ntitle: %s, " +
                "\ncontentBlocks: %s", contentId, imagePublicUrl, description, language, title, contentBlocks));
    }

    //getter

    public String getContentId() {
        return contentId;
    }

    public String getImagePublicUrl() {
        return imagePublicUrl;
    }

    public String getDescriptionOfContent() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public List<ContentBlock> getContentBlocks() {
        return contentBlocks;
    }
}
