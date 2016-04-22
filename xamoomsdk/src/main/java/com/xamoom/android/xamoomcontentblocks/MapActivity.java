package com.xamoom.android.xamoomcontentblocks;

import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xamoom.android.xamoomcontentblocks.Helper.BestLocationProvider;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock9ViewHolderUtils;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
  public static final String SPOTS_PARAM = "0000";
  public static final String ICON_PARAM = "0001";

  private GoogleMap mGoogleMap;
  private List<Spot> mSpotList;
  private ArrayMap<Marker, Spot> mMarkerSpotMap = new ArrayMap<>();
  private String mBase64Icon;
  private BestLocationProvider mBestLocationProvider;
  private String test;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);

    MapFragment mapFragment = (MapFragment) getFragmentManager()
        .findFragmentById(R.id.map);

    if (savedInstanceState == null) {
      mSpotList = getIntent().getExtras().getParcelableArrayList(SPOTS_PARAM);
      if (getIntent().getExtras().containsKey(ICON_PARAM)){
        mBase64Icon = getIntent().getExtras().getString(ICON_PARAM);
      }
      if (getIntent().getExtras().containsKey("test")) {
        test = getIntent().getExtras().getString("test");
      }
    }

    mapFragment.getMapAsync(this);
  }

  private void addMarkerToMap(List<Spot> spotList) {
    for (Spot s : spotList) {
      Marker marker = mGoogleMap.addMarker(new MarkerOptions()
          .icon(BitmapDescriptorFactory.fromBitmap(ContentBlock9ViewHolderUtils.getIcon(mBase64Icon, this)))
          .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
          .title(s.getName())
          .position(new LatLng(s.getLat(), s.getLon())));

      mMarkerSpotMap.put(marker, s);

      if (test != null && s.getId().equalsIgnoreCase(test)) {
        marker.showInfoWindow();
      }
    }

    mGoogleMap.moveCamera(ContentBlock9ViewHolderUtils.zoomToDisplayAllMarker(mMarkerSpotMap.keySet(), 200));
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mGoogleMap = googleMap;
    addMarkerToMap(mSpotList);
  }
}
