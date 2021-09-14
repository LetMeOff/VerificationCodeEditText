# 自定义验证码输入框

- 预览

![image](https://github.com/LetMeOff/VerificationCodeEditText/blob/main/ScreenShots/device-2021-09-14-180136.png)

## 使用

- 项目build.gradle

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

- App build.gradle

```
dependencies {
    implementation 'com.github.LetMeOff:VerificationCodeEditText:1.0.0'
}
```

- 使用

```
<com.zjh.verification.VerificationCodeEditText
        android:id="@+id/verificationEdit"
        android:layout_width="250dp"
        android:layout_height="60dp"
        android:layout_marginTop="20dp"
        android:textColor="#333333"
        android:textSize="36sp"
        app:cursorDrawable="@drawable/shape_cursor"
        app:cursorDrawableHeight="25dp"
        app:cursorDrawableWidth="1dp"
        app:isSquare="false"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:space="16dp"
        app:textBackground="@drawable/verification_bg_line" />
```

- 属性说明

```
<declare-styleable name="VerificationCodeEditText">
        <!-- 文字大小-->
        <attr name="android:textSize" />
        <!-- 文字颜色-->
        <attr name="android:textColor" />
        <!-- 文字是否加粗 -->
        <attr name="isBoldText" format="boolean" />
        <!-- 如果是密码类型，展示的内容 比如* -->
        <attr name="passwordText" format="string" />
        <!-- 输入的文字的个数 -->
        <attr name="inputCount" format="integer" />
        <!-- 文字之间的间距 -->
        <attr name="space" format="dimension" />
        <!-- 是否保持文字正方形 -->
        <attr name="isSquare" format="boolean" />
        <!-- 文字的背景 -->
        <attr name="textBackground" format="reference" />
        <!-- 是否是密码 -->
        <attr name="isPassword" format="boolean" />
        <!-- 光标样式 -->
        <attr name="cursorDrawable" format="reference" />
        <!-- 光标宽度 -->
        <attr name="cursorDrawableWidth" format="dimension" />
        <!-- 光标高度 -->
        <attr name="cursorDrawableHeight" format="dimension" />
</declare-styleable>
```

可以将背景设置为***selector***，光标对应的地方会变色，如：

```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/bg_purple" android:state_focused="true" />
    <item android:drawable="@drawable/bg_grey" android:state_focused="false" />
</selector>
```

- 输入完成监听

```kotlin
//输入完成监听
binding.verificationEdit.setOnEditCompleteListener(object :
    VerificationCodeEditText.OnEditCompleteListener {
    override fun onEditComplete(text: String) {
        Log.d(TAG, "输入完成 : $text")
    }
})
```