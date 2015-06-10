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
       return (String.format("systemName: %s, " +
               "\nsystemUrl: %s, " +
               "\nsystemId: %s, " +
               "\nhasContent: %s, " +
               "\nhasSpot: %s, " +
               "\ncontent: %s, " +
               "\nstyle: %s, " +
               "\nmenu: %s", systemName, systemUrl, systemId, hasContent, hasSpot, content, style, menu));
    }
}
