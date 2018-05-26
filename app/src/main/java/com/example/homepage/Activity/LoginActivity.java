package com.example.homepage.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homepage.Account;
import com.example.homepage.HomePageActivity;
import com.example.homepage.MessageAsync;
import com.example.homepage.MineActivity;
import com.example.homepage.Store.Cache;
import com.univ.chat.R;
import com.univ.chat.gcm.MyPushReceiver;
import com.univ.chat.push.PushSDK;
import com.univ.chat.util.ChatApplication;
import com.univ.chat.model.InputValidator;
import com.univ.chat.model.User;

import Message.MsgAccountInfo;
import Respond.RspSingleRow;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.univ.chat.util.ChatApplication.mypushreceiver;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private TextInputLayout inputLayoutEmail;
    private Button loginButton;
    private InputValidator inputValidator;
    private Context CONTEXT = this ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cache.onInit();
        User user ;

        setContentView(R.layout.chat_module_activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);

        password = (EditText) findViewById(R.id.input_password);
        email = (EditText) findViewById(R.id.input_email);
        loginButton = (Button) findViewById(R.id.btn_enter);

        inputValidator = new InputValidator(email, inputLayoutEmail, this);
        email.addTextChangedListener(inputValidator);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        if ((user = ChatApplication.getInstance().getPrefManager().getUser()) != null) {
            password_str = user.getPassword() ;
            sno_str = user.getId() ;
            login() ;
        }
    }
    User user = null ;
    String password_str = null ;
    String sno_str = null ;
    private void login() {
        if(password_str == null) password_str = this.password.getText().toString();
        if(sno_str == null) {
            sno_str = this.email.getText().toString();
        }
        if (!inputValidator.validateSno(sno_str)) {
            return;
        }
        user = new User();
        user.setId(sno_str);
        user.setPassword(password_str);
        //将用户输入的账号密码发送至服务器进行比对
        new MessageAsync<RspSingleRow>(new MsgAccountInfo(sno_str,password_str)) {
            @Override
            public void handle_result( RspSingleRow result , int cnt ){
                //此账户存在
                if(result.getState().equals("success") ){
                    Account.login_flag = true;
                    //获得用户的学号
                    String sno = "";
                    if(result.testKey("sno") == 2){
                        sno = result.getString("sno");
                    }
                    Account.account = sno_str ;
                    Account.password = password_str ;
                    user.setPassword(password_str);
                    if(result.testKey("nick") == 2) Account.nick = result.getString("nick") ;
                    user.setName(Account.nick);
                    ChatApplication.getInstance().getPrefManager().storeUser(user);
                    Intent intent = new Intent(CONTEXT,HomePageActivity.class);
                    Toast.makeText(CONTEXT ,"登录成功" ,Toast.LENGTH_SHORT).show();
                    Context context = ChatApplication.getInstance().getApplicationContext() ;

                    mypushreceiver = new MyPushReceiver(context , ChatMainActivity.class) ;
                    PushSDK.getInstance().registerApp(context , user.getId()) ;
                    PushSDK.getInstance().registerPushListener(mypushreceiver);

                    startActivity(intent);
                    finish() ;
                }
                else if(result.getState().equals("WrongPassword")){
                    new SweetAlertDialog(CONTEXT, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Wrong password or account")
                            .show();
                }
                else {
                    new SweetAlertDialog(CONTEXT, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Some thing Wrong in server\n")
                            .show();
                }

            }
        }.excute();
    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        Cache.onFinish();
        super.onDestroy();
    }
}

