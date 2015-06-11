package com.youloft.ui.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 用于协调周视图与月视图切换及广告视图切换
 * <p/>
 * <p/>
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
//        setState(STATE_INVALID);
        mListView.setFlingEnable(false);
    }


    private int preScrollY = -1;

    private int targetScrollY = -1;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            preScrollY = recyclerView.computeVerticalScrollOffset();
            if (targetScrollY != preScrollY && targetScrollY != -1) {
                preScrollY = targetScrollY;
            } else {
                targetScrollY = -1;
            }
        }


        /**
         * 闲置的时候
         */
        if (newState == RecyclerView.SCROLL_STATE_IDLE && !needReused) {
            int offset = recyclerView.computeVerticalScrollOffset();
            final int fHeight = recyclerView.getChildAt(0).getHeight() - mWeekView.getHeight();
            if (state == STATE_WEEK && (mListView.getFirstVisiblePosition() == 0 && offset >= fHeight || mListView.getFirstVisiblePosition() > 0)) {
                state = STATE_LIST;
                mListView.setFlingEnable(true);
            }
            canScrollToAd = mListView.computeVerticalScrollOffset() == 0;

            boolean isUp = offset > preScrollY;
            if (mListView.getFirstVisiblePosition() == 0) {
                if (offset > fHeight / 3 && isUp) {
                    mListView.smoothScrollBy(0, fHeight - offset);
                    targetScrollY = fHeight;
                    state = STATE_LIST;
                    mListView.setFlingEnable(true);
                } else if (offset < fHeight / 3 * 2 && !isUp) {
                    mListView.smoothScrollBy(0, -offset);
                    targetScrollY = 0;
                    state = STATE_INVALID;
                    mListView.setFlingEnable(false);
                } else {
                    if (preScrollY == fHeight) {
                        state = STATE_LIST;
                        mListView.setFlingEnable(true);
                    } else {
                        state = STATE_INVALID;
                        mListView.setFlingEnable(false);
                    }

                    preScrollY = Math.max(0, Math.min(preScrollY, fHeight));

                    mListView.smoothScrollBy(0, preScrollY - offset);
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
        mAdMaskView.setAlpha(1 + p);
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
        int height = mListView.getChildAt(0).getHeight() - mWeekView.getHeight();


        if (state == STATE_WEEK && (pos > 0 || (pos == 0 && bottom <= mWeekView.getHeight()))) {
            needReused = true;
            mListView.scrollBy(0, -dy);
            return;
        }


        if (pos == 0) {
            float p = (bottom - mWeekView.getHeight()) / (float) height;
            mListView.getChildAt(0).setAlpha(p);
            if (p < 0) {
                p = 1 - bottom / (float) mWeekView.getHeight();
            }
            mWeekView.scrollTo(0, (int) (mWeekView.getHeight() * p));

        } else {
            mWeekView.scrollTo(0, mWeekView.getHeight());
        }
//
//        if(state == STATE_LIST
//                &&dy<0
//                && pos==0
//                &&(downPos>0||downOffset>height)){
//            needReused = true;
//            mListView.scrollBy(0,height-mListView.computeVerticalScrollOffset());
//            return;
//        }
    }

    /**
     * 返回主View
     */
    private void returnMainView() {
        mAdView.setEnabled(false);
        smoothScroll(0);
    }

    float downX, downY, lastX, lastY, dx, dy, ex, ey;

    boolean canScrollToAd = false;

    int downOffset = -1, downPos = -1;


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            return true;
        }

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downOffset = mListView.computeVerticalScrollOffset();
                downPos = mListView.getFirstVisiblePosition();
                if (state == STATE_WEEK && mWeekView.getScrollY() <= 0) {
                    state = STATE_LIST;
                    mListView.setFlingEnable(true);
                }
                if (state == STATE_INVALID && mWeekView.getScrollY() > 0) {
                    state = STATE_WEEK;
                    mListView.setFlingEnable(false);
                } else {
                    mListView.setFlingEnable(true);
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
                if (mListFrame.getScrollY() > 0) {
                    showAdView();
                }
                break;
        }
        return false;
    }

    /**
     * 显示广告View
     */
    private void showAdView() {
        smoothScroll(-mListFrame.getHeight());
        mAdView.setEnabled(true);
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
