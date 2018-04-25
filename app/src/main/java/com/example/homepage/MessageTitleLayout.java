package com.example.homepage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by xuan on 2018/3/14.
 */

public class MessageTitleLayout extends LinearLayout {
    public MessageTitleLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.messagetitle,this);
    }
}
