# 水波纹扩散View

## 使用方式

1. 将JitPack存储库添加到构建文件中
```xml
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. 添加依赖关系
```
dependencies {
    implementation 'com.github.titlark:WaterRippleDiffuseView:1.0.0'
}
```

3. 在页面添加

```xml
<com.lark.diffuseview.widget.WaterRippleDiffuseView
        android:id="@+id/diffuseView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        app:diffuse_color="@color/diffuse_color"
        app:diffuse_coreColor="@color/white"
        app:diffuse_coreImage="@mipmap/ic_scan"
        app:diffuse_coreRadius="200"
        app:diffuse_maxWidth="300"
        app:diffuse_speed="3"
        app:diffuse_width="4" />
```

4. 在 `Activity` 或 `Fragment` 中添加
```java
// 开始扩撒
diffuseView.start()

// 停止扩散
diffuseView.stop()
```

## 属性&&方法
```xml
<!--扩散圆形的颜色-->
<attr name="diffuse_color" format="color" />
<!--扩散圆形的宽度，值越小越宽-->
<attr name="diffuse_width" format="integer" />
<!--扩散圆形的宽度，最大扩散宽度-->
<attr name="diffuse_maxWidth" format="integer" />
<!--扩散速度，值越大越快-->
<attr name="diffuse_speed" format="integer" />
<!--中心圆形的颜色-->
<attr name="diffuse_coreColor" format="color" />
<!--中心圆形视图-->
<attr name="diffuse_coreImage" format="reference" />
<!--中心圆形的半径-->
<attr name="diffuse_coreRadius" format="float" />
```

## 设置中心圆图片点击事件
```java
diffuseView.setOnCoreImageClickListener(object : OnCoreImageClickListener {
    override fun onClickCoreImage(view: View) {
        Toast.makeText(this@MainActivity, "点击了中心图片", Toast.LENGTH_SHORT).show()
    }

})
```