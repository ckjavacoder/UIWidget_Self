package com.youloft.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * 月视图
 * <p/>
 * <p/>
 * Created by javen on 15/6/9.
 */
public class MonthView extends BaseCalendarView {
    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getCount() {
        return 42;
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        if (getHeight() != getRealHeight()) {
            getLayoutParams().height = getRealHeight();
            requestLayout();
        }
    }

    @Override
    protected BaseDayView getDayView(DrawParams mDrawParams, int index, BaseDayView child) {
        if (child == null) {
            child = new SimpleDayView(mDrawParams, getDateAtIndex(index));
        } else {
            child.setDate(getDateAtIndex(index));
        }
        return child;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getRealHeight() > 0) {
            setMeasuredDimension(getMeasuredWidth(), getRealHeight());
        }
    }

    @Override
    protected Calendar getBaseDate() {
        Calendar calendar = getDisplayDate();
        calendar.setFirstDayOfWeek(mFirstDayOfWeek);
        calendar.set(Calendar.DAY_OF_WEEK, mFirstDayOfWeek);
        calendar.set(Calendar.WEEK_OF_MONTH, 1);
        return calendar;
    }

    @Override
    protected void onComputeSize(int w, int h) {
        itemWidth = w / 7;
        itemHeight = h / 6;
        spaceH = w % 7;
    }

    @Override
    protected int getDrawHeight() {
        return itemHeight * 6;
    }

    @Override
    public int getRealHeight() {
        return itemHeight * DateUtils.getWeeks(getDisplayDate());
    }
}
