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
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.preference.PreferenceManager
import android.provider.CalendarContract
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
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
class ContentEventViewHolder(itemView: View, val navigationMode: String?, val fragment: Fragment) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    private val mNavigationTitleTextView: TextView = itemView.findViewById<View>(R.id.event_navigation_titleTextView) as TextView
    private val mNavigationDescriptionTextView: TextView = itemView.findViewById<View>(R.id.event_navigation_contentTextView) as TextView
    private val mCalendarTitleTextView: TextView = itemView.findViewById<View>(R.id.event_calendar_titleTextView) as TextView
    private val mCalendarDescriptionTextView: TextView = itemView.findViewById<View>(R.id.event_calendar_contentTextView) as TextView
    private val mNavigationLayout: LinearLayout = itemView.findViewById(R.id.event_navigation_layout)
    private val mCalendarLayout: LinearLayout = itemView.findViewById(R.id.event_calendar_layout)
    private val mCalendarImageView: ImageView = itemView.findViewById(R.id.event_calendar_iconImageView)
    private val mNavigationImageView: ImageView = itemView.findViewById(R.id.event_navigation_iconImageView)



    private var mContext: Context? = null
    private var sharedPreferences: SharedPreferences? = null

    init {
        mContext = fragment.context
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        mNavigationLayout.visibility = View.GONE
        mCalendarLayout.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun setupContentBlock(contentBlock: ContentBlock, content: Content?, offline: Boolean, style: Style?) {

        if (content != null && content.relatedSpot != null) {
            val spot = content.relatedSpot

            mNavigationTitleTextView.text = mContext!!.resources.getString(R.string.event_navigation_title)
            mNavigationDescriptionTextView.text = spot.name
            mNavigationLayout.setOnClickListener {
                navigateToSpot(spot)
            }

            mNavigationLayout.visibility = View.VISIBLE

            if (content.fromDate != null) {

                val langPickerLanguage = sharedPreferences!!.getString("current_language_code", null)
                val locale: Locale?
                locale = if (langPickerLanguage != null){
                    Locale(langPickerLanguage)
                } else {
                    Locale("en")
                }

                val dateFormatter = SimpleDateFormat("E d MMM HH:mm", locale)
                val fromDate = dateFormatter.format(content.fromDate)

                mCalendarTitleTextView.text = mContext!!.resources.getString(R.string.event_calendar_title)
                mCalendarDescriptionTextView.text = "$fromDate"

                mCalendarLayout.setOnClickListener {
                    val intent = Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.Events.TITLE, content.title)
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, spot.name)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, content.fromDate.time)

                    if (content.toDate != null) {
                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, content.toDate.time)
                    } else {
                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, content.fromDate.time)
                    }

                    fragment.activity!!.startActivity(Intent.createChooser(intent, ""))
                }

                mCalendarLayout.visibility = View.VISIBLE
            } else {
                mCalendarLayout.visibility = View.GONE
            }
        } else if (content != null && content.fromDate != null) {
            val langPickerLanguage = sharedPreferences!!.getString("current_language_code", null)
            val locale: Locale?
            locale = if (langPickerLanguage != null){
                Locale(langPickerLanguage)
            } else {
                Locale("en")
            }
            val dateFormatter = SimpleDateFormat("E d MMM HH:mm", locale)
            val fromDate = dateFormatter.format(content.fromDate)

            mNavigationLayout.visibility = View.GONE

            mCalendarTitleTextView.text = mContext!!.resources.getString(R.string.event_calendar_title)
            mCalendarDescriptionTextView.text = "$fromDate"

            mCalendarLayout.setOnClickListener {
                val intent = Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, content.title)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, content.fromDate.time)

                if (content.toDate != null) {
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, content.toDate.time)
                } else {
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, content.fromDate.time)
                }

                fragment.activity!!.startActivity(Intent.createChooser(intent, ""))
            }

            mCalendarLayout.visibility = View.VISIBLE
        } else {
            mNavigationLayout.visibility = View.GONE
            mCalendarLayout.visibility = View.GONE
        }

        val ta = mContext!!.obtainStyledAttributes(R.style.ContentBlocksTheme_Event, R.styleable.Event)
        val calendarBackgroundColor = ta.getResourceId(R.styleable.Event_event_calendar_background_color, 0)
        val calendarTintColor = ta.getColor(R.styleable.Event_event_calendar_tint_color, Color.BLACK)
        val navigationBackgroundColor = ta.getResourceId(R.styleable.Event_event_navigation_background_color, 0)
        val navigationTintColor = ta.getColor(R.styleable.Event_event_navigation_tint_color, Color.BLACK)

        val isBackgroundImage = PreferenceManager.getDefaultSharedPreferences(mContext).getString("is_background_image", null)
        if (isBackgroundImage != null && isBackgroundImage == "true") {
            mCalendarLayout.setBackgroundResource(R.drawable.background_image)
            mNavigationLayout.setBackgroundResource(R.drawable.background_image)
        } else {
            mCalendarLayout.setBackgroundResource(calendarBackgroundColor)
            mNavigationLayout.setBackgroundResource(navigationBackgroundColor)
        }


        mNavigationTitleTextView.setTextColor(navigationTintColor)
        mNavigationDescriptionTextView.setTextColor(navigationTintColor)
        mCalendarTitleTextView.setTextColor(calendarTintColor)
        mCalendarDescriptionTextView.setTextColor(calendarTintColor)
        mCalendarImageView.setColorFilter(calendarTintColor)
        mNavigationImageView.setColorFilter(navigationTintColor)
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