package com.rubitree.dispatchtoucheventtutorial.dispatchstrategy

import android.view.MotionEvent
import com.rubitree.dispatchtoucheventtutorial.dispatchstrategy.ReturnStrategy.*

/**
 * >> Description <<
 *
 * >> Attention <<
 *
 * >> Others <<
 *
 * Created by RubiTree ; On 2019-01-14.
 */

val EVENT_SUPER = arrayOf(T_SUPER)
val EVENT_TRUE = arrayOf(T_TRUE)
val EVENT_FALSE = arrayOf(T_FALSE)

val ALL_SUPER = EventsReturnStrategy(T_SUPER, EVENT_SUPER, T_SUPER)
val ALL_TRUE = EventsReturnStrategy(T_TRUE, EVENT_TRUE, T_TRUE)
val ALL_FALSE = EventsReturnStrategy(T_FALSE, EVENT_FALSE, T_FALSE)

class EventsReturnStrategy(private val strategyMap: Map<Int, Array<ReturnStrategy>>) {
    constructor(
        down: ReturnStrategy,
        move: Array<ReturnStrategy>,
        up: ReturnStrategy,
        cancel: ReturnStrategy = T_SUPER
    ) : this(
        mapOf(
            MotionEvent.ACTION_DOWN to arrayOf(down),
            MotionEvent.ACTION_MOVE to move,
            MotionEvent.ACTION_UP to arrayOf(up),
            MotionEvent.ACTION_CANCEL to arrayOf(cancel)
        )
    )

    fun getStrategy(ev: MotionEvent, amount: Int = 0): ReturnStrategy {
        val strategyArray = strategyMap[ev.actionMasked]!!

        var index = 0
        if (ev.actionMasked == MotionEvent.ACTION_MOVE) {
            assert(amount > 0) {"Error mock event"}

            index = Math.min(amount - 1, strategyArray.size - 1)
        }

        return strategyArray[index]
    }
}

enum class ReturnStrategy(val mName: String, val value: Boolean) {
    T_SUPER("super", false),
    T_TRUE("true", true),
    T_FALSE("false", false)
}