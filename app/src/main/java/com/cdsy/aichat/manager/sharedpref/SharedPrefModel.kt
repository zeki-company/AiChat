package com.cdsy.aichat.manager.sharedpref

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.cdsy.aichat.MainApplication
import com.cdsy.aichat.model.api.app.AppConfigModel
import com.cdsy.aichat.model.api.finance.SubmitReceiptRequestModel
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.PreferencesOpener
import com.cdsy.aichat.util.Constants
import com.chibatching.kotpref.gsonpref.gsonPref

/**
 *  user data need to be encrypt / decrypt
 *
 */
object SharedPrefModel : KotprefModel() {
    override val kotprefName: String = Constants.SHARED_PREF_FILE_NAME

    //设备参数
    var xDeviceId: String by stringPref()
    var appEnvCode: String by stringPref()
    var appConfigVersionCode: String by stringPref()

    // 存储单个对象
    var appConfig: AppConfigModel by gsonPref(AppConfigModel())

    // 存储复杂对象Map
    var userSettingMap: MutableMap<String, UserModel> by gsonPref(hashMapOf())

    //do not use getter after login , this maybe clear , use MainApplication.nowUserId instead.
    var userId: String by stringPref()

    // 语言设置
    var languageCode: String by stringPref()

    fun getUserModel(userId: String = MainApplication.nowUserId): UserModel =
        userSettingMap[userId] ?: UserModel().apply {
            userSettingMap[userId] = this
        }

    fun setUserModel(userId: String = MainApplication.nowUserId, modify: UserModel.() -> Unit) {
        val map = userSettingMap
        val user = map[userId] ?: UserModel()
        user.apply { modify.invoke(this) }
        map[userId] = user
        userSettingMap = map
    }

    fun updateUserModelSet(map: Map<String, UserModel>) {
        userSettingMap = map.toMutableMap()
    }

    /*    // 添加用户到列表
        fun addUserToList(user: UserModel) {
            val currentList = userList.toMutableList()
            currentList.add(user)
            userList = currentList
        }

        // 更新用户列表
        fun updateUserList(users: List<UserModel>) {
            userList = users
        }*/

    fun setDefault() {
        setUserModel {

        }
        userId = ""
    }

    //添加未上传的小票
    fun addUnSubmitReceipt(submitReceiptRequestModel: SubmitReceiptRequestModel) {
        setUserModel {
            unSubmitReceiptList = unSubmitReceiptList.apply {
                add(submitReceiptRequestModel)
            }
        }
    }

    //移除小票
    fun removeUnSubmitReceipt(submitReceiptRequestModel: SubmitReceiptRequestModel) {
        setUserModel {
            unSubmitReceiptList = unSubmitReceiptList.apply {
                remove(submitReceiptRequestModel)
            }
        }
    }

}


private class EncryptionOpener : PreferencesOpener {
    override fun openPreferences(context: Context, name: String, mode: Int): SharedPreferences {
        return EncryptedSharedPreferences
            .create(
                name,
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    }
}