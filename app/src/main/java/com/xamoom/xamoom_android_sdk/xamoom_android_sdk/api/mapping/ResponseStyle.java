package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

/**
 * Created by raphaelseher on 28.05.15.
 */
public class ResponseStyle {

    public String backgroundColor;
    public String highlightColor;
    public String foregroundColor;
    public String chromeHeaderColor;
    public String customMarker;
    public String icon;

    public String toString () {
        return (String.format("{backgroundColor: %s, highlightColor: %s, foregroundColor: %s, chromeHeaderColor: %s, customMarker: %s, icon: %s}", backgroundColor, highlightColor, foregroundColor, chromeHeaderColor, customMarker, icon));
    }
}
