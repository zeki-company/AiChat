package com.cdsy.aichat.ui.chat

import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.ItemChatMessageBinding
import com.cdsy.aichat.ui.base.BaseRecyclerAdapter
import io.noties.markwon.Markwon

data class ChatMessage(
    val content: String,
    val isFromAI: Boolean,
    val time: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        return time == (other as ChatMessage).time
    }
}

class ChatRecyclerAdapter(private val markwon: Markwon) :
    BaseRecyclerAdapter<ChatMessage, ItemChatMessageBinding>(R.layout.item_chat_message) {
    override fun onCellClick(bean: ChatMessage) {

    }

    override fun bindData(binding: ItemChatMessageBinding, position: Int) {
        val item = baseList[position]
        binding.chatMessage = item
        if (item.isFromAI) {
            markwon.setMarkdown(binding.tvMsg, item.content)
        } else {
            binding.tvMsg.text = item.content
        }
        // 可根据msg.isFromAI设置不同背景
        if (item.isFromAI) {
            binding.tvMsg.setBackgroundResource(R.drawable.bg_chat_ai)
        } else {
            binding.tvMsg.setBackgroundResource(R.drawable.bg_chat_me)
        }
    }

}