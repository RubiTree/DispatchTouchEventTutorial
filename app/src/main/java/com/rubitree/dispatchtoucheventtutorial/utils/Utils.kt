package com.rubitree.dispatchtoucheventtutorial.utils

import android.util.Log
import android.view.MotionEvent

/**
 * >> Description <<
 *
 * >> Attention <<
 *
 * >> Others <<
 *
 * Created by RubiTree ; On 2019-01-14.
 */
private const val LOG_TAG = "TouchDojo"

fun MotionEvent?.log(layer: String, time: String, msg: String = "") {
    log("|layer:$layer |on:$time${if (msg.isEmpty()) "" else " |$msg"}")
}

fun MotionEvent?.log(msg: String) {
    Log.d(LOG_TAG, "$msg |type:${typeName()}".replaceFormat())
}

fun MotionEvent?.typeName(): String {
    if (this == null) return "null"

    return when (this.actionMasked) {
        MotionEvent.ACTION_DOWN -> "down"
        MotionEvent.ACTION_MOVE -> "move"
        MotionEvent.ACTION_UP -> "up"
        MotionEvent.ACTION_CANCEL -> "cancel"
        else -> "?"
    }
}

private fun String.replaceFormat(): String {
    var result = this

    result = result.replace("dispatchTouchEvent", "Dispatch")
    result = result.replace("onInterceptTouchEvent", "Intercept")
    result = result.replace("onTouchEvent", "Touch")

    return result
}

fun log(msg: String) {
    Log.d(LOG_TAG, msg)
}