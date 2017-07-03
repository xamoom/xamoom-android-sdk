/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock9ViewHolderUtils;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import java.util.ArrayList;
import java.util.List;

public class XamoomMapFragment extends Fragment implements OnMapReadyCallback {
  private static final String SPOTS_PARAM = "0000";
  private static final String SPOT_ID_PARAM = "0001";
  private static final String ICON_PARAM = "0002";

  private GoogleMap mGoogleMap;
  private BottomSheetBehavior mBottomSheetBehavior;
  private TextView mSpotTitleTextView;
  private TextView mSpotExcerptTextView;
  private Button mSpotContentButton;
  private Button mMapCloseButton;
  private ImageView mSpotImageView;
  private FloatingActionButton mFloatingActionButton;
  private List<Spot> mSpotList;
  private ArrayMap<Marker, Spot> mMarkerSpotMap = new ArrayMap<>();
  private Marker mActiveMarker;
  private String mBase64Icon;
  private String mSpotId;

  private XamoomContentFragment.OnXamoomContentFragmentInteractionListener mListener;

  public XamoomMapFragment() {
  }

  public static XamoomMapFragment newInstance(ArrayList<Spot> spotList, String spotId, String base64Icon) {
    XamoomMapFragment fragment = new XamoomMapFragment();

    Bundle args = new Bundle();
    args.putParcelableArrayList(SPOTS_PARAM, spotList);
    args.putString(SPOT_ID_PARAM, spotId);
    args.putString(ICON_PARAM, base64Icon);
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mSpotList = getArguments().getParcelableArrayList(SPOTS_PARAM);
    mSpotId = getArguments().getString(SPOT_ID_PARAM);
    mBase64Icon = getArguments().getString(ICON_PARAM);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_xamoom_map, container, false);
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

    mSpotTitleTextView = (TextView) view.findViewById(R.id.spot_title_text_view);
    mSpotExcerptTextView = (TextView) view.findViewById(R.id.spot_excerpt_text_view);
    mSpotImageView = (ImageView) view.findViewById(R.id.spot_image_view);
    mSpotContentButton = (Button) view.findViewById(R.id.spot_content_button);
    mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.spot_fab);
    mFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources()
        .getColor(R.color.googlemaps_linkblock_background_color)));
    mMapCloseButton = (Button) view.findViewById(R.id.map_close_button);

    View bottomSheet = view.findViewById(R.id.bottom_sheet);
    mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    mBottomSheetBehavior.setPeekHeight(300);
    mBottomSheetBehavior.setHideable(true);
    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    mFloatingActionButton.hide();

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
        if (mActiveMarker != null) {
          zoomToMarker(mActiveMarker, true);
        }
      }
    });

    mMapCloseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().popBackStack();
      }
    });

    return view;
  }

  private void setBottomPadding(int padding) {
    mGoogleMap.setPadding(0,0,0,padding);
  }

  private void addMarkerToMap(List<Spot> spotList) {
    for (Spot s : spotList) {
      Marker marker = mGoogleMap.addMarker(new MarkerOptions()
          .icon(BitmapDescriptorFactory.fromBitmap(ContentBlock9ViewHolderUtils.getIcon(mBase64Icon, getContext())))
          .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
          .title(s.getName())
          .position(new LatLng(s.getLocation().getLatitude(), s.getLocation().getLongitude())));

      mMarkerSpotMap.put(marker, s);

      if (mSpotId != null && s.getId().equalsIgnoreCase(mSpotId)) {
        showActiveMarker(marker);
      }
    }

    if (mSpotId == null && mMarkerSpotMap.keySet().size() > 0) {
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

  private void showActiveMarker(final Marker marker) {
    mActiveMarker = marker;
    mActiveMarker.showInfoWindow();
    zoomToMarker(mActiveMarker, false);
    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    mFloatingActionButton.show();

    final Spot spot = mMarkerSpotMap.get(marker);

    mSpotTitleTextView.setText(spot.getName());
    mSpotExcerptTextView.setText(spot.getDescription());
    Glide.with(this)
        .load(spot.getPublicImageUrl())
        .into(mSpotImageView);

    if (spot.getContent() != null && spot.getContent().getId() != null) {
      mSpotContentButton.setVisibility(View.VISIBLE);
      final String contentId = spot.getContent().getId();
      mSpotContentButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mListener.clickedSpotMapContentLink(contentId);
        }
      });
    } else {
      mSpotContentButton.setVisibility(View.GONE);
    }

    mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        String query = lat + "," + lon + "(" + spot.getName() + ")";
        Uri gmmIntentUri = Uri.parse("geo:"+lat+"+,"+lon+"?q="+query);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
          startActivity(mapIntent);
        }
      }
    });
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mGoogleMap = googleMap;
    mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

    mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
      @Override
      public void onMapLoaded() {
        if (mSpotList != null) {
          addMarkerToMap(mSpotList);
        }
      }
    });

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

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    Activity activity;
    if (context instanceof Activity){
      activity= (Activity)context;

      try {
        mListener = (XamoomContentFragment.OnXamoomContentFragmentInteractionListener) activity;
      } catch (ClassCastException e) {
        throw new ClassCastException(activity.toString()
            + " must implement OnXamoomContentFragmentInteractionListener");
      }
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }
}
