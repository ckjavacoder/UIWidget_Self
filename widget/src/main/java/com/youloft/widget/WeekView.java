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

    BaseDayView childs[] = new BaseDayView[7];

    GestureDetectorCompat mGesture;

    DrawParams mDrawParams;

    int itemWidth;

    int itemHeight;

    int spaceH;

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGesture = new GestureDetectorCompat(context, this);
        mDrawParams = new DrawParams(context, attrs);
        populateLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw && w > 0) {
            itemWidth = w / 7;
            itemHeight = h;
            spaceH = w % 7;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for (int j = 0; j < childs.length; j++) {
            setChildFrame(childs[j], j);
        }
    }

    /**
     * 设置日视图的Frame
     *
     * @param child
     * @param i
     */
    private void setChildFrame(BaseDayView child, int i) {
        int left = i * itemWidth + (spaceH > 0 ? spaceH / 2 : spaceH);
        int top = 0;
        child.setBounds(left, top, left + itemWidth, top + itemHeight);
    }

    /**
     *
     */
    public void populateLayout() {
        for (int j = 0; j < childs.length; j++) {
            BaseDayView child = childs[j];
            if (child == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE,j);
                child = new SimpleDayView(mDrawParams,calendar);
                childs[j] = child;
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < childs.length; i++) {
            childs[i].draw(canvas);
        }

    }


    /**
     * 简单的日视图
     */
    static class SimpleDayView extends BaseDayView {

        private static final boolean DEBUG = false;
        private TextPaint mPaint;

        private Calendar mDate = Calendar.getInstance();

        public SimpleDayView(DrawParams params) {
            super(params);
            mPaint = params.mLunarPaint;
        }

        public SimpleDayView(DrawParams params, Calendar date) {
            this(params);
            this.mDate.setTimeInMillis(date.getTimeInMillis());
            dateTxt = mDate.get(Calendar.DAY_OF_MONTH) + "";
            lunarTxt = "三月";
        }


        boolean isSelected = false;


        @Override
        public void draw(Canvas canvas) {

            if (DEBUG) {
                mParams.DebugPaint.setColor(Color.RED);
                mParams.DebugPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(mRect, mParams.DebugPaint);
            }

            Rect r = new Rect( mRect);
            r.inset(2,2);
            canvas.drawRect(r,mParams.DebugPaint);

            canvas.save();
            canvas.clipRect(mRect);
            canvas.translate(mRect.left, mRect.top+mParams.dp2px(5));
            canvas.drawColor(0x7F999999);

            mParams.initDatePaint(mPaint);
            StaticLayout dateTxtLayout = new StaticLayout(dateTxt, mPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false);
            dateTxtLayout.draw(canvas);

            final Drawable holidayDrawable = mParams.getHolidayDrawable(1);
            if(holidayDrawable!=null){
                int right = mRect.width();
                holidayDrawable.setBounds((int)(right-mParams.dp2px(13)),0,(int)(right-mParams.dp2px(2)),(int)mParams.dp2px(11));
                holidayDrawable.draw(canvas);
            }

            if(DEBUG){
                mParams.DebugPaint.setColor(Color.GREEN);
                canvas.drawLine(0,0,getWidth(),0,mParams.DebugPaint);
                float left = mRect.width()/2+dateTxtLayout.getLineWidth(0)/2;
                canvas.drawLine(left,0,left,getHeight(),mParams.DebugPaint);
                canvas.drawLine(left+mParams.dp2px(12),0,left+mParams.dp2px(12),getHeight(),mParams.DebugPaint);
                canvas.drawLine(0,mParams.dp2px(12),getWidth(),mParams.dp2px(12),mParams.DebugPaint);
                mParams.DebugPaint.setColor(Color.RED);
            }



            //draw debug
            if (DEBUG)
                canvas.drawLine(0, dateTxtLayout.getHeight(), getWidth(), dateTxtLayout.getHeight(), mParams.DebugPaint);

            mParams.initLunarPaint(mPaint);
            StaticLayout lunarTextLayout = new StaticLayout(lunarTxt, mPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false);


            //move canvas draw
            canvas.translate(0, dateTxtLayout.getHeight());
            lunarTextLayout.draw(canvas);
            //draw debug
            if (DEBUG)
                canvas.drawLine(0, lunarTextLayout.getHeight(), getWidth(), lunarTextLayout.getHeight(), mParams.DebugPaint);

            canvas.translate(0,lunarTextLayout.getHeight()+mParams.getEventMargin());

            //draw debug
            if (DEBUG){
                mParams.DebugPaint.setColor(Color.BLUE);
                canvas.drawLine(0, 0, getWidth(), 0, mParams.DebugPaint);
            }

            //draw event
//            canvas.drawPoint(mRect.centerX(),0,mPaint);
            canvas.drawCircle(mRect.width()/2,mParams.dp2px(2),mParams.dp2px(2),mPaint);


            canvas.translate(0,mParams.dp2px(8));
            //draw debug
            if (DEBUG){
                mParams.DebugPaint.setColor(Color.BLACK);
                canvas.drawLine(0, 0, getWidth(), 0, mParams.DebugPaint);
            }

            canvas.drawRoundRect(new RectF(mParams.dp2px(5),0,getWidth()-mParams.dp2px(5),mParams.dp2px(2)),mParams.dp2px(1),mParams.dp2px(1),mPaint);

            canvas.restore();


        }

        public int getWidth() {
            return mRect.width();
        }

        public  int getHeight(){
            return mRect.height();
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
//        mSelectPoint.set(sPoint.x, sPoint.y);
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
        return null;
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
