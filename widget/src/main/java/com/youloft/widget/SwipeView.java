package com.youloft.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.nineoldandroids.animation.ValueAnimator;


/**
 * 滑动的View用于切换
 * <p/>
 * Created by javen on 15/6/8.
 */
public class SwipeView extends ImageView implements GestureDetector.OnGestureListener {

    GestureDetectorCompat mGestureDetector;

    ValueAnimator mScrollAnimator = null;

    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetectorCompat(context, this);
        mGestureDetector.setIsLongpressEnabled(false);
    }


    /**
     * 动画移动
     *
     * @param x
     */
    private void smoothScrollTo(int x) {
        if (mScrollAnimator == null) {
            mScrollAnimator = mScrollAnimator.ofInt(getScrollX(), x);
            mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final Integer value = (Integer) animation.getAnimatedValue();
                    scrollTo(value, getScrollY());
                }
            });
            mScrollAnimator.setFrameDelay(10);
        }
        mScrollAnimator.setDuration(300);
        mScrollAnimator.setIntValues(getScrollX(), x);
        mScrollAnimator.start();
    }

    /**
     * 停上动作
     */
    private void stopScroll() {
        if (mScrollAnimator != null) {
            mScrollAnimator.cancel();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {

    }


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return true;
    }
}
