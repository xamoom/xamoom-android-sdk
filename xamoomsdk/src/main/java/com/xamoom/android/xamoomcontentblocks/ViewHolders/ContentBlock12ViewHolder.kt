package com.xamoom.android.xamoomcontentblocks.ViewHolders

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.xamoom.android.xamoomcontentblocks.Adapters.HorizontalRecyclerViewAdapter
import com.xamoom.android.xamoomsdk.R
import android.support.v7.widget.PagerSnapHelper
import android.widget.TextView
import com.xamoom.android.xamoomcontentblocks.Helper.LinePagerIndicatorDecoration
import com.xamoom.android.xamoomsdk.Resource.ContentBlock


class ContentBlock12ViewHolder(val view: View, val context: Context, val fragment: Fragment?, val youtubeApiKey: String?, val bitmapCache: android.support.v4.util.LruCache<String, Bitmap>?, parentRecyclerView: RecyclerView?, val interf: ContentBlock12ViewHolderInterface): RecyclerView.ViewHolder(view) {
    private var recyclerView: RecyclerView = view.findViewById(R.id.horizontal_recycler_view)

    init {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.isSmoothScrollbarEnabled = false
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //parentRecyclerView!!.adapter.notifyItemChanged(parentRecyclerView.getChildAdapterPosition(view))
                if (newState == 0) {
                    recyclerView!!.adapter.notifyDataSetChanged()
                    //interf.didScroll(parentRecyclerView!!.getChildAdapterPosition(view) + 1)
                }
            }
        })
    }

    fun setupContentBlock(cb: ArrayList<ContentBlock>) {
        var cbList: ArrayList<ContentBlock> = arrayListOf()
        for(block in cb) {
            if (block.blockType == 0 ||
                    block.blockType == 1 ||
                    block.blockType == 2 ||
                    block.blockType == 3) {
                cbList.add(block)
            }
        }

        if (recyclerView.adapter == null && recyclerView.itemDecorationCount == 0) {
            recyclerView.adapter = HorizontalRecyclerViewAdapter(cbList, context, fragment, youtubeApiKey, bitmapCache)
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(recyclerView)
            recyclerView.addItemDecoration(LinePagerIndicatorDecoration())
        }
    }
}

interface ContentBlock12ViewHolderInterface {
    fun didScroll(position: Int)
}