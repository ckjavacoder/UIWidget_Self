package com.youloft.ui.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 卡片列表页
 * <p/>
 * <p/>
 * Created by javen on 15/6/10.
 */
public class CardListView extends RecyclerView {

    private boolean mFlingEnabled = true;

    private float mTouchSlop;

    /**
     * 设置是否可以Fling
     *
     * @param enable
     */
    public void setFlingEnable(boolean enable) {
        this.mFlingEnabled = enable;
    }

    public CardListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        setDivider();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setScrollingTouchSlop((int) (mTouchSlop * 3));
    }

    boolean needIntercept = false;
    boolean ignore = false;

    float dx, dy, lx, ly;

    @Override
    public void smoothScrollBy(int dx, int dy) {
        super.smoothScrollBy(dx, dy);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        if (computeVerticalScrollOffset() > 0 && getFirstVisiblePosition() == 0) {
            return true;
        }

        final int action = e.getAction();
        if (needIntercept && action != MotionEvent.ACTION_DOWN) {
            return needIntercept;
        }
        if (ignore && action != MotionEvent.ACTION_DOWN) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lx = dx = e.getX();
                ly = dy = e.getY();
                needIntercept = false;
                ignore = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float ddx = e.getX();
                float ddy = e.getY();
                float diffX = ddx - lx;
                float diffy = ddy - ly;
                lx = ddx;
                ly = ddy;

                if (!ignore
                        && Math.abs(diffX) > Math.abs(diffy)
                        && Math.abs(diffX) > mTouchSlop) {
                    ignore = true;
                    needIntercept = false;
                    return false;
                }

                if (!needIntercept
                        && Math.abs(diffy) > Math.abs(diffX)
                        && Math.abs(diffy) > mTouchSlop) {
                    needIntercept = true;
                    return true;
                }


                break;
        }
        super.onInterceptTouchEvent(e);
        return needIntercept;
    }


    /**
     * 设置间隔
     */
    private void setDivider() {
        addItemDecoration(new ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, State state) {
//                c.drawColor(Color.WHITE);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                if (view.getVisibility() == View.VISIBLE)
                    outRect.set(0, 0, 0, 10);
                if (state.getItemCount() - 1 == parent.getChildAdapterPosition(view))
                    outRect.setEmpty();
            }
        });
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        if (!mFlingEnabled) {
            return false;
        }
        return super.fling(velocityX, velocityY);
    }


    /**
     * 获取第一个显示的位置
     *
     * @return
     */
    public int getFirstVisiblePosition() {
        View child = getChildAt(0);
        if (child == null)
            return 0;
        return getChildAdapterPosition(child);
    }


}
