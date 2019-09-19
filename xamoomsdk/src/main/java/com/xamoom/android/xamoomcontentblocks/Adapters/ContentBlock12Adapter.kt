package com.xamoom.android.xamoomcontentblocks.Adapters

import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.support.v4.util.LruCache
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import at.rags.morpheus.Error
import com.xamoom.android.xamoomcontentblocks.ListManager
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock12ViewHolder
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock12ViewHolderInterface
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment
import com.xamoom.android.xamoomsdk.APIPasswordCallback
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.R
import com.xamoom.android.xamoomsdk.Resource.Content
import com.xamoom.android.xamoomsdk.Resource.ContentBlock
import com.xamoom.android.xamoomsdk.Resource.Style
import java.util.ArrayList

class ContentBlock12Adapter(val inter: ContentBlock12ViewHolderInterface): AdapterDelegate<ArrayList<ContentBlock>> {

    override fun isForViewType(items: ArrayList<ContentBlock>, position: Int): Boolean {
        val cb = items[position]
        return cb.blockType == 12
    }

    override fun onBindViewHolder(items: ArrayList<ContentBlock>, position: Int, holder: RecyclerView.ViewHolder, style: Style?, offline: Boolean) {
        val holder = holder as ContentBlock12ViewHolder
        val item = items[position]

        holder.setIsRecyclable(true)

        EnduserApi.getSharedInstance().getContent(item.contentId, null, object : APIPasswordCallback<Content, List<Error>> {
            override fun finished(result: Content) {
                val contentBlocks = arrayListOf<ContentBlock>()
                contentBlocks.addAll(result.contentBlocks)
                holder.setupContentBlock(contentBlocks)
            }

            override fun error(error: List<Error>) {
            }

            override fun passwordRequested() {

            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, fragment: Fragment?, enduserApi: EnduserApi?, youtubeApiKey: String?, bitmapCache: LruCache<*, *>?, contentCache: LruCache<*, *>?,
                                    showContentLinks: Boolean, listManager: ListManager?, adapterDelegatesManager: AdapterDelegatesManager<*>?,
                                    onContentBlock3ViewHolderInteractionListener: ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener?,
                                    onXamoomContentFragmentInteractionListener: XamoomContentFragment.OnXamoomContentFragmentInteractionListener?, urls: ArrayList<String>?, mapboxStyleString: String?,
                                    navigationButtonTintColorString: String?, contentButtonTextColorString: String?, navigationMode: String?, content: Content?): RecyclerView.ViewHolder {
        val context = parent!!.context
        val view = LayoutInflater.from(context)
                .inflate(R.layout.content_block_12_layout, parent, false)
        val cache = bitmapCache as LruCache<String, Bitmap>
        if (parent is RecyclerView) {
            return ContentBlock12ViewHolder(view, context, fragment, youtubeApiKey, cache, parent, inter)
        }
        return ContentBlock12ViewHolder(view, context, fragment, youtubeApiKey, cache, null, inter)
    }
}