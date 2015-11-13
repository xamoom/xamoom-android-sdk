package com.xamoom.android.mapping;


import com.google.gson.annotations.SerializedName;
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
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("description")
    private String description;
    @SerializedName("location")
    private Location location;
    @SerializedName("image")
    private String image;
    @SerializedName("content_id")
    private String contentId;

    @Override
    public String toString() {
        return String.format("\ndisplayName: %s, \ndescription: %s, \nlocation: %f, %f, \nimage: %s", displayName, description, location.getLat(), location.getLon(), image);
    }

    //getters

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }

    public String getContentId() {
        return contentId;
    }
}