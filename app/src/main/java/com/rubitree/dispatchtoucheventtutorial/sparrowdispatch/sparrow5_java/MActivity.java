package com.rubitree.dispatchtoucheventtutorial.sparrowdispatch.sparrow5_java;

/**
 * >> Description <<
 * <p>
 * >> Attention <<
 * <p>
 * >> Others <<
 * <p>
 * Created by RubiTree ; On 2019-01-14.
 */

import android.view.MotionEvent;

/**
 * 这里的大逻辑类似 MViewGroup，但细节很多不同，主要因为没有 onIntercept，会简单一些
 */
public class MActivity {
    private MViewGroup childGroup;
    private boolean isChildNeedEvent = false;
    private boolean isSelfNeedEvent = false;

    public MActivity(MViewGroup childGroup) {
        this.childGroup = childGroup;
    }

    public boolean dispatch(MotionEvent ev) {
        boolean handled = false;

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            clearStatus();

            handled = childGroup.dispatch(ev);
            if (handled) isChildNeedEvent = true;

            if (!handled) {
                handled = onTouch(ev);
                if (handled) isSelfNeedEvent = true;
            }
        } else {
            // 这里 isSelfNeedEvent 和 isChildNeedEvent 不会同时为真，顺序无所谓
            if (isSelfNeedEvent) {
                handled = onTouch(ev);
            } else if (isChildNeedEvent) {
                handled = childGroup.dispatch(ev);
            }

            if (!handled) handled = onTouch(ev);
        }

        if (ev.getActionMasked() == MotionEvent.ACTION_UP || ev.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            this.clearStatus();
        }

        return handled;
    }

    private void clearStatus() {
        isChildNeedEvent = false;
        isSelfNeedEvent = false;
    }

    public boolean onTouch(MotionEvent ev) {
        return false;
    }
}
