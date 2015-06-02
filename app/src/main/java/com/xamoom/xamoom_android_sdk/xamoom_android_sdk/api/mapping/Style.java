package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

/**
 * Created by raphaelseher on 28.05.15.
 */
public class Style {

    private String backgroundColor;
    private String highlightColor;
    private String foregroundColor;
    private String chromeHeaderColor;
    private String customMarker;
    private String icon;

    public String toString () {
        return (String.format("{backgroundColor: %s, highlightColor: %s, foregroundColor: %s, chromeHeaderColor: %s, customMarker: %s, icon: %s}", backgroundColor, highlightColor, foregroundColor, chromeHeaderColor, customMarker, icon));
    }
}
