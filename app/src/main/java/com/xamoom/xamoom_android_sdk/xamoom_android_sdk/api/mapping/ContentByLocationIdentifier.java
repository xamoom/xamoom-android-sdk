package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

/**
 *
 */
public class ContentByLocationIdentifier {

    private String systemName;
    private String systemUrl;
    private String systemId;
    private boolean hasContent;
    private boolean hasSpot;
    private Content content;
    private Style style;
    private Menu menu;

    @Override
    public String toString () {
       return (String.format("{systemName: %s, systemUrl: %s, systemId: %s, hasContent: %s, hasSpot: %s, content: %s, style: %s, menu: %s}", systemName, systemUrl, systemId, hasContent, hasSpot, content, style, menu));
    }
}
