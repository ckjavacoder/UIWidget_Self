package com.youloft.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

import java.util.Calendar;

/**
 * 简单的日视图
 * <p/>
 * <p/>
 * Created by javen on 15/6/9.
 */
public class SimpleDayView extends BaseDayView {
    private static final boolean DEBUG = false;
    private TextPaint mPaint;


    public SimpleDayView(DrawParams params) {
        super(params);
        mPaint = params.mLunarPaint;
    }

    public SimpleDayView(DrawParams params, Calendar date) {
        this(params);
        setDate(date);
    }

    @Override
    public void setDate(Calendar date) {
        super.setDate(date);
        dateTxt = mDate.get(Calendar.DAY_OF_MONTH) + "";
        lunarTxt = "中华人民共和国中华人民共和国";
    }

    @Override
    public void draw(Canvas canvas) {

        if (DEBUG) {
            mParams.DebugPaint.setColor(Color.RED);
            mParams.DebugPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(mRect, mParams.DebugPaint);
        }
//        canvas.save();
        canvas.saveLayerAlpha(mRect.left, mRect.top, mRect.right, mRect.bottom, mAlpha, Canvas.ALL_SAVE_FLAG);
        canvas.clipRect(mRect);
        canvas.translate(mRect.left, mRect.top + mParams.dp2px(3));
        mParams.initDatePaint(mPaint, isSelected);
        StaticLayout dateTxtLayout = new StaticLayout(dateTxt, mPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false);
        dateTxtLayout.draw(canvas);
        final Drawable holidayDrawable = mParams.getHolidayDrawable(1);
        if (holidayDrawable != null) {
            int right = mRect.width();
            holidayDrawable.setBounds((int) (right - mParams.dp2px(13)), 0, (int) (right - mParams.dp2px(2)), (int) mParams.dp2px(11));
            holidayDrawable.draw(canvas);
        }

        if (DEBUG) {
            mParams.DebugPaint.setColor(Color.GREEN);
            canvas.drawLine(0, 0, getWidth(), 0, mParams.DebugPaint);
            float left = mRect.width() / 2 + dateTxtLayout.getLineWidth(0) / 2;
            canvas.drawLine(left, 0, left, getHeight(), mParams.DebugPaint);
            canvas.drawLine(left + mParams.dp2px(12), 0, left + mParams.dp2px(12), getHeight(), mParams.DebugPaint);
            canvas.drawLine(0, mParams.dp2px(12), getWidth(), mParams.dp2px(12), mParams.DebugPaint);
            mParams.DebugPaint.setColor(Color.RED);
            canvas.drawLine(0, dateTxtLayout.getHeight(), getWidth(), dateTxtLayout.getHeight(), mParams.DebugPaint);

        }
        canvas.translate(0, dateTxtLayout.getLineBaseline(0) + mParams.dp2px(2));
        mParams.initLunarPaint(mPaint, isSelected);
        StaticLayout lunarTextLayout = new StaticLayout(lunarTxt, mPaint, (int) (getWidth() - mParams.dp2px(2)), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false);

        final Rect lineRect = new Rect();
        for (int i = 0; i < 2; i++) {
            canvas.save();
            lunarTextLayout.getLineBounds(0, lineRect);
            canvas.clipRect(lineRect);
            //move canvas draw
            lunarTextLayout.draw(canvas);
            canvas.restore();
        }

        //draw debug
        if (DEBUG)
            canvas.drawLine(0, lunarTextLayout.getHeight(), getWidth(), lunarTextLayout.getHeight(), mParams.DebugPaint);

        canvas.translate(0, lunarTextLayout.getLineBaseline(0) + mParams.getEventMargin());

        //draw debug
        if (DEBUG) {
            mParams.DebugPaint.setColor(Color.BLUE);
            canvas.drawLine(0, 0, getWidth(), 0, mParams.DebugPaint);
        }

        //draw event
//            canvas.drawPoint(mRect.centerX(),0,mPaint);
        canvas.drawCircle(mRect.width() / 2, mParams.dp2px(2), mParams.dp2px(2), mPaint);


        canvas.translate(0, mParams.dp2px(8));
        //draw debug
        if (DEBUG) {
            mParams.DebugPaint.setColor(Color.BLACK);
            canvas.drawLine(0, 0, getWidth(), 0, mParams.DebugPaint);
        }

        canvas.drawRoundRect(new RectF(mParams.dp2px(5), 0, getWidth() - mParams.dp2px(5), mParams.dp2px(2)), mParams.dp2px(2), mParams.dp2px(2), mPaint);

        canvas.restore();
    }

    public int getWidth() {
        return mRect.width();
    }

    public int getHeight() {
        return mRect.height();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
