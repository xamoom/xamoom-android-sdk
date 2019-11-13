package com.xamoom.android.xamoomsdk.Chatbot

abstract class ActionClickedListener {
    abstract fun onSpotsByTagsClicked(tags: List<String>)
    abstract fun onContentByTagsClicked(tags: List<String>)
    abstract fun onSpotByIdClicked(spotId: String)
    abstract fun onContentByIdClicked(contentId: String)
}