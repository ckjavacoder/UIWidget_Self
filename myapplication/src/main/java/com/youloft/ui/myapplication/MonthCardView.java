package com.youloft.ui.myapplication;

import android.view.View;
import android.view.ViewGroup;

/**
 * 月卡片视图
 * Created by javen on 15/6/10.
 */
public class MonthCardView extends BaseCardView {


    public static int HEIGHT = 0;

    public MonthCardView(ViewGroup parent) {
        super(parent, R.layout.card_month);
        setIsRecyclable(true);

    }


    @Override
    public void bindUI() {


    }
}
