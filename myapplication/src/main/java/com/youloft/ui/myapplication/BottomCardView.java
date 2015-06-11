package com.youloft.ui.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by javen on 15/6/10.
 */
public class BottomCardView extends BaseCardView {
    public BottomCardView(ViewGroup parent) {
        super(parent, R.layout.card_bottom);
    }

    @Override
    public void bindUI() {

        final int realHeight = mOwnerRecyclerView.computeVerticalScrollExtent();
        final int height = mOwnerRecyclerView.getHeight();
        if (realHeight < height * 1.5f) {
            itemView.getLayoutParams().height = (int) ((height * 1.5f) - realHeight);
        } else {
            itemView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        itemView.requestLayout();

    }
}
