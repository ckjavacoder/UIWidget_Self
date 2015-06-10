package com.youloft.ui.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.youloft.widget.MonthView;

public class MainActivity extends AppCompatActivity {
    private CardListView cardlist;
    private View mWeekView;

    private View mAdView;

    WeekScroll mWeekScrollHelper;

    private void assignViews() {
        cardlist = (CardListView) findViewById(R.id.cardlist);
        mWeekView = findViewById(R.id.week);
        mAdView = findViewById(R.id.adlayer);
        mWeekView.post(new Runnable() {
            @Override
            public void run() {
                mWeekView.scrollTo(0, mWeekView.getHeight());
            }
        });

       cardlist.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
           @Override
           public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

           }
       });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        mWeekScrollHelper = new WeekScroll(this, mWeekView, cardlist, mAdView);
        cardlist.setAdapter(new CardAdapter());
        cardlist.addOnScrollListener(mWeekScrollHelper);
        cardlist.setOnTouchListener(mWeekScrollHelper);
    }


}
