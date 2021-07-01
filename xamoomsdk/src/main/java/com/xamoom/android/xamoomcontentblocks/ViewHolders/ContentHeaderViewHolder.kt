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
import android.location.Location
import android.net.Uri
import android.preference.PreferenceManager
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
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
class ContentHeaderViewHolder(itemView: View, val navigationMode: String?, val fragment: Fragment) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
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
    private var sharedPreferences: SharedPreferences? = null

    init {
        mContext = fragment.context
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
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

        val colorString = "#444444"
        val tintColor = Color.parseColor(colorString)

        mEventTimeImageView.setColorFilter(tintColor)
        mEventTimeTextView.setTextColor(tintColor)
        mEventLocationImageView.setColorFilter(tintColor)
        mEventLocationTextView.setTextColor(tintColor)

        if (content != null && content.relatedSpot != null) {
            val spot = content.relatedSpot
            mEventLayout.visibility = View.VISIBLE
            mEventLocationLayout.visibility = View.VISIBLE
            mEventLocationTextView.text = spot.name
            mEventLocationLayout.setOnClickListener {
                navigateToSpot(spot)
            }

            if (content.fromDate != null) {
                mEventTimeLayout.visibility = View.VISIBLE
                val langPickerLanguage = sharedPreferences!!.getString("current_language_code", null)
                val locale: Locale?
                locale = if (langPickerLanguage != null){
                    Locale(langPickerLanguage)
                } else {
                    Locale("en")
                }
                val dateFormatter = SimpleDateFormat("E d MMM HH:mm", locale)
                val fromDate = dateFormatter.format(content.fromDate)

                if (content.toDate != null) {
                    val toDate = dateFormatter.format(content.toDate)
                    mEventTimeTextView.text = "$fromDate - \n$toDate"
                } else {
                    mEventTimeTextView.text = "$fromDate"
                }
                mEventTimeLayout.setOnClickListener {
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
            } else {
                mEventTimeLayout.visibility = View.GONE
            }
        } else if (content != null && content.fromDate != null) {
            mEventLayout.visibility = View.VISIBLE
            mEventTimeLayout.visibility = View.VISIBLE
            mEventLocationLayout.visibility = View.GONE

            val langPickerLanguage = sharedPreferences!!.getString("current_language_code", null)
            val locale: Locale?
            locale = if (langPickerLanguage != null){
                Locale(langPickerLanguage)
            } else {
                Locale("en")
            }
            val dateFormatter = SimpleDateFormat("E d MMM HH:mm", locale)
            val fromDate = dateFormatter.format(content.fromDate)

            if (content.toDate != null) {
                val toDate = dateFormatter.format(content.toDate)
                mEventTimeTextView.text = "$fromDate - \n$toDate"
            } else {
                mEventTimeTextView.text = "$fromDate"
            }

            mEventTimeLayout.setOnClickListener {
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
        } else {
            mEventLayout.visibility = View.GONE
            mEventTimeLayout.visibility = View.GONE
            mEventLocationLayout.visibility = View.GONE
        }

        if (contentBlock.title != null) {
            mTitleTextView.text = contentBlock.title
            print("title text ${contentBlock.text}")
        } else {
            mTitleTextView.visibility = View.GONE
            val params = mTextView.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            mTextView.layoutParams = params
        }

        if (contentBlock.text != null && contentBlock.text != "" && !contentBlock.text.equals("<p><br></p>", ignoreCase = true)) {
            mTextView.text = contentBlock.text
        } else {
            mTextView.visibility = View.GONE
            val params = mTextView.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            mTextView.layoutParams = params
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