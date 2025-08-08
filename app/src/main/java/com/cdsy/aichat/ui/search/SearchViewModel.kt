package com.cdsy.aichat.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cdsy.aichat.manager.api.CharacterService
import com.cdsy.aichat.model.api.character.Character
import com.cdsy.aichat.model.api.character.Tag
import com.cdsy.aichat.ui.base.BaseViewModel
import com.cdsy.aichat.util.switchThread
import org.kodein.di.generic.instance

val mockTagList = listOf(
    Tag("safcasd", "Urban"),
    Tag("Popular", "Popular"),
    Tag("BBBBB", "BBBBB")
)


class SearchViewModel(application: Application) : BaseViewModel(application) {
    private var currentPage = 1
    val characterList = MutableLiveData<List<Character>>()
    val popularList = MutableLiveData<List<Tag>>()
    val keyword = MutableLiveData<String>()

    private val characterService by instance<CharacterService>()

    fun fetchPopularList() {
        popularList.postValue(mockTagList)
    }

    fun fetchCharacterList() {
        //currentTag.value
        characterService.fetchCharacterList(page = 1, filter = keyword.value)
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
            filter = keyword.value
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