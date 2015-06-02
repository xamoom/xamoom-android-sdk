package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlock;

import java.util.List;

/**
 *
 */
public class Content {

    private String contentId;
    private String imagePublicUrl;
    private String descriptionOfContent;
    private String language;
    private String title;
    private List<ContentBlock> contentBlocks;

    @Override
    public String toString() {
        return (String.format("{contentId: %s, imagePublicUrl: %s, descriptionOfContent: %s, language: %s, title: %s, contentBlocks: %s}", contentId, imagePublicUrl, descriptionOfContent, language, title, contentBlocks));
    }
}
