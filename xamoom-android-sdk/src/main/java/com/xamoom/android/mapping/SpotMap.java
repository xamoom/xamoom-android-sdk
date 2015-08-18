package com.xamoom.android.mapping;


import com.xamoom.android.APICallback;

import java.util.List;

/**
 * Used for mapping spotMap from the xamoom-cloud-api.
 * SpotMap will have a items (list of spots),
 * a style (only when calling <code>getSpotMap</code>),
 * a radius (only when calling <code>getClosestSpot</code>,
 * a limit (only when calling <code>getClosestSpot</code>.
 *
 * @author Raphael Seher
 *
 * @see Spot
 * @see Style
 * @see com.xamoom.android.XamoomEndUserApi#getSpotMap(String, String[], String, APICallback)
 * @see com.xamoom.android.XamoomEndUserApi#getClosestSpots(double, double, String, int, int, APICallback)
 */
public class SpotMap {
    private List<Spot> items;

    /**
     * only on getSpotMap;
     */
    private Style style;
    /**
     * only on getClosestSpot
     */
    private int radius;
    /**
     * only on getClosestSpot
     */
    private int limit;

    @Override
    public String toString() {
        return String.format("spots: %s, style: %s, radius: %d, limit: %d", items, style, radius, limit);
    }

    //getter
    public List<Spot> getItems() {
        return items;
    }

    public Style getStyle() {
        return style;
    }

    public int getRadius() {
        return radius;
    }

    public int getLimit() {
        return limit;
    }
}
