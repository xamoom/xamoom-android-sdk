package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xamoom.android.APICallback;
import com.xamoom.android.XamoomEndUserApi;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType9;
import com.xamoom.android.mapping.Spot;
import com.xamoom.android.mapping.SpotMap;
import com.xamoom.android.xamoomcontentblocks.R;

import java.io.UnsupportedEncodingException;

import at.theengine.android.bestlocation.BestLocationListener;
import at.theengine.android.bestlocation.BestLocationProvider;
import retrofit.RetrofitError;

/**
 * SpotMapBlock
 */
public class ContentBlock9ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
    private Fragment mFragment;
    private String mApiKey;
    private TextView mTitleTextView;
    private SupportMapFragment mMapFragment;
    private ContentBlockType9 mContentBlock;
    private GoogleMap mGoogleMap;
    private LinearLayout mRootLayout;
    private BestLocationProvider mBestLocationProvider;
    private Location mUserLocation;

    private static int mFrameId = 169147;
    private int mUniqueFrameId;

    public ContentBlock9ViewHolder(View itemView, Fragment fragment, String apiKey) {
        super(itemView);
        mFragment = fragment;
        mApiKey = apiKey;
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mRootLayout = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        mMapFragment = new SupportMapFragment().newInstance();

        //setting up map fragment
        FrameLayout parentFrameLayout = (FrameLayout) itemView.findViewById(R.id.map);

        mUniqueFrameId = mFrameId;
        mFrameId++;

        FrameLayout uniqueFrameLayout = new FrameLayout(mFragment.getActivity());
        uniqueFrameLayout.setId(mUniqueFrameId);

        parentFrameLayout.addView(uniqueFrameLayout);

        mFragment.getChildFragmentManager().beginTransaction().replace(uniqueFrameLayout.getId(), mMapFragment).commit();
    }

    public void setupContentBlock(ContentBlockType9 cb9) {
        mTitleTextView.setVisibility(View.VISIBLE);
        if (cb9.getTitle() != null)
            mTitleTextView.setText(cb9.getTitle());
        else
            mTitleTextView.setVisibility(View.GONE);

        mContentBlock = cb9;

        if (mGoogleMap == null)
            mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;

        final ArrayMap<Marker, Spot> mMarkerArray = new ArrayMap<>();

        XamoomEndUserApi.getInstance(mFragment.getActivity().getApplicationContext(), mApiKey).getSpotMap(null, mContentBlock.getSpotMapTag().split(","), null, new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                if (mMapFragment.isAdded()) {
                    //setup location
                    setupLocation();

                    googleMap.getUiSettings().setZoomControlsEnabled(true);

                    Bitmap icon;
                    if (result.getStyle().getCustomMarker() != null) {
                        String iconString = result.getStyle().getCustomMarker();
                        icon = getIconFromBase64(iconString, mFragment);
                    } else {
                        icon = BitmapFactory.decodeResource(mFragment.getResources(), R.drawable.ic_default_map_marker);
                        //float imageRatio = (float) icon.getWidth() / (float) icon.getHeight();
                        //icon = Bitmap.createScaledBitmap(icon, 70, (int) (70 / imageRatio), false);
                    }

                    //show all markers
                    for (Spot s : result.getItems()) {
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                                .title(s.getDisplayName())
                                .position(new LatLng(s.getLocation().getLat(), s.getLocation().getLon())));

                        mMarkerArray.put(marker, s);
                    }

                    //zoom to display all markers
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : mMarkerArray.keySet()) {
                        builder.include(marker.getPosition());
                    }

                    int deviceWidth = 1000;
                    if (mFragment.isAdded())
                        deviceWidth = mFragment.getResources().getDisplayMetrics().widthPixels;

                    LatLngBounds bounds = builder.build();
                    bounds = adjustBoundsForMaxZoomLevel(bounds);
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, deviceWidth, deviceWidth, 70);

                    googleMap.moveCamera(cu);

                    //click listener to move camera to spot and show the complete infowindow
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(final Marker marker) {
                            mRootLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    int zoom = (int) mGoogleMap.getCameraPosition().zoom;
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude + (double) 90 / Math.pow(2, zoom), marker.getPosition().longitude), zoom);
                                    mGoogleMap.animateCamera(cu);
                                }
                            });

                            marker.showInfoWindow();

                            return false;
                        }
                    });

                    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        private TextView mNameTextView;
                        private TextView mDescriptionTextView;
                        private TextView mDistanceTextView;
                        private ImageView mImageView;

                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            Spot spot = mMarkerArray.get(marker);
                            if (spot == null) {
                                return null;
                            } else {
                                View v;

                                if (mFragment.isAdded()) {
                                    v = mFragment.getActivity().getLayoutInflater().inflate(R.layout.info_window, null);
                                } else {
                                    return null;
                                }

                                mNameTextView = (TextView) v.findViewById(R.id.infoWindowNameTextView);
                                mDescriptionTextView = (TextView) v.findViewById(R.id.infoWindowDescriptionTextView);
                                mImageView = (ImageView) v.findViewById(R.id.infoWindowImageView);
                                mDistanceTextView = (TextView) v.findViewById(R.id.infoWindowDistanceTextView);

                                if (spot.getDisplayName() != null)
                                    mNameTextView.setText(spot.getDisplayName());

                                if (spot.getDescription() != null)
                                    mDescriptionTextView.setText(spot.getDescription());
                                else
                                    mDescriptionTextView.setVisibility(View.GONE);

                                if (spot.getImage() != null) {
                                    int width = (int) (200 * mFragment.getResources().getDisplayMetrics().density);
                                    int height = (int) (150 * mFragment.getResources().getDisplayMetrics().density);
                                    Picasso.with(mFragment.getActivity()).load(spot.getImage()).resize(width, height).centerCrop().into(mImageView, new MarkerCallback(marker));
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

                                return v;
                            }
                        }
                    });
                }
            }

            @Override
            public void error(RetrofitError error) {
                Log.e("xamoom-android-sdk", "Error:" + error);
            }
        });
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

    /**
     * Decodes a base64 string to an icon for mapMarkers.
     * Can handle normal image formats and also svgs.
     * The icon will be resized to width: 70, height will be resized to maintain imageRatio.
     *
     * @param base64String Base64 string that will be resized. Must start with "data:image/"
     * @return icon as BitMap, or null if there was a problem
     */
    public Bitmap getIconFromBase64(String base64String, Fragment fragment) {
        Bitmap icon = null;
        byte[] data1;
        byte[] data2 = "".getBytes();
        String decodedString1 = "";
        String decodedString2 = "";
        float newImageWidth = 25.0f;

        if (fragment.isAdded()) {
            //image will be resized depending on the density of the screen
            newImageWidth = newImageWidth * fragment.getResources().getDisplayMetrics().density;
        }

        if (base64String == null)
            return null;

        try {
            //encode 2 times
            data1 = Base64.decode(base64String, Base64.DEFAULT);
            decodedString1 = new String(data1, "UTF-8");

            //get rid of image/xxxx base64,
            int index = decodedString1.indexOf("base64,");
            String decodedString1WithoutPrefix = decodedString1.substring(index + 7);

            data2 = Base64.decode(decodedString1WithoutPrefix, Base64.DEFAULT);
            decodedString2 = new String(data2, "UTF-8");

            if (decodedString1.contains("data:image/svg+xml")) {
                //svg stuff
                SVG svg = null;
                svg = SVG.getFromString(decodedString2);

                if (svg != null) {
                    Log.v("pingeborg", "HELLYEAH SVG: " + svg);

                    //resize svg
                    float imageRatio = svg.getDocumentWidth() / svg.getDocumentHeight();
                    svg.setDocumentWidth(newImageWidth);
                    svg.setDocumentHeight(newImageWidth / imageRatio);

                    icon = Bitmap.createBitmap((int) svg.getDocumentWidth(), (int) svg.getDocumentHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas1 = new Canvas(icon);
                    svg.renderToCanvas(canvas1);
                }
            } else if (decodedString1.contains("data:image/")) {
                //normal image stuff
                icon = BitmapFactory.decodeByteArray(data2, 0, data2.length);
                //resize the icon
                double imageRatio = (double) icon.getWidth() / (double) icon.getHeight();
                double newHeight = newImageWidth / imageRatio;
                icon = Bitmap.createScaledBitmap(icon, (int) newImageWidth, (int) newHeight, false);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        return icon;
    }

    private void setupLocation() {
        mBestLocationProvider = new BestLocationProvider(mFragment.getActivity(), false, true, 1000, 1000, 5, 10);
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
            public void onLocationUpdate(Location location, BestLocationProvider.LocationType type, boolean isFresh) {
                if (isFresh) {
                    Log.i("pingeborg", "onLocationUpdate TYPE:" + type + " Location:" + mBestLocationProvider.locationToString(location));
                    mUserLocation = location;
                }
            }
        };

        //start Location Updates
        mBestLocationProvider.startLocationUpdatesWithListener(mBestLocationListener);
    }
}