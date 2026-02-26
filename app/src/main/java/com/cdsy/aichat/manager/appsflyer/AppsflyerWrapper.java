package com.cdsy.aichat.manager.appsflyer;



import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.appsflyer.deeplink.DeepLink;
import com.appsflyer.deeplink.DeepLinkListener;
import com.appsflyer.deeplink.DeepLinkResult;
import com.cdsy.aichat.BuildConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import io.reactivex.annotations.NonNull;
import timber.log.Timber;


public class AppsflyerWrapper {

    //"Non‑organic" → 广告用户
    //af_status = "Organic" → 自然用户
    public static String APP_USER_SOURCE;
    public final static String SOURCE_FACEBOOK = "facebook";
    public static String SOURCE_ORGANIC = "organic";

    private static AppsflyerWrapper _instance = null;

    public static AppsflyerWrapper getInstance() {
        if (_instance == null) {
            _instance = new AppsflyerWrapper();
        }
        return _instance;
    }

    private String AF_DEV_KEY = "nr4oEJwQ24GX4QMjEeVXiN";//""5H5xCvMgyHWEC4uJbxdQkA";
    private final String LOG_TAG = "[Appsflyer]";
    public static final String MEDIA_SOURCE = "share_invite";
    private final String BRANDED_LINK_DOMAIN = "app.kasualapp.com";
    private String INVITE_ONE_LINK_TEMPLATE_ID_DEV = "hkQn";//muO1";//""XDgD";
    //    private String INVITE_ONE_LINK_TEMPLATE_ID = "hhCm";//muO1";//""XDgD";
    private String INVITE_ONE_LINK_TEMPLATE_ID = "8ldG";//muO1";//""XDgD";

    private Context sContext;

