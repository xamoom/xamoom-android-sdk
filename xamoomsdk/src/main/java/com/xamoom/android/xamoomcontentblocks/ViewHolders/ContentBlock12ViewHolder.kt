package com.xamoom.android.xamoomcontentblocks.ViewHolders

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.xamoom.android.xamoomcontentblocks.Adapters.HorizontalRecyclerViewAdapter
import com.xamoom.android.xamoomsdk.R
import androidx.recyclerview.widget.PagerSnapHelper
import android.widget.TextView
import androidx.collection.LruCache
import com.xamoom.android.xamoomcontentblocks.Helper.LinePagerIndicatorDecoration
import com.xamoom.android.xamoomsdk.Resource.ContentBlock


class ContentBlock12ViewHolder(val view: View, val context: Context, val fragment: Fragment?, val youtubeApiKey: String?, val bitmapCache: LruCache<String, Bitmap>?, parentRecyclerView: androidx.recyclerview.widget.RecyclerView?, val interf: ContentBlock12ViewHolderInterface): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    private var recyclerView: androidx.recyclerview.widget.RecyclerView = view.findViewById(R.id.horizontal_recycler_view)

    init {
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        layoutManager.isSmoothScrollbarEnabled = false
        recyclerView.layoutManager = layoutManager
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.setHasFixedSize(false)
        recyclerView.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == 0) {
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        })
    }

    fun setContentToNull() {
        recyclerView.adapter = null
        if(recyclerView.itemDecorationCount != 0) {
            for(i in 0 until recyclerView.itemDecorationCount) {
                recyclerView.removeItemDecorationAt(i)
            }
        }
    }


    fun setupContentBlock(cb: ArrayList<ContentBlock>) {
        var currentCbList: ArrayList<ContentBlock> = arrayListOf()
        for(block in cb) {
            if (block.blockType == 0 ||
                    block.blockType == 1 ||
                    block.blockType == 2 ||
                    block.blockType == 3) {
                currentCbList.add(block)
            }
        }

        if (recyclerView.adapter == null && recyclerView.itemDecorationCount == 0) {
            recyclerView.adapter = HorizontalRecyclerViewAdapter(currentCbList, context, fragment, youtubeApiKey, bitmapCache)
            recyclerView.addItemDecoration(LinePagerIndicatorDecoration())
        }
    }
}

interface ContentBlock12ViewHolderInterface {
    fun didScroll(position: Int)
}