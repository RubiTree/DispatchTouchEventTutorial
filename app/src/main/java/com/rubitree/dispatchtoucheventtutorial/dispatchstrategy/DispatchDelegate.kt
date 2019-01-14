package com.rubitree.dispatchtoucheventtutorial.dispatchstrategy

import android.view.MotionEvent
import com.rubitree.dispatchtoucheventtutorial.utils.log

/**
 * >> Description <<
 *
 * >> Attention <<
 *
 * >> Others <<
 *
 * Created by RubiTree ; On 2019-01-14.
 */
interface IDispatchDelegate {
    fun dispatchTouchEvent(event: MotionEvent?, callSuper: (event: MotionEvent?) -> Boolean): Boolean
    fun onTouchEvent(event: MotionEvent?, callSuper: (event: MotionEvent?) -> Boolean): Boolean
    fun onInterceptTouchEvent(ev: MotionEvent?, callSuper: (event: MotionEvent?) -> Boolean): Boolean
}

abstract class BaseDispatchDelegate(val layer: String) : IDispatchDelegate

class DispatchDelegate(
    layer: String,
    private val dispatch: EventsReturnStrategy = ALL_SUPER,
    private val intercept: EventsReturnStrategy = ALL_SUPER,
    private val touch: EventsReturnStrategy = ALL_SUPER
) : BaseDispatchDelegate(layer) {

    private var eventAmount = 0

    override fun dispatchTouchEvent(event: MotionEvent?, callSuper: (event: MotionEvent?) -> Boolean): Boolean {
        if (event?.actionMasked == MotionEvent.ACTION_DOWN) eventAmount = 0 else eventAmount++
        return deal(event, dispatch, callSuper)
    }

    override fun onTouchEvent(event: MotionEvent?, callSuper: (event: MotionEvent?) -> Boolean): Boolean {
        return deal(event, touch, callSuper)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?, callSuper: (event: MotionEvent?) -> Boolean): Boolean {
        return deal(ev, intercept, callSuper)
    }

    private fun deal(
        event: MotionEvent?,
        eventsReturnStrategy: EventsReturnStrategy,
        callSuper: (event: MotionEvent?) -> Boolean
    ): Boolean {
        val condition = Thread.currentThread().stackTrace[3].methodName
        var result = false

        val returnStrategy: ReturnStrategy = eventsReturnStrategy.getStrategy(event!!, eventAmount)

        if (returnStrategy == ReturnStrategy.T_SUPER) {
            logBefore(event, condition)
            result = callSuper(event)
            logAfter(event, condition, returnStrategy, result)
        } else {
            result = returnStrategy.value
            log(event, condition, returnStrategy, result)
        }

        return result
    }

    /*--------------------------------------------------------------------------------------------*/

    private fun logBefore(event: MotionEvent?, condition: String) {
        event.log(layer, "${condition}_BE")
    }

    private fun logAfter(event: MotionEvent?, condition: String, resultStrategy: ReturnStrategy, result: Boolean) {
        event.log(layer, "${condition}_AF", "result(${resultStrategy.mName}):$result")
    }

    private fun log(event: MotionEvent?, condition: String, resultStrategy: ReturnStrategy, result: Boolean) {
        event.log(layer, condition, "result(${resultStrategy.mName}):$result")
    }
}