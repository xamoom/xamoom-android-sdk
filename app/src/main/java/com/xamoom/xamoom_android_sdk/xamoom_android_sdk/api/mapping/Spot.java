package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.request.Location;

/**
 * TODO:
 */
public class Spot {
    private String displayName;
    private String description;
    private Location location;
    private String image;

    @Override
    public String toString() {
        return String.format("\ndisplayName: %s, \ndescription: %s, \nlocation: %f, %f, \nimage: %s", displayName, description, location.getLat(), location.getLon(), image);
    }
}