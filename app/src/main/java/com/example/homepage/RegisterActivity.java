package com.example.homepage;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Respond.*;
import Message.*;

import static junit.framework.Assert.assertEquals;

public class RegisterActivity extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
        editText1=(EditText)findViewById(R.id.edittext_number);
        editText2=(EditText)findViewById(R.id.edittext_password_confirm_1);
        editText3=(EditText)findViewById(R.id.edittext_password_confirm_2);

        Button ConfirmButton = (Button) findViewById(R.id.button_confirm);
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = editText1.getText().toString();
                String password_1 = editText2.getText().toString();
                String password_2 = editText3.getText().toString();
                if(password_1.equals(password_2)){

                    new MessageAsync<Respond>(new MsgRegister(account,password_1)) {
                        @Override
                        public  void handle_result(Respond result) {
                            if(result.success())
                                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            /*else
                                Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();*/
                        }
                    }.excute();

                }
                else {
                    String inputtext = "两次密码不一致";
                    Toast.makeText(RegisterActivity.this,inputtext,Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button CancelButton = (Button) findViewById(R.id.button_cancel);
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
