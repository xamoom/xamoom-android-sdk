package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 *
 */
public class ContentBlock {

    private String title;
    private String publicStatus;
    private int contentBlockType;

    private String text;
    private String artists;
    private String fileId;
    private String youtubeUrl;
    private String soundcloudUrl;
    private String linkType;
    private String linkUrl;
    private String contentId;
    private String downloadType;
    private String spotMapTag;

    @Override
    public String toString() {
        return String.format("{title: %s, publicStatus: %s, contentBlockType: %s}", title, publicStatus, Integer.toString(contentBlockType));
    }
}