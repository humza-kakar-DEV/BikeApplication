package com.example.smartbikefyp.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.smartbikefyp.databinding.RecyclerViewChatBot1LayoutBinding
import com.example.smartbikefyp.databinding.RecyclerViewChatBot2LayoutBinding
import com.example.smartbikefyp.model.ChatBotRecyclerViewItem

sealed class ChatBotRecyclerViewHolder (binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
    class BotChatViewHolder(private val binding: RecyclerViewChatBot1LayoutBinding) : ChatBotRecyclerViewHolder(binding){
        fun bind(botChat: ChatBotRecyclerViewItem.BotChat){
//            set views and on clicks here please
            binding.chatBotTextView.text = botChat.body
        }
    }
    class UserChatViewHolder(private val binding: RecyclerViewChatBot2LayoutBinding) : ChatBotRecyclerViewHolder(binding){
        fun bind(userChat: ChatBotRecyclerViewItem.UserChat){
//            set views and on clicks here please
            binding.chatUserTextView.text = userChat.body
        }
    }
}
