package com.comparinginteraction;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.transition.Transition;
import android.view.View;

import com.animationutils.ChangeBoundsToCenter;
import com.animationutils.MAnimationUtils;


public class SecondActivity extends AppCompatActivity {

    private static final int MED_DURATION = 350;
    private static final int LONG_DURATION = 400;

    View mRootView;

    View mHeaderView;
    View mAvatarView;
    View mNameTextView;
    View mContentImageView;

    private int mDuration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);

        mDuration = getIntent().getIntExtra("duration", 200);
        ChangeBoundsToCenter boundsToCenter = new ChangeBoundsToCenter();

        if (getIntent().getBooleanExtra("arcMotion", false)) {
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMaximumAngle(45);
            arcMotion.setMinimumHorizontalAngle(45);
            arcMotion.setMinimumVerticalAngle(45);
            boundsToCenter.setPathMotion(arcMotion);
        }
        boundsToCenter.setDuration(mDuration);
        boundsToCenter.setInterpolator(new FastOutSlowInInterpolator());

        getWindow().setSharedElementEnterTransition(boundsToCenter);

        mHeaderView = findViewById(R.id.headerview);
        mAvatarView = findViewById(R.id.avatar);
        mNameTextView = findViewById(R.id.name);
        mContentImageView = findViewById(R.id.imageView5);
        mRootView = findViewById(R.id.rootview);

        initDefaultValues();
        boundsToCenter.addListener(new BaseTransitionListener() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                transition.removeListener(this);
                animateContent();
            }
        });

//        mRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                mRootView.removeOnLayoutChangeListener(this);
//                Rect rect = new Rect();
//                mRootView.getLocalVisibleRect(rect);
//                rect.bottom = 0;
//                mRootView.setClipBounds(rect);
//
//                Animator animator = MAnimationUtils.createSlidingReveal(
//                        mRootView, MAnimationUtils.Position.TOP, 0);
//                animator.setDuration(MED_DURATION)
//                        .setStartDelay(500);
//                animator.setInterpolator(new FastOutSlowInInterpolator());
//                animator.start();
//            }
//        });
    }

    private void initDefaultValues() {
        ViewCompat.setTransitionName(mHeaderView, "shared");

        mAvatarView.setScaleX(0);
        mAvatarView.setScaleY(0);

        mNameTextView.setAlpha(0);
        mContentImageView.setAlpha(0);

    }

    private void animateContent() {
        mAvatarView.animate()
                .scaleXBy(1f).scaleY(1f)
                .setDuration(MED_DURATION)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();

        mNameTextView.animate()
                .alpha(1)
                .setDuration(LONG_DURATION)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();

        //TODO : play together with alpha
        Animator animator = MAnimationUtils.createSlidingReveal(mContentImageView, MAnimationUtils.Position.TOP, 0/*mContentImageView.getMeasuredHeight() / 4*/);
        animator.setDuration(MED_DURATION).setInterpolator(new FastOutSlowInInterpolator());
        animator.start();

        //        Animator alphaAnimator = ObjectAnimator.ofFloat(mContentImageView, View.ALPHA, 0, 1);

        mContentImageView.animate()
                .alpha(1)
                .setDuration(MED_DURATION)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }
}
