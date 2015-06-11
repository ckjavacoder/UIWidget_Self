package com.youloft.ui.myapplication;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * FlowView
 * <p/>
 * <p/>
 * Created by javen on 15/6/11.
 */
public class HFlowView extends FrameLayout {

    public HFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        populateLayout();
    }

    /**
     * 初始化View
     */
    private void populateLayout() {

    }

    /**
     * 显示一下个View
     */
    public void showNextView() {

    }

    /**
     * 显示上一个View
     */
    public void showPreView() {

    }

}
