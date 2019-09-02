package com.xamoom.android.xamoomcontentblocks.ViewHolders

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import at.rags.morpheus.Error
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.xamoom.android.xamoomcontentblocks.Views.CustomMapView
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment
import com.xamoom.android.xamoomsdk.APICallback
import com.xamoom.android.xamoomsdk.APIListCallback
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.Enums.SpotFlags
import com.xamoom.android.xamoomsdk.R
import com.xamoom.android.xamoomsdk.Resource.ContentBlock
import com.xamoom.android.xamoomsdk.Resource.Spot
import com.xamoom.android.xamoomsdk.Resource.Style
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("ClickableViewAccessibility")
class ContentBlock9ViewHolder(val view: CustomMapView, bundle: Bundle?, val enduserApi: EnduserApi, fragment: Fragment, val listener: XamoomContentFragment.OnXamoomContentFragmentInteractionListener,
                              private val mapBoxStyleString: String? = com.mapbox.mapboxsdk.maps.Style.MAPBOX_STREETS, private val navigationTintColorString: String?, private val contentButtonTextColorString: String?, private val navigationMode: String? = "d") : RecyclerView.ViewHolder(view), OnMapReadyCallback {

    private var mapBoxStyle: com.mapbox.mapboxsdk.maps.Style? = null
    private var mapBoxMap: MapboxMap? = null
    private var mContext: Context? = null
    private var mContentBlock: ContentBlock? = null
    private var mBase64Icon: String? = null
    private var closeBottomSheet = false
    private var mSpotList: ArrayList<Spot> = arrayListOf()
    var mActiveSpot: Spot? = null
    var showContentLinks: Boolean = false
    private var mTextColor: Int? = null
    var mapView = view.mapView
    var titleView = view.textView
    var bottomSheet = view.bottomSheetBehavior
    var centerspotButton = view.centerSpotsButton
    var centerUserButton = view.centerUserButton
    var spotTitleTextView = view.spotTitleTextView
    var spotExcerptTextView = view.spotExcerptTextView
    var spotContentButton = view.spotContentButton
    var spotNavigationButton = view.spotNavigationButton
    var spotImageView = view.spotImageView
    var mLastLocation: Location? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    init {
        mContext = fragment.context
        mapView.onCreate(bundle)

        mapView.setOnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            mapView.onTouchEvent(motionEvent)

            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                closeBottomSheet = true
            }

            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                closeBottomSheet = true
            }

            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (closeBottomSheet) {
                    bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                    mActiveSpot = null
                } else {
                    closeBottomSheet = true
                }
            }
            true
        }

        bottomSheet.peekHeight = 100
        bottomSheet.isHideable = true
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

        if (!contentButtonTextColorString.isNullOrEmpty()) {
            try {
                spotContentButton.setTextColor(Color.parseColor(contentButtonTextColorString))
            } catch (exc: IllegalArgumentException) {
            }
        }

        val fineLocationPermission = ContextCompat.checkSelfPermission(mContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION)
        val locationPermission = ContextCompat.checkSelfPermission(mContext!!,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED
                || locationPermission != PackageManager.PERMISSION_GRANTED) {
            centerUserButton.visibility = View.GONE
        } else {
            centerUserButton.visibility = View.VISIBLE
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(fragment.activity!!)
            getLastLocation(fragment.activity!!)
        }

        centerUserButton.setOnClickListener {
            if (mapBoxMap != null && mLastLocation != null) {
                val position = CameraPosition.Builder().target(LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude)).zoom(16.0).tilt(0.0).build()
                mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))
            }
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        }

        centerspotButton.setOnClickListener {
            centerSpotBounds()
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        }

        spotContentButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor(navigationTintColorString))
        spotContentButton.setTextColor(Color.parseColor(contentButtonTextColorString))
        spotNavigationButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor(navigationTintColorString))
        spotNavigationButton.setTextColor(Color.parseColor(contentButtonTextColorString))

        centerspotButton.requestLayout()
        centerUserButton.requestLayout()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(activity: Activity) {
        mFusedLocationClient!!.lastLocation
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLastLocation = task.result
                    }
                }
    }

    fun setupContentBlock(contentBlock: ContentBlock, offline: Boolean, showContentInSpotMap: Boolean) {
        mContentBlock = contentBlock
        showContentLinks = showContentInSpotMap
        titleView.text = contentBlock.title

        if (mSpotList.isEmpty()) {
            downloadAllSpots(mContentBlock!!.spotMapTags, null, object : APIListCallback<List<Spot>, List<Error>> {
                override fun finished(result: List<Spot>, cursor: String, hasMore: Boolean) {
                }

                override fun error(error: List<Error>) {
                    Log.e("", "Error:$error")
                }
            })
        }
    }

    private fun centerSpotBounds() {
        if (mapBoxMap != null && mSpotList.isNotEmpty()) {
            val latLngArray = java.util.ArrayList<LatLng>()

            for (spot in mSpotList) {
                latLngArray.add(com.mapbox.mapboxsdk.geometry.LatLng(spot.getLocation().getLatitude(), spot.getLocation().getLongitude()))
            }

            if (!latLngArray.isEmpty()) {
                val builder = LatLngBounds.Builder()
                for (latLng in latLngArray) {
                    builder.include(latLng)
                }

                val bounds = builder.build()
                mapBoxMap!!.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngBounds(bounds, 50))
            }
        }
    }

    private fun downloadAllSpots(tags: List<String>, cursor: String?,
                                 callback: APIListCallback<List<Spot>, List<Error>>) {

        var spotOptions = EnumSet.of(SpotFlags.HAS_LOCATION)
        if (showContentLinks) {
            spotOptions = EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.HAS_LOCATION)
        }

        enduserApi.getSpotsByTags(tags, PAGE_SIZE, cursor, spotOptions, null, object : APIListCallback<List<Spot>, List<Error>> {
            override fun finished(result: List<Spot>, cursor: String, hasMore: Boolean) {
                if (!result.isEmpty()) {
                    mSpotList.addAll(result)
                    if (hasMore) {
                        downloadAllSpots(tags, cursor, callback)
                    } else {
                        getStyle(result[0].system.id)
                        callback.finished(mSpotList, "", false)
                    }
                } else {
                    centerspotButton.visibility = View.GONE
                }
            }

            override fun error(error: List<Error>) {
                callback.error(error)
            }
        })
    }

    private fun getStyle(systemId: String) {
        enduserApi.getStyle(systemId, object : APICallback<Style, List<Error>> {
            override fun finished(result: Style) {
                mBase64Icon = result.customMarker
                if (mapView.isDestroyed) {
                    return
                }
                mapView.getMapAsync(this@ContentBlock9ViewHolder)
            }

            override fun error(error: List<Error>) {
                Log.e("", "getStyle error: $error")
            }
        })
    }

    fun setStyle(style: Style?) {
        if (style != null) {
            if (style.foregroundFontColor != null) {
                mTextColor = Color.parseColor(style.foregroundFontColor)
            }
            if (style.customMarker != null) {
                mBase64Icon = style.customMarker
            }
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        if (mapView.isDestroyed) {
            return
        }

        this.mapBoxMap = mapboxMap
        var style = com.mapbox.mapboxsdk.maps.Style.MAPBOX_STREETS
        if (!mapBoxStyleString.isNullOrEmpty()) {
            style = mapBoxStyleString!!
        }
        mapboxMap.setMaxZoomPreference(17.4)
        mapBoxMap!!.setStyle(style) { style ->
            var image = ContentBlock9ViewHolderUtils.getIcon(mBase64Icon, mContext)
            val icon = IconFactory.getInstance(mContext!!)

            var markers: ArrayList<Marker> = arrayListOf()
            for (s in mSpotList) {
                val marker = MarkerOptions()
                        .position(LatLng(s.location.latitude, s.location.longitude))
                        .title(s.name)
                        .icon(icon.fromBitmap(image))
                markers.add(mapboxMap.addMarker(marker))
            }

            mapboxMap.setOnMarkerClickListener { marker ->
                val markerindex = markers.indexOf(marker)
                val spot = mSpotList.get(markerindex)

                showSpotDetails(spot)
                return@setOnMarkerClickListener true
            }

            mapBoxStyle = style
            if (mSpotList.size > 1) {
                mapBoxMap!!.animateCamera(ContentBlock9ViewHolderUtils.zoomToDisplayAllSpots(mSpotList, 50))
            } else if (mSpotList.size == 1) {
                val position = CameraPosition.Builder().target(com.mapbox.mapboxsdk.geometry.LatLng(mSpotList[0].location.latitude, mSpotList[0].location.longitude)).zoom(10.0).tilt(0.0).build()
                mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))
            }

            enableLocationComponent(mapBoxStyle!!)
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: com.mapbox.mapboxsdk.maps.Style) {
        if (PermissionsManager.areLocationPermissionsGranted(mContext!!)) {
            val locationComponent = mapBoxMap!!.locationComponent

            locationComponent.activateLocationComponent(mContext!!, loadedMapStyle)

            locationComponent.isLocationComponentEnabled = true
        } else {
            centerUserButton.visibility = View.GONE
        }
    }

    private fun showSpotDetails(spot: Spot) {
        mActiveSpot = spot

        val position = CameraPosition.Builder().target(com.mapbox.mapboxsdk.geometry.LatLng(spot.location.latitude - 0.0005, spot.location.longitude)).zoom(16.0).tilt(0.0).build()
        mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))

        spotTitleTextView.text = spot.name
        spotTitleTextView.measure(0,0)
        spotExcerptTextView.text = spot.description

        var imageParams = spotImageView.layoutParams
        imageParams.height =  mapView.height / 2 - spotTitleTextView.height - (8 * Resources.getSystem().displayMetrics.density).toInt() * 3
        imageParams.width =  imageParams.height
        spotImageView.layoutParams = imageParams
        spotImageView.requestLayout()

        Glide.with(mContext!!)
                .load(spot.publicImageUrl)
                .dontAnimate()
                .dontTransform()
                .override(spotImageView.layoutParams.height, spotImageView.layoutParams.width)
                .into(spotImageView)

        if (spot.content != null && spot.content.id != null) {
            spotContentButton.visibility = View.VISIBLE
            val contentId = spot.content.id
            spotContentButton.setOnClickListener {
                listener.clickedSpotMapContentLink(contentId)
            }
        } else {
            spotContentButton.visibility = View.GONE
        }

        spotNavigationButton.setOnClickListener {
            val urlString = String.format(Locale.US, "google.navigation:q=%f,%f&mode=%s", spot.location.latitude, spot.location.longitude, this.navigationMode)
            val gmmIntentUri = Uri.parse(urlString)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(mContext!!.getPackageManager()) != null) {
                mContext!!.startActivity(mapIntent)
            }
        }
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    companion object {
        val PAGE_SIZE = 100
    }
}