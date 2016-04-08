package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.xamoom.android.xamoomcontentblocks.MarkerCallback;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Spot;

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

  @SuppressLint("DefaultLocale")
  public View displayContent(View v, final Spot spot, final Marker marker) {
    TextView mNameTextView = (TextView) v.findViewById(R.id.infoWindowNameTextView);
    TextView mDescriptionTextView = (TextView) v.findViewById(R.id.infoWindowDescriptionTextView);
    TextView mDistanceTextView = (TextView) v.findViewById(R.id.infoWindowDistanceTextView);
    ImageView mContentLinksLinearLayout =
        (ImageView) v.findViewById(R.id.openContentLinkIndicator);
    ImageView mImageView = (ImageView) v.findViewById(R.id.infoWindowImageView);

    if (spot.getName() != null) {
      mNameTextView.setText(spot.getName());
    }

    if (spot.getDescription() != null) {
      mDescriptionTextView.setText(spot.getDescription());
    } else {
      mDescriptionTextView.setVisibility(View.GONE);
    }

    if (spot.getPublicImageUrl() != null) {
      int width = (int) (200 * mFragment.getResources().getDisplayMetrics().density);
      int height = (int) (110 * mFragment.getResources().getDisplayMetrics().density);
      Picasso.with(mFragment.getActivity())
          .load(spot.getPublicImageUrl()).resize(width, height)
          .centerCrop().into(mImageView,
          new MarkerCallback(marker));
    } else {
      mImageView.setVisibility(View.GONE);
    }

    //display the distance from user to spot
    float distance = 0;
    if (mUserLocation != null) {
      Location spotLocation = new Location("xamoom-api");
      spotLocation.setLatitude(spot.getLat());
      spotLocation.setLongitude(spot.getLon());
      distance = spotLocation.distanceTo(mUserLocation);

      mDistanceTextView.setText(String.format("%.0f %s", distance, mFragment.getString(R.string.meterLabel)));
    } else {
      mDistanceTextView.setText(mFragment.getActivity().getString(R.string.noLocation));
    }

    if (!showContentLinks || spot.getContent() == null) {
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

  public void setMarkerArray(ArrayMap<Marker, Spot> markerArray) {
    mMarkerArray = markerArray;
  }
}
