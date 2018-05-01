package com.example.homepage;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Message.MsgCommodityByTime;


public class HomePageActivity extends AppCompatActivity {
    //private BottomTitleLayout bottomTitleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home_page);
        ((CommodityView)findViewById(R.id.commodity_view)).setMessage(new MsgCommodityByTime(0 , 10));
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
    }
    protected void onStart() {
        super.onStart() ;

        //if(first_start) {refleshGoods(); first_start = false;}
    }
}
