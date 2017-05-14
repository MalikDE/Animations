package com.circleToRect;

import android.animation.Animator;
import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;


public class CircleRectView extends android.support.v7.widget.AppCompatImageView
        implements CircleViewAnimatorHandler {
    private float cornerRadius = 0;

    private RectF bitmapRect;
    private Path clipPath = new Path();

    public CircleRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Animator createCircleToRectAnimator(Rect fromRect, Rect toRect) {
        final int maxStart = Math.max(fromRect.width(), fromRect.height());
        final int maxEnd = Math.max(toRect.width(), toRect.height());
        final int maxDimen = Math.max(maxStart, maxEnd);
        final ValueAnimator rectAnimator = ValueAnimator.ofObject(new RectEvaluator(), fromRect, toRect);
        rectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Rect val = (Rect) animation.getAnimatedValue();
                cornerRadius = maxDimen - Math.max(val.width(), val.height());
            }
        });

        return rectAnimator;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Animator createCircleToCircleAnimator(Rect fromRect, Rect toRect) {
        final ValueAnimator rectAnimator = ValueAnimator.ofObject(new RectEvaluator(), fromRect, toRect);
        rectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Rect val = (Rect) animation.getAnimatedValue();
                cornerRadius = Math.max(val.width(), val.height());
            }
        });

        return rectAnimator;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //This event-method provides the real dimensions of this custom view.
        bitmapRect = new RectF(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        clipPath.reset();
        clipPath.addRoundRect(bitmapRect, cornerRadius, cornerRadius, Path.Direction.CW);

        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

}