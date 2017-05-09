package com.animationutils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.RectEvaluator;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

public class MAnimationUtils {

    public enum Position {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static Animator createSlidingReveal(View view, Position fromPosition, int fromOffset) {
        Rect local = new Rect();
        view.getLocalVisibleRect(local);
        Rect from = new Rect(local);
        Rect to = new Rect(local);

        switch (fromPosition) {
            case LEFT:
                from.right = from.left + fromOffset;
                break;
            case RIGHT:
                from.left = from.right + fromOffset;
                break;
            case TOP:
                from.bottom = from.top + fromOffset;
                break;
            case BOTTOM:
                from.top = from.bottom + fromOffset;
                break;
        }

        return ObjectAnimator.ofObject(view,
                "clipBounds",
                new RectEvaluator(),
                from, to);
    }
}
