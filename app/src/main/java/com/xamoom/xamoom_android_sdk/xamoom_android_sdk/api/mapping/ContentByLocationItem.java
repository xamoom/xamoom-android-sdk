package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

/**
 *
 */
public class ContentByLocationItem {

    private String language;
    private String title;
    private String description;
    private String imagePublicUrl;
    private String contentId;
    private double lat;
    private double lon;
    private String systemName;
    private String systemUrl;
    private String styleFgColor;
    private String styleBgColor;
    private String styleHlColor;
    private String styleIcon;
    private String systemIcon;
    private String systemId;
    private String spotId;
    private String spotName;
    private String contentName;

    @Override
    public String toString() {
        return (String.format("ContentByLocationItem with contentId: %s",contentId));
    }
}
