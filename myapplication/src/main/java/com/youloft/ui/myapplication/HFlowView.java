package com.youloft.ui.myapplication;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;


/**
 * FlowView
 * <p/>
 * <p/>
 * Created by javen on 15/6/11.
 */
public class HFlowView<T extends View> extends FrameLayout implements GestureDetector.OnGestureListener {


    ValueAnimator mScrollAnimator;

    ValueAnimator mHeightAnimator;

    boolean isAnimate = false;

    float mTouchSlop;

    GestureDetectorCompat mGestureDetector;

    public HFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetectorCompat(context, this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    /**
     * 动画设置高度
     *
     * @param newHeight
     */
    public void setViewHeight(int newHeight) {
        if (getHeight() == 0 || newHeight < 0) {
            getLayoutParams().height = newHeight;
            requestLayout();
            return;
        }
        if (mHeightAnimator == null) {
            mHeightAnimator = ValueAnimator.ofInt(getHeight(), newHeight);
            mHeightAnimator.setFrameDelay(10);
            mHeightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final Integer mHeight = (Integer) animation.getAnimatedValue();
                    getLayoutParams().height = mHeight;
                    requestLayout();
                }
            });
        }
        mHeightAnimator.setIntValues(getHeight(), newHeight);
        mHeightAnimator.setDuration(250);
        mHeightAnimator.start();
    }


    /**
     * animate scroll
     *
     * @param x
     */
    public void smoothScrollTo(int x) {
        if (isAnimate = false)
            isAnimate = true;
        final int oldX = getScrollX();
        if (mScrollAnimator == null) {
            mScrollAnimator = ValueAnimator.ofInt(oldX, x);
            mScrollAnimator.setFrameDelay(10);
            mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final Integer value = (Integer) animation.getAnimatedValue();
                    scrollTo(value, getScrollY());
                }
            });
            mScrollAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimate = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimate = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isAnimate = false;
                }
            });
        }
        mScrollAnimator.setDuration(300);
        mScrollAnimator.setIntValues(oldX, x);
        mScrollAnimator.start();
    }


    final int FLAG_PRE = -1;

    final int FLAG_NEXT = 1;

    int flag = 0;


    /**
     * 显示一下个View
     */
    public void showNextView() {
    }


    /**
     * 显示上一个View
     */
    public void showPreView() {
    }


    float dx, dy, lx, ly, fx, fy, ddx, ddy;


    boolean needIntercept = false;

    /**
     * 事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isAnimate) {
            return false;
        }
        if (needIntercept && ev.getAction() != MotionEvent.ACTION_DOWN)
            return true;
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                dx = lx = fx = ev.getX();
                dy = ly = fy = ev.getY();
                needIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                fx = ev.getX();
                fy = ev.getY();
                ddx = fx - lx;
                ddy = fy - ly;
                if (Math.abs(ddx) > Math.abs(ddy) && Math.abs(ddx) >= mTouchSlop) {
                    needIntercept = true;
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                lx = fx;
                fy = ly;
                break;
        }
        mGestureDetector.onTouchEvent(ev);
        return needIntercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAnimate) {
            return false;
        }

        /**
         * 弹起
         */
        if (event.getAction() == MotionEvent.ACTION_UP) {

        }
        return mGestureDetector.onTouchEvent(event);
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
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        scrollBy((int) distanceX, getScrollY());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
