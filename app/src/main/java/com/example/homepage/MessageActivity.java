package com.example.homepage;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private List<Message1> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initMessage();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.message_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MessageAdapter adapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(adapter);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
    }

    private void initMessage() {
        for(int i=0;i<2;i++) {
            Message1 m1 = new Message1("mmmmmmmmmm1",R.drawable.m1);
            messageList.add(m1);
            Message1 m2 = new Message1("mmmmmmmmmm2",R.drawable.m2);
            messageList.add(m2);
            Message1 m5 = new Message1("mmmmmmmmmm5",R.drawable.m5);
            messageList.add(m5);
            Message1 m4 = new Message1("mmmmmmmmmm4",R.drawable.m4);
            messageList.add(m4);
            Message1 m6 = new Message1("mmmmmmmmmm6",R.drawable.m6);
            messageList.add(m6);
        }
    }


}
