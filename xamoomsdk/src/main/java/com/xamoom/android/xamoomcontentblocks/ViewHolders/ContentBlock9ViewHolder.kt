package com.xamoom.android.xamoomcontentblocks.ViewHolders

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import at.rags.morpheus.Error
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import com.mapbox.mapboxsdk.maps.UiSettings
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

@SuppressLint("ClickableViewAccessibility")
class ContentBlock9ViewHolder(val view: CustomMapView, bundle: Bundle?, val enduserApi: EnduserApi, fragment: Fragment, val listener: XamoomContentFragment.OnXamoomContentFragmentInteractionListener,
                              private val mapBoxStyleString: String? = DEFAULT_MAPBOX_STYLE_STRING, private val navigationTintColorString: String?, private val contentButtonTextColorString: String?, private val navigationMode: String? = "d") : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), OnMapReadyCallback {

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
    var fragment: Fragment
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    var sharedPreferences: SharedPreferences? = null

    init {
        mContext = fragment.context
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        mapView.onCreate(bundle)
        this.fragment = fragment

        mapView.setOnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            mapView.onTouchEvent(motionEvent)

            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                closeBottomSheet = true
                if (mapBoxMap != null) {
                    if (motionEvent.pointerCount == 2) {
                        val uiSettings: UiSettings = mapBoxMap!!.uiSettings
                        uiSettings.isScrollGesturesEnabled = true
                    } else {
                        val uiSettings: UiSettings = mapBoxMap!!.uiSettings
                        uiSettings.isScrollGesturesEnabled = false
                    }
                }
            }

            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                closeBottomSheet = true
                if (mapBoxMap != null) {
                    if (motionEvent.pointerCount == 2) {
                        val uiSettings: UiSettings = mapBoxMap!!.uiSettings
                        uiSettings.isScrollGesturesEnabled = true
                    }
                }
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

        if (fineLocationPermission == PackageManager.PERMISSION_GRANTED
                && locationPermission == PackageManager.PERMISSION_GRANTED) {
            updateLocation()
        } else {
            val icon = mContext!!.resources.getDrawable(R.drawable.ic_user_location)
            val newIcon = icon.constantState?.newDrawable()
            newIcon?.mutate()?.setColorFilter(Color.parseColor("#D3D3D3"), PorterDuff.Mode.SRC_ATOP)
            centerUserButton.setImageDrawable(newIcon)
        }

        centerUserButton.setOnClickListener {

            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED && locationPermission != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(mContext!!, android.Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(mContext!!, android.Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(fragment.activity!!, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
                } else {
                    centerSpotToUserLocation()
                }
            } else {
                if (mLastLocation != null) {
                    centerSpotToUserLocation()
                } else {
                    AlertDialog.Builder(fragment.activity!!)
                            .setTitle(mContext!!.resources.getString(R.string.no_location_alert_title))
                            .setMessage(mContext!!.resources.getString(R.string.no_location_alert_message))
                            .setCancelable(false)
                            .setNegativeButton(mContext!!.resources.getString(R.string.no_location_alert_cancel)) { p0, _ ->
                                p0!!.dismiss()
                            }
                            .setPositiveButton(mContext!!.resources.getString(R.string.no_location_alert_settings)) { _, _ ->
                                startActivityForResult(fragment.activity!!, Intent(android.provider.Settings.ACTION_SETTINGS), 0, null)
                            }.show()
                }
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
                    var color = Color.parseColor("#D3D3D3")
                    if (task.isSuccessful && task.result != null) {
                        mLastLocation = task.result
                        color = Color.BLACK
                    }

                    val icon = mContext!!.resources.getDrawable(R.drawable.ic_user_location)
                    val newIcon = icon.constantState?.newDrawable()
                    newIcon?.mutate()?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                    centerUserButton.setImageDrawable(newIcon)
                }
    }

    fun updateLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(fragment.activity!!)
        getLastLocation(fragment.activity!!)
    }

    fun centerSpotToUserLocation() {
        if (mapBoxMap != null && mLastLocation != null) {
            val position = CameraPosition.Builder().target(LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude)).zoom(16.0).tilt(0.0).build()
            mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))
        }
    }

    fun setupContentBlock(contentBlock: ContentBlock, offline: Boolean, showContentInSpotMap: Boolean) {
        mContentBlock = contentBlock
        showContentLinks = showContentInSpotMap
        titleView.text = contentBlock.title

        if (contentBlock.title.isNullOrEmpty()) {
            titleView.visibility = View.GONE
        } else {
            titleView.visibility = View.VISIBLE
        }

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

            if (latLngArray.size > 1) {
                val builder = LatLngBounds.Builder()
                for (latLng in latLngArray) {
                    builder.include(latLng)
                }
                val bounds = builder.build()
                mapBoxMap!!.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngBounds(bounds, 50))
            } else if (latLngArray.size == 1) {
                mapBoxMap!!.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder().target(latLngArray[0]).zoom(16.0).tilt(0.0).build()))
            }
        }
    }

    private fun downloadAllSpots(tags: List<String>, cursor: String?,
                                 callback: APIListCallback<List<Spot>, List<Error>>) {

        var spotOptions = EnumSet.of(SpotFlags.HAS_LOCATION)
        if (showContentLinks) {
            spotOptions = EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.HAS_LOCATION)
        }

        val langPickerLanguage: String? = sharedPreferences?.getString("current_language_code", null)
        if (langPickerLanguage != null) enduserApi.language = langPickerLanguage else enduserApi.language = enduserApi.systemLanguage
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

        val uiSettings: UiSettings = mapboxMap.uiSettings
        uiSettings.isTiltGesturesEnabled = false

        this.mapBoxMap = mapboxMap
        var style = DEFAULT_MAPBOX_STYLE_STRING
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


        val imageUrl = spot.publicImageUrl
        if(imageUrl != null && imageUrl.endsWith(".gif")) {
            Glide.with(mContext!!)
                    .load(spot.publicImageUrl)
                    .asGif()
                    .dontAnimate()
                    .dontTransform()
                    .override(spotImageView.layoutParams.height, spotImageView.layoutParams.width)
                    .into(spotImageView)

        } else {
            Glide.with(mContext!!)
                    .load(spot.publicImageUrl)
                    .dontAnimate()
                    .dontTransform()
                    .override(spotImageView.layoutParams.height, spotImageView.layoutParams.width)
                    .into(spotImageView)
        }

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
        const val DEFAULT_MAPBOX_STYLE_STRING = "mapbox://styles/xamoom-georg/ck4zb0mei1l371coyi41snaww"
    }
}