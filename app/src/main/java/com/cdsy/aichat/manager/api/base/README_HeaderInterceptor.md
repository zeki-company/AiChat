# HeaderInterceptor 使用说明

## 功能概述

`HeaderInterceptor` 会自动为所有请求添加以下header：
- `Content-Type: application/json`
- `X-X-App-Id: {BuildConfig.X_APP_ID}`
- `X-App-Version: {BuildConfig.VERSION_NAME}`
- `Authorization: Bearer {token}` (可选)
- `X-Device-Id: {deviceId}` (可选)

## 跳过认证Header

如果某个请求不需要 `Authorization` 和 `X-Device-Id` header，可以使用跳过功能。

### 方法1：在API接口方法上添加注解

```kotlin
@POST("public/endpoint")
fun publicEndpoint(
    @Body data: String,
    @Header(ApiHeaders.SKIP_AUTH_HEADERS) skipAuth: String = ApiHeaders.skipAuthHeaders()
): Single<String>
```

### 方法2：使用常量

```kotlin
@POST("public/endpoint")
fun publicEndpoint(
    @Body data: String,
    @Header("X-Skip-Auth-Headers") skipAuth: String = "true"
): Single<String>
```

### 方法3：动态添加header

```kotlin
// 在ViewModel或其他地方动态添加header
val headers = mapOf(
    ApiHeaders.getSkipAuthHeadersPair()
)
```

## 工作原理

1. 拦截器检查请求中是否包含 `X-Skip-Auth-Headers: true`
2. 如果存在此标记，则跳过添加 `Authorization` 和 `X-Device-Id` header
3. 移除临时标记header，避免发送到服务器
4. 继续添加其他基础header

## 注意事项

- 只有 `Authorization` 和 `X-Device-Id` 会被跳过
- 其他基础header（Content-Type、X-X-App-Id、X-App-Version）仍会正常添加
- 临时标记header会在发送前被移除，不会发送到服务器 