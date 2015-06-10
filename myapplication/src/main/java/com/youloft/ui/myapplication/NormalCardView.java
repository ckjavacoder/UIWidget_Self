package com.youloft.ui.myapplication;

import android.view.ViewGroup;

/**
 * Created by javen on 15/6/10.
 */
public class NormalCardView extends BaseCardView {
    public NormalCardView(ViewGroup parent) {
        super(parent, R.layout.card_normal);
        setIsRecyclable(false);
    }
}
