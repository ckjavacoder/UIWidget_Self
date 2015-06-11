package com.youloft.ui.myapplication;

import android.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 卡片适配器
 * <p/>
 * Created by javen on 15/6/10.
 */
public class CardAdapter extends RecyclerView.Adapter<BaseCardView> {


    @Override
    public BaseCardView onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseCardView mCardView = null;
        switch (viewType) {
            case 0:
                mCardView = new MonthCardView(parent);
                break;
            case 1:
                mCardView = new NormalCardView(parent);
                break;
            case 2:
                mCardView = new BottomCardView(parent);
                break;
        }
        return mCardView;
    }

    @Override
    public void onBindViewHolder(BaseCardView holder, int position) {
        System.out.println("postion:" + position);
//        if (position > 0 && position % 2 == 0) {
//            holder.itemView.getLayoutParams().height = 0;
//            holder.itemView.setVisibility(View.GONE);
//        } else {
//            holder.itemView.getLayoutParams().height = ActionBar.LayoutParams.WRAP_CONTENT;
//        }

        holder.bindUI();
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }

        if (position == getItemCount() - 1) {
            return 2;
        }

        return 1;
    }
}
