package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import java.util.List;

/**
 * SpotMapBlock
 */
public class ContentBlock9ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
  private static final String TAG = ContentBlock9ViewHolder.class.getSimpleName();

  private Fragment mFragment;
  private String mApiKey;
  private TextView mTitleTextView;
  private SupportMapFragment mMapFragment;
  private ContentBlock mContentBlock;
  private GoogleMap mGoogleMap;
  private LinearLayout mRootLayout;
  //private BestLocationProvider mBestLocationProvider;
  private Location mUserLocation;
  private ArrayMap<Marker, Spot> mMarkerArray;
  private ContentBlock9InfoWindowAdapter mInfoWindowAdapter;

  private static int mFrameId = 169147;
  private int mUniqueFrameId;
  public boolean showContentLinks;

  public ContentBlock9ViewHolder(View itemView, Fragment fragment, EnduserApi enduserApi) {
    super(itemView);
    mFragment = fragment;
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mRootLayout = (LinearLayout) itemView.findViewById(R.id.rootLayout);
      mMapFragment = SupportMapFragment.newInstance();
    mMarkerArray = new ArrayMap<>();

    //setting up unique map fragment
    FrameLayout parentFrameLayout = (FrameLayout) itemView.findViewById(R.id.map);

    mUniqueFrameId = mFrameId;
    mFrameId++;

    FrameLayout uniqueFrameLayout = new FrameLayout(mFragment.getActivity());
    uniqueFrameLayout.setId(mUniqueFrameId);
    parentFrameLayout.addView(uniqueFrameLayout);
    mFragment.getChildFragmentManager().beginTransaction().replace(uniqueFrameLayout.getId(), mMapFragment).commit();
  }

  public void setupContentBlock(ContentBlock contentBlock) {
    mTitleTextView.setVisibility(View.VISIBLE);
    if (contentBlock.getTitle() != null) {
      mTitleTextView.setText(contentBlock.getTitle());
    } else {
      mTitleTextView.setVisibility(View.GONE);
    }

    mContentBlock = contentBlock;

    if (mGoogleMap == null)
      mMapFragment.getMapAsync(this);
  }

  @Override
  public void onMapReady(final GoogleMap googleMap) {
    mGoogleMap = googleMap;
        /*
        XamoomEndUserApi.getInstance(mFragment.getActivity().getApplicationContext(),
                mApiKey).getSpotMap(mContentBlock.getSpotMapTag().split(","),
                null, true, new APICallback<SpotMap>() {
                    @Override
                    public void finished(SpotMap result) {
                        if (mMapFragment.isAdded()) {
                            setupLocation();
                            setupGoogleMapAndMarker(result);
                        }
                    }

                    @Override
                    public void error(RetrofitError error) {
                        Log.e(TAG, "Error:" + error);
                    }
                });
        */
  }

  private void setupGoogleMapAndMarker(List<Spot> result) {
    /*
    mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
    mInfoWindowAdapter = new ContentBlock9InfoWindowAdapter(mFragment,
        mMarkerArray, mUserLocation, showContentLinks);
    mGoogleMap.setInfoWindowAdapter(mInfoWindowAdapter);

    //get icon
    Bitmap icon = getIcon(result.getStyle().getCustomMarker());

    //display markers
    for (Spot s : result.getItems()) {
      Marker marker = mGoogleMap.addMarker(new MarkerOptions()
          .icon(BitmapDescriptorFactory.fromBitmap(icon))
          .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
          .title(s.getDisplayName())
          .position(new LatLng(s.getLocation().getLat(), s.getLocation().getLon())));

      mMarkerArray.put(marker, s);
    }

    //click listener to move camera to spot and show the complete infoWindow
    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick(final Marker marker) {
        mRootLayout.post(new Runnable() {
          @Override
          public void run() {
            int zoom = (int) mGoogleMap.getCameraPosition().zoom;
            CameraUpdate cu = CameraUpdateFactory
                .newLatLngZoom(new LatLng(
                    marker.getPosition().latitude +
                        (double) 110 / Math.pow(2, zoom),
                    marker.getPosition().longitude), zoom);
            mGoogleMap.animateCamera(cu);
          }
        });

        marker.showInfoWindow();

        return false;
      }

    });

    if (showContentLinks) {
      mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
          Spot spot = mMarkerArray.get(marker);

          if (spot.getContentId() != null) {
            XamoomContentFragment xamoomContentFragment = (XamoomContentFragment) mFragment;
            xamoomContentFragment.spotMapContentLinkClick(spot.getContentId());
          }
        }
      });
    }

    //zoom map to display all markers
    zoomToDisplayAllMarker();
    */
  }

  /**
   * Returns the icon for mapMarker.
   *
   * @param customMarker
   * @return icon
   */
  private Bitmap getIcon(String customMarker) {
    Bitmap icon;
    if (customMarker != null) {
      String iconString = customMarker;
      icon = ContentBlock9ViewHolderUtils.getIconFromBase64(iconString, mFragment);
    } else {
      icon = BitmapFactory.decodeResource(mFragment.getResources(), R.drawable.ic_default_map_marker);
      //float imageRatio = (float) icon.getWidth() / (float) icon.getHeight();
      //icon = Bitmap.createScaledBitmap(icon, 70, (int) (70 / imageRatio), false);
    }

    return icon;
  }

  private void zoomToDisplayAllMarker() {
    //zoom to display all markers
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    for (Marker marker : mMarkerArray.keySet()) {
      builder.include(marker.getPosition());
    }

    int deviceWidth = 1000;
    if (mFragment.isAdded()) {
      deviceWidth = mFragment.getResources().getDisplayMetrics().widthPixels;
    }

    LatLngBounds bounds = builder.build();
    bounds = adjustBoundsForMaxZoomLevel(bounds);
    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, deviceWidth, deviceWidth, 70);

    mGoogleMap.moveCamera(cu);
  }

  private LatLngBounds adjustBoundsForMaxZoomLevel(LatLngBounds bounds) {
    LatLng sw = bounds.southwest;
    LatLng ne = bounds.northeast;
    double deltaLat = Math.abs(sw.latitude - ne.latitude);
    double deltaLon = Math.abs(sw.longitude - ne.longitude);

    final double zoomN = 0.001; // minimum zoom coefficient

    if (deltaLat < zoomN) {
      sw = new LatLng(sw.latitude - (zoomN - deltaLat / 2), sw.longitude);
      ne = new LatLng(ne.latitude + (zoomN - deltaLat / 2), ne.longitude);
      bounds = new LatLngBounds(sw, ne);
    }
    else if (deltaLon < zoomN) {
      sw = new LatLng(sw.latitude, sw.longitude - (zoomN - deltaLon / 2));
      ne = new LatLng(ne.latitude, ne.longitude + (zoomN - deltaLon / 2));
      bounds = new LatLngBounds(sw, ne);
    }

    return bounds;
  }

  private void setupLocation() {
    /*
    if (ContextCompat.checkSelfPermission(mFragment.getContext(),
        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      Log.e(TAG, "No location permission. Ask the user for location permission.");
      return;
    }

    mBestLocationProvider = new BestLocationProvider(mFragment.getActivity(), false, true,
        1000, 1000, 5, 10);
    BestLocationListener mBestLocationListener = new BestLocationListener() {
      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {
      }

      @Override
      public void onProviderEnabled(String provider) {
      }

      @Override
      public void onProviderDisabled(String provider) {
      }

      @Override
      public void onLocationUpdateTimeoutExceeded(BestLocationProvider.LocationType type) {
      }

      @Override
      public void onLocationUpdate(Location location, BestLocationProvider.LocationType type,
                                   boolean isFresh) {
        if (isFresh) {
          Log.i(TAG, "onLocationUpdate TYPE:" + type + " Location:" +
              mBestLocationProvider.locationToString(location));
          mUserLocation = location;
          mInfoWindowAdapter.setUserLocation(mUserLocation);
        }
      }
    };

    //start Location Updates
    mBestLocationProvider.startLocationUpdatesWithListener(mBestLocationListener);
    */
  }
}