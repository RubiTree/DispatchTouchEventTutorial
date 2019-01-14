package com.rubitree.dispatchtoucheventtutorial.touchdojo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.rubitree.dispatchtoucheventtutorial.R
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
class DelegatedView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val dispatchDelegate = DispatchConfig.getViewDispatchDelegate()

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return dispatchDelegate.dispatchTouchEvent(event) { super.dispatchTouchEvent(event) }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return dispatchDelegate.onTouchEvent(event) { super.onTouchEvent(event) }
    }
}

class DelegatedLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val dispatchDelegate = DispatchConfig.getViewGroupDispatchDelegate()

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return dispatchDelegate.dispatchTouchEvent(event) { super.dispatchTouchEvent(event) }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return dispatchDelegate.onTouchEvent(event) { super.onTouchEvent(event) }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return dispatchDelegate.onInterceptTouchEvent(ev) { super.onInterceptTouchEvent(ev) }
    }
}

class DelegatedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /*--------------------------------------------------------------------------------------------*/

    private val dispatchDelegate = DispatchConfig.getActivityDispatchDelegate()

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        log(" ")
        log("[${event.typeName()}]")
        return dispatchDelegate.dispatchTouchEvent(event) { super.dispatchTouchEvent(event) }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return dispatchDelegate.onTouchEvent(event) { super.onTouchEvent(event) }
    }
}