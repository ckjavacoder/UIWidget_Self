package com.youloft.widget.evaluators;

import android.graphics.Rect;

import com.nineoldandroids.animation.TypeEvaluator;

/**
 * Rect动画
 * <p/>
 * Created by javen on 15/6/9.
 */
public class RectEvaluator implements TypeEvaluator<Rect> {

    private Rect mRect = new Rect();

    public RectEvaluator(Rect mSelectorRect) {
        this.mRect = mSelectorRect;
    }

    @Override
    public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
        int left = startValue.left + (int) ((endValue.left - startValue.left) * fraction);
        int top = startValue.top + (int) ((endValue.top - startValue.top) * fraction);
        int right = startValue.right + (int) ((endValue.right - startValue.right) * fraction);
        int bottom = startValue.bottom + (int) ((endValue.bottom - startValue.bottom) * fraction);
        if (mRect == null) {
            return new Rect(left, top, right, bottom);
        } else {
            mRect.set(left, top, right, bottom);
            return mRect;
        }
    }
}
