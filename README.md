![main](https://github.com/android-module/android-logger/actions/workflows/android.yml/badge.svg?branch=main)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat-square)](https://android-arsenal.com/api?level=21)
![maven](https://img.shields.io/maven-central/v/io.github.caldremch/android-logger?style=flat-square)
![core](https://img.shields.io/maven-central/v/io.github.caldremch/core-logger?style=flat-square)

## 说明

- 自动连接探测器(基于socket udp)
- websocket日志传输器
- 日志打印器

## 功能

1. 自动探测局域网并匹配服务
2. 未连接时所打印的日志, 连接后一次性补打出来
3. 自动依赖并开启探测,无需任何代码初始化处理, 也可以手动开关日志
4. 探测端口目前最多支持10个设备(10个端口),被占用时自动使用下一个端口
5. 客户端和服务端都是相互自动连接, 无需担心任何一段重启
6. 自动监听应用前后台切换并恢复和暂停探测
7. 简单的api使用方式

## 使用建议

建议接入方, 统一日志打印入口, 可以做到灵活切换打印方式, 较少耦合

## 使用方式

### 1. 在build.gradle中添加依赖

也是提供了两种方式,分别对应两种依赖

#### 1.1自动依赖(推荐)

一个依赖完成所有初始化, 无需任何代码操作

```gradle
implementation 'io.github.caldremch:android-logger:1.0.6-rc1'
```

#### 1.2手动依赖

```gradle
implementation 'io.github.caldremch:core-logger:1.0.6-rc1'
```

需要手动调用初始化方法

```kotlin

//场景1.出api封装, 不会开启探测, 也不会触发日志发送到服务端
DebugLogInitializer.initLite

//场景2.开启探测和自动连接(推荐)
DebugLogInitializer.initWithDetect

```

#### 2.方法调用

**打印**

```kotlin
debugLog { "log printer debug" }
errorLog { "log printer error" }
errorLog { "https://baidu.com" }
```

**开启或者关闭所有日志的打印**

```kotlin
// logEnable 关闭所有的日志打印, 如果要用在线上就设置logEnable 设置为false
DebugLogInitializer.setEnable(logEnable: Boolean)
```

**暂停/恢复, 关闭/启动探测**

暂停/恢复是针对生命周期控制行为, 短暂性的关闭
关闭则是app声明周期内都不会再执行探测, 触发再次启动

```kotlin
//暂停探测
DebugLogInitializer.pauseDetect()
//恢复探测
DebugLogInitializer.resumeDetect()
//关闭探测
DebugLogInitializer.shutDownNetLog()
//启动探测
DebugLogInitializer.setUpNetLog()
```


