package com.cdsy.aichat.ui.message.list

import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentMessagesListBinding
import com.cdsy.aichat.ui.base.BaseFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdsy.aichat.ui.message.MessagesRecyclerAdapter
import com.cdsy.aichat.ui.view.MessagePopupDialog
import com.cdsy.aichat.ui.message.Message
import com.cdsy.aichat.ui.view.MessageDialog

class MessageListFragment(val type: String) :
    BaseFragment<FragmentMessagesListBinding, MessageListViewModel>(
        MessageListViewModel::class.java,
        R.layout.fragment_messages_list
    ) {
    override fun initView() {
        setupRecyclerView()
        binding.srl.setOnRefreshListener {
            viewModel.fetchMessageList()
        }
        viewModel.progressDialog.observeNonNull {
            binding.srl.isRefreshing = it
        }
        viewModel.messageList.observeNonNull {
            (binding.rvMessages.adapter as MessagesRecyclerAdapter).replaceData(it)
        }
    }

    override fun initData() {
        viewModel.fetchMessageList()
    }

    private fun setupRecyclerView() {
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = MessagesRecyclerAdapter(
                onItemClick = {
                    //requireContext().navigateToChat(it.characterId)
                },
                onItemLongClick = { msg, view ->
                    showMessagePopup(msg, view)
                }
            )
        }
    }

    private fun showMessagePopup(message: Message, anchorView: android.view.View) {
        MessagePopupDialog(
            context = requireContext(),
            anchorView = anchorView,
            onPinClick = {
                // 处理置顶逻辑
                viewModel.pinMessage(message)
            },
            onDeleteClick = {
                showDeleteDialog(message)

            }
        ).show()
    }


    private fun showDeleteDialog(message: Message){
        MessageDialog(
            requireContext(),
            R.string.messages_item_delete_chat_title,
            R.string.messages_item_delete_chat_content,
            {
                // 处理删除逻辑
                viewModel.deleteMessage(message)
            },
            {

            }
        ).show()
    }
}