# EasyBT
BlueTooth 2.0 通讯

```
QQ : 695344490
Email : pengle609@163.com
```

### Gradle（后续修改完成）
~~` compile 'com.microape.easybt:easybt:1.0.0' `~~

## Usage

> ### 前提条件
> 1. 设备是否支持WiFi
> 2. 6.0手机是否允许定位权限（影响扫描）

### init 
` EasyBT.newInstance().init(this); `

### 设置监听
```
easyBT.setOpenCallBack(this);
easyBT.setScanCallBack(this);
easyBT.setConnCallBack(this);
```

### 开启WiFi
` easyBT.enable(); `

### 关闭WiFi
` easyBT.disable(); `

### 扫描WiFi
` easyBT.startScan(); `

### 连接指定WiFi、并建立通讯(后续更新)
```


```

### 感谢
* PermissionsDispatcher
