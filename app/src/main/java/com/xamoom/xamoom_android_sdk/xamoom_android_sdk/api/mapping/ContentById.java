package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

/**
 * Created by raphaelseher on 29.05.15.
 */
public class ContentById {
    public String systemName;
    public String systemUrl;
    public String systemId;
    public ResponseContent content;
    public ResponseStyle style;
    public ResponseMenu menu;

    @Override
    public String toString () {
        return (String.format("{systemName: %s, systemUrl: %s, systemId: %s, content: %s, style: %s, menu: %s}", systemName, systemUrl, systemId, content, style, menu));
    }
}
