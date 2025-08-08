# 语言切换功能指南

## 功能概述

本应用支持多语言切换功能，目前支持以下语言：
- 英语 (English)
- 中文 (Chinese)
- 法语 (Français)

## 使用方法

### 1. 进入设置页面
在应用的主界面，点击底部导航栏的"账户"选项，然后点击"设置"。

### 2. 切换语言
在设置页面中，点击"语言"选项，会弹出一个语言选择对话框。

### 3. 选择语言
在弹出的对话框中，选择你想要的语言：
- 选择 "English" 切换到英语
- 选择 "中文" 切换到中文
- 选择 "Français" 切换到法语

### 4. 重启应用
选择语言后，系统会提示你重启应用以应用语言更改。点击"重启"按钮，应用将自动重启并应用新的语言设置。

## 技术实现

### 核心组件

1. **LocaleHelper** (`app/src/main/java/com/cdsy/aichat/util/LocaleHelper.kt`)
   - 处理语言切换的核心工具类
   - 支持 Android N 及以上版本的现代 API
   - 兼容旧版本 Android 的 Legacy API

2. **SettingFragment** (`app/src/main/java/com/cdsy/aichat/ui/setting/SettingFragment.kt`)
   - 实现语言选择对话框
   - 处理语言切换后的重启逻辑

3. **SettingViewModel** (`app/src/main/java/com/cdsy/aichat/ui/setting/SettingViewModel.kt`)
   - 管理语言切换的业务逻辑
   - 保存语言设置到 SharedPreferences

4. **MainApplication** (`app/src/main/java/com/cdsy/aichat/MainApplication.kt`)
   - 应用启动时初始化语言设置
   - 确保应用启动时使用正确的语言

### 字符串资源

语言相关的字符串资源存储在以下文件中：
- `app/src/main/res/values/strings.xml` - 英语（默认）
- `app/src/main/res/values-zh/strings.xml` - 中文
- `app/src/main/res/values-fr/strings.xml` - 法语

### 语言选项配置

语言选项在 `arrays.xml` 文件中配置：
```xml
<string-array name="language_options">
    <item>English</item>
    <item>中文</item>
    <item>Français</item>
</string-array>

<string-array name="language_codes">
    <item>en</item>
    <item>zh</item>
    <item>fr</item>
</string-array>
```

## 添加新语言

要添加新的语言支持，需要：

1. 创建新的语言资源文件夹（如 `values-es` 用于西班牙语）
2. 复制并翻译 `strings.xml` 文件
3. 在 `arrays.xml` 中添加新语言的选项和代码
4. 更新所有语言版本的 `arrays.xml` 文件

## 测试

运行测试来验证语言切换功能：
```bash
./gradlew testDebugUnitTest
```

测试文件位置：`app/src/test/java/com/cdsy/aichat/LocaleHelperTest.kt`

## 注意事项

1. 语言切换后需要重启应用才能完全生效
2. 语言设置会保存在 SharedPreferences 中
3. 应用启动时会自动应用上次保存的语言设置
4. 支持 Android 6.0 (API 23) 及以上版本 