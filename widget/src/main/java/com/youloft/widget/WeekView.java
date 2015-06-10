package com.youloft.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * 周视图
 * <p/>
 * Created by javen on 15/6/8.
 */
public class WeekView extends BaseCalendarView {


    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
    }

    @Override
    protected int getCount() {
        return 7;
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
    protected void onComputeSize(int w, int h) {
        itemWidth = w / 7;
        spaceH = w % 7;
    }


    @Override
    protected Calendar getBaseDate() {
        Calendar calendar = getDisplayDate();
        calendar.setFirstDayOfWeek(mFirstDayOfWeek);
        calendar.set(Calendar.DAY_OF_WEEK, mFirstDayOfWeek);
        return calendar;
    }


}
