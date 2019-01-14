package com.rubitree.dispatchtoucheventtutorial.sparrowdispatch.sparrow5

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
interface ViewParent {
    fun requestDisallowInterceptTouchEvent(isDisallowIntercept: Boolean)
}

open class MView {
    var parent: ViewParent? = null

    open fun dispatch(ev: MotionEvent): Boolean {
        // 源码里没有这么直接但区别不大，主要会考虑是否设置了 onTouchListener 和是否 enable
        return onTouch(ev)
    }

    open fun onTouch(ev: MotionEvent): Boolean {
        return false
    }
}

open class MViewGroup(private val child: MView) : MView(), ViewParent {
    private var isChildNeedEvent = false
    private var isSelfNeedEvent = false
    private var isDisallowIntercept = false

    init {
        child.parent = this // 这里只是示意，实际中不建议这么写，会造成提前发布未构造完成的实例
    }

    override fun dispatch(ev: MotionEvent): Boolean {
        var handled = false

        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            clearStatus()

            if (!isDisallowIntercept && onIntercept(ev)) {
                isSelfNeedEvent = true
                handled = onTouch(ev)
            } else {
                handled = child.dispatch(ev)
                if (handled) isChildNeedEvent = true

                if (!handled) { // 这里没有用 if else 是因为这样写上下一致，更清晰
                    handled = onTouch(ev)
                    if (handled) isSelfNeedEvent = true // 这一步有没有必要呢？还是有的，会更清晰，否则下面的else要多写一个不太清晰的else
                }
            }
        } else {
            // 这里 isSelfNeedEvent 条件判断应该放在 isChildNeedEvent 前面
            // 因为两个都为真的情况只能是自己之后通过 onIntercept 抢了控制权，那这之后的控制权就不会去 child 那儿了
            if (isSelfNeedEvent) {
                handled = onTouch(ev)
            } else if (isChildNeedEvent) {
                if (!isDisallowIntercept && onIntercept(ev)) {
                    isSelfNeedEvent = true

                    val cancel = MotionEvent.obtain(ev)
                    cancel.action = MotionEvent.ACTION_CANCEL
                    handled = child.dispatch(cancel)
                    cancel.recycle()
                } else {
                    handled = child.dispatch(ev)
                }
            }
            // 这里不用再 else 了，因为如果 isSelfNeedEvent 和 isChildNeedEvent 都不为 true，上面不会再发事件下来了
        }

        if (ev.actionMasked == MotionEvent.ACTION_UP
            || ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            clearStatus()
        }

        return handled
    }

    private fun clearStatus() {
        isChildNeedEvent = false
        isSelfNeedEvent = false
        isDisallowIntercept = false
    }

    override fun onTouch(ev: MotionEvent): Boolean {
        return false
    }

    open fun onIntercept(ev: MotionEvent): Boolean {
        return false
    }

    override fun requestDisallowInterceptTouchEvent(isDisallowIntercept: Boolean) {
        this.isDisallowIntercept = isDisallowIntercept
        parent?.requestDisallowInterceptTouchEvent(isDisallowIntercept)
    }
}

/**
 * 这里的大逻辑类似 MViewGroup，但细节很多不同，主要因为没有 onIntercept，会简单一些
 */
open class MActivity(private val childGroup: MViewGroup) {
    private var isChildNeedEvent = false
    private var isSelfNeedEvent = false

    open fun dispatch(ev: MotionEvent): Boolean {
        var handled = false

        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            clearStatus()

            handled = childGroup.dispatch(ev)
            if (handled) isChildNeedEvent = true

            if (!handled) {
                handled = onTouch(ev)
                if (handled) isSelfNeedEvent = true
            }
        } else {
            // 这里 isSelfNeedEvent 和 isChildNeedEvent 不会同时为真，顺序无所谓
            if (isSelfNeedEvent) {
                handled = onTouch(ev)
            } else if (isChildNeedEvent) {
                handled = childGroup.dispatch(ev)
            }

            if (!handled) handled = onTouch(ev)
        }

        if (ev.actionMasked == MotionEvent.ACTION_UP
            || ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            clearStatus()
        }

        return handled
    }

    private fun clearStatus() {
        isChildNeedEvent = false
        isSelfNeedEvent = false
    }

    open fun onTouch(ev: MotionEvent): Boolean {
        return false
    }
}