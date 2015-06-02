package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.request;

/**
 *
 */
public class APIRequestByLocation {
    private APILocation location;
    private String language;

    public APIRequestByLocation(APILocation location, String language) {
        this.location = location;
        this.language = language;
    }
}


