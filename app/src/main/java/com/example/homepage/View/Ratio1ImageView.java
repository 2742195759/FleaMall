package com.example.homepage.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/5/4.
 */

public class Ratio1ImageView extends android.support.v7.widget.AppCompatImageView {
    public Ratio1ImageView(Context context) {
        super(context);
    }

    public Ratio1ImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Ratio1ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec) ;
        int height = width ;
        setMeasuredDimension(width , height);
    }

}
