package com.cdsy.aichat.ui.me

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cdsy.aichat.manager.api.FinanceService
import com.cdsy.aichat.manager.api.UserService
import com.cdsy.aichat.model.api.finance.Goods
import com.cdsy.aichat.model.api.user.UserInfo
import com.cdsy.aichat.ui.base.BaseViewModel
import com.cdsy.aichat.util.switchThread
import org.kodein.di.generic.instance

class AccountViewModel(application: Application) : BaseViewModel(application) {

    val userService by instance<UserService>()
    val financeService by instance<FinanceService>()

    val userInfo = MutableLiveData<UserInfo>()
    val portrait = MutableLiveData<String>()
    val name = MutableLiveData<String>()

    val goodsList = MutableLiveData<List<Goods>>()


    fun fetchUserInfo() {
        userService.fetchUserInfo()
            .switchThread()
            .autoSwipeRefresh()
            .catchApiError()
            .doOnSuccess {
                userInfo.postValue(it.data)
            }.bindLife()
    }

    fun fetchGoodsList() {
        financeService.fetchGoodsList()
            .switchThread()
            .catchApiError()
            .doOnSuccess {
                goodsList.postValue(it.data.list)
            }.bindLife()
    }
}