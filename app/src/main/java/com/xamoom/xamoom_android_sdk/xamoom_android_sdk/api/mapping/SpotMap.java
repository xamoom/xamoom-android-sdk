package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import java.util.List;

/**
 *
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
