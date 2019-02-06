package com.xamoom.android.xamoomcontentblocks.ViewHolders

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.util.ArrayMap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import at.rags.morpheus.Error
import com.google.android.gms.maps.GoogleMap
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.xamoom.android.xamoomsdk.APIListCallback
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.Enums.SpotFlags
import com.xamoom.android.xamoomsdk.R
import com.xamoom.android.xamoomsdk.Resource.ContentBlock
import com.xamoom.android.xamoomsdk.Resource.Spot
import com.xamoom.android.xamoomsdk.Resource.Style
import java.util.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import android.graphics.BitmapFactory
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.FloatingActionButton
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.android.gms.maps.model.*
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.style.expressions.Expression.zoom
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset
import com.xamoom.android.xamoomcontentblocks.XamoomMapFragment
import com.xamoom.android.xamoomsdk.APICallback
import kotlin.collections.ArrayList


class ContentBlock91ViewHolder(val view: View, val fragment: Fragment, val enduserApi: EnduserApi): RecyclerView.ViewHolder(view), OnMapReadyCallback {

    private var mapBoxStyle: com.mapbox.mapboxsdk.maps.Style? = null
    private var mapBoxMap: MapboxMap? = null
    private val PAGE_SIZE = 100
    private var mContentBlock: ContentBlock? = null
    private var mBase64Icon: String? = null
    private var mSpotList: ArrayList<Spot> = arrayListOf()
    var showContentLinks: Boolean = false
    private val mContext: Context = fragment.context!!
    private var mTextColor: Int? = null
    private lateinit var mTitleTextView: TextView
    private var mMapView: com.mapbox.mapboxsdk.maps.MapView? = null

    @SuppressLint("ClickableViewAccessibility")
    fun setupContentBlock(contentBlock: ContentBlock, offline: Boolean) {

        mTitleTextView = itemView.findViewById(R.id.titleTextView)
        mMapView = itemView.findViewById(R.id.mapImageView)

        mMapView!!.onCreate(null)
        mMapView!!.setOnTouchListener { view, motionEvent ->

            view.parent.requestDisallowInterceptTouchEvent(true)
            mMapView!!.onTouchEvent(motionEvent)
            true
        }

        mSpotList = ArrayList()

        mTitleTextView.visibility = View.VISIBLE
        if (contentBlock.title != null && !contentBlock.title.equals("", ignoreCase = true)) {
            mTitleTextView.text = contentBlock.title
        } else {
            mTitleTextView.visibility = View.GONE
        }

        if (mTextColor != null) {
            mTitleTextView.setTextColor(mTextColor!!)
        }

        mContentBlock = contentBlock

        downloadAllSpots(mContentBlock!!.spotMapTags, null, object : APIListCallback<List<Spot>, List<Error>> {
            override fun finished(result: List<Spot>, cursor: String, hasMore: Boolean) {
            }

            override fun error(error: List<Error>) {
                Log.e("", "Error:$error")
            }
        })

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
                    mSpotList!!.addAll(result)
                    if (hasMore) {
                        downloadAllSpots(tags, cursor, callback)
                    } else {
                        getStyle(result[0].system.id)
                        callback.finished(mSpotList, "", false)
                    }
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
                mMapView!!.getMapAsync(this@ContentBlock91ViewHolder)
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
        mapBoxMap = mapboxMap
        mapBoxMap!!.setStyle(com.mapbox.mapboxsdk.maps.Style.LIGHT) { style ->

            var image = ContentBlock9ViewHolderUtils.getIcon(mBase64Icon, mContext)
            var coordinates: MutableList<Feature> = mutableListOf()
            val latLngBounds = LatLngBounds.Builder()
            for (s in mSpotList) {
                coordinates.add(Feature.fromGeometry(
                        Point.fromLngLat(s.location.longitude, s.location.latitude)))
                latLngBounds.include(LatLng(s.location.latitude, s.location.longitude))
            }

            style.addSource(GeoJsonSource("marker-source",
                    FeatureCollection.fromFeatures(coordinates)))

            style.addImage("my-marker-image", image)

            style.addLayer(SymbolLayer("marker-layer", "marker-source")
                    .withProperties(PropertyFactory.iconImage("my-marker-image"),
                            iconOffset(arrayOf(0f, -9f))))

            style.addSource(GeoJsonSource("selected-marker"))

            style.addLayer(SymbolLayer("selected-marker-layer", "selected-marker")
                    .withProperties(PropertyFactory.iconImage("my-marker-image"),
                            iconOffset(arrayOf(0f, -9f))))

            mapBoxStyle = style
            if (mSpotList.size > 1) {
                mapBoxMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 50))
            } else {
                val position = CameraPosition.Builder().target(com.mapbox.mapboxsdk.geometry.LatLng(mSpotList.get(0).location.latitude, mSpotList.get(0).location.longitude)).zoom(10.0).tilt(20.0).build()
                mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))
            }
        }
    }

    fun createMap(contentBlock: ContentBlock) {
        if (mMapView == null) {
            setupContentBlock(contentBlock, false)
        }
    }

    fun destroyMapView() {
        if (mMapView != null) {
            mMapView!!.onDestroy()
        }
    }
}