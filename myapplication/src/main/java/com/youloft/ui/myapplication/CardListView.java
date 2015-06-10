package com.youloft.ui.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 卡片列表页
 * <p/>
 * <p/>
 * Created by javen on 15/6/10.
 */
public class CardListView extends RecyclerView {

    public CardListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        setDivider();
    }

    /**
     * 设置间隔
     */
    private void setDivider() {
        addItemDecoration(new ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, State state) {
//                c.drawColor(Color.WHITE);
            }


            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                if (view.getVisibility() == View.VISIBLE)
                    outRect.set(0, 0, 0, 10);
                if (state.getItemCount() - 1 == parent.getChildAdapterPosition(view))
                    outRect.setEmpty();
            }
        });
    }

    /**
     * 获取第一个显示的位置
     *
     * @return
     */
    public int getFirstVisiblePosition() {
        View child = getChildAt(0);
        if (child == null)
            return 0;
        return getChildAdapterPosition(child);
    }


}
