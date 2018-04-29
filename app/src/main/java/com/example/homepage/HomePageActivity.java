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
        /*
            使用Msg请求Goods并且填入属性.必要时可以获取图片.其实应该使用异步来实现,
            使用一个后台任务,一直在处理,然后把结果填入list中就可以了,因为,RecycleView会利用后台数据开始绑定.
         */
        //if(first_start) {refleshGoods(); first_start = false;}
    }
}
