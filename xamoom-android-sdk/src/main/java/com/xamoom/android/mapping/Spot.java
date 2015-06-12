package com.xamoom.android.mapping;


import com.xamoom.android.request.Location;

/**
 * Used for mapping spot form the xamoom-cloud-api.
 * Spot will have a displayName, a description (optional), a location and a image (optional).
 *
 * @author Raphael Seher
 *
 * @see SpotMap
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