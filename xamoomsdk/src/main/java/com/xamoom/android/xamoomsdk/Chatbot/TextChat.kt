package com.xamoom.android.xamoomsdk.Chatbot

import android.content.Context
import android.widget.Toast
import com.xamoom.android.xamoomsdk.ChatAPICallback
import com.xamoom.android.xamoomsdk.ChatbotAction
import com.xamoom.android.xamoomsdk.EnduserApi
import java.util.*
import kotlin.concurrent.schedule

class TextChat(val context: Context, val enduserApi: EnduserApi, val botId: String) {

    private var conversationContext: String = ""
    private var retries: Int = 0

    public fun chat(input: String, listener: TextChatListener){
        listener.onStartRequest()

        enduserApi.chat(input, conversationContext, botId, object : ChatAPICallback {
            override fun finished(text: String, context: String, actions: ArrayList<ChatbotAction>, confidence: Double, success: Boolean) {
                retries = 0

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
    abstract fun onStartRequest()
    abstract fun onFinished(answer: Answer)
    abstract fun onError(errorCode: String, message: String)
}