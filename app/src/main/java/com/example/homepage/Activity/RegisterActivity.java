package com.example.homepage.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.homepage.MessageAsync;
import com.univ.chat.R;
import com.univ.chat.util.URL;
import com.univ.chat.util.ChatApplication;
import com.univ.chat.helper.GsonHelper;
import com.univ.chat.model.InputValidator;
import com.univ.chat.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import Message.MsgRegister;
import Respond.Respond;

public class RegisterActivity extends AppCompatActivity {

    private String TAG = com.example.homepage.Activity.RegisterActivity.class.getSimpleName();
    private EditText email, password, confirmPassword, name;
    private TextInputLayout regEmailLayout,passwordLayout;
    private Button registerButton;
    private InputValidator inputValidator, passwordValidator;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ChatApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, ChatMainActivity.class));
            finish();
        }

        setContentView(R.layout.chat_module_activity_registration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        regEmailLayout = (TextInputLayout) findViewById(R.id.input_register_layout_email);
        passwordLayout = (TextInputLayout) findViewById(R.id.input_register_layout_reenter_password);

        name = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
        confirmPassword = (EditText) findViewById(R.id.register_reenter_password);
        spinner = (Spinner) findViewById(R.id.userType);

        registerButton = (Button) findViewById(R.id.register);

        inputValidator = new InputValidator(email, regEmailLayout, this);
        passwordValidator = new InputValidator(password,confirmPassword,passwordLayout,this);

        email.addTextChangedListener(inputValidator);
        confirmPassword.addTextChangedListener(passwordValidator);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        final String password = this.password.getText().toString();
        final String email = this.email.getText().toString();
        final String name = this.name.getText().toString();
        final String confirmPassword = this.confirmPassword.getText().toString();
        final String type = this.spinner.getSelectedItem().toString().toLowerCase();

        if (!inputValidator.validateSno(email)) {
            return;
        }

        if (!passwordValidator.validatePassword(confirmPassword)) {
            return;
        }
        String id = email ;
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setName(name);
        //user.setType(type);


        new MessageAsync<Respond>(new MsgRegister(id,password)) {
            @Override
            public  void handle_result(Respond result , int cnt) {
                if(result.success())
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, ChatMainActivity.class);
                startActivity(intent);
                finish();
            }
        }.excute();
    }
}
