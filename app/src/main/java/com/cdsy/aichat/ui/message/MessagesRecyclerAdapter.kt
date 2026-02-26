package com.cdsy.aichat.ui.message

import com.bumptech.glide.Glide
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.ItemEnableNotificationBinding
import com.cdsy.aichat.databinding.ItemMessagesBinding
import com.cdsy.aichat.databinding.ItemNoMoreBinding
import com.cdsy.aichat.ui.base.HeaderRecyclerAdapter
import timber.log.Timber


class MessagesRecyclerAdapter(
    private val onItemClick: (Message) -> Unit,
    private val onItemLongClick: (Message, android.view.View) -> Unit
) :
    HeaderRecyclerAdapter<Message, ItemMessagesBinding, ItemEnableNotificationBinding, ItemNoMoreBinding>(
        R.layout.item_messages,
        R.layout.item_enable_notification
    ) {
    override fun onCellClick(bean: Message) {
        onItemClick(bean)
    }

    override fun bindData(binding: ItemMessagesBinding, position: Int) {
        val message = baseList[position]
        binding.message = message
        if (message.isSystem) {
            binding.ivPortrait.setImageResource(R.drawable.icon_system_message)
        } else {
            Glide.with(binding.ivPortrait.context).load(message.portrait)
                .placeholder(R.color.hintColor)
                .skipMemoryCache(false)
                .centerCrop()
                .into(binding.ivPortrait)
        }

        binding.root.setOnClickListener { view ->
            onCellClick(baseList[position])
        }

        binding.root.setOnLongClickListener { view ->
            Timber.d("MessagesRecyclerAdapter", "Long click detected for position: $position")
            onItemLongClick(baseList[position], view)
            true
        }

    }

    override fun bindHeader(headerBinding: ItemEnableNotificationBinding) {
        headerBinding.tvNotNow.setOnClickListener {

        }
        headerBinding.tvTurnOn.setOnClickListener {

        }
    }

    override fun bindTail(tailBinding: ItemNoMoreBinding) {

    }
}