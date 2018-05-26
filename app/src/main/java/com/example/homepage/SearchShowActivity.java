package com.example.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.widget.SearchView;

import com.example.homepage.View.BottomTitleLayout;
import com.example.homepage.View.CommodityView;

import Message.Message;

public class SearchShowActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_home_page);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar) ;
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.search);
        SearchView searchview = (SearchView)getSupportActionBar().getCustomView().findViewById(R.id.searchview) ;
        CommodityView commodityView = ((CommodityView)findViewById(R.id.commodity_view))   ;
        commodityView.setHolderSize(Account.global_width/2 , Account.global_width/2) ;
        Intent intent = getIntent() ;
        if(intent != null && intent.getExtras() != null && intent.getExtras().get("msg") != null) {
            commodityView.setMessage((Message)(intent.getExtras().get("msg"))) ;
        }
        searchview.clearFocus();
        searchview.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b == true) {
                    view.clearFocus();
                    Intent intent = new Intent(view.getContext() , SearchActivity.class) ;
                    startActivity(intent) ;
                }
            }
        });

        ///隐藏一些东西:
        ((BottomTitleLayout)findViewById(R.id.bottomtitle)).setVisibility(View.GONE);
    }
}
