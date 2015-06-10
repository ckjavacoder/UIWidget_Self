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

    int mLineHeight;


    Drawable mHolidayDrawable;

    Drawable mLadyDrawable;

    Drawable mEventDrawable;

    ColorStateList mTextColor;

    TextPaint mLunarPaint;

    TextPaint mFestivalPaint;

    int mDayTextSize;

    int mLunarTextSize;

    int mFetivalTextSize;

    int mTermTextSize;

    public Paint DebugPaint;

    private Resources mRes;

    public DrawParams(Context context) {
        mRes = context.getResources();
        mLunarPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mFestivalPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        DebugPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        DebugPaint.setStyle(Paint.Style.STROKE);
        DebugPaint.setColor(Color.RED);


        mLineHeight = (int) dp2px(52);
        mDayTextSize = (int) dp2px(21);
        mLunarTextSize = (int) dp2px(10);
        mFetivalTextSize = mLunarTextSize;
        mTermTextSize = mLunarTextSize;
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
        mLineHeight = ta.getDimensionPixelOffset(R.styleable.DayStyle_lineHeight, mLineHeight);
        mDayTextSize = ta.getDimensionPixelSize(R.styleable.DayStyle_dayTextSize, mDayTextSize);
        mLunarTextSize = ta.getDimensionPixelSize(R.styleable.DayStyle_lunarTextSize, mLunarTextSize);
        mFetivalTextSize = ta.getDimensionPixelSize(R.styleable.DayStyle_festTextSize, mLunarTextSize);
        mTermTextSize = ta.getDimensionPixelSize(R.styleable.DayStyle_termTextSize, mLunarTextSize);
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
        return mDayTextSize;
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
        return mLunarTextSize;
    }



    public float getEventMargin() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mRes.getDisplayMetrics());
    }

    public float dp2px(int i) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, mRes.getDisplayMetrics());
    }

    /**
     * 获取行高
     *
     * @return
     */
    public int getLineHeight() {
        return mLineHeight;
    }
}
