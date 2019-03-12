package com.xamoom.android.xamoomcontentblocks.Views

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.mapbox.mapboxsdk.maps.MapView
import com.xamoom.android.xamoomsdk.R

class CustomMapView : CoordinatorLayout {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    var mapView: MapView
    var textView: TextView
    var bottomSheetBehavior: BottomSheetBehavior<View>
    var floatingActionButton: FloatingActionButton
    var spotTitleTextView: TextView
    var spotExcerptTextView: TextView
    var spotContentButton: Button
    var spotImageView: ImageView

    init {
        val v = LayoutInflater.from(context)
                .inflate(R.layout.item_map, this, true)
        mapView = v.findViewById(R.id.mapImageView)
        textView = v.findViewById(R.id.titleTextView)
        val view: View = v.findViewById(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from<View>(view)
        floatingActionButton = v.findViewById(R.id.spot_fab)
        spotTitleTextView = v.findViewById(R.id.spot_title_text_view)
        spotExcerptTextView = v.findViewById(R.id.spot_excerpt_text_view)
        spotImageView = v.findViewById(R.id.spot_image_view)
        spotContentButton = v.findViewById(R.id.spot_content_button)
    }
}