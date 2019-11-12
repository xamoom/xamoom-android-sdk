package com.xamoom.android.xamoomsdk.Chatbot

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xamoom.android.xamoomsdk.ChatbotAction
import kotlinx.android.synthetic.main.chatbot_action.view.*
import androidx.core.graphics.drawable.DrawableCompat
import android.graphics.drawable.Drawable
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.R




class ChatbotActionsAdapter(private val actions: List<ChatbotAction>, val actionClickedListener: ActionClickedListener, val enduserApi: EnduserApi) :
        RecyclerView.Adapter<ChatbotActionsViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ChatbotActionsViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chatbot_action, parent, false)

        return ChatbotActionsViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ChatbotActionsViewHolder, position: Int) {
        val action = actions[position]
        holder.setupView(action.type, action.value, actionClickedListener, enduserApi)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = actions.size
}