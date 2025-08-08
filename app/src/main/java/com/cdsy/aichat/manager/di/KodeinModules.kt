package com.cdsy.aichat.manager.di

import android.app.Application
import com.cdsy.aichat.BuildConfig
import com.cdsy.aichat.manager.api.AppService
import com.cdsy.aichat.manager.api.CharacterService
import com.cdsy.aichat.manager.api.DeviceService
import com.cdsy.aichat.manager.api.FinanceService
import com.cdsy.aichat.manager.api.UserService
import com.cdsy.aichat.manager.api.base.ApiClient
import com.cdsy.aichat.manager.api.base.HeaderInterceptor
import com.cdsy.aichat.manager.api.base.NetErrorInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.concurrent.TimeUnit

fun koinModule(app: Application) = Kodein.Module("database") {
    /*bind<AppDatabase>() with singleton {
        Room.databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME_APP)
            //.addMigrations(Migrations.Migration_1_2)
            //.addCallback(AppDatabase.CALLBACK)
            .build()
    }*/

    bind<ApiClient>() with singleton { provideApiClient() }
    bind<DeviceService>() with singleton { instance<ApiClient>().createService(DeviceService::class.java) }
    bind<AppService>() with singleton { instance<ApiClient>().createService(AppService::class.java) }
    bind<UserService>() with singleton { instance<ApiClient>().createService(UserService::class.java) }
    bind<CharacterService>() with singleton { instance<ApiClient>().createService(CharacterService::class.java) }
    bind<FinanceService>() with singleton { instance<ApiClient>().createService(FinanceService::class.java) }
}

fun provideApiClient(): ApiClient {
    val client = ApiClient.Builder()
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    client.okBuilder
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(NetErrorInterceptor())
        .readTimeout(30, TimeUnit.SECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }
    return client.build(baseUrl = BuildConfig.BASE_URL)
}