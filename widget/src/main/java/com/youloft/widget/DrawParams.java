package com.youloft.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Debug;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * 绘图参数
 * <p/>
 * <p/>
 * Created by javen on 15/6/8.
 */
public class DrawParams {

    float mHeight;

    int width;

    Drawable mHolidayDrawable;

    Drawable mLadyDrawable;

    Drawable mEventDrawable;

    ColorStateList mTextColor;

    TextPaint mLunarPaint;

    TextPaint mFestivalPaint;
    public Paint DebugPaint;

    private Resources mRes;

    public DrawParams(Context context) {
        mRes = context.getResources();
        mLunarPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mFestivalPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        DebugPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        DebugPaint.setStyle(Paint.Style.STROKE);
        DebugPaint.setColor(Color.RED);
    }


    /**
     * 绘制参数解析
     *
     * @param context
     * @param attrs
     */
    public DrawParams(Context context, AttributeSet attrs) {
        this(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DayStyle);
        mHolidayDrawable = ta.getDrawable(R.styleable.DayStyle_holiday);
        mHolidayDrawable = mRes.getDrawable(R.drawable.ban_icon1);
        mLadyDrawable = ta.getDrawable(R.styleable.DayStyle_lady);
        mEventDrawable = ta.getDrawable(R.styleable.DayStyle_event);
        mTextColor = ta.getColorStateList(R.styleable.DayStyle_textColor);
        mHeight = ta.getDimension(R.styleable.DayStyle_height, 50);
        ta.recycle();
    }


    /**
     * 获取放假标识
     *
     * @param level
     * @return
     */
    public Drawable getHolidayDrawable(int level) {
        if (mHolidayDrawable instanceof StateListDrawable) {
            mHolidayDrawable.setLevel(level);
        }
        return mHolidayDrawable;
    }

    /**
     * 获取上面日视图的大小
     *
     * @return
     */
    public float getDateTextSize() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, mRes.getDisplayMetrics());
    }

    /**
     * 填充画日期的画笔
     *
     * @param mPaint
     * @param isSelected
     */
    public void initDatePaint(TextPaint mPaint, boolean isSelected) {
        mPaint.setTextSize(getDateTextSize());
        mPaint.setColor(isSelected ? Color.WHITE : 0xFF111111);
    }

    public void initLunarPaint(TextPaint mPaint, boolean isSelected) {
        mPaint.setTextSize(getLunarTextSize());
        mPaint.setColor(isSelected ? Color.WHITE : 0xFFB34A8F);
    }

    private float getLunarTextSize() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mRes.getDisplayMetrics());
    }

    public float getEventMargin() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mRes.getDisplayMetrics());
    }

    public float dp2px(int i) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, mRes.getDisplayMetrics());
    }
}
