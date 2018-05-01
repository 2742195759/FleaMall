package com.example.homepage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Message.MsgAccoutUpdate;
import Respond.Respond;
import com.example.homepage.Account;

public class ChangeNickActivity extends AppCompatActivity {
    String acc = Account.account;
    String pas = Account.password;
    String new_nick_str ;
    EditText new_nick ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nick);

        //保存按钮
        Button store = (Button) findViewById(R.id.store);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_nick = (EditText) findViewById(R.id.change_nick_text);
                new_nick_str = new_nick.getText().toString();
                new MessageAsync<Respond>(new MsgAccoutUpdate(acc,pas,new_nick_str,null,null,null) ) {
                    @Override
                    public void handle_result(Respond result , int cnt) {
                        if(result.getState().equals("success") ){
                            //得到用户输入的新昵称
                            Account.nick = new_nick_str ;
                            Toast.makeText(ChangeNickActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangeNickActivity.this,MineActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(ChangeNickActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }.excute();
            }
        });
    }
}
