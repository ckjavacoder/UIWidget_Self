package com.youloft.ui.myapplication;

import android.content.Context;
import android.util.AttributeSet;

import com.youloft.widget.WeekView;

/**
 * 周视图
 * <p/>
 * Created by javen on 15/6/11.
 */
public class WeekFlowView extends HFlowView<WeekView> {


    public WeekFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(new WeekView(context, attrs));
        addView(new WeekView(context, attrs));
        addView(new WeekView(context, attrs));
    }

}
