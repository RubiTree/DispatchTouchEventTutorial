package com.rubitree.dispatchtoucheventtutorial.touchdojo

import android.view.MotionEvent
import org.junit.Test

import org.junit.Assert.*

/**
 * >> Description <<
 *
 *
 * >> Attention <<
 *
 *
 * >> Others <<
 *
 *
 * Created by BigBear ; On 2019-01-14.
 */
class SparrowActivityTest {

    @Test
    fun dispatch() {
        val activity = SparrowActivity(SparrowLayout(SparrowView()))

        activity.dispatch(getMotionEvent(MotionEvent.ACTION_DOWN))
        activity.dispatch(getMotionEvent(MotionEvent.ACTION_MOVE))
        activity.dispatch(getMotionEvent(MotionEvent.ACTION_MOVE))
        activity.dispatch(getMotionEvent(MotionEvent.ACTION_MOVE))
        activity.dispatch(getMotionEvent(MotionEvent.ACTION_UP))
    }

    private fun getMotionEvent(action: Int): MotionEvent {
        val currentTimeMillis = System.currentTimeMillis()
        return MotionEvent.obtain(
            currentTimeMillis, currentTimeMillis,
            action, 100f, 100f, 0
        )
    }
}