package com.youloft.widget;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.Calendar;

/**
 * Created by javen on 15/6/8.
 */
public abstract class BaseDayView extends Drawable {

    DrawParams mParams;

    String dateTxt;

    String lunarTxt;

    Calendar mDate = Calendar.getInstance();


    public BaseDayView(DrawParams params) {
        this.mParams = params;
    }


    Rect mRect = new Rect();


    public void setBounds(Rect rect) {
        this.mRect.set(rect);
        super.setBounds(rect);
        mSelectBounds.set(rect);
        mSelectBounds.inset((int) mParams.dp2px(1), (int) mParams.dp2px(1));
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        this.mRect.set(left, top, right, bottom);
        super.setBounds(left, top, right, bottom);
        mSelectBounds.set(mRect);
        mSelectBounds.inset((int) mParams.dp2px(1), (int) mParams.dp2px(1));
    }


    @Override
    public Rect getDirtyBounds() {
        return super.getDirtyBounds();
    }

    /**
     * 是否包含
     *
     * @param x
     * @param y
     * @return
     */
    public boolean contain(float x, float y) {
        return mRect.contains((int) x, (int) y);
    }

    boolean isSelected = false;


    public void setSelected(boolean selected) {
        if (isSelected != selected) {
            this.isSelected = selected;
            invalidateSelf();
        }

    }


    Rect mSelectBounds = new Rect();

    public Rect getSelectBounds() {
//        return mSelectBounds;

        return mRect;
    }

    /**
     * 设置日期
     *
     * @param date
     */
    public void setDate(Calendar date) {
        mDate.setTimeInMillis(date.getTimeInMillis());
        invalidateSelf();
    }

    public Calendar getDate() {
        return mDate;
    }


    int mAlpha = 255;

    @Override
    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
        invalidateSelf();
    }
}
