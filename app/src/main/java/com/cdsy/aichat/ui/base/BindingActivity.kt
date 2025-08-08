package com.cdsy.aichat.ui.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cdsy.aichat.R
import com.cdsy.aichat.util.LocaleHelper
import com.cdsy.aichat.util.setStatusBarColor
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

abstract class BindingActivity<T : ViewDataBinding, VM : BaseViewModel>(
    layoutRes: Int,
    private val clazz: Class<VM>,
) : AppCompatActivity(), KodeinAware, BindLife {
    override val compositeDisposable = CompositeDisposable()
    override val kodein by closestKodein()
    private var currentThrottleTime = 0L

    private var quiteTime: Long = System.currentTimeMillis()
    protected open fun backNeedTwiceDown() = false

    protected val binding: T by lazy { DataBindingUtil.setContentView<T>(this, layoutRes) }
    open val viewModel: VM by lazy { ViewModelProvider(this).get(clazz) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(this, Color.TRANSPARENT)
        // 沉浸式状态栏和导航栏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        // 如果需要让内容延伸到状态栏下方
        ViewCompat.getWindowInsetsController(window.decorView)?.apply {
            isAppearanceLightStatusBars = false // 状态栏字体黑色，false为白色
            isAppearanceLightNavigationBars = false // 导航栏字体黑色
        }
        initView()
        initData()
        viewModel.apiErrorEvent.handleEvent(this,this)
        viewModel.progressDialog.observeNonNull {
            if (it) DialogUtil.showProgressDialog(this)
            else DialogUtil.hideProgressDialog()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    abstract fun initView()
    abstract fun initData()

    /**
     *  startActivity / Navigation with Throttle by default
     */
    fun startActivity(intent: Intent?, throttleFirst: Boolean) {
        if (throttleFirst) startActivity(intent)
        else super.startActivity(intent)
    }

    /*    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
            if (System.currentTimeMillis() - currentThrottleTime < 1000) return
            currentThrottleTime = System.currentTimeMillis()
            super.startActivityForResult(intent, requestCode, options)
        }*/

    //ext
    protected fun <T> LiveData<T>.observe(observer: (T?) -> Unit) where T : Any =
        observe(this@BindingActivity, Observer<T> { v -> observer(v) })

    protected fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        this.observe(this@BindingActivity, Observer {
            if (it != null) {
                observer(it)
            }
        })
    }

    override fun onBackPressed() {
        if (backNeedTwiceDown()) {
            if (System.currentTimeMillis() - quiteTime > 3000) {
                Toast.makeText(this, R.string.exit_info, Toast.LENGTH_SHORT).show()
                quiteTime = System.currentTimeMillis()
            } else {
                finish()
            }
        } else {
            super.onBackPressed()
        }
    }

}
