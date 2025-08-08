package com.cdsy.aichat.manager.finance

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.cdsy.aichat.MainApplication
import com.cdsy.aichat.manager.api.FinanceService
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.model.api.finance.Goods
import com.cdsy.aichat.model.api.finance.OriginPurchase
import com.cdsy.aichat.model.api.finance.SubmitReceiptRequestModel
import com.cdsy.aichat.ui.base.ApiErrorLiveEvent
import com.cdsy.aichat.ui.base.autoProgressDialog
import com.cdsy.aichat.ui.base.catchApiError
import com.cdsy.aichat.util.fromJson
import com.cdsy.aichat.util.globalMoshi
import com.cdsy.aichat.util.switchThread
import io.reactivex.disposables.Disposable
import timber.log.Timber

object GoogleBillingManager {

    private lateinit var billingClient: BillingClient


    fun init() {
        setupBillingClient()
    }

    //初始化BillingClient
    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(MainApplication.instance!!)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
            )
            .enableAutoServiceReconnection()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    // 可以查询商品信息了
                    Timber.d("Google Billing 连接成功!!")
                }
            }

            override fun onBillingServiceDisconnected() {
                // 尝试重新连接
                Timber.d("Google Billing 尝试重新连接!!")
            }
        })
    }


    //查询商品信息
    fun Goods.fetchProductsInfo(onSuccess: (ProductDetails) -> Unit) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                //.setProductId(id)
                .setProductId("android.test.purchased") //测试商品id
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsListResult ->
            if (billingResult.responseCode == BillingResponseCode.OK) {
                onSuccess(productDetailsListResult.productDetailsList.last())
            } else {
                Timber.e("Google Billing error ${billingResult.debugMessage}")
            }
        }

    }

    private lateinit var financeService: FinanceService

    //发起购买流程
    fun Activity.startPurchase(productDetails: ProductDetails, financeService: FinanceService) {
        this@GoogleBillingManager.financeService = financeService
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(this, billingFlowParams)
    }

    private lateinit var progressDialog: MutableLiveData<Boolean>
    private lateinit var apiErrorLiveEvent: ApiErrorLiveEvent
    private val disposables = mutableListOf<Disposable>()

    fun setUpUIListener(
        progressDialog: MutableLiveData<Boolean>,
        apiErrorLiveEvent: ApiErrorLiveEvent
    ) {
        this.progressDialog = progressDialog
        this.apiErrorLiveEvent = apiErrorLiveEvent
    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
            Timber.d("Google 用户内购成功！")
            val purchaseOrigin = globalMoshi.fromJson<OriginPurchase>(purchases.last().originalJson)
            val submitReceiptRequestModel = SubmitReceiptRequestModel(
                product_id = purchaseOrigin?.productId?:"",
                receipt_data = purchases.last().purchaseToken
            )
            Timber.d("Google 小票: Purchase $purchases")
            Timber.d("Google 小票origin: Origin Purchase $purchaseOrigin")
            Timber.d("将小票数据存入本地")
            SharedPrefModel.addUnSubmitReceipt(submitReceiptRequestModel)
            // 处理购买成功
            Timber.d("开始向服务器提交小票")
            val disposable = financeService.submitReceipt(submitReceiptRequestModel)
                .switchThread()
                .autoProgressDialog(progressDialog)
                .catchApiError(apiErrorLiveEvent)
                .doOnSuccess {
                    Timber.d("向服务器提交小票成功")
                    Timber.d("移除本地小票数据")
                    SharedPrefModel.removeUnSubmitReceipt(submitReceiptRequestModel)
                }
                .subscribe()
            disposables.add(disposable)
        } else if (billingResult.responseCode == BillingResponseCode.USER_CANCELED) {
            // 用户取消购买
            Timber.d("Google 用户取消购买！")
        } else {
            // 处理错误
            Timber.d("Google 内购错误！${billingResult.debugMessage}")
        }
    }

    fun release() {
        disposables.forEach {
            it.dispose()
        }
    }


    /*
*         // 检查未处理的购买
    billingClient.queryPurchasesAsync(
        QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
    ) { billingResult, purchasesList ->
        if (purchasesList.isNotEmpty()) {
            // 验证并处理购买
        }
    }

    // 对于订阅商品
    billingClient.queryPurchasesAsync(
        QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
    ) { billingResult, purchasesList ->
        // 处理订阅

    }
* */


}