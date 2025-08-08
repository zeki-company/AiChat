package com.cdsy.aichat.ui.base

import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

interface BindLife {
    val compositeDisposable: CompositeDisposable

    fun Disposable.bindLife() = addDisposable(this)

    fun Single<*>.bindLife() = subscribe({ }, { Timber.d(it.toString(), "Single error") }).bindLife()

    fun Observable<*>.bindLife() =
        subscribe({ }, { Timber.d(it.toString(), "Observable error") }).bindLife()

    fun Completable.bindLife() =
        subscribe({ }, { Timber.d(it.toString(), "Completable error") }).bindLife()

    fun Maybe<*>.bindLife() = subscribe({}, { Timber.d(it.toString(), "Maybe error") }).bindLife()

    fun Flowable<*>.bindLife() = subscribe({ }, { Timber.d(it.toString(), "Flowable  error") }).bindLife()

    fun addDisposable(disposable: Disposable): Disposable {
        compositeDisposable.add(disposable)
        return disposable
    }

    fun removeDisposable(disposable: Disposable?) {
        if (disposable != null)
            compositeDisposable.remove(disposable)
    }

    fun destroyDisposable() = compositeDisposable.clear()
}