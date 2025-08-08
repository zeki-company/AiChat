package com.cdsy.aichat.ui.chat

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cdsy.aichat.manager.api.base.globalMoshi
import com.cdsy.aichat.model.api.character.CharacterDetail
import com.cdsy.aichat.ui.base.BaseViewModel
import com.cdsy.aichat.ui.character_detail.KEY_CHARACTER_DETAIL
import com.cdsy.aichat.ui.character_detail.KEY_CREDIT
import com.cdsy.aichat.util.fromJson

class ChatViewModel(application: Application) : BaseViewModel(
    application
) {

    val credit = MutableLiveData(0)
    val characterDetail = MutableLiveData<CharacterDetail>()
    val chatMessageList = MutableLiveData<List<ChatMessage>>(mutableListOf())

    fun sendMessage(message: String) {
        // TODO: 实现网络请求，发送消息到AI
        chatMessageList.value = chatMessageList.value!!.toMutableList().apply { add(ChatMessage(message, false)) }
    }

}