    public void initAppsflyer(Context context) {
        Timber.d(LOG_TAG, "initAppsflyer");
        sContext = context;
        AppsFlyerLib.getInstance().init(AF_DEV_KEY, null, context);
        AppsFlyerLib.getInstance().setDebugLog(BuildConfig.DEBUG);

        AppsFlyerLib.getInstance().setAppInviteOneLink(INVITE_ONE_LINK_TEMPLATE_ID);
        AppsFlyerLib.getInstance().registerConversionListener(context, new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                for (String attrName : conversionData.keySet())
                    Log.d(LOG_TAG, "Conversion attribute: " + attrName + " = " + conversionData.get(attrName));
                //TODO - remove this
                String status = Objects.requireNonNull(conversionData.get("af_status")).toString();
                if (status.equals("Non-organic")) {
                    APP_USER_SOURCE = SOURCE_FACEBOOK;
                    if (Objects.requireNonNull(conversionData.get("is_first_launch")).toString().equals("true")) {
                        Log.d(LOG_TAG, "Conversion: First Launch");
                    } else {
                        Log.d(LOG_TAG, "Conversion: Not First Launch");
                    }
                } else {
                    APP_USER_SOURCE = SOURCE_ORGANIC;
                    Log.d(LOG_TAG, "Conversion: This is an organic install.");
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d(LOG_TAG, "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {
                if (!attributionData.containsKey("is_first_launch"))
                    Log.d(LOG_TAG, "onAppOpenAttribution: This is NOT deferred deep linking");
                for (String attrName : attributionData.keySet()) {
                    String deepLinkAttrStr = attrName + " = " + attributionData.get(attrName);
                    Log.d(LOG_TAG, "Deeplink attribute: " + deepLinkAttrStr);
                }
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d(LOG_TAG, "error onAttributionFailure : " + errorMessage);
            }
        });


        AppsFlyerLib.getInstance().subscribeForDeepLink(new DeepLinkListener() {
            @Override
            public void onDeepLinking(@NonNull DeepLinkResult deepLinkResult) {
                DeepLinkResult.Status dlStatus = deepLinkResult.getStatus();
                if (dlStatus == DeepLinkResult.Status.FOUND) {
                    Log.d(LOG_TAG, "Deep link found");
                } else if (dlStatus == DeepLinkResult.Status.NOT_FOUND) {
                    Log.d(LOG_TAG, "Deep link not found");
                    return;
                } else {
                    // dlStatus == DeepLinkResult.Status.ERROR
                    DeepLinkResult.Error dlError = deepLinkResult.getError();
                    Log.d(LOG_TAG, "There was an error getting Deep Link data: " + dlError.toString());
                    return;
                }
                DeepLink deepLinkObj = deepLinkResult.getDeepLink();
                try {
                    Log.d(LOG_TAG, "--------------------");
                    Log.d(LOG_TAG, "The DeepLink data is: " + deepLinkObj.toString());
                    String deepLinkValue = deepLinkObj.getDeepLinkValue();
                    String sub1 = deepLinkObj.getAfSub1();
                    String campaign = deepLinkObj.getCampaign();//c
                    String mediaSource = deepLinkObj.getMediaSource();//pid
                    String referrerId = deepLinkObj.getStringValue("af_referrer_customer_id");
                    String deepLinkSub1 = deepLinkObj.getStringValue("deep_link_sub1");

                    if (TextUtils.isEmpty(deepLinkSub1)) {
                        deepLinkSub1 = sub1;
                    }
                    Log.d(LOG_TAG, "campaign: " + campaign);
                    Log.d(LOG_TAG, "mediaSource: " + mediaSource);
                    Log.d(LOG_TAG, "referrerId: " + referrerId);
                    Log.d(LOG_TAG, "deepLinkSub1: " + deepLinkSub1);
                    Log.d(LOG_TAG, "af_sub1: " + sub1);


                    /*if (!TextUtils.isEmpty(deepLinkValue)) {
//                        if("AZM".equals(type) || "azm".equals(type)) {
                        if (DEEP_LINK_VALUE_SHARE.equals(deepLinkValue)) {
                            String deviceId = DeviceUtil.getDeviceId();
                            String appsFlyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(AppApplication.getInstance());
                            ShareInfoEntity request = new ShareInfoEntity();
                            request.setDeepLinkValue(deepLinkSub1);
//                            request.setC(campaign);
//                            request.setPid(mediaSource);
                            request.setAppsflyerId(appsFlyerId);
                            request.setImei(deviceId);
                            Log.d(LOG_TAG, "上传：deepLinkValue: " + deepLinkValue);
                            Log.d(LOG_TAG, "上传：deepLinkValue sub1: " + deepLinkSub1);
                            Log.d(LOG_TAG, "上传：deviceId: " + deviceId);
                            FetchUserInfoService.shareOpenServer(AppApplication.getInstance(), request);
                            AttributionInfoBean attributionInfoBean = new AttributionInfoBean();
                            attributionInfoBean.setDeepLinkValue(deepLinkValue);
                            attributionInfoBean.setDeepLinkSub1(deepLinkSub1);
                            AppGlobal.setAttributionInfo(attributionInfoBean);
                        } else {
                            AttributionInfoBean attributionInfoBean = new AttributionInfoBean();
                            attributionInfoBean.setDeepLinkValue(deepLinkValue);
                            attributionInfoBean.setDeepLinkSub1(deepLinkSub1);
                            attributionInfoBean.setCampaign(campaign);
                            attributionInfoBean.setMediaSource(mediaSource);
                            attributionInfoBean.setReferrerId(referrerId);
                            attributionInfoBean.setHasReport(false);
                            AppGlobal.setAttributionInfo(attributionInfoBean);
                        }
                    }*/


                    Log.d(LOG_TAG, "--------------------");
//                   String pid = deepLinkObj.getP

                } catch (Exception e) {
                    Log.d(LOG_TAG, "DeepLink data came back null");
                    return;
                }
            }
        });


        AppsFlyerLib.getInstance().start(context, AF_DEV_KEY, new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Timber.d(LOG_TAG, "Launch sent successfully, got 200 response code from server");
            }

            @Override
            public void onError(int i, String s) {
                Timber.d(LOG_TAG, "Launch failed to be sent:\n" +
                        "Error code: " + i + "\n"
                        + "Error description: " + s);
            }
        });
    }


    public static final String PURCHASE_TYPE_SUBSCRIPTION = "subscription";
    public static final String PURCHASE_TYPE_SUBSCRIPTION_35_OFF = "subscription_35_off";
    public static final String PURCHASE_TYPE_BOOSTS = "boosts";
    public static final String PURCHASE_TYPE_SUPER_FLIPS = "super_flips";

    public void sendRevenueEvent(float price, String currency, String productId, String purchaseType) {
        Log.d(LOG_TAG, "[sendRevenueEvent]========== ");
        Log.d(LOG_TAG, "[sendRevenueEvent]purchaseType= " + purchaseType);
        Log.d(LOG_TAG, "[sendRevenueEvent]productId= " + productId);
        Log.d(LOG_TAG, "[sendRevenueEvent]price= " + price);
        Log.d(LOG_TAG, "[sendRevenueEvent]currency= " + currency);
        Log.d(LOG_TAG, "[sendRevenueEvent]========== ");
        String event = "";
        Map<String, Object> eventValues = new HashMap<String, Object>();
        if (price > 0) {
            //收入
            eventValues.put(AFInAppEventParameterName.CURRENCY, currency);
            eventValues.put(AFInAppEventParameterName.REVENUE, price);
            eventValues.put(AFInAppEventParameterName.CONTENT_ID, productId);
            eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, purchaseType);
            event = AFInAppEventType.PURCHASE;
        } else {
            //退款
            eventValues.put(AFInAppEventParameterName.CURRENCY, currency);
            eventValues.put(AFInAppEventParameterName.REVENUE, price);
            eventValues.put(AFInAppEventParameterName.CONTENT_ID, productId);
            eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, purchaseType);
            event = "cancel_purchase";
        }
        logEvent(event, eventValues);
    }

    // 发送应用内事件
    // 已定义的事件类型在 AFInAppEventType 中可查看
    public void logEvent(String event, Map<String, Object> eventValues) {
//        Map<String, Object> eventValues = new HashMap<String, Object>();
        //eventValues.put(AFInAppEventParameterName.PRICE, 1234.56);
        //eventValues.put(AFInAppEventParameterName.CONTENT_ID,"1234567");

        AppsFlyerLib.getInstance().logEvent(sContext,
                event, eventValues, new AppsFlyerRequestListener() {
                    @Override
                    public void onSuccess() {
                        Timber.d(LOG_TAG, "Event sent successfully");
                        Timber.d(LOG_TAG, "Event :" + event);
                        Timber.d(LOG_TAG, "EventValues :" + eventValues.toString());
                    }

                    @Override
                    public void onError(int i, String s) {
                        Timber.d(LOG_TAG, "Event failed to be sent:\n" +
                                "Error code: " + i + "\n"
                                + "Error description: " + s);
                    }
                });
    }
}

