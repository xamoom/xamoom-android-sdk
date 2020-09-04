package com.xamoom.android.xamoomcontentblocks.ViewHolders

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import at.rags.morpheus.Error
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
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
import java.text.DecimalFormat
import java.util.*
import kotlin.math.acos
import kotlin.math.ceil
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
    var elevationImperialRadioButton = view.imperialRadioButton
    var elevationMetricRadioButton = view.metricRadioButton
    var elevationRadioGroup = view.elevationRadioGroup
    var zoomIn = view.zoomInButton
    var zoomOut = view.zoomOutButton
    var infoButton = view.infoButton
    var mLastLocation: Location? = null
    var fragment: Fragment
    private lateinit var graphColor: String
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var shownCoordinates = arrayListOf<LatLng>()
    private var elevationMetricValues = arrayListOf<Entry>()
    private var elevationImperialValues = arrayListOf<Entry>()
    private var imperialTotalDistance = BigDecimal(0.0, MathContext.DECIMAL64)
    private var metricTotalDistance = BigDecimal(0.0, MathContext.DECIMAL64)
    private var ascentMetres = 0.0
    private var descentMetres = 0.0
    private var ascentFeet = 0.0
    private var descentFeet = 0.0
    private var routeSpentTime = ""
    private var infoTitle = ""
    private var isCurrentMetric = true

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

        elevationImperialRadioButton.setOnClickListener {
            setElevationGraphData(elevationImperialValues)
            isCurrentMetric = false
        }

        elevationMetricRadioButton.setOnClickListener {
            setElevationGraphData(elevationMetricValues)
            isCurrentMetric = true
        }
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
        if (mapBoxMap != null) {
            mapBoxMap!!.animateCamera(zoomToDisplayRouteAndAllSpots(shownCoordinates)!!)
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
                    for (spot in mSpotList) {
                        shownCoordinates.add(LatLng(spot.location.latitude, spot.location.longitude))
                    }
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
                graphColor = result.highlightFontColor
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

            enableLocationComponent(mapBoxStyle!!)
        }

        setExtraButtons()
        calculateCoordinates()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setExtraButtons() {
        zoomIn.setOnClickListener {
            val previousCameraPosition = mapBoxMap?.cameraPosition
            mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(previousCameraPosition!!.target).zoom(previousCameraPosition.zoom + 1).tilt(previousCameraPosition.tilt).build()))
        }

        zoomOut.setOnClickListener {
            val previousCameraPosition = mapBoxMap?.cameraPosition
            mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(previousCameraPosition!!.target).zoom(previousCameraPosition.zoom - 1).tilt(previousCameraPosition.tilt).build()))
        }

        mapBoxMap!!.uiSettings.isCompassEnabled = true
        mapBoxMap!!.uiSettings.compassGravity = Gravity.START
        mapBoxMap!!.uiSettings.setCompassFadeFacingNorth(false)
    }


    private fun showRoute() {
        fragment.activity?.runOnUiThread {
            mapBoxMap?.getStyle {
                it.addSource(GeoJsonSource("line-source", URL(mContentBlock?.fileId)))
                it.addLayer(LineLayer("linelayer", "line-source")
                        .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                                PropertyFactory.lineWidth(4f),
                                PropertyFactory.lineColor(Color.parseColor(graphColor))))
            }
        }
    }


    //Drawing elevation graph and calculating camera position
    private fun calculateCoordinates() {
        val url = mContentBlock?.fileId
        val client = OkHttpClient()
        var showGraph = true
        if (url != null) {
            val request = Request.Builder()
                    .get()
                    .url(url)
                    .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Content block 14 Tours request failure")
                }

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                @SuppressLint("SimpleDateFormat", "ResourceType")
                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body()?.string()


                    val firstFeature = JSONObject(responseBody).getJSONArray("features").getJSONObject(0)

                    infoTitle = firstFeature.getJSONObject("properties").getString("name")
                    val geometry = firstFeature.getJSONObject("geometry")
                    val coordinates = geometry.getJSONArray("coordinates")


                    var distanceMetric = BigDecimal(0.0, MathContext.DECIMAL64)
                    var distanceImperial = BigDecimal(0.0, MathContext.DECIMAL64)
                    var previousLtd = 0.0
                    var previousLong = 0.0

                    var previousAltitudeMetres = 0.0
                    var previousAltitudeFeet = 0.0

                    for (i in 0 until coordinates.length()) {
                        val coordinate = coordinates.getJSONArray(i)
                        var longitude: Double?
                        var latitude: Double?
                        var altitude: Double?
                        var feet: Double?
                        try {
                            longitude = coordinate.getDouble(0)
                            latitude = coordinate.getDouble(1)
                            shownCoordinates.add(LatLng(latitude, longitude))

                            print("longitude latitude $longitude")
                            println(" $latitude")
                            if (coordinate.length() > 2) {
                                altitude = coordinate.getDouble(2)
                                feet = altitude * 3.281
                            } else {
                                showGraph = false
                                altitude = null
                                feet = null
                            }

                            if (i == 0) {
                                fragment.activity?.runOnUiThread {
                                    mapBoxMap?.getStyle {
                                        val tourBeginning = fragment.activity?.getDrawable(R.drawable.tour_start_icon)
                                        if(tourBeginning != null) {

                                            val symbolLayerIconFeatureList = mutableListOf<Feature>()
                                            symbolLayerIconFeatureList.add(Feature.fromGeometry(Point.fromLngLat(longitude, latitude)))

                                            it.addImage("tour-start-icon", setTint(tourBeginning, Color.parseColor(graphColor)))
                                            it.addSource(GeoJsonSource("tour-start-source", FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))
                                            it.addLayer(SymbolLayer("tour-start-layer", "tour-start-source")
                                                    .withProperties(
                                                            iconImage("tour-start-icon"),
                                                            iconSize(1.2f),
                                                            iconAllowOverlap(true),
                                                            iconIgnorePlacement(true)
                                                    ))
                                        }
                                    }
                                }
                            }

                            if (i != 0) {
                                val kiloMetres = getDistanceBetweenPoints(previousLtd, previousLong, latitude, longitude)
                                distanceMetric += kiloMetres
                                distanceImperial += kiloMetres.div(BigDecimal(1.609344, MathContext.DECIMAL64))
                                if (altitude != null && feet != null) {
                                    if (altitude - previousAltitudeMetres > 0) {
                                        ascentMetres += altitude - previousAltitudeMetres
                                        ascentFeet += feet - previousAltitudeFeet
                                    } else if (previousAltitudeMetres - altitude > 0) {
                                        descentMetres += previousAltitudeMetres - altitude
                                        descentFeet += previousAltitudeFeet - feet
                                    }
                                }

                            }

                            println("x(dist) in kiloMetres is $distanceMetric")
                            println("y(alt) in metres is $altitude")
                            println("x(dist) in miles is $distanceImperial")
                            println("y(alt) in feet is $feet")
                            previousLong = longitude
                            previousLtd = latitude
                            if (altitude != null && feet != null) {
                                previousAltitudeMetres = altitude
                                previousAltitudeFeet = feet
                                elevationMetricValues.add(Entry(distanceMetric.toFloat(), altitude.toFloat()))
                                elevationImperialValues.add(Entry(distanceImperial.toFloat(), feet.toFloat()))
                            }
                        } catch (e: NumberFormatException) {
                            println("Number format exception while trying to parse CB 14 route coordinates $e")
                        }
                    }
                    metricTotalDistance = distanceMetric
                    imperialTotalDistance = distanceImperial
                    if (distanceImperial.toInt() != 0)
                        routeSpentTime = getTimeInHours(distanceImperial.toDouble() / 3.1) + " h"

                    print("ascent metres feet $ascentMetres $ascentFeet\n")
                    print("total distance metres feet $metricTotalDistance $imperialTotalDistance\n")
                    print("spent time is $routeSpentTime")



                    fragment.activity?.runOnUiThread {
                        mapBoxMap?.animateCamera(zoomToDisplayRouteAndAllSpots(shownCoordinates)!!)
                        showInfoButton()
                        elevationRadioGroup.visibility = View.VISIBLE
                        elevationMetricRadioButton.isChecked = true
                        if (mContentBlock!!.showElevation && showGraph) {
                            setElevationGraphParams()
                            setElevationGraphData(elevationMetricValues)
                            isCurrentMetric = true
                        }
                    }
                }
            })
        }


    }

    private fun setTint(d: Drawable, color: Int): Drawable {
        val wrappedDrawable: Drawable = DrawableCompat.wrap(d)
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }



    private fun getTimeInHours(timeInDec: Double): String {
        val doubleAsString: String = java.lang.String.valueOf(timeInDec)
        val dotIndex = doubleAsString.indexOf(".")
        val wholePart = doubleAsString.substring(0, dotIndex).toInt()
        val fractionalPart = ("0" + doubleAsString.substring(dotIndex)).substring(0, 4).toDouble()
        return "$wholePart:${ceil(fractionalPart * 60).toInt()}"
    }


    @SuppressLint("SetTextI18n")
    private fun showInfoButton() {
        infoButton.visibility = View.VISIBLE
        infoButton.setOnClickListener {
            val dialog = Dialog(fragment.activity!!)
            dialog.setContentView(R.layout.info_popup)
            val df = DecimalFormat()
            df.maximumFractionDigits = 2
            val distance = dialog.findViewById<TextView>(R.id.info_distance)
            val ascent = dialog.findViewById<TextView>(R.id.info_ascent)
            val descent = dialog.findViewById<TextView>(R.id.info_descent)
            val timeDescription = dialog.findViewById<TextView>(R.id.info_time_description)
            dialog.findViewById<TextView>(R.id.info_time).text = routeSpentTime
            dialog.findViewById<TextView>(R.id.info_title).text = infoTitle
            if (isCurrentMetric) {
                distance.text = "${df.format(metricTotalDistance)} km"
                ascent.text = "${ascentMetres.toInt()} m"
                descent.text = "${descentMetres.toInt()} m"
                timeDescription.text = String.format(fragment.activity?.getString(R.string.info_time_label)!!, "5 kph")
            } else {
                distance.text = "${df.format(imperialTotalDistance)} mi"
                ascent.text = "${ascentFeet.toInt()} ft"
                descent.text = "${descentFeet.toInt()} ft"
                timeDescription.text = String.format(fragment.activity?.getString(R.string.info_time_label)!!, "3.1 mph")
            }
            dialog.show()
        }
    }


    private fun setElevationGraphData(values: ArrayList<Entry>) {
        val lineDataSet = LineDataSet(values, "")
        lineDataSet.lineWidth = 3f
        lineDataSet.fillAlpha = 100
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.color = Color.parseColor(graphColor)
        if (elevationChart.data != null) elevationChart.data.removeDataSet(0)
        else elevationChart.data = LineData()
        elevationChart.data.addDataSet(lineDataSet)
        elevationChart.notifyDataSetChanged()
        elevationChart.invalidate()
    }


    private fun setElevationGraphParams() {
        elevationChart.visibility = View.VISIBLE
        elevationChart.description.isEnabled = false
        elevationChart.setTouchEnabled(false)
        elevationChart.legend.isEnabled = false
        elevationChart.axisRight.isEnabled = false
        elevationChart.axisLeft.setLabelCount(3, true)
        elevationChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
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

    private fun zoomToDisplayRouteAndAllSpots(coordinates: ArrayList<LatLng>): CameraUpdate? {
        if (coordinates.size == 0) {
            return null
        }

        val builder = LatLngBounds.Builder()
        for (coordinate in coordinates) {
            builder.include(coordinate)
        }

        val bounds = builder.build()
        return CameraUpdateFactory.newLatLngBounds(bounds, 100)
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