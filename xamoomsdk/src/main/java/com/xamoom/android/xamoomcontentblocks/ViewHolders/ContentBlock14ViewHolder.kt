package com.xamoom.android.xamoomcontentblocks.ViewHolders

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.net.Uri
import android.os.Bundle
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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.xamoom.android.xamoomcontentblocks.Views.CustomMapViewWithChart
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment
import com.xamoom.android.xamoomsdk.APICallback
import com.xamoom.android.xamoomsdk.APIListCallback
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.Enums.SpotFlags
import com.xamoom.android.xamoomsdk.R
import com.xamoom.android.xamoomsdk.Resource.ContentBlock
import com.xamoom.android.xamoomsdk.Resource.Spot
import com.xamoom.android.xamoomsdk.Resource.Style
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.math.BigDecimal
import java.math.MathContext
import java.net.URL
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("ClickableViewAccessibility")
class ContentBlock14ViewHolder(val view: CustomMapViewWithChart, bundle: Bundle?, val enduserApi: EnduserApi, fragment: Fragment, val listener: XamoomContentFragment.OnXamoomContentFragmentInteractionListener,
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
    var elevationChart = view.chart
    var mLastLocation: Location? = null
    var fragment: Fragment
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var routeCameraPosition: CameraPosition? = null

    init {
        mContext = fragment.context
        mapView.onCreate(bundle)
        this.fragment = fragment

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
        if (mapBoxMap != null && routeCameraPosition != null) {
            mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(routeCameraPosition!!))
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
                mapView.getMapAsync(this@ContentBlock14ViewHolder)
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
        var style = DEFAULT_MAPBOX_STYLE_STRING
        if (!mapBoxStyleString.isNullOrEmpty()) {
            style = mapBoxStyleString!!
        }


        mapboxMap.setMaxZoomPreference(17.4)
        mapBoxMap!!.setStyle(style) { style ->
            showRoute()

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

//            if (mSpotList.size > 1) {
//                mapBoxMap!!.animateCamera(ContentBlock9ViewHolderUtils.zoomToDisplayAllSpots(mSpotList, 50))
//            } else if (mSpotList.size == 1) {
//                val position = CameraPosition.Builder().target(LatLng(mSpotList[0].location.latitude, mSpotList[0].location.longitude)).zoom(10.0).tilt(0.0).build()
//                    mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))
//            }

            enableLocationComponent(mapBoxStyle!!)
        }

        //TODO: needed to rewrite this method
        showElevationGraphAndCalculateCameraPosition()

    }


    private fun showRoute() {
        fragment.activity?.runOnUiThread {
            mapBoxMap?.getStyle {
                it.addSource(GeoJsonSource("line-source", URL(mContentBlock?.fileId)))
                it.addLayer(LineLayer("linelayer", "line-source")
                        .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                                PropertyFactory.lineWidth(4f),
                                PropertyFactory.lineColor(Color.parseColor("#D0A9F5"))))
            }
        }
    }


    //TODO: needed to rewrite this method
    //Drawing elevation graph and calculating camera position
    private fun showElevationGraphAndCalculateCameraPosition() {
        val url = mContentBlock?.fileId
        val client = OkHttpClient()
        if (url != null) {
            val request = Request.Builder()
                    .get()
                    .url(url)
                    .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Content block 14 Tours request failure")
                }

                @SuppressLint("SimpleDateFormat")
                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body()?.string()


                    val firstFeature = JSONObject(responseBody).getJSONArray("features").getJSONObject(0)

                    val geometry = firstFeature.getJSONObject("geometry")
                    val coordinates = geometry.getJSONArray("coordinates")

                    //TODO: rewrite calculating routecameraposition
                    val middleCoordinate = coordinates.getJSONArray(coordinates.length() / 2)
                    fragment.activity?.runOnUiThread{
                        routeCameraPosition = CameraPosition.Builder().target(LatLng(middleCoordinate.getDouble(1), middleCoordinate.getDouble(0))).zoom(13.0).tilt(0.0).build()
                        mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(routeCameraPosition!!))
                    }

                    if(!mContentBlock!!.showElevation) return

                    val values = arrayListOf<Entry>()
                    var distance = BigDecimal(0.0, MathContext.DECIMAL64)
                    var previousLtd = 0.0
                    var previousLong = 0.0


                    for (i in 0 until coordinates.length()) {
                        val coordinate = coordinates.getJSONArray(i)
                        var longitude: Double?
                        var latitude: Double?
                        var altitude: Double?
                        try {
                            longitude = coordinate.getDouble(0)
                            latitude = coordinate.getDouble(1)

                            print("longitude latitude $longitude")
                            println(" $latitude")
                            if (coordinate.length() > 2) altitude = coordinate.getDouble(2)
                            else return


                            if (i != 0) {
                                distance += getDistanceBetweenPoints(previousLtd, previousLong, latitude, longitude)
                            }
                            println("x(dist) is $distance")
                            println("y(alt) is $altitude")
                            previousLong = longitude
                            previousLtd = latitude
                            values.add(Entry(distance.toFloat(), altitude.toFloat()))
                        } catch (e: NumberFormatException) {
                            println("Number format exception while trying to parse CB 14 route coordinates $e")
                        }
                    }

                    fragment.activity?.runOnUiThread {
                        elevationChart.visibility = View.VISIBLE
                        val lineDataSet = LineDataSet(values, "Elevation chart")
                        lineDataSet.lineWidth = 3f
                        lineDataSet.fillAlpha = 100
                        lineDataSet.setDrawCircles(false)
                        lineDataSet.setDrawValues(false)
                        lineDataSet.color = Color.parseColor("#D0A9F5")
                        elevationChart.description.isEnabled = false
                        elevationChart.setTouchEnabled(false)
                        elevationChart.axisRight.isEnabled = false
                        elevationChart.axisLeft.setLabelCount(3, true)
                        elevationChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                        elevationChart.data = LineData(arrayListOf<ILineDataSet>(lineDataSet))

                    }
                }
            })
        }


    }


    private fun getDistanceBetweenPoints(lat1: Double, lon1: Double, lat2: Double, lon2: Double): BigDecimal {
        val theta = lon1 - lon2
        var dist = (sin(degToRad(lat1))
                * sin(degToRad(lat2))
                + (cos(degToRad(lat1))
                * cos(degToRad(lat2))
                * cos(degToRad(theta))))
        dist = acos(dist)
        dist = radToDeg(dist)
        //60 * 1.1515 is in miles and * 1.609344 is in kilometres
        dist *= 60 * 1.1515 * 1.609344
        return BigDecimal(dist, MathContext.DECIMAL64)

    }

    private fun degToRad(deg: Double): Double {
        return (deg * Math.PI / 180.0)
    }

    private fun radToDeg(rad: Double): Double {
        return (rad * 180.0 / Math.PI)
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
        spotTitleTextView.measure(0, 0)
        spotExcerptTextView.text = spot.description

        var imageParams = spotImageView.layoutParams
        imageParams.height = mapView.height / 2 - spotTitleTextView.height - (8 * Resources.getSystem().displayMetrics.density).toInt() * 3
        imageParams.width = imageParams.height
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
        const val DEFAULT_MAPBOX_STYLE_STRING = "mapbox://styles/xamoom-georg/ck4zb0mei1l371coyi41snaww"
    }
}