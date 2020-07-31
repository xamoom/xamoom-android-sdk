package com.xamoom.android.xamoomcontentblocks.Views

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.mapbox.mapboxsdk.maps.MapView
import com.xamoom.android.xamoomsdk.R

class CustomMapViewWithChart : RelativeLayout {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    var mapView: MapView
    var textView: TextView
    var bottomSheetBehavior: BottomSheetBehavior<View>
    var centerSpotsButton: FloatingActionButton
    var centerUserButton: FloatingActionButton
    var spotTitleTextView: TextView
    var spotExcerptTextView: TextView
    var spotNavigationButton: Button
    var spotContentButton: Button
    var spotImageView: ImageView
    var chart: LineChart

    init {
        val v = LayoutInflater.from(context)
                .inflate(R.layout.item_map_and_chart, this, true)
        mapView = v.findViewById(R.id.mapImageView)
        textView = v.findViewById(R.id.titleTextView)
        val view: View = v.findViewById(R.id.bottom_sheet)
        view.setOnTouchListener { _, event ->
            view.onTouchEvent(event)
            true
        }
        bottomSheetBehavior = BottomSheetBehavior.from<View>(view)
        centerSpotsButton = v.findViewById(R.id.center_spot_fab)
        centerUserButton = v.findViewById(R.id.user_location_fab)
        spotTitleTextView = v.findViewById(R.id.spot_title_text_view)
        spotExcerptTextView = v.findViewById(R.id.spot_excerpt_text_view)
        spotImageView = v.findViewById(R.id.spot_image_view)
        spotContentButton = v.findViewById(R.id.spot_content_button)
        spotNavigationButton = v.findViewById(R.id.spot_navigation_button)
        chart = v.findViewById(R.id.elevationChart)
    }
}