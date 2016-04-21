package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityManagerCompat;
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
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xamoom.android.xamoomcontentblocks.Helper.BestLocationListener;
import com.xamoom.android.xamoomcontentblocks.Helper.BestLocationProvider;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.EnumSet;
import java.util.List;

import at.rags.morpheus.Error;

/**
 * SpotMapBlock
 */
public class ContentBlock9ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
  private static final String TAG = ContentBlock9ViewHolder.class.getSimpleName();

  private Fragment mFragment;
  private Context mContext;
  private EnduserApi mEnduserApi;
  private TextView mTitleTextView;
  private SupportMapFragment mMapFragment;
  private ContentBlock mContentBlock;
  private GoogleMap mGoogleMap;
  private LinearLayout mRootLayout;
  private BestLocationProvider mBestLocationProvider;
  private Location mUserLocation;
  private ArrayMap<Marker, Spot> mMarkerArray;
  private ContentBlock9InfoWindowAdapter mInfoWindowAdapter;
  private String mBase64Icon;
  private List<Spot> mSpotList;

  private static int mFrameId = 169147;
  private int mUniqueFrameId;
  public boolean showContentLinks;

  public ContentBlock9ViewHolder(View itemView, Fragment fragment, EnduserApi enduserApi, BestLocationProvider bestLocationProvider) {
    super(itemView);
    mFragment = fragment;
    mContext = fragment.getContext();
    mEnduserApi = enduserApi;
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mRootLayout = (LinearLayout) itemView.findViewById(R.id.rootLayout);
    mMarkerArray = new ArrayMap<>();
    mBestLocationProvider = bestLocationProvider;

    GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
    mMapFragment = SupportMapFragment.newInstance(options);

    FrameLayout parentFrameLayout = (FrameLayout) itemView.findViewById(R.id.map);
    mUniqueFrameId = mFrameId;
    mFrameId++;

    FrameLayout uniqueFrameLayout = new FrameLayout(mContext);
    uniqueFrameLayout.setId(mUniqueFrameId);
    parentFrameLayout.addView(uniqueFrameLayout);

    mFragment.getChildFragmentManager().beginTransaction()
        .replace(uniqueFrameLayout.getId(), mMapFragment).commit();
  }

  public void setupContentBlock(ContentBlock contentBlock) {
    mTitleTextView.setVisibility(View.VISIBLE);
    if (contentBlock.getTitle() != null) {
      mTitleTextView.setText(contentBlock.getTitle());
    } else {
      mTitleTextView.setVisibility(View.GONE);
    }

    mContentBlock = contentBlock;

    if (mGoogleMap == null) {
      mMapFragment.getMapAsync(this);
    }
  }

  @Override
  public void onMapReady(final GoogleMap googleMap) {
    mGoogleMap = googleMap;

    EnumSet<SpotFlags> spotOptions = null;
    spotOptions = EnumSet.of(SpotFlags.HAS_LOCATION);
    if (showContentLinks) {
      spotOptions = EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.HAS_LOCATION);
    }

    mEnduserApi.getSpotsByTags(mContentBlock.getSpotMapTags(), spotOptions, null, new APIListCallback<List<Spot>, List<at.rags.morpheus.Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        if (mMapFragment.isAdded()) {
          mSpotList = result;
          getStyle(result.get(0).getSystem().getId());
          addMarkerToMap(result);
        }
      }

      @Override
      public void error(List<Error> error) {
        Log.e(TAG, "Error:" + error);
      }
    });
  }

  private void getStyle(String systemId) {
    mEnduserApi.getStyle(systemId, new APICallback<Style, List<Error>>() {
      @Override
      public void finished(Style result) {
        mBase64Icon = result.getCustomMarker();
        mGoogleMap.clear();
        addMarkerToMap(mSpotList);
      }

      @Override
      public void error(List<Error> error) {
        Log.e(TAG, "getStyle error: " + error);
      }
    });
  }

  private void addMarkerToMap(List<Spot> spotList) {
    for (Spot s : spotList) {
      Marker marker = mGoogleMap.addMarker(new MarkerOptions()
          .icon(BitmapDescriptorFactory.fromBitmap(getIcon(mBase64Icon)))
          .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
          .title(s.getName())
          .position(new LatLng(s.getLat(), s.getLon())));

      mMarkerArray.put(marker, s);
    }

    zoomToDisplayAllMarker();
  }

  /**
   * Returns the icon for mapMarker.
   *
   * @param customMarker base64 custom marker (mappin) from xamoom
   * @return icon Bitmap of custom marker
   */
  private Bitmap getIcon(String customMarker) {
    Bitmap icon;
    if (customMarker != null) {
      String iconString = customMarker;
      icon = ContentBlock9ViewHolderUtils.getIconFromBase64(iconString, mFragment);
    } else {
      icon = BitmapFactory.decodeResource(mFragment.getResources(), R.drawable.ic_default_map_marker);
    }

    return icon;
  }

  private void zoomToDisplayAllMarker() {
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    for (Marker marker : mMarkerArray.keySet()) {
      builder.include(marker.getPosition());
    }

    LatLngBounds bounds = builder.build();
    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);

    mGoogleMap.moveCamera(cu);
  }

}