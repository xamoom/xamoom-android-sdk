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
class ContentHeaderViewHolder(itemView: View, val navigationMode: String?, val fragment: Fragment) : RecyclerView.ViewHolder(itemView) {
    private val mTitleTextView: TextView = itemView.findViewById<View>(R.id.titleTextView) as TextView
    private val mTextView: TextView = itemView.findViewById<View>(R.id.textView) as TextView
    private val mCoverImageCopyRight: TextView = itemView.findViewById(R.id.coverImageCopyRight)
    private val mEventLayout: LinearLayout = itemView.findViewById(R.id.event_layout)
    private val mEventLocationLayout: RelativeLayout = itemView.findViewById(R.id.event_location_layout)
    private val mEventTimeLayout: RelativeLayout = itemView.findViewById(R.id.event_time_layout)
    private val mEventTimeImageView: ImageView = itemView.findViewById(R.id.event_time_imageview)
    private var mEventLocationImageView: ImageView = itemView.findViewById(R.id.event_location_imageView)
    private val mEventTimeTextView: TextView = itemView.findViewById(R.id.event_time_textview)
    private val mEventLocationTextView: TextView = itemView.findViewById(R.id.event_location_textview)
    private var mLinkColor = "00F"
    private var mStyle: Style? = null
    private var mTextSize = 18.0f
    private var mContext: Context? = null

    init {
        mContext = fragment.context
        mTitleTextView.visibility = View.GONE
        mTextView.visibility = View.GONE
        mCoverImageCopyRight.visibility = View.GONE
        mEventLayout.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun setupContentBlock(contentBlock: ContentBlock, content: Content?, offline: Boolean, style: Style?) {
        mTitleTextView.visibility = View.VISIBLE
        mTextView.visibility = View.VISIBLE
        mCoverImageCopyRight.visibility = View.VISIBLE

        if (content != null && content.relatedSpot != null && content.fromDate != null && content.toDate != null) {
            var spot = content.relatedSpot
            val dateFormatter = SimpleDateFormat("E d MMM HH:mm", Locale.getDefault())
            val fromDate = dateFormatter.format(content.fromDate)
            val toDate = dateFormatter.format(content.toDate)

            mEventTimeTextView.text = "$fromDate - \n$toDate"
            mEventLocationTextView.text = spot.name
            mEventLayout.visibility = View.VISIBLE

            val colorString = "#444444"
            val tintColor = Color.parseColor(colorString)

            mEventTimeImageView.setColorFilter(tintColor)
            mEventTimeTextView.setTextColor(tintColor)
            mEventLocationImageView.setColorFilter(mContext!!.resources.getColor(android.R.color.darker_gray))
            mEventLocationTextView.setTextColor(mContext!!.resources.getColor(android.R.color.darker_gray))

            mEventLocationLayout.setOnClickListener {
                navigateToSpot(spot)
            }

            mEventTimeLayout.setOnClickListener {
                val intent = Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, spot.name)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, content.fromDate.time)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, content.toDate.time)
                fragment.activity!!.startActivity(Intent.createChooser(intent, ""))
            }
        }

        if (contentBlock.title != null) {
            mTitleTextView.text = contentBlock.title
        } else {
            mTitleTextView.visibility = View.GONE
            val params = mTextView.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            mTextView.layoutParams = params
        }

        if (contentBlock.text != null && !contentBlock.text.equals("<p><br></p>", ignoreCase = true)) {
            mTextView.text = contentBlock.text
        } else {
            mTextView.visibility = View.GONE
        }

        if (mStyle != null && mStyle!!.foregroundFontColor != null) {
            mTitleTextView.setTextColor(Color.parseColor(mStyle!!.foregroundFontColor))
            mTextView.setTextColor(Color.parseColor(mStyle!!.foregroundFontColor))
            mCoverImageCopyRight.setTextColor(Color.parseColor(mStyle!!.foregroundFontColor))
        }

        if (contentBlock.coverImageCopyRight != null && contentBlock.coverImageCopyRight != "") {
            mCoverImageCopyRight.text = contentBlock.coverImageCopyRight
        } else {
            mCoverImageCopyRight.visibility = View.GONE
        }
    }

    fun setLinkColor(mLinkColor: String) {
        this.mLinkColor = mLinkColor
    }

    fun setTextSize(textSize: Float) {
        mTextSize = textSize
    }

    fun setStyle(style: Style?) {
        mStyle = style
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