package com.example.homepage;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Message.Message;
import Message.MsgLogin;
import Respond.Respond;
import Respond.RspSingleRow;

import static junit.framework.Assert.assertEquals;

public class LoginActivity extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;
    private LocalBroadcastManager localBroadcastManager;
    //private LocalReceiver localReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MineActivity.instance.finish();
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
        //获得用户输入的账户，密码
        editText1=(EditText)findViewById(R.id.edittext_account);
        editText2=(EditText)findViewById(R.id.edittext_password);
        //通过按钮跳转至注册界面
        Button RegisterButton = (Button) findViewById(R.id.button_register);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //通过按钮进行登录并销毁此活动返回上一活动
        Button LoginButton = (Button) findViewById(R.id.button_login);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得哟用户输入的账户，密码
                String account = editText1.getText().toString();
                final String password = editText2.getText().toString();
                //将用户输入的账号密码发送至服务器进行比对
                new MessageAsync<RspSingleRow>(new MsgLogin(account,password)) {
                    @Override
                    public void handle_result( RspSingleRow result){
                        //此账户存在
                        if(result.getState().equals("success") ){
                            Account.login_flag = true;
                            //获得用户的学号
                            String sno = "";
                            if(result.testKey("sno") == 2){
                                sno = result.getString("sno");
                            }
                            Account.account = sno;
                            Account.password = password;
                            if(result.testKey("nick") == 2) Account.nick = result.getString("nick") ;
                            Intent intent = new Intent(LoginActivity.this,MineActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"登录失败" ,Toast.LENGTH_SHORT).show();
                        }

                    }
                }.excute();
            }
        });
    }
}
