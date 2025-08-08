package com.cdsy.aichat.ui.homepage

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cdsy.aichat.manager.api.CharacterService
import com.cdsy.aichat.manager.api.UserService
import com.cdsy.aichat.model.api.character.Character
import com.cdsy.aichat.model.api.character.CharacterType
import com.cdsy.aichat.model.api.user.Balance
import com.cdsy.aichat.ui.base.BaseViewModel
import com.cdsy.aichat.util.switchThread
import org.kodein.di.generic.instance

class HomepageViewModel(application: Application) : BaseViewModel(application) {

    private val characterService by instance<CharacterService>()
    private val userService by instance<UserService>()

    val currentTag = MutableLiveData<CharacterType>()
    val tabList = MutableLiveData<List<CharacterType>>()

    private var currentPage = 1
    val characterList = MutableLiveData<List<Character>>()
    val balance = MutableLiveData<Balance>()

    fun fetchTabList() {
        characterService.fetchCharacterTypeList()
            .switchThread()
            .catchApiError()
            .doOnSuccess {
                tabList.postValue(it.data.list)
            }.bindLife()
    }

    fun fetchBalance(){
        userService.fetchUserBalance()
            .switchThread()
            .catchApiError()
            .doOnSuccess {
                balance.postValue(it.data)
            }.bindLife()
    }

    fun fetchCharacterList() {
        //currentTag.value
        characterService.fetchCharacterList(page = 1, filter = currentTag.value?.name)
            .switchThread()
            .autoSwipeRefresh()
            .catchApiError()
            .doOnSuccess { result ->
                characterList.postValue(result.data.list)
                currentPage = 1
            }.bindLife()
    }

    fun loadMoreCharacterList() {
        characterService.fetchCharacterList(
            page = currentPage + 1,
            filter = currentTag.value?.name
        )
            .switchThread()
            .catchApiError()
            .doOnSuccess { result ->
                characterList.postValue(
                    characterList.value?.toMutableList()?.apply { addAll(result.data.list) }
                )
                currentPage += 1
            }.bindLife()
    }

}