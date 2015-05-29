package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks;

/**
 *
 */
public class ResponseContentBlock {

    public String title;
    public String publicStatus;
    public int contentBlockType;

    public String text;
    public String artists;
    public String fileId;
    public String youtubeUrl;
    public String soundcloudUrl;
    public String linkType;
    public String linkUrl;
    public String contentId;
    public String downloadType;
    public String spotMapTag;

    @Override
    public String toString() {
        return String.format("{title: %s, publicStatus: %s, contentBlockType: %s}", title, publicStatus, Integer.toString(contentBlockType));
    }
}