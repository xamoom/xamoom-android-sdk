/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.ViewHolders

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.provider.CalendarContract
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.xamoom.android.xamoomsdk.R
import com.xamoom.android.xamoomsdk.Resource.Content
import com.xamoom.android.xamoomsdk.Resource.ContentBlock
import com.xamoom.android.xamoomsdk.Resource.Spot
import com.xamoom.android.xamoomsdk.Resource.Style
import java.text.SimpleDateFormat
import java.util.*

/**
 * Displays the content heading.
 */
class ContentEventViewHolder(itemView: View, val navigationMode: String?, val fragment: Fragment) : RecyclerView.ViewHolder(itemView) {
    private val mNavigationTitleTextView: TextView = itemView.findViewById<View>(R.id.event_navigation_titleTextView) as TextView
    private val mNavigationDescriptionTextView: TextView = itemView.findViewById<View>(R.id.event_navigation_contentTextView) as TextView
    private val mCalendarTitleTextView: TextView = itemView.findViewById<View>(R.id.event_calendar_titleTextView) as TextView
    private val mCalendarDescriptionTextView: TextView = itemView.findViewById<View>(R.id.event_calendar_contentTextView) as TextView
    private val mNavigationLayout: LinearLayout = itemView.findViewById(R.id.event_navigation_layout)
    private val mCalendarLayout: LinearLayout = itemView.findViewById(R.id.event_calendar_layout)
    private val mCalendarImageView: ImageView = itemView.findViewById(R.id.event_calendar_iconImageView)
    private val mNavigationImageView: ImageView = itemView.findViewById(R.id.event_navigation_iconImageView)


    private var mContext: Context? = null

    init {
        mContext = fragment.context
        mNavigationLayout.visibility = View.GONE
        mCalendarLayout.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun setupContentBlock(contentBlock: ContentBlock, content: Content?, offline: Boolean, style: Style?) {
        if (content != null && content.relatedSpot != null && content.fromDate != null && content.toDate != null) {
            var spot = content.relatedSpot
            val dateFormatter = SimpleDateFormat("E d MMM HH:mm", Locale.getDefault())
            val fromDate = dateFormatter.format(content.fromDate)
            val toDate = dateFormatter.format(content.toDate)

            mCalendarDescriptionTextView.text = "$fromDate"
            mNavigationDescriptionTextView.text = spot.name

            mNavigationLayout.setOnClickListener {
                navigateToSpot(spot)
            }

            mCalendarLayout.setOnClickListener {
                val intent = Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, content.title)
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, spot.name)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, content.fromDate.time)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, content.toDate.time)
                fragment.activity!!.startActivity(Intent.createChooser(intent, ""))
            }

            val ta = mContext!!.obtainStyledAttributes(R.style.ContentBlocksTheme_Event, R.styleable.Event)
            val calendarBackgroundColor = ta.getResourceId(R.styleable.Event_event_calendar_background_color, 0)
            val calendarTintColor = ta.getColor(R.styleable.Event_event_calendar_tint_color, Color.BLACK)
            val navigationBackgroundColor = ta.getResourceId(R.styleable.Event_event_navigation_background_color, 0)
            val navigationTintColor = ta.getColor(R.styleable.Event_event_navigation_tint_color, Color.BLACK)

            mCalendarLayout.setBackgroundResource(calendarBackgroundColor)
            mNavigationLayout.setBackgroundResource(navigationBackgroundColor)
            mNavigationTitleTextView.setTextColor(navigationTintColor)
            mNavigationDescriptionTextView.setTextColor(navigationTintColor)
            mCalendarTitleTextView.setTextColor(calendarTintColor)
            mCalendarDescriptionTextView.setTextColor(calendarTintColor)
            mCalendarImageView.setColorFilter(calendarTintColor)
            mNavigationImageView.setColorFilter(navigationTintColor)

            mNavigationLayout.visibility = View.VISIBLE
            mCalendarLayout.visibility = View.VISIBLE
        }
    }

    private fun navigateToSpot(spot: Spot) {
        val urlString = String.format(Locale.US, "google.navigation:q=%f,%f&mode=%s", spot.location.latitude, spot.location.longitude, navigationMode)
        val gmmIntentUri = Uri.parse(urlString)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(mContext!!.getPackageManager()) != null) {
            mContext!!.startActivity(mapIntent)
        }
    }
}