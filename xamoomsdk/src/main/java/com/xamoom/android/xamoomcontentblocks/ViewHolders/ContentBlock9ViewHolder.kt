package com.xamoom.android.xamoomcontentblocks.ViewHolders

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import at.rags.morpheus.Error
import com.bumptech.glide.Glide
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
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
class ContentBlock9ViewHolder(val view: CustomMapView, bundle: Bundle?, val enduserApi: EnduserApi, fragment: Fragment, val listener: XamoomContentFragment.OnXamoomContentFragmentInteractionListener) : RecyclerView.ViewHolder(view), OnMapReadyCallback {

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
    var fab = view.floatingActionButton
    var spotTitleTextView = view.spotTitleTextView
    var spotExcerptTextView = view.spotExcerptTextView
    var spotContentButton = view.spotContentButton
    var spotImageView = view.spotImageView
    var mapViewHeight = 0

    init {
        mContext = fragment.context
        mapView.onCreate(bundle)
        mapViewHeight = mapView.layoutParams.height

        mapView.setOnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            mapView.onTouchEvent(motionEvent)
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                closeBottomSheet = false
            }

            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (closeBottomSheet) {
                    bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                    mActiveSpot = null
                    fab.visibility = View.GONE
                } else {
                    closeBottomSheet = true
                }
             }
            true
        }

        bottomSheet.peekHeight = 325
        bottomSheet.isHideable = true
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    setBottomPadding(bottomSheet.height)
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    setBottomPadding(350)
                }

                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    setBottomPadding(mapViewHeight)
                    fab.hide()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (mActiveSpot != null) {
                    val position = CameraPosition.Builder().target(com.mapbox.mapboxsdk.geometry.LatLng(mActiveSpot!!.location.latitude, mActiveSpot!!.location.longitude)).zoom(16.0).tilt(20.0).build()
                    mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))
                }
            }
        })

        fab.backgroundTintList = ColorStateList.valueOf(mContext!!.resources
                .getColor(R.color.linkblock_navigation_background_color))
        fab.visibility = View.GONE
    }

    private fun setBottomPadding(padding: Int) {
        mapView.layoutParams = CoordinatorLayout.LayoutParams(mapView.layoutParams.width, padding)
    }

    fun setupContentBlock(contentBlock: ContentBlock, offline: Boolean) {
        mContentBlock = contentBlock

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
        mapBoxMap = mapboxMap
        mapBoxMap!!.setStyle(com.mapbox.mapboxsdk.maps.Style.LIGHT) { style ->

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
            } else {
                val position = CameraPosition.Builder().target(com.mapbox.mapboxsdk.geometry.LatLng(mSpotList[0].location.latitude, mSpotList[0].location.longitude)).zoom(10.0).tilt(0.0).build()
                mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))
            }
        }
    }

    private fun showSpotDetails(spot: Spot) {
        mActiveSpot = spot

        val position = CameraPosition.Builder().target(com.mapbox.mapboxsdk.geometry.LatLng(spot.location.latitude, spot.location.longitude)).zoom(16.0).tilt(0.0).build()
        mapBoxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED

        spotTitleTextView.text = spot.name
        spotExcerptTextView.text = spot.description
        Glide.with(mContext!!)
                .load(spot.publicImageUrl)
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

        fab.setOnClickListener {
            val lat = spot.location.latitude
            val lon = spot.location.longitude
            val query = "$lat,$lon(${spot.name})"
            val gmmIntentUri = Uri.parse("geo:$lat+,$lon?q=$query")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(mContext!!.getPackageManager()) != null) {
                mContext!!.startActivity(mapIntent)
            }
        }
        fab.visibility = View.VISIBLE
    }

    companion object {
        val PAGE_SIZE = 100
    }
}