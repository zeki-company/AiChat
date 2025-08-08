package com.cdsy.aichat.ui.message

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cdsy.aichat.R
import com.cdsy.aichat.ui.message.list.MessageListFragment

class MessageListPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments by lazy {
        listOf(
            MessageListFragment(fragment.getString(R.string.messages_item_messages)),
            MessageListFragment(fragment.getString(R.string.messages_item_following))
        )
    }

    private val tabTitles = listOf(
        fragment.getString(R.string.messages_item_messages),
        fragment.getString(R.string.messages_item_following)
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getTabTitle(position: Int): String = tabTitles[position]
}