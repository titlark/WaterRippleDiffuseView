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
    implementation 'com.github.huangziye.base:utils:${latest_version}'
    implementation 'com.github.huangziye.base:views:${latest_version}'
}
```

2.

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
