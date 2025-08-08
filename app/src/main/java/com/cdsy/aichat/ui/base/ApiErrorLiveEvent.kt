package com.cdsy.aichat.ui.base

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.cdsy.aichat.manager.api.base.ServerError
import com.cdsy.aichat.util.toJson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author Zeki
 * @version 1.0
 * @date 2019/10/11 18:10
 */

class ApiErrorLiveEvent : LiveDataEvent<Throwable>()

class SkipCatchApiException : RuntimeException()

fun <T> Single<T>.catchApiError(liveEvent: ApiErrorLiveEvent): Single<T> =
    doOnError {
        if (it is Exception && it !is SkipCatchApiException)
            liveEvent.value = Event(it)
    }

fun <T> Observable<T>.catchApiError(liveEvent: ApiErrorLiveEvent): Observable<T> =
    doOnError { if (it is Exception && it !is SkipCatchApiException) liveEvent.value = Event(it) }

fun Completable.catchApiError(liveEvent: ApiErrorLiveEvent): Completable =
    doOnError { if (it is Exception && it !is SkipCatchApiException) liveEvent.value = Event(it) }

fun ApiErrorLiveEvent.handleEvent(context: Context, owner: LifecycleOwner) {
    observe(owner) { event ->
        event.handleIfNot { error -> showApiErrorToast(context, error) }
    }
}

fun showApiErrorToast(context: Context, error: Throwable) {
    val msg = when (error){
        is ServerError -> error.message
        else -> error.message
    }
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
