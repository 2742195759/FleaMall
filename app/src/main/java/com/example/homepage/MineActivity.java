package com.example.homepage;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import Message.* ;
import Respond.* ;

public class MineActivity extends AppCompatActivity {
    Button nickButton,headPortraitButton;
    public  static MineActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        instance=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mine);
        //昵称显示控件
        nickButton = (Button) findViewById(R.id.user_name);
        //标题栏隐去
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
        //通过按钮跳转到发布消息界面
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
                nickButton.setText("昵称");
            }
            else{
                nickButton.setText(Account.nick);
            }
        }
    }

    //登陆界面销毁后此活动重启调用
   /* @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    //获得用户登录后的学号
                    user_sno = data.getStringExtra("user_sno");
                    //将用户学号传到更改昵称的活动
                    Intent intentnick = new Intent(MineActivity.this,ChangeNickActivity.class);
                    intentnick.putExtra("nick",user_sno);
                    nickButton.setText(user_sno);
                }break;
            default:
        }
    }*/
}


