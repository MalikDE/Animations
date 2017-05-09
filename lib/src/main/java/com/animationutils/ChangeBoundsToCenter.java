package com.animationutils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.ViewGroup;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ChangeBoundsToCenter extends ChangeBounds {
    private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, final TransitionValues startValues, final TransitionValues endValues) {
        Object startObj = startValues.values.get(PROPNAME_BOUNDS);
        Object endObj = endValues.values.get(PROPNAME_BOUNDS);

        Rect endRect = (Rect) endObj;
        Rect startRect = (Rect) startObj;
        Rect tmpEndRect = new Rect(endRect);

        final boolean isExpanding = startRect.height() * startRect.width() < endRect.height() * endRect.width();


        //Calculate position values for the temp animation state
        if (isExpanding) {
            int minW = Math.min(tmpEndRect.width(), tmpEndRect.height());
            tmpEndRect.left = (tmpEndRect.right - tmpEndRect.left - minW) / 2;
            tmpEndRect.right = (tmpEndRect.left + minW);

            int minH = Math.min(tmpEndRect.width(), tmpEndRect.height());
            tmpEndRect.top = (tmpEndRect.bottom - tmpEndRect.top - minH) / 2;
            tmpEndRect.bottom = (tmpEndRect.top + minH);
        } else {
            int minW = Math.min(startRect.width(), startRect.height());
            tmpEndRect.top = (startRect.bottom - startRect.top - minW) / 2;
            tmpEndRect.bottom = (tmpEndRect.top + minW);

            int minH = Math.min(startRect.width(), startRect.height());
            tmpEndRect.left = (startRect.right - startRect.left - minH) / 2;
            tmpEndRect.right = (tmpEndRect.left + minH);
        }


        //Put the temp end value for the first animator
        endValues.values.put(PROPNAME_BOUNDS, tmpEndRect);
        //Create the first animator
        Animator first = super.createAnimator(sceneRoot, startValues, endValues);

        AnimatorSet firstSet = new AnimatorSet();
        firstSet.playTogether(first, isExpanding ? createCircleToCircleAnimator(startValues, endValues) : createCircleToRectAnimator(startValues, endValues));


        //Put the temp end value as a start value for the second animator
        startValues.values.put(PROPNAME_BOUNDS, tmpEndRect);
        //set the real end value
        endValues.values.put(PROPNAME_BOUNDS, endRect);

        //Create a second animator
        Animator second = super.createAnimator(sceneRoot, startValues, endValues);
        AnimatorSet secondSet = new AnimatorSet();
        secondSet.playTogether(second, isExpanding ? createCircleToRectAnimator(startValues, endValues) : createCircleToCircleAnimator(startValues, endValues));

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(firstSet, secondSet);
        return set;
    }

    private Animator createCircleToCircleAnimator(final TransitionValues startValues, final TransitionValues endValues) {
        if (!(startValues.view instanceof CircleRectView)) {
            Log.w(ChangeBoundsToCenter.class.getSimpleName(), "transition view should be CircleRectView");
            return null;
        }
        CircleRectView view = (CircleRectView) (startValues.view);

        Rect startRect = (Rect) startValues.values.get(PROPNAME_BOUNDS);
        final Rect endRect = (Rect) endValues.values.get(PROPNAME_BOUNDS);

        return view.createCircleToCircleAnimator(startRect, endRect);
    }

    private Animator createCircleToRectAnimator(final TransitionValues startValues, final TransitionValues endValues) {
        if (!(startValues.view instanceof CircleRectView)) {
            Log.w(ChangeBoundsToCenter.class.getSimpleName(), "transition view should be CircleRectView");
            return null;
        }
        CircleRectView view = (CircleRectView) (startValues.view);

        Rect startRect = (Rect) startValues.values.get(PROPNAME_BOUNDS);
        final Rect endRect = (Rect) endValues.values.get(PROPNAME_BOUNDS);

        return view.createCircleToRectAnimator(startRect, endRect);
    }
}
