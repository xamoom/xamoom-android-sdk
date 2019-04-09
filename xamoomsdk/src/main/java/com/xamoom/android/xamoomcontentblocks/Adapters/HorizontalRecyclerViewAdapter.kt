package com.xamoom.android.xamoomcontentblocks.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
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

class HorizontalRecyclerViewAdapter(val items : ArrayList<ContentBlock>, val context: Context, val fragment: Fragment?, val youtubeApiKey: String?, val bitmapCache: android.support.v4.util.LruCache<String, Bitmap>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
                val view = ContentBlock3ViewHolder(contentView, context, null, null, fragment)
                return view
            }
        }

        return ViewHolder1(LayoutInflater.from(context).inflate(R.layout.rva_test, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].blockType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var nHolder: RecyclerView.ViewHolder

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

class ViewHolder1 (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvAnimalType = view.findViewById<TextView>(R.id.testTextView)
    val testLayout = view.findViewById<LinearLayout>(R.id.testLayout)

    fun resize() {
        val params = testLayout.layoutParams
        params.height = 200
        testLayout.layoutParams = params
    }
}

class ViewHolder2 (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvAnimalType = view.findViewById<TextView>(R.id.testTextView)
    val testLayout = view.findViewById<LinearLayout>(R.id.testLayout)

    fun resize() {
        val params = testLayout.layoutParams
        params.height = 600
        testLayout.layoutParams = params
    }
}

class ViewHolder3 (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvAnimalType = view.findViewById<TextView>(R.id.testTextView)
    val testLayout = view.findViewById<LinearLayout>(R.id.testLayout)

    fun resize() {
        val params = testLayout.layoutParams
        params.height = 100
        testLayout.layoutParams = params
    }
}