package com.xamoom.android.xamoomsdk.Chatbot

abstract class ActionClickedListener {
    abstract fun onSpotsByTagsClicked(tags: String)
    abstract fun onContentByTagsClicked(tags: String)
    abstract fun onSpotByIdClicked(spotId: String)
    abstract fun onContentByIdClicked(contentId: String)
}