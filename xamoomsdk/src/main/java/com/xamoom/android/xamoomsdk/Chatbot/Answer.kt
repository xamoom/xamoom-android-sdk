package com.xamoom.android.xamoomsdk.Chatbot

import com.xamoom.android.xamoomsdk.ChatbotAction
import java.util.ArrayList

class Answer {
    var text: String? = null
    var actions: ArrayList<ChatbotAction>? = null
    var confidence: Double? = null
    var success: Boolean? = null
}