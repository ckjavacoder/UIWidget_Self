package com.youloft.ui.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by javen on 15/6/10.
 */
public abstract class BaseCardView extends RecyclerView.ViewHolder {


    public BaseCardView(View itemView) {
        super(itemView);
    }

    public BaseCardView(ViewGroup parent, int layoutRes) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
    }


}
