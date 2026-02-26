package com.cdsy.aichat.ui.homepage

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cdsy.aichat.manager.api.ClubService
import com.cdsy.aichat.manager.api.UserService
import com.cdsy.aichat.model.api.club.ClubItem
import com.cdsy.aichat.model.api.club.ClubListRequest
import com.cdsy.aichat.model.api.club.NewBonusInfo
import com.cdsy.aichat.model.api.user.Balance
import com.cdsy.aichat.ui.base.BaseViewModel
import com.cdsy.aichat.util.switchThread
import org.kodein.di.generic.instance

/**
 * 对应旧项目 ClubFragment + GetClubListPresenterImpl + GetNewBonusCountDownPresenterImpl 的 MVVM 版。
 * - 列表接口：v1/club/search
 * - 新用户优惠：v1/video/call/coin
 */
class ClubViewModel(application: Application) : BaseViewModel(application) {

    private val clubService by instance<ClubService>()
    private val userService by instance<UserService>()

    val clubList = MutableLiveData<List<ClubItem>>()
    val balance = MutableLiveData<Balance>()
    val newBonusInfo = MutableLiveData<NewBonusInfo>()
    val canLoadMore = MutableLiveData(false)

    private var lastId: String = ""
    private val pageSize = 20

    fun fetchBalance() {
        userService.fetchUserBalance()
            .switchThread()
            .catchApiError()
            .doOnSuccess { result ->
                result.data.let { balance.postValue(it) }
            }
            .bindLife()
    }

    fun refreshClubList() {
        lastId = ""
        loadClubList(isLoadMore = false)
    }

    fun loadMoreClubList() {
        if (canLoadMore.value == true) {
            loadClubList(isLoadMore = true)
        }
    }

    private fun loadClubList(isLoadMore: Boolean) {
        val request = ClubListRequest(
            lastId = if (isLoadMore) lastId else "",
            pageSize = pageSize
        )
        clubService.getClubList(request)
            .switchThread()
            .catchApiError()
            .autoSwipeRefresh()
            .doOnSuccess { resp ->
                val list = resp.data.orEmpty()
                val current = clubList.value?.toMutableList() ?: mutableListOf()
                if (isLoadMore) {
                    current.addAll(list)
                } else {
                    current.clear()
                    current.addAll(list)
                }
                clubList.postValue(current)

                resp.page?.let { page ->
                    canLoadMore.postValue(page.haveMore == 1)
                    lastId = page.lastId.orEmpty()
                }
            }
            .bindLife()
    }

    fun fetchNewBonusInfo() {
        clubService.getNewBonusInfo()
            .switchThread()
            .catchApiError()
            .doOnSuccess { resp ->
                resp.data?.let { newBonusInfo.postValue(it) }
            }
            .bindLife()
    }
}
