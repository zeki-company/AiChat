package com.cdsy.aichat.manager.api

import com.cdsy.aichat.model.api.ResultModel
import com.cdsy.aichat.model.api.character.CharacterDetail
import com.cdsy.aichat.model.api.character.CharacterListModel
import com.cdsy.aichat.model.api.character.CharacterTypeListModel
import com.cdsy.aichat.model.api.character.UIModeListModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/*************************************************角色相关******************************************/
interface CharacterService {


    //拉取角色列表
    @GET("character/list")
    fun fetchCharacterList(
        @Query("page") page: Int = 0,
        @Query("page_size") pageSize: Int = 6,
        @Query("sort") sort: String = "popular",
        @Query("filter") filter: String? = null
    ): Single<ResultModel<CharacterListModel>>

    //拉取角色分类列表
    @GET("category/list")
    fun fetchCharacterTypeList(): Single<ResultModel<CharacterTypeListModel>>

    //拉取角色详情
    @GET("character")
    fun fetchCharacterDetails(
        @Query("id") id: String
    ): Single<ResultModel<CharacterDetail>>

    //查询AI模型列表
    @GET("model/list")
    fun fetchAIModeList():Single<ResultModel<UIModeListModel>>

}
