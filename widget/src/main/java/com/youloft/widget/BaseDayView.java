package com.youloft.widget;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by javen on 15/6/8.
 */
public abstract class BaseDayView extends Drawable {

    DrawParams mParams;

    String dateTxt;

    String lunarTxt;


    public BaseDayView(DrawParams params) {
        this.mParams = params;
    }


    Rect mRect = null;


    public void setBounds(Rect rect) {
        this.mRect = rect;
    }

}
