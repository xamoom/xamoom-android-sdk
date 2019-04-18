package com.xamoom.android.xamoomcontentblocks.Adapters

import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.support.v4.util.LruCache
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.xamoom.android.xamoomcontentblocks.ListManager
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock12ViewHolder
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.R
import com.xamoom.android.xamoomsdk.Resource.ContentBlock
import com.xamoom.android.xamoomsdk.Resource.Style
import java.util.ArrayList

class ContentBlock12Adapter: AdapterDelegate<ArrayList<ContentBlock>> {
    override fun isForViewType(items: ArrayList<ContentBlock>, position: Int): Boolean {
        return true
    }

    override fun onBindViewHolder(items: ArrayList<ContentBlock>, position: Int, holder: RecyclerView.ViewHolder, style: Style?, offline: Boolean) {
        val holder = holder as ContentBlock12ViewHolder
        holder.setIsRecyclable(true)
        holder.setupContentBlock(items)
    }


    override fun onCreateViewHolder(parent: ViewGroup?, fragment: Fragment?, enduserApi: EnduserApi?, youtubeApiKey: String?, bitmapCache: LruCache<*, *>?, contentCache: LruCache<*, *>?, showContentLinks: Boolean, listManager: ListManager?, adapterDelegatesManager: AdapterDelegatesManager<*>?, onContentBlock3ViewHolderInteractionListener: ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener?, onXamoomContentFragmentInteractionListener: XamoomContentFragment.OnXamoomContentFragmentInteractionListener?, urls: ArrayList<String>?, mapboxStyleString: String?): RecyclerView.ViewHolder {
        val context = parent!!.context
        val view = LayoutInflater.from(context)
                .inflate(R.layout.content_block_12_layout, parent, false)
        val cache = bitmapCache as LruCache<String, Bitmap>
        if (parent is RecyclerView) {
            return ContentBlock12ViewHolder(view, context, fragment, youtubeApiKey, cache, parent)
        }
        return ContentBlock12ViewHolder(view, context, fragment, youtubeApiKey, cache, null)
    }
}