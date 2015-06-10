package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import java.util.List;

/**
 *
 */
public class SpotMap {
    private List<Spot> items;
    private Style style;
    private int radius;
    private int limit;

    @Override
    public String toString() {
        return String.format("spots: %s, radius: %d, limit: %d", items, radius, limit);
    }
}
