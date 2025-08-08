package com.cdsy.aichat

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.util.LocaleHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class LocaleHelperTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testSetLocale() {
        // 测试设置中文语言
        val chineseContext = LocaleHelper.setLocale(context, "zh")
        assert(chineseContext.resources.configuration.locales[0].language == "zh")

        // 测试设置英文语言
        val englishContext = LocaleHelper.setLocale(context, "en")
        assert(englishContext.resources.configuration.locales[0].language == "en")

        // 测试设置法语语言
        val frenchContext = LocaleHelper.setLocale(context, "fr")
        assert(frenchContext.resources.configuration.locales[0].language == "fr")
    }

    @Test
    fun testGetLanguage() {
        // 测试获取默认语言
        val defaultLanguage = LocaleHelper.getLanguage(context)
        assert(defaultLanguage.isNotEmpty())

        // 测试设置语言后获取
        SharedPrefModel.languageCode = "zh"
        val chineseLanguage = LocaleHelper.getLanguage(context)
        assert(chineseLanguage == "zh")
    }

    @Test
    fun testGetLocale() {
        val locale = LocaleHelper.getLocale(context.resources)
        assert(locale is Locale)
    }
} 