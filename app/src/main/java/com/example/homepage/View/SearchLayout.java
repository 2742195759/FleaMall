package com.example.homepage.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.homepage.R;

/**
 * Created by xuan on 2018/3/13.
 */

public class SearchLayout extends LinearLayout{
    public SearchLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.search,this);
    }
}
