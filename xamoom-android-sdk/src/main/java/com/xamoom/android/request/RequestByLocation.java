package com.xamoom.android.request;

/**
 *
 */
public class RequestByLocation {
    private Location location;
    private String language;
    private int radius;
    private int limit;

    public RequestByLocation(Location location, String language) {
        this.location = location;
        this.language = language;
    }

    //getter & setter

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}


