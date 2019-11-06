package com.xamoom.android.xamoomsdk.Chatbot

import android.content.Context
import com.xamoom.android.xamoomsdk.ChatAPICallback
import com.xamoom.android.xamoomsdk.ChatbotAction
import com.xamoom.android.xamoomsdk.EnduserApi
import java.util.ArrayList

class TextChat(val context: Context, val enduserApi: EnduserApi, val botId: String) {

    private var conversationContext: String = ""

    public fun chat(input: String, listener: TextChatListener){
        enduserApi.chat(input, conversationContext, botId, object : ChatAPICallback {
            override fun finished(text: String, context: String, actions: ArrayList<ChatbotAction>, confidence: Double, success: Boolean) {
                conversationContext = context

                val answer = Answer()
                answer.text = text
                answer.actions = actions
                answer.confidence = confidence
                answer.success = success

                listener.onFinished(answer)
            }

            override fun error(status_code: String, message: String) {
                listener.onError(status_code, message)
            }
        })
    }

    fun getCurrentConversationContext(): String{
        return conversationContext
    }

    fun clearConversationContext() {
        conversationContext = ""
    }
}




abstract class TextChatListener {
    abstract fun onFinished(answer: Answer)
    abstract fun onError(errorCode: String, message: String)
}