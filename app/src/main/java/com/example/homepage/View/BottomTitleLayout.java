package com.example.homepage.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.homepage.CreatSellCommodity;
import com.example.homepage.HomePageActivity;
import com.example.homepage.MineActivity;
import com.example.homepage.R;

/**
 * Created by xuan on 2018/3/13.
 */

public class BottomTitleLayout extends LinearLayout {
    static int cnt_state = -1 ;


    //static BottomTitleState global_state = new BottomTitleState();
    ButtonSingle[] bottoms = new ButtonSingle[4];

    public BottomTitleLayout(final Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_title,this);
        bottoms[0] = (ButtonSingle) findViewById(R.id.Bottom_button_1) ;
        bottoms[1] = (ButtonSingle) findViewById(R.id.Bottom_button_2);
        bottoms[2] = (ButtonSingle) findViewById(R.id.Bottom_button_3);
        bottoms[3] = (ButtonSingle) findViewById(R.id.Bottom_button_4);
        bottoms[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CreatSellCommodity.class);
                context.startActivity(intent);
            }
        });
        bottoms[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,HomePageActivity.class);
                context.startActivity(intent);
            }
        });
        bottoms[3].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MineActivity.class);
                context.startActivity(intent);
            }
        });
        for(int i=0;i<bottoms.length;++i) bottoms[i].setId(i);
    }
    public void redrawPicture(int state) {
        if(cnt_state != -1) bottoms[cnt_state].unSelected();
        cnt_state = state ;
        bottoms[cnt_state].onSelected();
    }
}
