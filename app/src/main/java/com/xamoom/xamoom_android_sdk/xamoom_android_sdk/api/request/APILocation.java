package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.request;

/**
 *
 */
public class APILocation {
    private double lat;
    private double lon;

    public APILocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
