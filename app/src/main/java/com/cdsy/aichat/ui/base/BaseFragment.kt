package com.cdsy.aichat.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.cdsy.aichat.BR
import com.cdsy.aichat.R
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseFragment<Bind : ViewDataBinding, VM : BaseViewModel>
constructor(
    private val clazz: Class<VM>,
    private val bindingCreator: (LayoutInflater, ViewGroup?) -> Bind
) : Fragment(), BindLife, KodeinAware {

    constructor(clazz: Class<VM>, @LayoutRes layoutRes: Int) : this(clazz, { inflater, group ->
        DataBindingUtil.inflate(inflater, layoutRes, group, false)
    })

    protected open val viewModel: VM by lazy { ViewModelProvider(this).get(clazz) }
    protected lateinit var binding: Bind
    override val kodein by kodein()
    override val compositeDisposable = CompositeDisposable()
    private var currentThrottleTime = 0L

    private var lazyInitDataCompleted = false
    //method

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = bindingCreator.invoke(layoutInflater, container)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupToolBar()
        initEventObserver()
        initView()
        initDataAlways()
        if (!viewModel.vmInit) {
            initData()
            viewModel.vmInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        //activity?.window?.decorView?.post { detectClipFunction() }
        isVisibleToUser = true
        if (!lazyInitDataCompleted) {
            lazyInitData()
            lazyInitDataCompleted = true
        }
        /*AndroidSchedulers.mainThread().scheduleDirect(
            {
                if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                    lazyInitData()
                    lazyInitDataCompleted = true
                }
            },
            500,
            TimeUnit.MILLISECONDS
        )*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lazyInitDataCompleted = false
    }

    override fun onStop() {
        super.onStop()
        isVisibleToUser = false
    }

    private fun initEventObserver() {
        viewModel.apiErrorEvent.handleEvent(requireContext(), this)
        viewModel.dialogEvent.handleEvent(requireContext(), this)
        viewModel.progressDialog.observeNonNull {
            if (it) DialogUtil.showProgressDialog(context)
            else DialogUtil.hideProgressDialog()
        }
    }

    abstract fun initView()
    abstract fun initData()
    open fun lazyInitData() {}
    open fun initDataAlways() {}

    //当前是否对用户可见
    var isVisibleToUser = true

    /**
     *  startActivity / Navigation with Throttle by default
     */

    fun startActivity(intent: Intent, throttleFirst: Boolean = false) {
        if (throttleFirst) startActivity(intent)
        else super.startActivity(intent)
    }

    override fun startActivity(intent: Intent, options: Bundle?) {
        if (System.currentTimeMillis() - currentThrottleTime < 1000) return
        currentThrottleTime = System.currentTimeMillis()
        super.startActivity(intent, options)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        if (System.currentTimeMillis() - currentThrottleTime < 1000) return
        currentThrottleTime = System.currentTimeMillis()
        super.startActivityForResult(intent, requestCode, options)
    }

    fun navigate(@IdRes resId: Int, args: Bundle? = null, navOptions: NavOptions? = null) {
        if (System.currentTimeMillis() - currentThrottleTime < 1000) return
        currentThrottleTime = System.currentTimeMillis()
        findNavController().navigate(resId, args, navOptions, null)
    }

    //ext

    protected fun <T> LiveData<T>.observe(observer: (T?) -> Unit) where T : Any =
        observe(viewLifecycleOwner) { v -> observer(v) }

    protected fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        this.observe(viewLifecycleOwner) {
            if (it != null) observer(it)
        }
        val sd = HashSet<String>()
        sd.forEach { }
    }

    override fun onDestroy() {
        super.onDestroy()
        isVisibleToUser = false
        destroyDisposable()
    }

}
