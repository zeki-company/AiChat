package com.cdsy.aichat.ui.message.list

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.cdsy.aichat.ui.base.BaseViewModel
import com.cdsy.aichat.ui.message.Message

val mockUrl =
    "https://iknow-pic.cdn.bcebos.com/86d6277f9e2f07086706a89dfb24b899a901f228?for=bg"

val list = listOf(
    Message("xxxxxx", "asdsafddsgfewfewfzdfcsqwa", "sd","", 4,"2020-9-3", true),
    Message("CCCCC", "AQQQQQQQQ", mockUrl,"sd", 0,"2020-9-2", false),
    Message("ZZZ", "MMMMMMMM", mockUrl, "sd",1,"2020-9-1", false)
)

class MessageListViewModel(application: Application) : BaseViewModel(application) {

    val messageList = MutableLiveData<List<Message>>()

    fun fetchMessageList() {
        progressDialog.postValue(true)
        messageList.postValue(list)
        progressDialog.postValue(false)
    }

    fun pinMessage(message: Message) {
        // 处理置顶逻辑
        /*val currentList = messageList.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOf(message)
        if (index != -1) {
            // 将消息移到列表顶部
            currentList.removeAt(index)
            currentList.add(0, message)
            messageList.postValue(currentList)
        }*/
    }

    fun deleteMessage(message: Message) {
        // 处理删除逻辑
        /*val currentList = messageList.value?.toMutableList() ?: mutableListOf()
        currentList.remove(message)
        messageList.postValue(currentList)*/
    }

}