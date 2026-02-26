package com.cdsy.aichat.util

import android.content.Context
import com.cdsy.aichat.model.country.CountryEntity
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * 对应旧项目的 LocalCountryData，负责从 assets/countries_all.json 读取国家信息，
 * 并提供 alpha2Code -> "emoji + name" 的映射。
 */
object LocalCountryData {

    private var countriesAllList: MutableList<CountryEntity>? = null
    private var countriesNameMap: MutableMap<String, String>? = null

    private fun countryCodeToEmojiFlag(countryCode: String): String {
        if (countryCode.length < 2) return ""
        val firstLetter = countryCode[0].code - 0x41 + 0x1F1E6
        val secondLetter = countryCode[1].code - 0x41 + 0x1F1E6
        return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
    }

    /**
     * 根据 ISO2 国家码返回 "🇺🇸 United States" 这样的展示文案。
     */
    fun getCountryNameByIso(ctx: Context, iso: String): String {
        val map = getAllCountriesNameMap(ctx)
        return map[iso] ?: ""
    }

    fun getAllCountriesNameMap(ctx: Context): MutableMap<String, String> {
        countriesNameMap?.let { return it }
        val map = mutableMapOf<String, String>()
        val json = readCountriesJson(ctx) ?: return map.also { countriesNameMap = it }
        val ja = JSONArray(json)
        for (i in 0 until ja.length()) {
            val jo = ja.getJSONObject(i)
            val name = jo.optString("name")
            val alpha2Code = jo.optString("alpha2Code")
            val callingCode = jo.optString("callingCode")
            if (name.isNotEmpty() && alpha2Code.isNotEmpty() && callingCode.isNotEmpty()) {
                val flag = countryCodeToEmojiFlag(alpha2Code)
                val flagAndName = "$flag $name"
                map[alpha2Code] = flagAndName
            }
        }
        countriesNameMap = map
        return map
    }

    fun getAll(ctx: Context): List<CountryEntity> {
        countriesAllList?.let { return it }
        val list = mutableListOf<CountryEntity>()
        val json = readCountriesJson(ctx) ?: return list.also { countriesAllList = it }
        val ja = JSONArray(json)
        for (i in 0 until ja.length()) {
            val jo = ja.getJSONObject(i)
            val name = jo.optString("name")
            val alpha2Code = jo.optString("alpha2Code")
            val callingCode = jo.optString("callingCode")
            if (name.isNotEmpty() && alpha2Code.isNotEmpty() && callingCode.isNotEmpty()) {
                list.add(CountryEntity(alpha2Code, callingCode, name))
            }
        }
        countriesAllList = list
        return list
    }

    fun destroy() {
        countriesAllList = null
        countriesNameMap = null
    }

    private fun readCountriesJson(ctx: Context): String? {
        return try {
            val input = ctx.assets.open("countries_all.json")
            BufferedReader(InputStreamReader(input)).use { br ->
                val sb = StringBuilder()
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                sb.toString()
            }
        } catch (_: Exception) {
            null
        }
    }
}

