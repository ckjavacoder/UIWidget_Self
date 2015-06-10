package com.youloft.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 月周视图
 * <p/>
 * Created by javen on 15/6/9.
 */
public class MonthWeekView extends FrameLayout {

    private MonthView mMonthView;

    private WeekView mWeekView;

    public MonthWeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMonthView = (MonthView) findViewWithTag("month");
        mWeekView = (WeekView) findViewWithTag("week");
        bringChildToFront(mWeekView);
        mWeekView.offsetTopAndBottom(-mWeekView.getHeight());
    }


}
