package com.xamoom.android.xamoomcontentblocks.Adapters

import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock0ViewHolder
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock1ViewHolder
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock2ViewHolder
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder
import com.xamoom.android.xamoomsdk.R
import com.xamoom.android.xamoomsdk.Resource.ContentBlock

class HorizontalRecyclerViewAdapter(val items : ArrayList<ContentBlock>, val context: Context, val fragment: Fragment?, val youtubeApiKey: String?, val bitmapCache: androidx.collection.LruCache<String, Bitmap>?): androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val contentView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.content_block_0_layout, parent, false)
                val view = ContentBlock0ViewHolder(contentView)
                return view
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.content_block_1_layout, parent, false)
                return ContentBlock1ViewHolder(view, fragment)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.content_block_2_layout, parent, false)
                return ContentBlock2ViewHolder(view, fragment, youtubeApiKey, bitmapCache)
            }
            3 -> {
                val contentView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.content_block_3_layout, parent, false)
                val view = ContentBlock3ViewHolder(contentView, context, null, null, null,  fragment, null)
                return view
            }
        }

        val contentView = LayoutInflater.from(parent.context)
                .inflate(R.layout.content_block_0_layout, parent, false)
        return ContentBlock0ViewHolder(contentView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].blockType
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        var nHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder

        when (holder.itemViewType) {
            0 -> {
                val cb = items[position]
                val newHolder = holder as ContentBlock0ViewHolder
                newHolder.setupContentBlock(cb, false, 8)
            }
            1 -> {
                val cb = items[position]
                val newHolder = holder as ContentBlock1ViewHolder
                newHolder.setupContentBlock(cb, false)
            }
            2 -> {
                val cb = items[position]
                val newHolder = holder as ContentBlock2ViewHolder
                newHolder.setIsRecyclable(false)
                newHolder.setupContentBlock(cb, false)
            }
            3 -> {
                val cb = items[position]
                val newHolder = holder as ContentBlock3ViewHolder
                newHolder.setupContentBlock(cb, false)
            }
        }
    }
}