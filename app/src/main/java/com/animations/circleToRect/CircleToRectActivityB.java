package com.comparinginteraction.circleToRect;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.transition.Transition;
import android.view.View;

import com.circleToRect.ChangeBoundsToCenter;
import com.animationutils.MAnimationUtils;
import com.comparinginteraction.BaseTransitionListener;
import com.comparinginteraction.R;


public class CircleToRectActivityB extends AppCompatActivity {

    private static final int MED_DURATION = 350;
    private static final int LONG_DURATION = 400;

    View mRootView;

    View mHeaderView;
    View mAvatarView;
    View mNameTextView;
    View mContentImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_to_rect_b);

        int duration = getIntent().getIntExtra("duration", 400);
        ChangeBoundsToCenter boundsToCenter = new ChangeBoundsToCenter();

        if (getIntent().getBooleanExtra("arcMotion", false)) {
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMaximumAngle(45);
            arcMotion.setMinimumHorizontalAngle(45);
            arcMotion.setMinimumVerticalAngle(45);
            boundsToCenter.setPathMotion(arcMotion);
        }
        boundsToCenter.setDuration(duration);
        getWindow().setSharedElementEnterTransition(boundsToCenter);

        mHeaderView = findViewById(R.id.headerview);
        mAvatarView = findViewById(R.id.avatar);
        mNameTextView = findViewById(R.id.name);
        mContentImageView = findViewById(R.id.imageView5);
        mRootView = findViewById(R.id.rootview);

        //initialize starting values (before animation)
        initDefaultValues();

        boundsToCenter.addListener(new BaseTransitionListener() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                transition.removeListener(this);
                animateContent();
            }
        });

        //TODO translate all the container (Card) and not only the content
    }

    private void initDefaultValues() {
        ViewCompat.setTransitionName(mHeaderView, "shared");

        mAvatarView.setScaleX(0);
        mAvatarView.setScaleY(0);

        mNameTextView.setAlpha(0);
        mContentImageView.setAlpha(0);

    }

    //Animate avatar, name and content
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

        Animator animator = MAnimationUtils.createSlidingReveal(mContentImageView, MAnimationUtils.Position.TOP, 0/*mContentImageView.getMeasuredHeight() / 4*/);
        animator.setDuration(MED_DURATION).setInterpolator(new FastOutSlowInInterpolator());
        animator.start();

        mContentImageView.animate()
                .alpha(1)
                .setDuration(MED_DURATION)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }
}
