package com.xamoom.android.xamoomcontentblocks;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.xamoom.android.mapping.Spot;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock9ViewHolder;

/**
 * Created by raphaelseher on 16.11.15.
 */
public class ContentBlock9InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Fragment mFragment;
    private ArrayMap<Marker, Spot> mMarkerArray;
    private Location mUserLocation;
    private boolean showContentLinks;

    public ContentBlock9InfoWindowAdapter(Fragment fragment, ArrayMap<Marker, Spot> markerArray,
                                          Location userLocation, boolean showSpotMapContentLinks) {
        mFragment = fragment;
        mMarkerArray = markerArray;
        mUserLocation = userLocation;
        showContentLinks = showSpotMapContentLinks;
    }

    public View displayContent(View v, final Spot spot, Marker marker) {
        TextView mNameTextView = (TextView) v.findViewById(R.id.infoWindowNameTextView);
        TextView mDescriptionTextView = (TextView) v.findViewById(R.id.infoWindowDescriptionTextView);
        TextView mDistanceTextView = (TextView) v.findViewById(R.id.infoWindowDistanceTextView);
        ImageView mContentLinksLinearLayout =
                (ImageView) v.findViewById(R.id.openContentLinkIndicator);
        ImageView mImageView = (ImageView) v.findViewById(R.id.infoWindowImageView);

        if (spot.getDisplayName() != null) {
            mNameTextView.setText(spot.getDisplayName());
        }

        if (spot.getDescription() != null) {
            mDescriptionTextView.setText(spot.getDescription());
        } else {
            mDescriptionTextView.setVisibility(View.GONE);
        }

        if (spot.getImage() != null) {
            int width = (int) (200 * mFragment.getResources().getDisplayMetrics().density);
            int height = (int) (110 * mFragment.getResources().getDisplayMetrics().density);
            Picasso.with(mFragment.getActivity()).load(spot.getImage()).resize(width, height)
                    .centerCrop().into(mImageView,
                    new MarkerCallback(marker));
        } else {
            mImageView.setVisibility(View.GONE);
        }

        //display the distance from user to spot
        float distance = 0;
        if (mUserLocation != null) {
            Location spotLocation = new Location("xamoom-api");
            spotLocation.setLatitude(spot.getLocation().getLat());
            spotLocation.setLongitude(spot.getLocation().getLon());
            distance = spotLocation.distanceTo(mUserLocation);

            mDistanceTextView.setText(String.format("%.0f %s", distance, mFragment.getString(R.string.meterLabel)));
        } else {
            mDistanceTextView.setText(mFragment.getActivity().getString(R.string.noLocation));
        }

        if (!showContentLinks) {
            mContentLinksLinearLayout.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        final Spot spot = mMarkerArray.get(marker);

        if (spot == null || !mFragment.isAdded()) {
            return null;
        }

        View v = mFragment.getActivity().getLayoutInflater().inflate(R.layout.info_window, null);
        v = displayContent(v, spot, marker);

        return v;
    }

    public void setUserLocation(Location userLocation) {
        mUserLocation = userLocation;
    }
}
