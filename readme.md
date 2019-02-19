# Dispatch TouchEvent Tutorial

<img src="./resource/title.gif" width="100%"/>

## SparrowDispatch

可以在此处查看详细内容：[《看穿 > 触摸事件分发》](https://juejin.im/post/5c3c8538f265da6142741d63)

[SparrowDispatch](https://github.com/RubiTree/DispatchTouchEventTutorial/blob/master/app/src/main/java/com/rubitree/dispatchtoucheventtutorial/sparrowdispatch/sparrow5/SparrowDispatch.kt)把源码中与事件分发相关的内容剥离了出来，至少有这两个优点：

1. 相比源码，这份代码足够短足够简单，那些跟事件分发无关的东西通通不要来干扰我
    1. 长度总共不超过150行，剔除了所有跟事件分发无关的代码，并且把一些因为其他细节导致写得比较复杂的逻辑，用更简单直接的方式表达了
1. 相比那段经典的事件分发伪代码，这份代码又足够详细，详细到能告诉你所有你需要知道的事件分发的具体细节
    1. 那段经典伪代码只能起到提纲挈领的作用，而这份麻雀代码虽然极其精简但它五脏俱全，全到可以直接跑
    1. 你可以用它进行为伪布局，然后触发触摸事件，如果在回调中打印日志，它打印出的事件分发过程与你使用系统控件真实布局时事件分发的过程是一模一样的

## 测试

测试的思路是通过在每个事件分发的钩子中打印日志来跟踪事件分发的过程。
于是就需要在不同的 View 层级的不同钩子中，针对不同的触摸事件进行不同的操作，以制造各种事件分发的场景。

为了减少重复代码简单搭建了一个测试框架，包括一个可以代理 View 中这些的操作的接口`IDispatchDelegate`及其实现类，和一个`DispatchConfig`统一进行不同的场景的配置。
之后创建了使用统一配置和代理操作的 真实控件们`SystemViews` 和 用`SparrowDispatch`实现的麻雀控件们`SparrowViews`。

在`DispatchConfig`中配置好事件分发的策略后，直接启动`SystemViews`中的`DelegatedActivity`，进行触摸，使用关键字`TouchDojo`过滤，就能得到事件分发的跟踪日志。
同时，运行`SparrowActivityTest`中的`dispatch()`测试方法，也能得到麻雀控件的事件分发跟踪日志。

### 场景一

先配置策略，模拟`View`和`ViewGroup`都不消费事件的场景：

```kotlin
fun getActivityDispatchDelegate(layer: String = "Activity"): IDispatchDelegate {
    return DispatchDelegate(layer)
}

fun getViewGroupDispatchDelegate(layer: String = "ViewGroup"): IDispatchDelegate {
    return DispatchDelegate(layer)
}

fun getViewDispatchDelegate(layer: String = "View"): IDispatchDelegate {
    return DispatchDelegate(layer)
}
```

能看到打印的事件分发跟踪日志：

```log
[down]
|layer:SActivity |on:Dispatch_BE |type:down
|layer:SViewGroup |on:Dispatch_BE |type:down
|layer:SViewGroup |on:Intercept_BE |type:down
|layer:SViewGroup |on:Intercept_AF |result(super):false |type:down
|layer:SView |on:Dispatch_BE |type:down
|layer:SView |on:Touch_BE |type:down
|layer:SView |on:Touch_AF |result(super):false |type:down
|layer:SView |on:Dispatch_AF |result(super):false |type:down
|layer:SViewGroup |on:Touch_BE |type:down
|layer:SViewGroup |on:Touch_AF |result(super):false |type:down
|layer:SViewGroup |on:Dispatch_AF |result(super):false |type:down
|layer:SActivity |on:Touch_BE |type:down
|layer:SActivity |on:Touch_AF |result(super):false |type:down
|layer:SActivity |on:Dispatch_AF |result(super):false |type:down

[move]
|layer:SActivity |on:Dispatch_BE |type:move
|layer:SActivity |on:Touch_BE |type:move
|layer:SActivity |on:Touch_AF |result(super):false |type:move
|layer:SActivity |on:Dispatch_AF |result(super):false |type:move

[move]
...

[up]
|layer:SActivity |on:Dispatch_BE |type:up
|layer:SActivity |on:Touch_BE |type:up
|layer:SActivity |on:Touch_AF |result(super):false |type:up
|layer:SActivity |on:Dispatch_AF |result(super):false |type:up
```

1. 因为系统控件和麻雀控件打印的日志一模一样，所以只贴出一份
2. 这里用`BE`代表 `before`，表示该方法开始处理事件的时候，用`AF`代表`after`，表示该方法结束处理事件的时候，并且打印处理的结果
3. 从日志中能清楚看到，当`View`和`ViewGroup`都不消费`DOWN`事件时，后续事件将不再传递给`View`和`ViewGroup`

### 场景二

再配置策略，模拟`View`和`ViewGroup`都消费事件，同时`ViewGroup`在第二个`MOVE`事件时认为自己需要拦截事件的场景：

```kotlin
fun getActivityDispatchDelegate(layer: String = "Activity"): IDispatchDelegate {
    return DispatchDelegate(layer)
}

fun getViewGroupDispatchDelegate(layer: String = "ViewGroup"): IDispatchDelegate {
    return DispatchDelegate(
        layer,
        ALL_SUPER,
        // 表示 onInterceptTouchEvent 方法中，DOWN 事件返回 false，第一个 MOVE 事件返回 false，第二个第三个 MOVE 事件返回 true
        EventsReturnStrategy(T_FALSE, arrayOf(T_FALSE, T_TRUE, T_TRUE), T_SUPER),
        ALL_TRUE
    )
}

fun getViewDispatchDelegate(layer: String = "View"): IDispatchDelegate {
    return DispatchDelegate(layer, ALL_SUPER, ALL_SUPER, ALL_TRUE)
}
```

能看到打印的事件分发跟踪日志：

```log
[down]
|layer:SActivity |on:Dispatch_BE |type:down
|layer:SViewGroup |on:Dispatch_BE |type:down
|layer:SViewGroup |on:Intercept |result(false):false |type:down
|layer:SView |on:Dispatch_BE |type:down
|layer:SView |on:Touch |result(true):true |type:down
|layer:SView |on:Dispatch_AF |result(super):true |type:down
|layer:SViewGroup |on:Dispatch_AF |result(super):true |type:down
|layer:SActivity |on:Dispatch_AF |result(super):true |type:down

[move]
|layer:SActivity |on:Dispatch_BE |type:move
|layer:SViewGroup |on:Dispatch_BE |type:move
|layer:SViewGroup |on:Intercept |result(false):false |type:move
|layer:SView |on:Dispatch_BE |type:move
|layer:SView |on:Touch |result(true):true |type:move
|layer:SView |on:Dispatch_AF |result(super):true |type:move
|layer:SViewGroup |on:Dispatch_AF |result(super):true |type:move
|layer:SActivity |on:Dispatch_AF |result(super):true |type:move

[move]
|layer:SActivity |on:Dispatch_BE |type:move
|layer:SViewGroup |on:Dispatch_BE |type:move
|layer:SViewGroup |on:Intercept |result(true):true |type:move
|layer:SView |on:Dispatch_BE |type:cancel
|layer:SView |on:Touch_BE |type:cancel
|layer:SView |on:Touch_AF |result(super):false |type:cancel
|layer:SView |on:Dispatch_AF |result(super):false |type:cancel
|layer:SViewGroup |on:Dispatch_AF |result(super):false |type:move
|layer:SActivity |on:Touch_BE |type:move
|layer:SActivity |on:Touch_AF |result(super):false |type:move
|layer:SActivity |on:Dispatch_AF |result(super):false |type:move

[move]
|layer:SActivity |on:Dispatch_BE |type:move
|layer:SViewGroup |on:Dispatch_BE |type:move
|layer:SViewGroup |on:Touch |result(true):true |type:move
|layer:SViewGroup |on:Dispatch_AF |result(super):true |type:move
|layer:SActivity |on:Dispatch_AF |result(super):true |type:move

[up]
|layer:SActivity |on:Dispatch_BE |type:up
|layer:SViewGroup |on:Dispatch_BE |type:up
|layer:SViewGroup |on:Touch |result(true):true |type:up
|layer:SViewGroup |on:Dispatch_AF |result(super):true |type:up
|layer:SActivity |on:Dispatch_AF |result(super):true |type:up
```

1. 同样因为系统控件和麻雀控件打印的日志一模一样，所以只贴出一份
2. 从日志中能清楚看到，在`ViewGroup`拦截事件前后，事件是如何分发的

## License
Apache License 2.0, here is the [LICENSE](https://github.com/RubiTree/DispatchTouchEventTutorial/blob/master/LICENSE).
