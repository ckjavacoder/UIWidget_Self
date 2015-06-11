package com.youloft.ui.myapplication;

import android.content.Context;
import android.util.AttributeSet;

import com.youloft.widget.MonthView;

/**
 * 月视图
 * <p/>
 * Created by javen on 15/6/11.
 */
public class MonthFlowView extends HFlowView<MonthView> {
    public MonthFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(new MonthView(context, attrs));
        addView(new MonthView(context, attrs));
        addView(new MonthView(context, attrs));
    }


}
