package com.circleToRect;

import android.animation.Animator;
import android.graphics.Rect;

public interface CircleViewAnimatorHandler {
    Animator createCircleToRectAnimator(Rect fromRect, Rect toRect);

    Animator createCircleToCircleAnimator(Rect fromRect, Rect toRect);
}
