package com.rubitree.dispatchtoucheventtutorial.sparrowdispatch.sparrow5_java;

import android.view.MotionEvent;

/**
 * >> Description <<
 * <p>
 * >> Attention <<
 * <p>
 * >> Others <<
 * <p>
 * Created by RubiTree ; On 2019-01-14.
 */
public class MView {
    public ViewParent parent = null;

    public boolean dispatch(MotionEvent ev) {
        // 源码里没有这么直接但区别不大，主要会考虑是否设置了 onTouchListener 和是否 enable
        return onTouch(ev);
    }

    public boolean onTouch(MotionEvent ev) {
        return false;
    }
}
