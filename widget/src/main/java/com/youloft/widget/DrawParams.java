package com.youloft.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;

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

    public DrawParams() {
        mLunarPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mFestivalPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }


    /**
     * 绘制参数解析
     *
     * @param context
     * @param attrs
     */
    public DrawParams(Context context, AttributeSet attrs) {
        this();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DayStyle);
        mHolidayDrawable = ta.getDrawable(R.styleable.DayStyle_holiday);
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
}
