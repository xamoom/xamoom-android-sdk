package com.xamoom.android.xamoomsdk.Chatbot

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import at.rags.morpheus.Error
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment
import com.xamoom.android.xamoomsdk.*
import com.xamoom.android.xamoomsdk.Enums.ContentFlags
import com.xamoom.android.xamoomsdk.Resource.Content
import com.xamoom.android.xamoomsdk.Resource.Spot
import kotlinx.android.synthetic.main.chatbot_action.view.*
import java.util.*
import kotlin.collections.ArrayList

class ChatbotActionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var action_type: Int = 0
    private var action_value: String = ""
    private lateinit var action_listener: ActionClickedListener
    private lateinit var enduserAPI: EnduserApi

    fun setupView(type: Int, value: String, listener: ActionClickedListener, enduserApi: EnduserApi){
        action_type = type
        action_value = value
        action_listener = listener
        enduserAPI = enduserApi

        itemView.contentTextView.text = ""

        setupText()
        setupIcon()
        setupTitle()
        setupOnClickAction()
    }

    private fun setupText(){
        when (action_type) {
            0 -> itemView.contentTextView.text = action_value
            1 -> itemView.contentTextView.text = action_value
            2 -> itemView.contentTextView.text = "" // TODO What to show on navigate?
            3 -> itemView.contentTextView.text = action_value
            4 -> itemView.contentTextView.text = action_value
            5 -> getSpotListByTag(action_value)
            6 -> getSpotTitle(action_value)
            7 -> getContentListByTag(action_value)
            8 -> getContentTitle(action_value)
            else -> { // unknown type
                itemView.iconImageView.setImageResource(R.drawable.chatbot_action_0)
            }
        }
    }

    private fun getContentTitle(contentId: String){
        enduserAPI.getContent(contentId, EnumSet.of(ContentFlags.PRIVATE), null,
                object: APIPasswordCallback<Content, List<Error>> {
                    override fun finished(result: Content?) {
                        itemView.contentTextView.text = result?.title
                    }

                    override fun error(error: List<Error>?) {
                        itemView.contentTextView.text = error?.get(0)?.title
                    }

                    override fun passwordRequested() {
                        itemView.contentTextView.text = ""
                    }
                })
    }

    private fun getContentListByTag(tags: String){
        val tagList = ArrayList<String>()
        tagList.add(tags)
        enduserAPI.getContentsByTags(tagList, 1, null, null, object: APIListCallback<List<Content>, List<Error>> {
            override fun finished(result: List<Content>?, cursor: String?, hasMore: Boolean) {
                itemView.contentTextView.text = result?.size.toString() + " Seiten" // TODO translate
            }

            override fun error(error: List<Error>?) {
                itemView.contentTextView.text = error?.get(0)?.title
            }
        })
    }

    private fun getSpotTitle(spotId: String){
        enduserAPI.getSpot(spotId, object: APICallback<Spot, List<Error>> {
            override fun finished(result: Spot?) {
                itemView.contentTextView.text = result?.name
            }

            override fun error(error: List<Error>?) {
                itemView.contentTextView.text = error?.get(0)?.title
            }
        })
    }

    private fun getSpotListByTag(tags: String){
        val tagList = ArrayList<String>()
        tagList.add(tags)
        enduserAPI.getSpotsByTags(tagList, null, null, object: APIListCallback<List<Spot>, List<Error>> {
            override fun finished(result: List<Spot>?, cursor: String?, hasMore: Boolean) {
                itemView.contentTextView.text = result?.size.toString() + " Orte" // TODO translate
            }

            override fun error(error: List<Error>?) {
                itemView.contentTextView.text = error?.get(0)?.title
            }
        })
    }

    private fun setupIcon(){
        when (action_type) {
            0 -> itemView.iconImageView.setImageResource(R.drawable.chatbot_action_0)
            1 -> itemView.iconImageView.setImageResource(R.drawable.chatbot_action_1)
            2 -> itemView.iconImageView.setImageResource(R.drawable.chatbot_action_2)
            3 -> itemView.iconImageView.setImageResource(R.drawable.chatbot_action_3)
            4 -> itemView.iconImageView.setImageResource(R.drawable.chatbot_action_4)
            5 -> itemView.iconImageView.setImageResource(R.drawable.chatbot_action_5)
            6 -> itemView.iconImageView.setImageResource(R.drawable.chatbot_action_6)
            7 -> itemView.iconImageView.setImageResource(R.drawable.chatbot_action_7)
            8 -> itemView.iconImageView.setImageResource(R.drawable.chatbot_action_8)
            else -> { // unknown type
                itemView.iconImageView.setImageResource(R.drawable.chatbot_action_0)
            }
        }
    }

    private fun setupTitle(){
        when (action_type) {
            0 -> itemView.titleTextView.setText(R.string.chatbot_action_title_0)
            1 -> itemView.titleTextView.setText(R.string.chatbot_action_title_1)
            2 -> itemView.titleTextView.setText(R.string.chatbot_action_title_2)
            3 -> itemView.titleTextView.setText(R.string.chatbot_action_title_3)
            4 -> itemView.titleTextView.setText(R.string.chatbot_action_title_4)
            5 -> itemView.titleTextView.setText(R.string.chatbot_action_title_5)
            6 -> itemView.titleTextView.setText(R.string.chatbot_action_title_6)
            7 -> itemView.titleTextView.setText(R.string.chatbot_action_title_7)
            8 -> itemView.titleTextView.setText(R.string.chatbot_action_title_8)
            else -> { // unknown type
                itemView.titleTextView.setText(R.string.chatbot_action_title_0)
            }
        }
    }

    private fun setupOnClickAction(){
        itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                when (action_type) {
                    0 -> openUri(action_value)
                    1 -> openUri(action_value)
                    2 -> openUri(action_value)
                    3 -> openUri("tel:$action_value")
                    4 -> openUri("mailto:$action_value")
                    5 -> action_listener.onSpotsByTagsClicked(action_value)
                    6 -> action_listener.onSpotByIdClicked(action_value)
                    7 -> action_listener.onContentByTagsClicked(action_value)
                    8 -> action_listener.onContentByIdClicked(action_value)
                    else -> { // unknown type
                        print("Unknown Action")
                    }
                }
            }
        })
    }

    private fun openUri(uri: String){
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        itemView.context.startActivity(i)
    }
}