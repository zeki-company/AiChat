package com.cdsy.aichat.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.cdsy.aichat.BuildConfig
import com.cdsy.aichat.model.api.device.DeviceRequestModel
import com.cdsy.aichat.model.api.device.NetworkInfo
import java.util.*


class DeviceUtil {

    companion object {

        /**
         * 获取设备唯一标识符
         */
        @SuppressLint("HardwareIds")
        fun getUniqueId(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                ?: ""
        }

        /**
         * 获取平台信息
         */
        fun getPlatform(): String {
            return "android"
        }

        /**
         * 获取设备名称
         */
        fun getDeviceName(): String {
            return Build.MANUFACTURER + " " + Build.MODEL
        }

        /**
         * 获取设备模型
         */
        fun getDeviceModel(): String {
            return Build.MODEL
        }

        /**
         * 获取系统名称
         */
        fun getOsName(): String {
            return "Android"
        }

        /**
         * 获取系统版本
         */
        fun getOsVersion(): String {
            return Build.VERSION.RELEASE
        }

        /**
         * 获取时区
         */
        fun getTimezone(): String {
            return TimeZone.getDefault().id
        }

        /**
         * 获取语言
         */
        fun getLanguage(): String {
            return Locale.getDefault().language
        }

        /**
         * 检查设备是否已root
         */
        fun isRooted(): String {
            val buildTags = Build.TAGS
            val buildFingerprint = Build.FINGERPRINT
            val buildModel = Build.MODEL
            val buildProduct = Build.PRODUCT

            return if (buildTags != null && buildTags.contains("test-keys") ||
                buildFingerprint.contains("generic") ||
                buildModel.contains("google_sdk") ||
                buildModel.contains("Emulator") ||
                buildModel.contains("Android SDK built for x86") ||
                buildProduct.contains("sdk") ||
                buildProduct.contains("google_sdk") ||
                buildProduct.contains("sdk_gphone") ||
                buildProduct.contains("vbox86p") ||
                buildProduct.contains("emulator") ||
                buildProduct.contains("simulator")
            ) {
                "true"
            } else {
                "false"
            }
        }

        /**
         * 获取屏幕宽度
         */
        fun getScreenWidth(context: Context): Int {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

        /**
         * 获取屏幕高度
         */
        fun getScreenHeight(context: Context): Int {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }

        /**
         * 获取像素密度比
         */
        fun getPixelRatio(context: Context): Double {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.density.toDouble()
        }

        /**
         * 获取网络类型
         */
        fun getNetworkType(context: Context): String {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

            return when {
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "wifi"
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "cellular"
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> "ethernet"
                else -> "unknown"
            }
        }

        /**
         * 获取MCC（移动国家代码）
         */
        @SuppressLint("HardwareIds")
        fun getMcc(context: Context): String? {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return null
            }
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.networkOperator?.substring(0, 3)
        }

        /**
         * 获取MNC（移动网络代码）
         */
        @SuppressLint("HardwareIds")
        fun getMnc(context: Context): String? {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return null
            }
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val networkOperator = telephonyManager.networkOperator
            return if (networkOperator != null && networkOperator.length > 3) {
                networkOperator.substring(3)
            } else null
        }

        /**
         * 获取SIM卡国家代码
         */
        @SuppressLint("HardwareIds")
        fun getSimCountryCode(context: Context): String? {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return null
            }
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.simCountryIso
        }

        /**
         * 获取SIM卡运营商
         */
        @SuppressLint("HardwareIds")
        fun getSimOperator(context: Context): String? {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return null
            }
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.simOperatorName
        }

        /**
         * 检查是否连接VPN
         */
        fun isVpnConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true
        }

        /**
         * 检查是否启用代理
         */
        fun isProxyEnabled(context: Context): String {
            val proxyHost = System.getProperty("http.proxyHost")
            val proxyPort = System.getProperty("http.proxyPort")
            return if (proxyHost != null && proxyPort != null) "true" else "false"
        }

        /**
         * 获取网络信息
         */
        fun getNetworkInfo(context: Context): NetworkInfo {
            return NetworkInfo(
                network_type = getNetworkType(context),
                mcc = getMcc(context),
                mnc = getMnc(context),
                sim_country_code = getSimCountryCode(context),
                sim_operator = getSimOperator(context),
                is_vpn_connected = isVpnConnected(context),
                is_proxy_enabled = isProxyEnabled(context)
            )
        }
    }
}