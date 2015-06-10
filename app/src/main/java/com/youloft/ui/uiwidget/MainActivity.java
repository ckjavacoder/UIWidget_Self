package com.youloft.ui.uiwidget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_fragment);

        final CardFlowView mFlowView = (CardFlowView) findViewById(R.id.cardflow);
        mFlowView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mFlowView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                final View itemView = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                return new RecyclerView.ViewHolder(itemView) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                final TextView tv = (TextView) viewHolder.itemView;
                tv.setText("index:" + i);
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });

    }

    int step = 0;

    boolean lock = false;
    int contentOffset = 0;

    static class JRecyc extends RecyclerView {

        public JRecyc(Context context) {
            super(context);

        }

        public JRecyc(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public JRecyc(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void offsetChildrenVertical(int dy) {
            System.out.println("offset child " + dy);
            super.offsetChildrenVertical(dy);

        }


        @Override
        public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
            System.out.println("offset dispatchNestedScroll " + dxConsumed);
            return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);

        }


        @Override
        public void setTranslationY(float translationY) {
            super.setTranslationY(translationY);
            System.out.println("translatinY hgas setting" + translationY);
        }

        @Override
        public boolean startNestedScroll(int axes) {

            System.out.println("offset startNestedScroll " + axes);
            return super.startNestedScroll(axes);
        }

        @Override
        public void offsetTopAndBottom(int offset) {
            super.offsetTopAndBottom(offset);
            System.out.println("offset:" + offset);
        }


        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            System.out.println("layou top :" + getTop());
        }
    }
}
