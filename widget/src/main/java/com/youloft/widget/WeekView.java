package com.youloft.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

/**
 * 周视图
 * <p/>
 * Created by javen on 15/6/8.
 */
public class WeekView extends View implements GestureDetector.OnGestureListener {
    int drawLeft = 0;

    int itemWidth = 0;

    TextPaint mPaint = null;


    GestureDetectorCompat mGesture;

    Paint mLinePaint = null;

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setColor(Color.GRAY);

        mGesture = new GestureDetectorCompat(context, this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        drawLeft = w % 7 / 2;
        itemWidth = w / 7;

    }


    /**
     * 更新某一项
     *
     * @param pos
     */
    public void updatePosition(int pos) {
        if (mBufferCanvas != null) {
            Rect rect = getRectByPoint(new Point(pos, 0));
            mBufferCanvas.save();
            mBufferCanvas.clipRect(rect, Region.Op.REPLACE);
            mBufferCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            drawDay(mBufferCanvas, rect, pos, true);
            mBufferCanvas.restore();
        }
    }

    private Rect mRect = new Rect();

    private Bitmap mBufferBitmap = null;

    private Canvas mBufferCanvas = null;

    int preSelect = 0;

    @Override
    protected void onDraw(Canvas canvas) {


        long begin = System.currentTimeMillis();

        if (mSelectPoint.x > 0) {
            final Rect rect = getRectByPoint(mSelectPoint);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.RED);
            canvas.drawRect(rect, mPaint);
        }

