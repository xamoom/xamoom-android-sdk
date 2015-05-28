package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by raphaelseher on 28.05.15.
 */
public class ContentByLocationIdentifier {

    public String systemName;
    public String systemUrl;
    public String systemId;
    public boolean hasContent;
    public boolean hasSpot;
    public ResponseContent content;
    public ResponseStyle style;
    public ResponseMenu menu;

    @Override
    public String toString () {
       return (String.format("{systemName: %s, systemUrl: %s, systemId: %s, hasContent: %s, hasSpot: %s, content: %s, style: %s, menu: %s}", systemName, systemUrl, systemId, hasContent, hasSpot, content, style, menu));
    }
}
