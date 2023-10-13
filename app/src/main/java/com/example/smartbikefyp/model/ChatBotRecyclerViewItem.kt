package com.example.smartbikefyp.model

import kotlinx.serialization.descriptors.PrimitiveKind

sealed class ChatBotRecyclerViewItem {
    data class BotChat(val name: String, val body: String): ChatBotRecyclerViewItem()
    data class UserChat(val email: String, val body: String): ChatBotRecyclerViewItem()
}
