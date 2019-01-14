package com.rubitree.dispatchtoucheventtutorial.touchdojo

import android.view.MotionEvent
import com.rubitree.dispatchtoucheventtutorial.sparrowdispatch.sparrow5.MActivity
import com.rubitree.dispatchtoucheventtutorial.sparrowdispatch.sparrow5.MView
import com.rubitree.dispatchtoucheventtutorial.sparrowdispatch.sparrow5.MViewGroup
import com.rubitree.dispatchtoucheventtutorial.utils.log
import com.rubitree.dispatchtoucheventtutorial.utils.typeName

/**
 * >> Description <<
 *
 * >> Attention <<
 *
 * >> Others <<
 *
 * Created by RubiTree ; On 2019-01-14.
 */
class SparrowView : MView() {
    private val dispatchDelegate = DispatchConfig.getViewDispatchDelegate("SView")

    override fun dispatch(ev: MotionEvent): Boolean {
        return dispatchDelegate.dispatchTouchEvent(ev) { super.dispatch(ev) }
    }

    override fun onTouch(ev: MotionEvent): Boolean {
        return dispatchDelegate.onTouchEvent(ev) { super.onTouch(ev) }
    }
}

class SparrowLayout(view: MView) : MViewGroup(view) {
    private val dispatchDelegate = DispatchConfig.getViewGroupDispatchDelegate("SViewGroup")

    override fun dispatch(ev: MotionEvent): Boolean {
        return dispatchDelegate.dispatchTouchEvent(ev) { super.dispatch(ev) }
    }

    override fun onTouch(ev: MotionEvent): Boolean {
        return dispatchDelegate.onTouchEvent(ev) { super.onTouch(ev) }
    }

    override fun onIntercept(ev: MotionEvent): Boolean {
        return dispatchDelegate.onInterceptTouchEvent(ev) { super.onIntercept(ev) }
    }
}

class SparrowActivity(viewGroup: MViewGroup) : MActivity(viewGroup) {
    private val dispatchDelegate = DispatchConfig.getActivityDispatchDelegate("SActivity")

    override fun dispatch(ev: MotionEvent): Boolean {
        log(" ")
        log("[${ev.typeName()}]")
        return dispatchDelegate.dispatchTouchEvent(ev) { super.dispatch(ev) }
    }

    override fun onTouch(ev: MotionEvent): Boolean {
        return dispatchDelegate.onTouchEvent(ev) { super.onTouch(ev) }
    }
}