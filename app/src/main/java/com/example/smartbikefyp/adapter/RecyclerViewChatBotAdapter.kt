package com.example.smartbikefyp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartbikefyp.R
import com.example.smartbikefyp.databinding.RecyclerViewChatBot1LayoutBinding
import com.example.smartbikefyp.databinding.RecyclerViewChatBot2LayoutBinding
import com.example.smartbikefyp.model.ChatBotRecyclerViewItem

class RecyclerViewChatBotAdapter : RecyclerView.Adapter<ChatBotRecyclerViewHolder>() {

    var items = listOf<ChatBotRecyclerViewItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatBotRecyclerViewHolder {
        return when (viewType) {
            R.layout.recycler_view_chat_bot_1_layout -> ChatBotRecyclerViewHolder.BotChatViewHolder(
                RecyclerViewChatBot1LayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.recycler_view_chat_bot_2_layout -> ChatBotRecyclerViewHolder.UserChatViewHolder(
                RecyclerViewChatBot2LayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: ChatBotRecyclerViewHolder, position: Int) {
        when(holder){
            is ChatBotRecyclerViewHolder.BotChatViewHolder -> holder.bind(items[position] as ChatBotRecyclerViewItem.BotChat)
            is ChatBotRecyclerViewHolder.UserChatViewHolder -> holder.bind(items[position] as ChatBotRecyclerViewItem.UserChat)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is ChatBotRecyclerViewItem.BotChat -> R.layout.recycler_view_chat_bot_1_layout
            is ChatBotRecyclerViewItem.UserChat -> R.layout.recycler_view_chat_bot_2_layout
        }
    }
}