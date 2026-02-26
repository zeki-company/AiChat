# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.a
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#-----------基本配置--------------
# 代码混淆压缩比，在0~7之间，默认为5，一般不需要改
-optimizationpasses 5

# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclasses

# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers

# 不做预校验，可加快混淆速度
# preverify是proguard的4个步骤之一
# Android不需要preverify，去掉这一步可以加快混淆速度
-dontpreverify

# 不优化输入的类文件
-dontoptimize

# 混淆时生成日志文件，即映射文件
-verbose

# 指定映射文件的名称
-printmapping proguardMapping.txt

#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 保护代码中的Annotation不被混淆
-keepattributes *Annotation*

# 忽略警告
-ignorewarnings

# 保护泛型不被混淆
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

#-----------需要保留的东西--------------
# 保留所有的本地native方法不被混淆
-keepclasseswithmembers class * {
    native <methods>;
}

# 保留了继承自Activity、Application、Fragment这些类的子类
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
#-keep public class com.android.vending.licensing.ILicensingService

# support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
# support-v7
-dontwarn android.support.v7.**                                             #去掉警告
-keep class android.support.v7.** { *; }                                    #过滤android.support.v7
-keep interface android.support.v7.app.** { *; }
-keep public class * extends android.support.v7.**

#----------------保护指定的类和类的成员，但条件是所有指定的类和类成员是要存在------------------------------------
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保持自定义控件类不被混淆，指定格式的构造方法不去混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保持自定义控件类不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    *** get*();
}

# 保留在Activity中的方法参数是View的方法
# 从而我们在layout里边编写onClick就不会被影响
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# 保留枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 不混淆资源类
-keepclassmembers class **.R$* { *; }

# 对于带有回调函数onXXEvent()的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
}
# WebView
#-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
#   public *;
#}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#	public void *(android.webkit.WebView, jav.lang.String);
#}
-keepattributes *JavascriptInterface*


# androidx
-keep class android.support.v8.renderscript.** { *; }
-keep class androidx.renderscript.** { *; }
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

# agora
-keep class io.agora.**{*;}

#utils
-keep class com.blankj.utilcode.** { *; }
-keepclassmembers class com.blankj.utilcode.** { *; }
-dontwarn com.blankj.utilcode.**



# For Glide ---->
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public class * extends com.bumptech.glide.module.AppGlideModule
#-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}

#ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#
-keep class com.dueeeke.videoplayer.** { *; }
-dontwarn com.dueeeke.videoplayer.**

# IjkPlayer
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

# ExoPlayer
-keep class com.google.android.exoplayer2.** { *; }
-dontwarn com.google.android.exoplayer2.**


#---------------------------------5.第三方包-------------------------------
#如果报The same input jar is specified twice异常，把-libraryjars注释掉，在AndroidStudio可以不需要，直接对jar里面的类进行混淆操作
#-libraryjars libs/xxxxxxx.jar
#-libraryjars **.jar



#
##-------------------------------------------基本不用动区域--------------------------------------------
##---------------------------------基本指令区----------------------------------
#-optimizationpasses 5
#-dontskipnonpubliclibraryclassmembers
#-printmapping proguardMapping.txt
#-optimizations !code/simplification/cast,!field/*,!class/merging/*
#-keepattributes *Annotation*,InnerClasses
#-keepattributes Signature
#-keepattributes SourceFile,LineNumberTable
#
#
##----------------------------手动启用support keep注解------------------------
#-dontskipnonpubliclibraryclassmembers
#-printconfiguration
##-keep,allowobfuscation @interface android.support.annotation.Keep
##-keep @android.support.annotation.Keep class *
##-keepclassmembers class * {
##    @android.support.annotation.Keep *;
##}


##---------------------------------webview------------------------------------
#-keepclassmembers class fqcn.of.javascript.interface.for.Webview { public *; }
-keepclassmembers class * extends android.webkit.WebViewClient {
	public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
	public boolean *(android.webkit.WebView, java.lang.String);
}

#-keepclassmembers class * extends android.webkit.WebViewClient {
#	public void *(android.webkit.WebView, jav.lang.String);
#}
#
#
#-------------------------------------------------------------目前支持的开源库------------------------------------------------
##================支付宝支付
#-keep class com.alipay.android.app.IAlixPay{*;}
#-keep class com.alipay.android.app.IAlixPay$Stub{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
#-keep class com.alipay.sdk.app.PayTask{ public *;}
#-keep class com.alipay.sdk.app.AuthTask{ public *;}
#-keep public class * extends android.os.IInterface
##================微信支付
#-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
#-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
#-keep class com.tencent.wxop.** { *; }
#-dontwarn com.tencent.mm.**
#-keep class com.tencent.mm.**{*;}
#
#-keep class sun.misc.Unsafe { *; }
#
#-keep class com.taobao.** {*;}
#-keep class com.alibaba.** {*;}
#-keep class com.alipay.** {*;}
#-dontwarn com.taobao.**
#-dontwarn com.alibaba.**
#-dontwarn com.alipay.**
#
#-keep class com.ut.** {*;}
#-dontwarn com.ut.**
#
#-keep class com.ta.** {*;}
#-dontwarn com.ta.**
#
#-keep class anet.**{*;}
#-keep class org.android.spdy.**{*;}
#-keep class org.android.agoo.**{*;}
#-dontwarn anet.**
#-dontwarn org.android.spdy.**
#-dontwarn org.android.agoo.**
#
#-keepclasseswithmembernames class com.xiaomi.**{*;}
#-keep public class * extends com.xiaomi.mipush.sdk.PushMessageReceiver
#
#-dontwarn com.xiaomi.push.service.b
#
#-keep class org.apache.http.**
#-keep interface org.apache.http.**
#-dontwarn org.apache.**
#
##===================================okhttp3.x
#-dontwarn com.squareup.okhttp3.**
#-keep class com.squareup.okhttp3.** { *;}
#-dontwarn okio.**

# okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.* { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# okhttp 3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Retrofit2
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
-dontwarn javax.annotation.**

# 保留协程 Continuation 的泛型签名（Kotlin 挂起函数包装类）
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# 保留 Retrofit 接口方法定义的泛型信息（防止返回类型签名丢失）
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# 保留 Response 类的泛型签名（用于反序列化）
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# 保留 Call 接口的泛型签名（核心网络请求类型）
-keep,allowobfuscation,allowshrinking interface retrofit2.Call



-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
#    rx.internal.util.atomic.LinkedQueueNode producerNode;
#}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
#    rx.internal.util.atomic.LinkedQueueNode consumerNode;
#}
-keep public class rx.**{
    *;
}


##===================================sharesdk
#-keep class cn.sharesdk.**{*;}
#-keep class com.sina.**{*;}
#-keep class **.R$* {*;}
#-keep class **.R{*;}
#
#-keep class com.mob.**{*;}
#-dontwarn com.mob.**
#-dontwarn cn.sharesdk.**
#-dontwarn **.R$*
#
##=========================nineoldandroids-2.4.0.jar
#-keep public class com.nineoldandroids.** {*;}
#
##============================zxing
#-keep class com.google.zxing.** {*;}
#-dontwarn com.google.zxing.**
##=============================百度定位
#-keep class com.baidu.** {*;}
#-keep class vi.com.** {*;}
#-dontwarn com.baidu.**
#
##=====================================okhttp
#-dontwarn com.squareup.okhttp.**
#-keep class com.squareup.okhttp.{*;}
##retrofit
#-dontwarn retrofit.**
#-keep class retrofit.** { *; }
#-keepattributes Signature
#-keepattributes Exceptions
#-dontwarn okio.**
#
##========================recyclerview-animators
#-keep class jp.wasabeef.** {*;}
#-dontwarn jp.wasabeef.*
#
##============================universal-image-loader 混淆
#-dontwarn com.nostra13.universalimageloader.**
#-keep class com.nostra13.universalimageloader.** { *; }
#
##===============================ormlite
#-keep class com.j256.**
#-keepclassmembers class com.j256.** { *; }
#-keep enum com.j256.**
#-keepclassmembers enum com.j256.** { *; }
#-keep interface com.j256.**
#-keepclassmembers interface com.j256.** { *; }
##umeng
##===================================友盟
#-dontshrink
#-dontoptimize
#-dontwarn com.google.android.maps.**
#-dontwarn android.webkit.WebView
#-dontwarn com.umeng.**
#-dontwarn com.tencent.weibo.sdk.**
#-dontwarn com.facebook.**
#
#
#-keep enum com.facebook.**
#-keepattributes Exceptions,InnerClasses,Signature
#-keepattributes *Annotation*
#-keepattributes SourceFile,LineNumberTable
#
#-keep public interface com.facebook.**
#-keep public interface com.tencent.**
#-keep public interface com.umeng.socialize.**
#-keep public interface com.umeng.socialize.sensor.**
#-keep public interface com.umeng.scrshot.**
#
#-keep public class com.umeng.socialize.* {*;}
#-keep public class javax.**
#-keep public class android.webkit.**
#
#-keep class com.facebook.**
#-keep class com.umeng.scrshot.**
#-keep public class com.tencent.** {*;}
#-keep class com.umeng.socialize.sensor.**
#
#-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
#
#-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
#
#-keep class im.yixin.sdk.api.YXMessage {*;}
#-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
#
#-keep public class com.android.ljywp.R$*{
#    public static final int *;
#}
#-keepclassmembers class * {
#   public <init> (org.json.JSONObject);
#}
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
##===============================友盟自动更新
#-keep public class com.umeng.fb.ui.ThreadView {
#}
#-keep public class * extends com.umeng.**
## 以下包不进行过滤
#-keep class com.umeng.** { *; }
#
#
##========================================ButterKnife 7.0
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -keepclasseswithmembers class * {
  @butterknife.* <fields>;
 }
 -keepclasseswithmembers class * {
 @butterknife.* <methods>;
 }
