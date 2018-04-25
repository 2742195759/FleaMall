package com.example.homepage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        //通过按钮进入修改昵称界面
        Button change_nick =(Button) findViewById(R.id.change_nick);
        change_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserUpdateActivity.this,ChangeNickActivity.class);
                startActivityForResult(intent,1);
            }
        });


    }
}
