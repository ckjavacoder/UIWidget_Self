package com.youloft.ui.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by javen on 15/6/10.
 */
public class WeekScroll extends RecyclerView.OnScrollListener implements View.OnTouchListener {

    final int STATE_WEEK = 1;
    final int STATE_LIST = 2;
    final int STATE_INVALID = 0;

    View mWeekView;

    int state = STATE_INVALID;

    View mListFrame;
    CardListView mListView;

    View mAdView;
    View mAdMaskView;

    public WeekScroll(Context context, View weekView, CardListView listView, View adView) {
        this.mWeekView = weekView;
        this.mListView = listView;
        mListFrame = (View) listView.getParent();
        this.mAdView = adView;
        this.mAdView.setOnTouchListener(new AdTouchListener(context, this));
        mAdMaskView = mAdView.findViewWithTag("mask");
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        /**
         * 闲置的时候
         */
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int offset = recyclerView.computeVerticalScrollOffset();
            if (mListView.getFirstVisiblePosition() == 0) {
                final int fHeight = recyclerView.getChildAt(0).getHeight() - mWeekView.getHeight();
                if (offset > fHeight / 3) {
                    mListView.smoothScrollBy(0, fHeight - offset);
                } else {
                    mListView.smoothScrollBy(0, -offset);
                    state = STATE_INVALID;
                }
            }
        }
    }

    private ValueAnimator mScrollAnimator;


    /**
     * 动画移动
     *
     * @param y
     */
    protected void smoothScroll(int y) {
        if (mScrollAnimator == null) {
            mScrollAnimator = ValueAnimator.ofInt(mListFrame.getScrollY(), 0);
            mScrollAnimator.setFrameDelay(10);
            mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    setAdScroll(value);
                }
            });
        }
        mScrollAnimator.setIntValues(mListFrame.getScrollY(), y);
        mScrollAnimator.setDuration(400);
        mScrollAnimator.start();
    }


    /**
     * 设置广告展开进度
     */
    private void setAdScroll(final int scrollY) {
        mListFrame.scrollTo(0, scrollY);
        float p = scrollY / (float) mListFrame.getHeight();
        mAdMaskView.setAlpha((Math.abs(p)));
    }


    boolean needReused = false;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        if (needReused) {
            needReused = false;
            return;
        }

        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            return;
        }
        //如果已经在展开不能进行滑动
        if (mListFrame.getScrollY() != 0) {
            return;
        }
        if (dy < 0) {
            canScrollToAd = false;
        }
        int bottom = mListView.getChildAt(0).getBottom();
        int pos = mListView.getFirstVisiblePosition();

        if (state == STATE_WEEK && (pos > 0 || (pos == 0 && bottom <= mWeekView.getHeight()))) {
            needReused = true;
            mListView.scrollBy(0, -dy);
            return;
        }

        if (pos == 0) {
            int height = mListView.getChildAt(0).getHeight() - mWeekView.getHeight();
            float p = (bottom - mWeekView.getHeight()) / (float) height;
            mListView.getChildAt(0).setAlpha(p);
            if (p < 0) {
                p = 1 - bottom / (float) mWeekView.getHeight();
            }
            mWeekView.scrollTo(0, (int) (mWeekView.getHeight() * p));

        } else {
            mWeekView.scrollTo(0, mWeekView.getHeight());
        }

    }

    /**
     * 返回主View
     */
    private void returnMainView() {
        smoothScroll(0);
    }

    float downX, downY, lastX, lastY, dx, dy, ex, ey;

    boolean canScrollToAd = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            return true;
        }

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (state == STATE_WEEK && mWeekView.getScrollY() <= 0) {
                    state = STATE_LIST;
                }
                if (state == STATE_INVALID && mWeekView.getScrollY() > 0) {
                    state = STATE_WEEK;
                }
                canScrollToAd = mListView.computeVerticalScrollOffset() == 0;
                ex = downX = lastX = event.getX();
                ey = downY = lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                ey = event.getY();
                ex = event.getX();

                dy = lastY - ey;
                dx = lastX - ex;

                /**
                 *
                 */
                if (Math.abs(dy) > Math.abs(dx) && canScrollToAd) {
                    if (dy < 0 && mListView.computeVerticalScrollOffset() <= 0) {
                        mListView.scrollBy(0, -mListView.computeVerticalScrollOffset());
                        setAdScroll((int) Math.max(-mListFrame.getHeight(), Math.min(mListFrame.getScrollY() + dy, 0)));
                        return true;
                    } else if (dy > 0 && mListFrame.getScrollY() != 0) {
                        mListView.scrollBy(0, -mListView.computeVerticalScrollOffset());
                        setAdScroll((int) Math.max(-mListFrame.getHeight(), Math.min(mListFrame.getScrollY() + dy, 0)));
                        return true;
                    }
                }
                lastY = ey;
                lastX = ex;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mListFrame.getScrollY() != 0) {
                    smoothScroll(-mListFrame.getHeight());
                }
                break;
        }
        return false;
    }


    static class AdTouchListener implements View.OnTouchListener {

        WeekScroll mScrollHelper;

        GestureDetector mGestureDetector;

        public AdTouchListener(Context context, WeekScroll scrollHelper) {
            this.mScrollHelper = scrollHelper;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    mScrollHelper.returnMainView();
                    return true;

                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    if (Math.abs(distanceY) > Math.abs(distanceX)) {
                        if (distanceY > 0) {
                            mScrollHelper.returnMainView();
                        }
                    }
                    return true;
                }
            });
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mGestureDetector.onTouchEvent(event);
        }
    }


}
