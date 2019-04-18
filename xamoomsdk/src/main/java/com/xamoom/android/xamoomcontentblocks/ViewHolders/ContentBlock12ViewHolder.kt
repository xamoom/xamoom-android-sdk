package com.xamoom.android.xamoomcontentblocks.ViewHolders

import android.content.Context
import android.graphics.Bitmap
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


class ContentBlock12ViewHolder(val view: View, val context: Context, val fragment: Fragment?, val youtubeApiKey: String?, val bitmapCache: android.support.v4.util.LruCache<String, Bitmap>?, parentRecyclerView: RecyclerView?): RecyclerView.ViewHolder(view) {
    private var recyclerView: RecyclerView = view.findViewById(R.id.horizontal_recycler_view)
    private var titleTextView: TextView = view.findViewById(R.id.horizontal_recycler_title)

    init {
        titleTextView.text = "Horizontal RecyclerView Title"
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.isSmoothScrollbarEnabled = false
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //parentRecyclerView!!.adapter.notifyItemChanged(parentRecyclerView.getChildAdapterPosition(view))
                recyclerView!!.adapter.notifyDataSetChanged()
            }
        })
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(LinePagerIndicatorDecoration())
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
        recyclerView.adapter = HorizontalRecyclerViewAdapter(cbList, context, fragment, youtubeApiKey, bitmapCache)
    }
}