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
        return String.format("\nlanguage: %s," +
                "\ntitle: %s, " +
                "\ndescription: %s, " +
                "\nimagePublicUrl: %s, " +
                "\ncontentId: %s, " +
                "\nlat: %f, " +
                "\nlon: %f, " +
                "\nsystemName: %s, " +
                "\nsystemUrl: %s, " +
                "\nstyleFgColor: %s, " +
                "\nstyleBgColor: %s, " +
                "\nstyleHlColor: %s, " +
                "\nstyleIcon: %s, " +
                "\nsystemIcon: %s, " +
                "\nsystemId: %s, " +
                "\nspotId: %s, " +
                "\nspotName: %s, " +
                "\ncontentName: %s", language, title, description, imagePublicUrl, contentId, lat, lon, systemName, systemUrl, styleFgColor, styleBgColor, styleHlColor, styleIcon, systemIcon, systemId, spotId, spotName, contentName);
    }

    //getter & setter

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePublicUrl() {
        return imagePublicUrl;
    }

    public String getContentId() {
        return contentId;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getSystemUrl() {
        return systemUrl;
    }

    public String getStyleFgColor() {
        return styleFgColor;
    }

    public String getStyleBgColor() {
        return styleBgColor;
    }

    public String getStyleHlColor() {
        return styleHlColor;
    }

    public String getStyleIcon() {
        return styleIcon;
    }

    public String getSystemIcon() {
        return systemIcon;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getSpotId() {
        return spotId;
    }

    public String getSpotName() {
        return spotName;
    }

    public String getContentName() {
        return contentName;
    }
}
