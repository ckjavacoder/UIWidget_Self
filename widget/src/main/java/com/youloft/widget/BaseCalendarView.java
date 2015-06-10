package com.youloft.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;
import com.youloft.widget.evaluators.RectEvaluator;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * 月视图周视图的基类用于管理日视图
 * <p/>
 * <p/>
 * Created by javen on 15/6/9.
 */
public abstract class BaseCalendarView extends View implements GestureDetector.OnGestureListener {

    GestureDetectorCompat mGesture;

    DrawParams mDrawParams;

    ValueAnimator mSelectorAnimator = null;

    Rect mSelectorRect = new Rect();

    int itemWidth;

    int itemHeight;

    int spaceH;

    int mSelectedIndex = -1;

    int mFirstDayOfWeek = Calendar.SUNDAY;

    Set<BaseDayView> mInvalidateViews = new HashSet<BaseDayView>(10);


    public int getRealHeight() {
        return getHeight();
    }


    public void setFirstDayOfWeek(int i) {
        if (this.mFirstDayOfWeek != i) {
            mFirstDayOfWeek = i;
            initLayout();
        }
    }


    BaseDayView childs[] = null;

    public BaseCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        mGesture = new GestureDetectorCompat(context, this);
        mDrawParams = new DrawParams(context, attrs);
        itemHeight = mDrawParams.getLineHeight();
        initLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getLayoutParams().height < 0) {
            setMeasuredDimension(getMeasuredWidth(), itemHeight * (getCount() / 7));
        }
    }

    boolean useCache = true;

    Bitmap mBufferBitmap = null;

    Canvas mBufferCanvas = null;

    boolean needDrawBuffer = true;

    protected abstract int getCount();

    protected abstract BaseDayView getDayView(DrawParams mDrawParams, int index, BaseDayView child);

    protected void initLayout() {
        if (childs == null)
            childs = new SimpleDayView[getCount()];
        for (int j = 0; j < childs.length; j++) {
            BaseDayView child = childs[j];
            if (child == null) {
                child = getDayView(mDrawParams, j, child);
                childs[j] = child;
            } else {
                getDayView(mDrawParams, j, child);
                needDrawBuffer = true;
            }
            child.setAlpha(DateUtils.isSameMonth(child.getDate(), getDisplayDate()) ? 255 : 127);
        }
        if (needDrawBuffer) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }


    /**
     * 获取Child
     *
     * @param i
     * @return
     */
    public BaseDayView getChildAt(int i) {
        return childs[i];
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (childs == null)
            return;
        Paint selectPaint = null;
        if (selectPaint == null) {
            selectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            selectPaint.setColor(Color.RED);
        }
        if (mSelectorRect != null && !mSelectorRect.isEmpty())
            canvas.drawRect(mSelectorRect, selectPaint);
        if (useCache) {
            boolean flag = initBuffer();
            if (needDrawBuffer || flag) {
                mBufferCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                drawChildren(mBufferCanvas);
                mInvalidateViews.clear();
                needDrawBuffer = false;
            }

            if (mInvalidateViews != null) {
                for (BaseDayView dayView : mInvalidateViews) {
                    dayView.draw(mBufferCanvas);
                }
                mInvalidateViews.clear();
            }
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        } else {
            drawChildren(canvas);
        }
        selectPaint.setStyle(Paint.Style.STROKE);
        if (mSelectorRect != null && !mSelectorRect.isEmpty())
            canvas.drawRect(mSelectorRect, selectPaint);
    }

    /**
     * 创建Buffer
     */
    protected boolean initBuffer() {
        if (mBufferCanvas == null
                || mBufferBitmap == null
                || mBufferBitmap.getWidth() != getWidth()
                || mBufferBitmap.getHeight() != getDrawHeight()) {
            if (mBufferBitmap != null && !mBufferBitmap.isRecycled()) {
                mBufferBitmap.recycle();
            }
            mBufferBitmap = Bitmap.createBitmap(getWidth(), getDrawHeight(), Bitmap.Config.ARGB_8888);
            mBufferCanvas = new Canvas(mBufferBitmap);
            return true;
        }

        return false;
    }


    /**
     * 画布大小
     *
     * @return
     */
    protected int getDrawHeight() {
        return getRealHeight();
    }

    /**
     * 画
     *
     * @param canvas
     */
    private void drawChildren(Canvas canvas) {
        for (int i = 0; i < childs.length; i++) {
            if (childs[i] != null) {
                childs[i].draw(canvas);
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for (int j = 0; j < childs.length; j++) {
            setChildFrame(childs[j], j);
        }
        if (mSelectedIndex == -1) {
            setItemSelected(getIndexAtDate(getDisplayDate()));
        }
    }

    Calendar mDisplayDate = Calendar.getInstance();


    public Calendar getDisplayDate() {
        return (Calendar) mDisplayDate.clone();
    }

    /**
     * 设置显示Date
     *
     * @param date
     */
    public void setDisplayDate(Calendar date) {
        boolean isSameMonth = DateUtils.isSameMonth(date, mDisplayDate);
        if (!isSameMonth) {
            mDisplayDate.setTimeInMillis(date.getTimeInMillis());
            initLayout();

        }
        setItemSelected(getIndexAtDate(date));
    }

    /**
     * ClearSelect
     */
    private void clearSelected() {
        if (mSelectedIndex != -1) {
            childs[mSelectedIndex].setSelected(false);
            invalidateItem(mSelectedIndex);
            mSelectedIndex = -1;
        }
    }


    /**
     * 获取开头的Calendar
     *
     * @return
     */
    protected abstract Calendar getBaseDate();

    /**
     * 获取Index对应的日期对象
     *
     * @param index
     * @return
     */
    protected Calendar getDateAtIndex(int index) {
        Calendar baseDate = getBaseDate();
        baseDate.add(Calendar.DATE, index);
        return baseDate;
    }

    /**
     * 根据Date查找相应的Index
     *
     * @param date
     * @return
     */
    protected int getIndexAtDate(Calendar date) {
        return DateUtils.getDayOffset(date, getBaseDate());
    }

    /**
     * 设置日视图的Frame
     *
     * @param child
     * @param i
     */
    protected void setChildFrame(BaseDayView child, int i) {
        int left = i % 7 * itemWidth + (spaceH > 0 ? spaceH / 2 : spaceH);
        int top = i / 7 * itemHeight;
        child.setBounds(left, top, left + itemWidth, top + itemHeight);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw && w > 0) {
            onComputeSize(w, h);
        }

    }

    /**
     * 设置某项被选中
     *
     * @param index
     */
    public void setItemSelected(int index) {
        setItemSelected(index, true);
    }


    /**
     * 根据坐标查找到相关的内容
     *
     * @param x
     * @param y
     * @return
     */
    private int findPos(float x, float y) {
        if (childs == null)
            return -1;
        for (int i = 0; i < childs.length; i++) {
            BaseDayView dv = childs[i];
            if (dv != null && dv.contain(x, y)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 设置选中项的移动动画
     *
     * @param index
     * @param animate
     */
    public void setItemSelected(int index, boolean animate) {
        if (mSelectedIndex == index || index == -1) {
            return;
        }
        mSelectedIndex = Math.max(Math.min(mSelectedIndex, getCount() - 1), 0);
        if (mSelectedIndex != -1) {
            childs[mSelectedIndex].setSelected(false);
            invalidateItem(mSelectedIndex);
        }

        BaseDayView dv = childs[index];
        mDisplayDate.setTimeInMillis(dv.getDate().getTimeInMillis());
        dv.setSelected(true);
        invalidateItem(index);
        Rect rect = dv.getSelectBounds();
        if (mSelectorRect.isEmpty()) {
            animate = false;
        }
        mSelectedIndex = index;

        final View self = this;
        if (animate) {
            if (mSelectorAnimator == null) {
                mSelectorAnimator = ValueAnimator.ofObject(new RectEvaluator(mSelectorRect), mSelectorRect, rect);
                mSelectorAnimator.setFrameDelay(10);
                mSelectorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ViewCompat.postInvalidateOnAnimation(self);
                    }
                });
            }
            mSelectorAnimator.setObjectValues(mSelectorRect, rect);
            mSelectorAnimator.setDuration(350);
            mSelectorAnimator.start();
        } else {
            if (mSelectorAnimator != null)
                mSelectorAnimator.cancel();
            mSelectorRect.set(rect);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 使某个子项过期
     *
     * @param index
     */
    public void invalidateItem(int index) {
        mInvalidateViews.add(childs[index]);
    }

    protected abstract void onComputeSize(int w, int h);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClickable()) {
            mGesture.onTouchEvent(event);

            if(event.getAction() == MotionEvent.ACTION_UP){

//                if(event.getX()-downX>getWidth()/4){
////                    this.animate().x(getWidth()).setDuration(300).start();
//                }else{
////                    this.animate().x(-getWidth()).setDuration(300).start();
//                }
            }
            return true;


        }


        return super.onTouchEvent(event);
    }

    float downX = -1;
    @Override
    public boolean onDown(MotionEvent e) {

        downX = e.getX();
        return true;

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        int index = findPos(e.getX(), e.getY());
        if (index != -1) {
            performItemClick(index);
        }
        return true;
    }

    /**
     * 点击
     *
     * @param index
     */
    protected void performItemClick(int index) {
        setItemSelected(index);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        System.out.println("MonthView onScroll:"+distanceX+"  y:"+distanceY);

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
