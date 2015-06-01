package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ResponseContentBlock;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class ResponseContent {

    private String contentId;
    private String imagePublicUrl;
    private String descriptionOfContent;
    private String language;
    private String title;
    private List<ResponseContentBlock> contentBlocks;

    @Override
    public String toString() {
        return (String.format("{contentId: %s, imagePublicUrl: %s, descriptionOfContent: %s, language: %s, title: %s, contentBlocks: %s}", contentId, imagePublicUrl, descriptionOfContent, language, title, contentBlocks));
    }
}
