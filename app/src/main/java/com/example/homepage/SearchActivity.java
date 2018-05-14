package com.example.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.homepage.View.CommodityView;
import com.example.homepage.View.Permission;

import org.w3c.dom.Text;

import Message.Message;
import Message.MsgCommodityByTime;
import Respond.RspMultiRow;

public class SearchActivity extends AppCompatActivity{
    TextView textview = null ;
    Button search_button = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
        textview = (TextView)findViewById(R.id.search_edittext) ;
        search_button = (Button) findViewById(R.id.search_button) ;
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_text = textview.getText().toString() ;
                Message msg = new MsgCommodityByTime(search_text) ;
                Intent intent = new Intent(textview.getContext() , SearchShowActivity.class) ;
                intent.putExtra("msg" , msg) ;
                startActivity(intent);
            }
        });
        Permission.verifyStoragePermissions(this) ;
    }
}
