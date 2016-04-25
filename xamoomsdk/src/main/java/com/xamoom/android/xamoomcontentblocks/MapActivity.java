package com.xamoom.android.xamoomcontentblocks;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xamoom.android.xamoomcontentblocks.Helper.BestLocationProvider;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock9ViewHolderUtils;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import org.w3c.dom.Text;

import java.util.List;

import at.rags.morpheus.Logger;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
  public static final String SPOTS_PARAM = "0000";
  public static final String ICON_PARAM = "0001";

  private GoogleMap mGoogleMap;
  private BottomSheetBehavior mBottomSheetBehavior;
  private TextView mSpotTitleTextView;
  private ImageView mSpotImageView;
  private FloatingActionButton mFloatingActionButton;
  private List<Spot> mSpotList;
  private ArrayMap<Marker, Spot> mMarkerSpotMap = new ArrayMap<>();
  private Marker mActiveMarker;
  private String mBase64Icon;
  private BestLocationProvider mBestLocationProvider;
  private String mSpotId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);

    MapFragment mapFragment = (MapFragment) getFragmentManager()
        .findFragmentById(R.id.map);

    mSpotTitleTextView = (TextView) findViewById(R.id.spot_title_text_view);
    mSpotImageView = (ImageView) findViewById(R.id.spot_image_view);
    mFloatingActionButton = (FloatingActionButton) findViewById(R.id.spot_fab);

    View bottomSheet = findViewById(R.id.bottom_sheet);
    mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    mBottomSheetBehavior.setPeekHeight(300);
    mBottomSheetBehavior.setHideable(true);
    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    mFloatingActionButton.hide();
    
    if (savedInstanceState == null) {
      mSpotList = getIntent().getExtras().getParcelableArrayList(SPOTS_PARAM);
      if (getIntent().getExtras().containsKey(ICON_PARAM)){
        mBase64Icon = getIntent().getExtras().getString(ICON_PARAM);
      }
      if (getIntent().getExtras().containsKey("test")) {
        mSpotId = getIntent().getExtras().getString("test");
      }
    }

    mapFragment.getMapAsync(this);

    mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
          setBottomPadding(bottomSheet.getHeight());
          zoomToMarker(mActiveMarker, true);
        }

        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
          setBottomPadding(300);
        }

        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
          setBottomPadding(0);
          mFloatingActionButton.hide();
        }
      }

      @Override
      public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        Log.v("TEST", "Slide offset: " + slideOffset);
        if (mActiveMarker != null) {
          zoomToMarker(mActiveMarker, true);
        }
      }
    });
  }

  private void setBottomPadding(int padding) {
    mGoogleMap.setPadding(0,0,0,padding);
  }

  private void addMarkerToMap(List<Spot> spotList) {
    for (Spot s : spotList) {
      Marker marker = mGoogleMap.addMarker(new MarkerOptions()
          .icon(BitmapDescriptorFactory.fromBitmap(ContentBlock9ViewHolderUtils.getIcon(mBase64Icon, this)))
          .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
          .title(s.getName())
          .position(new LatLng(s.getLocation().getLatitude(), s.getLocation().getLongitude())));

      mMarkerSpotMap.put(marker, s);

      if (mSpotId != null && s.getId().equalsIgnoreCase(mSpotId)) {
        showActiveMarker(marker);
      }
    }

    if (mSpotId == null) {
      mGoogleMap.moveCamera(ContentBlock9ViewHolderUtils.zoomToDisplayAllMarker(mMarkerSpotMap.keySet(), 200));
    }
  }

  private void zoomToMarker(Marker marker, boolean animate) {
    LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, 10000);

    if (animate) {
      mGoogleMap.animateCamera(cu);
    } else {
      mGoogleMap.moveCamera(cu);
    }
  }

  private void showActiveMarker(Marker marker) {
    mActiveMarker = marker;
    mActiveMarker.showInfoWindow();
    zoomToMarker(mActiveMarker, false);
    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    mFloatingActionButton.show();

    Spot spot = mMarkerSpotMap.get(marker);
    mSpotTitleTextView.setText(spot.getName());
    Glide.with(this)
        .load(spot.getPublicImageUrl())
        .into(mSpotImageView);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mGoogleMap = googleMap;
    mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
    addMarkerToMap(mSpotList);

    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick(Marker marker) {
        showActiveMarker(marker);
        return false;
      }
    });

    mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
      @Override
      public void onMapClick(LatLng latLng) {
        mFloatingActionButton.hide();
        mActiveMarker = null;
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
      }
    });
  }

}
