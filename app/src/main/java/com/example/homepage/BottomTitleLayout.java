package com.example.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by xuan on 2018/3/13.
 */

public class BottomTitleLayout extends LinearLayout {

    private Button bottomtitle_message;
    private Button bottomtitle_homepage;
    private Button bottomtitle_mine;

    public BottomTitleLayout(final Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_title,this);
        Button bottomtitle_homepage = (Button) findViewById(R.id.Bottom_button_1);
        Button bottomtitle_creat_commodity = (Button) findViewById(R.id.Bottom_button_2);
        Button bottomtitle_mine = (Button) findViewById(R.id.Bottom_button_3);

        bottomtitle_creat_commodity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CreatSellCommodity.class);
                context.startActivity(intent);
            }
        });
        bottomtitle_homepage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,HomePageActivity.class);
                context.startActivity(intent);
            }
        });
        bottomtitle_mine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MineActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