#
# Realm
#-keep class io.realm.annotations.RealmModule
#-keep @io.realm.annotations.RealmModule class *
#-keep class io.realm.internal.Keep
#-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**

##========================================AndFix
#-keep class * extends java.lang.annotation.Annotation
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
##===========================================eventbus 3.0
#-keepattributes *Annotation*
#-keepclassmembers class ** {
#    @org.greenrobot.eventbus.Subscribe <methods>;
#}
#-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
#    <init>(java.lang.Throwable);
#}
#-libraryjars libs/pksdk.jar
#

#-keepclassmembers class ** {
    #@de.greenrobot.event.Subscribe <methods>;
#    @org.greenrobot.eventbus.Subscribe <methods>;
#}
-keepattributes *Annotation*
#-keepclassmembers class * {
#    @org.greenrobot.eventbus.Subscribe <methods>;
#}
#-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
#-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
#    <init>(java.lang.Throwable);
#}

##============================================EventBus
#-keepclassmembers class ** {
#    public void onEvent*(**);
#}
#-keepclassmembers class ** {
#public void xxxxxx(**);
#}
#
#
##==========================================gson
#-keep class com.google.gson.** {*;}
#-keep class com.google.**{*;}
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
#-keep class com.google.gson.examples.android.model.** { *; }
#
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#-keep public class * implements java.io.Serializable {*;}
#
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keepattributes Singature
-keepattributes *Annotation
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe.** { *; }
#-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type


# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

##---------------End: proguard configuration for Gson  ----------
##========================================support-v4
##https://stackoverflow.com/questions/18978706/obfuscate-android-support-v7-widget-gridlayout-issue
#-dontwarn android.support.v4.**
#-keep class android.support.v4.app.** { *; }
#-keep interface android.support.v4.app.** { *; }
#-keep class android.support.v4.** { *; }
#
#
##========================================support-v7
#-dontwarn android.support.v7.**
#-keep class android.support.v7.internal.** { *; }
#-keep interface android.support.v7.internal.** { *; }
#-keep class android.support.v7.** { *; }
#
##=======================================support design
##@link http://stackoverflow.com/a/31028536
#-dontwarn android.support.design.**
#-keep class android.support.design.** { *; }
#-keep interface android.support.design.** { *; }
#-keep public class android.support.design.R$* { *; }
#
##==============================================picasso
#-keep class com.squareup.picasso.** {*; }
#-dontwarn com.squareup.picasso.**
#
##================================================glide
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#
##================================volley混淆
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }
#



# facebook
-dontwarn com.facebook.**
-keep enum com.facebook.**
-keep public interface com.facebook.**
-keep class com.facebook.**

# RenderScript
-keepclasseswithmembernames class * {
native <methods>;
}
-keep class androidx.renderscript.** { *; }


# 在开发的时候我们可以将所有的实体类放在一个包内，这样我们写一次混淆就行了。
-keep public class com.xpt.unicorn.entity.**{*;}
-keep public class com.xpt.unicorn.network.response.**{*;}
-keep public class com.xpt.unicorn.common.adapter.**{*;}



#S3
-keep public class com.amazonaws.*{*;}

# autosize适配方案
-keep class me.jessyan.autosize.** { *; }
-keep interface me.jessyan.autosize.** { *; }
#BaseQuickAdapter
-keep class com.chad.library.adapter.** {
*;
}
#-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
#-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
#-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
#     <init>(...);
#}

#-keepclassmembers class * extends com.chad.library.adapter.base.BaseViewHolder { <init>(...); }


#im即使通讯的混淆
-dontwarn com.netease.**
-keep class com.netease.** {*;}
#如果你使用全文检索插件，需要加入
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}
-dontwarn com.google.**
-keep class com.google.** {*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}


#XBanner 图片轮播混淆配置
-keep class com.stx.xhb.xbanner.**{*;}
#AppFlyer混淆配置
#-dontwarn com.android.installreferrer
-keep class com.appsflyer.** { *; }
-keep public class com.android.installreferrer.** { *; }

#sendbird混淆配置

-dontwarn com.sendbird.android.shadow.**
-keep class com.sendbird.** { *; }

-keep class com.iceteck.** { *; }
-keep class com.coremedia.** { *; }

-keep class com.marvhong.** { *; }
-keep class com.xpt.unicorn.camera.** { *; }
-keep class com.mp4parser.** { *; }
-keep class com.googlecode.** { *; }

#Zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.**{*;}
#-keepclasseswithmembernames class * { native <methods>; }


-keep class android.view.**


#云信
-dontwarn com.netease.nim.**
-keep class com.netease.nim.** {*;}

-dontwarn com.netease.nimlib.**
-keep class com.netease.nimlib.** {*;}

-dontwarn com.netease.share.**
-keep class com.netease.share.** {*;}

-dontwarn com.netease.mobsec.**
-keep class com.netease.mobsec.** {*;}

#如果你使用全文检索插件，需要加入
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}



#如果你开启数据库功能，需要加入
-keep class net.sqlcipher.** {*;}

#sentry
-keep class io.sentry.** { *; }
-dontwarn io.sentry.**


