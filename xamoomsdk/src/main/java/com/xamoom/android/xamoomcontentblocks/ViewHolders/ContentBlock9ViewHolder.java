package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xamoom.android.xamoomcontentblocks.XamoomMapFragment;
import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import at.rags.morpheus.Error;

/**
 * SpotMapBlock
 */
public class ContentBlock9ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
  private static final String TAG = ContentBlock9ViewHolder.class.getSimpleName();
  private static final int PAGE_SIZE = 100;

  private Fragment mFragment;
  private Context mContext;
  private EnduserApi mEnduserApi;
  private TextView mTitleTextView;
  private MapView mMapView;
  private ContentBlock mContentBlock;
  private GoogleMap mGoogleMap;
  private ArrayMap<Marker, Spot> mMarkerArray;
  private String mBase64Icon;
  private ArrayList<Spot> mSpotList;
  private int mTextColor;

  public boolean showContentLinks;

  public ContentBlock9ViewHolder(View itemView, Fragment fragment, EnduserApi enduserApi) {
    super(itemView);
    mFragment = fragment;
    mContext = fragment.getContext();
    mEnduserApi = enduserApi;
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mMarkerArray = new ArrayMap<>();
    mMapView = (MapView) itemView.findViewById(R.id.mapImageView);
  }

  public void setupContentBlock(ContentBlock contentBlock, boolean offline) {
    mMapView.onCreate(null);

    mTitleTextView.setVisibility(View.VISIBLE);
    if (contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setText(contentBlock.getTitle());
      mTitleTextView.setTextColor(mTextColor);
    } else {
      mTitleTextView.setVisibility(View.GONE);
    }

    mContentBlock = contentBlock;

    mMapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mGoogleMap = googleMap;

    /*
    mEnduserApi.getSpotsByTags(mContentBlock.getSpotMapTags(), spotOptions, null, new APIListCallback<List<Spot>, List<at.rags.morpheus.Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        mSpotList = (ArrayList<Spot>) result;
        getStyle(result.get(0).getSystem().getId());
        addMarkerToMap(result);
      }

      @Override
      public void error(List<Error> error) {
        Log.e(TAG, "Error:" + error);
      }
    });
    */
    mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
      @Override
      public void onMapClick(LatLng latLng) {
        openMapFragment(null);
      }
    });

    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick(Marker marker) {
        openMapFragment(marker);
        return true;
      }
    });
  }

  private void downloadAllSpots(ArrayList<String> tags, String cursor,
                                final APIListCallback<List<Spot>, List<at.rags.morpheus.Error>> callback) {
    EnumSet<SpotFlags> spotOptions = EnumSet.of(SpotFlags.HAS_LOCATION);
    if (showContentLinks) {
      spotOptions = EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.HAS_LOCATION);
    }

    mEnduserApi.getSpotsByTags(tags, PAGE_SIZE, cursor, spotOptions, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {

      }

      @Override
      public void error(List<Error> error) {
        callback.error(error);
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
          .icon(BitmapDescriptorFactory.fromBitmap(ContentBlock9ViewHolderUtils.getIcon(mBase64Icon,
              mContext)))
          .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
          .title(s.getName())
          .position(new LatLng(s.getLocation().getLatitude(), s.getLocation().getLongitude())));

      mMarkerArray.put(marker, s);
    }

    mGoogleMap.animateCamera(ContentBlock9ViewHolderUtils.zoomToDisplayAllMarker(mMarkerArray.keySet(), 20));
  }

  private void openMapFragment(Marker activeMarker) {
    String spotId = null;
    if (activeMarker != null) {
      Spot spot = mMarkerArray.get(activeMarker);
      spotId = spot.getId();
    }

    mFragment.getFragmentManager().beginTransaction()
        .add(R.id.xamoom_content_frame_layout, XamoomMapFragment.newInstance(mSpotList, spotId, mBase64Icon))
        .addToBackStack(null)
        .commit();
  }

  public void setStyle(Style style) {
    if (style != null && style.getForegroundFontColor() != null) {
      mTextColor = Color.parseColor(style.getForegroundFontColor());
    }
  }
}