# 上下带文本的进度条

## 效果如下

![expand.gif](https://upload-images.jianshu.io/upload_images/4029874-2b9c5c6c8e5ffa3d.gif?imageMogr2/auto-orient/strip)

​	这个需求来自于项目，产品的意图是做一个冲刺进度条。大概意思是，下面的文字是汽车销售台数区间，而上面是每个区间所对应的返点费率。由于这些数据都是来自于后台，所以考虑到自定义View来实现

## 自定义属性

```xml
<resources>
    <declare-styleable name="TextProgressBar">
        <attr name="barHeight" format="dimension" /> <!--进度条高度 -->
        <attr name="trackColor" format="color" /> <!--底下滑轨的颜色 -->
        <attr name="barStartColor" format="color" /> <!--进度条开始的颜色 -->
        <attr name="barEndColor" format="color"/> <!--进度条结束的颜色 -->
        <attr name="barTextColor" format="color" /> <!--文字的颜色 -->
        <attr name="barTextSize" format="dimension" /> <!--文字的大小 -->
        <attr name="topTextMargin" format="dimension" /> <!--上文字距进度条的距离 -->
        <attr name="bottomTextMargin" format="dimension" /><!--下文字距进度条的距离 -->
    </declare-styleable>
</resources>
```

## 具体思路

- 确定控件高度：控件高度 = 进度条高度 + 上文字距离 + 下文字距离 + 上下文字大小
- 根据传入的最大进度和当前进度，使用`drawLine`绘制进度条，为`paint`设置`LinearGradient`渐变
- 根据传入的下面文字数量，分割进度条，平分摆放；上面文字的摆放位置为，底部的区间的中间。

## 具体使用

activity中

```kotlin
progressBar.setBottomText(listOf("0", "20", "40")) //设置下面文字
progressBar.setTopText(listOf("2.0%", "2.5%")) //设置上面文字
progressBar.setMaxProgress(40f) //设置最大进度
progressBar.setCurrentProgress(35f) //设置当前进度
```

xml中

```xml
<com.m1ku.textprogressbar.TextProgressBar
    android:id="@+id/progressBar2"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="45dp"
    app:barEndColor="@color/colorAccent"
    app:barHeight="8dp"
    app:barStartColor="@color/colorPrimary"
    app:barTextSize="13sp"
    app:bottomTextMargin="10dp"
    app:topTextMargin="15dp" />
```

由于UI需求，这里的进度条是渐变色的，`barStartColor`指定渐变开始色，`barEndColor`指定渐变结束色