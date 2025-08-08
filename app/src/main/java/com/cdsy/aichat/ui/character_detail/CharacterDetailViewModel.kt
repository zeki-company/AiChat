package com.cdsy.aichat.ui.character_detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cdsy.aichat.manager.api.CharacterService
import com.cdsy.aichat.model.api.character.CharacterDetail
import com.cdsy.aichat.model.api.character.getCredit
import com.cdsy.aichat.ui.base.BaseViewModel
import com.cdsy.aichat.util.switchThread
import org.kodein.di.generic.instance

class CharacterDetailViewModel(application: Application) : BaseViewModel(
    application
) {
    val characterService by instance<CharacterService>()
    val characterDetail = MutableLiveData<CharacterDetail>()
    val credit = MutableLiveData<Int>()
    var isTextExpanded = MutableLiveData(true)// 添加状态变量来跟踪文本是否展开
    fun fetchCharacterDetail(id: String) {
        characterService.fetchCharacterDetails(id)
            .flatMap {
                characterDetail.postValue(it.data)
                characterService.fetchAIModeList()
            }
            .switchThread()
            .catchApiError()
            .autoSwipeRefresh()
            .doOnSuccess {
                credit.postValue(it.data.list.getCredit())
            }.bindLife()
    }

}