        if (mBufferCanvas == null) {

            mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mBufferCanvas = new Canvas(mBufferBitmap);
            mBufferCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            for (int i = 0; i < 42; i++) {
                mRect.set(drawLeft + itemWidth * i + 1, 0, drawLeft + itemWidth * i + itemWidth - 1, getHeight());
                if (i == mSelectPoint.x) {
                    drawDay(mBufferCanvas, mRect, i, true);
                } else {
                    drawDay(mBufferCanvas, mRect, i, false);
                }
                mPaint.setStyle(Paint.Style.STROKE);
            }
        }
        if (mSelectPoint.x > 0 && mSelectPoint.x != preSelect) {
            updatePosition(mSelectPoint.x);
            updatePosition(preSelect);

        }
        canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        if (mSelectPoint.x > 0) {
            final Rect rect = getRectByPoint(mSelectPoint);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.GRAY);
            canvas.drawRect(rect, mPaint);
        }
        preSelect = mSelectPoint.x;
        long end = System.currentTimeMillis();
        System.out.println("cost time:" + (end - begin));
    }

    private Rect getRectByPoint(Point mSelectPoint) {
        return new Rect(mSelectPoint.x * itemWidth, mSelectPoint.y * getHeight(), mSelectPoint.x * itemWidth + itemWidth, mSelectPoint.y * getHeight() + getHeight());
    }

    int i = 0;


    /**
     * 简单的日视图
     */
    static class SimpleDayView extends BaseDayView {

        private TextPaint mPaint;

        private Calendar mDate = Calendar.getInstance();

        public SimpleDayView(DrawParams params) {
            super(params);
            mPaint = params.mLunarPaint;
        }

        public SimpleDayView(DrawParams params, Calendar date) {
            this(params);
            this.mDate.setTimeInMillis(date.getTimeInMillis());
        }


        boolean isSelected = false;


        @Override
        public void draw(Canvas canvas) {


//            canvas.save();
//            canvas.clipRect(mRect, Region.Op.REPLACE);
//            canvas.drawColor(Color.TRANSPARENT);
//
//            canvas.restore();
//
//            mPaint.setTextSize(mRect.width() / 2.5f);
//            mPaint.setColor(isSelected ? Color.WHITE : 0xFF111111);
//            mPaint.setStyle(Paint.Style.FILL);
//            StaticLayout layout = new StaticLayout(dateTxt, mPaint, mRect.width(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false);
//
//
//            float w = layout.getLineWidth(0);
//            float left = mRect.centerX() + w / 2;
//            float width = Math.min(dp2px(12), mRect.right - left);
//
//            Rect mRect1 = new Rect((int) left, mRect.top, (int) (left + width), (int) (mRect.top + width));
//            Drawable dw = mParams.getHolidayDrawable(1);
//            dw.setBounds(mRect1);
//            dw.draw(canvas);
//            canvas.save();
//            canvas.translate(mRect.left, mRect.top);
//            layout.draw(canvas);
//            canvas.restore();
//
//
//            mPaint.setColor(0xFFD93448);
//            mPaint.setTextSize(mRect.width() / 4);
//            String text = lunarTxt;
//
//            StaticLayout layout1 = new StaticLayout(text, mPaint, mRect.width(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false);
//            int top = mRect.top + layout.getHeight();
//            canvas.save();
//            canvas.translate(mRect.left, top);
//            layout1.draw(canvas);
//            canvas.restore();
//
//            int top1 = top + layout1.getHeight() / layout1.getLineCount();
//            mPaint.setStyle(Paint.Style.FILL);
//            mPaint.setStrokeWidth(1);
//            mPaint.setColor(0xFF999999);
//            if (getWidth() > 480) {
//                top1 += dp2px(4);
//            }
//            canvas.drawCircle(mRect.centerX(), top1 + dp2px(2), dp2px(2), mPaint);
//            mRect.offset(0, -15);
//            mPaint.setStyle(Paint.Style.FILL);
//            mPaint.setColor(Color.BLUE);
//            canvas.drawRoundRect(new RectF(mRect.left + dp2px(3), mRect.bottom - dp2px(2), mRect.right - dp2px(3), mRect.bottom - dp2px(1)), dp2px(1), dp2px(1), mPaint);


        }

        public int getWidth() {
            return mParams.width;
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter cf) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    }

    /**
     * 在Rect区域中绘制天
     * 13
     * <p/>
     * 39x39
     *
     * @param canvas
     * @param mRect
     * @param isSelected
     */
    private void drawDay(Canvas canvas, Rect mRect, int index, boolean isSelected) {

        canvas.save();
        canvas.clipRect(mRect);
        mPaint.setTextSize(30);
        canvas.translate(mRect.left, mRect.top);
        canvas.drawText("F", mRect.width() / 2, mRect.height() / 2, mPaint);
        canvas.restore();

    }

    private void drawLine(Canvas canvas, int y, int green) {
        if (!DEBUG) return;

        mLinePaint.setColor(green);
        canvas.drawLine(0, y, getWidth(), y, mLinePaint);
    }

    boolean DEBUG = false;

    private void drawLinX(Canvas canvas, int x, int red) {
        if (!DEBUG) return;
        mLinePaint.setColor(red);
        canvas.drawLine(x, 0, x, getHeight(), mLinePaint);
    }

    private void drawLine1(Canvas canvas, float y) {
        if (!DEBUG) return;

        mLinePaint.setColor(Color.BLUE);
        canvas.drawLine(0, y, getWidth(), y, mLinePaint);
    }

    private void drawLine(Canvas canvas, float y) {
        if (!DEBUG) return;

        mLinePaint.setColor(Color.GRAY);
        canvas.drawLine(0, y, getWidth(), y, mLinePaint);
    }

    private void drawLinX(Canvas canvas, float x) {
        if (!DEBUG) return;
        mLinePaint.setColor(Color.GREEN);
        canvas.drawLine(x, 0, x, getHeight(), mLinePaint);
    }

    Point mSelectPoint = new Point(0, 0);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGesture.onTouchEvent(event);
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
        Point sPoint = findPointIn(e.getX(), e.getY());
        mSelectPoint.set(sPoint.x, sPoint.y);
        ViewCompat.postInvalidateOnAnimation(this);
        return true;

    }

    /**
     * 返回Point
     *
     * @param x
     * @param y
     * @return
     */
    private Point findPointIn(float x, float y) {
        int xpos = (int) (x / itemWidth);
        int ypos = (int) (y / getHeight());

        return new Point(xpos, ypos);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    private float dp2px(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);

    }
}
