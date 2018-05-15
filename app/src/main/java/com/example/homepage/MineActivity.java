package com.example.homepage;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.homepage.View.BottomTitleLayout;
import com.example.homepage.View.CommodityView;

import Message.* ;

public class MineActivity extends AppCompatActivity {
    Button headPortraitButton;
    TextView nickButton ;
    public  static MineActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        instance=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine);
        ((CommodityView)findViewById(R.id.commodity_view)).setMessage(new MsgCommodityByTable(Account.account,
                Account.password, MsgCommodityByTable.Sell)) ;
        //昵称显示控件
        nickButton = (TextView) findViewById(R.id.nick);
        //标题栏隐去
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
        //通过按钮跳转到发布消息界面..
        ((Button) findViewById(R.id.commodity_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CommodityView)findViewById(R.id.commodity_view)).setVisibility(View.VISIBLE);
            }
        });
        //
        Button person_history_button = (Button) findViewById(R.id.person_history_button);
        person_history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this,MessageActivity.class);
                startActivity(intent);
            }
        });
        //如果没有登录过通过按钮跳转到登陆界面，登录过跳转到修改用户信息界面
        Button headPortraitButton = (Button) findViewById(R.id.head_portrait);
        headPortraitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Account.login_flag == false) {
                        Intent intent = new Intent(MineActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {

                        Intent intent = new Intent(MineActivity.this, UserUpdateActivity.class);
                        startActivity(intent);
                    }

                }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        ((BottomTitleLayout)findViewById(R.id.bottomtitle)).redrawPicture(3);
        //点击昵称按钮修改昵称
        nickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this,UserUpdateActivity.class);
                startActivity(intent);
            }
        });
        if(Account.login_flag == false){
            nickButton.setText("未登录");
        }
        else{
            if(Account.nick.equals("")){
                nickButton.setText("请更改昵称");
            }
            else{
                nickButton.setText(Account.nick);
            }
        }
    }
}


