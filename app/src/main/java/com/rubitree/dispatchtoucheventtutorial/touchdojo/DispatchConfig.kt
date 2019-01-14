package com.rubitree.dispatchtoucheventtutorial.touchdojo

import com.rubitree.dispatchtoucheventtutorial.dispatchstrategy.*
import com.rubitree.dispatchtoucheventtutorial.dispatchstrategy.ReturnStrategy.*

/**
 * >> Description <<
 *
 * >> Attention <<
 *
 * >> Others <<
 * 配置有点多，就没有给每个配置组合命名，简单测试就直接用注释的方式进行配置
 *
 * Created by RubiTree ; On 2019-01-14.
 */
class DispatchConfig {
    companion object {

        fun getActivityDispatchDelegate(layer: String = "Activity"): IDispatchDelegate {
            return DispatchDelegate(layer)
        }

        fun getViewGroupDispatchDelegate(layer: String = "ViewGroup"): IDispatchDelegate {
//            return DispatchDelegate(layer)
//        return DispatchDelegate(layer, ALL_SUPER, ALL_SUPER, ALL_TRUE)
//        return DispatchDelegate(layer, ALL_SUPER, ALL_TRUE, ALL_SUPER)
//        return DispatchDelegate(layer, ALL_SUPER, ALL_TRUE, ALL_TRUE)
//        return DispatchDelegate(layer, ALL_TRUE, ALL_SUPER, ALL_SUPER)
//        return DispatchDelegate(layer, ALL_FALSE, ALL_SUPER, ALL_SUPER)
//        return DispatchDelegate(
//            layer,
//            ALL_SUPER,
//            ALL_SUPER,
//            EventsReturnStrategy(T_TRUE, EVENT_SUPER, T_SUPER)
//        )
//        return DispatchDelegate(
//            layer,
//            ALL_SUPER,
//            EventsReturnStrategy(T_TRUE, EVENT_SUPER, T_SUPER),
//            EventsReturnStrategy(T_TRUE, EVENT_SUPER, T_SUPER)
//        )
        return DispatchDelegate(
            layer,
            ALL_SUPER,
            EventsReturnStrategy(T_FALSE, arrayOf(T_FALSE, T_TRUE, T_TRUE), T_SUPER),
            ALL_TRUE
        )
//        return DispatchDelegate(
//            layer,
//            EventsReturnStrategy(T_TRUE, EVENT_SUPER, T_SUPER),
//            ALL_SUPER,
//            ALL_SUPER
//        )
        }

        fun getViewDispatchDelegate(layer: String = "View"): IDispatchDelegate {
//            return DispatchDelegate(layer)
            return DispatchDelegate(layer, ALL_SUPER, ALL_SUPER, ALL_TRUE)
//            return DispatchDelegate(
//                layer,
//                ALL_SUPER, ALL_SUPER,
//                EventsReturnStrategy(T_TRUE, EVENT_SUPER, T_SUPER)
//            )
        }
    }
}