package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import java.util.List;

/**
 *
 */
public class ResponseSpotMap {
    private List<ResponseSpot> items;
    private ResponseStyle style;
    private int radius;
    private int limit;

    @Override
    public String toString() {
        String output = "spots: ";
        for (ResponseSpot spot : items) {
            output += String.format("%s", spot.toString());
        }
        return output + "radius: " + radius + ", limit: " + limit;
    }
}
