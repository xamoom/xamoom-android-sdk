package com.xamoom.android.xamoomcontentblocks;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;

/**
 * Callback is needed to load the spotImage async with picasso.
 * InfoWindow is drawn and cannot be updated after loading the image.
 * This callback will hide and show the infoWindow after loading the image,
 * so that it gets redrawn and the image will be shown.
 */
public class MarkerCallback implements Callback {
    Marker marker = null;

    MarkerCallback(Marker marker) {
        this.marker = marker;
    }

    @Override
    public void onError() {
        Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
    }

    @Override
    public void onSuccess() {
        if (marker != null && marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
    }
}