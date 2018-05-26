package com.example.homepage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.support.v7.widget.SearchView;

import com.example.homepage.View.BottomTitleLayout;
import com.example.homepage.View.CommodityView;
import com.example.homepage.View.Permission;

import Message.MsgCommodityByTime;


public class HomePageActivity extends AppCompatActivity {
    //private BottomTitleLayout bottomTitleLayout;
    public void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        Account.global_width = (int) (width);  // 屏幕宽度(dp)
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getAndroiodScreenProperty() ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_home_page);
        ((CommodityView)findViewById(R.id.commodity_view)).setHolderSize(Account.global_width / 2 , Account.global_width / 2) ;
        ((CommodityView)findViewById(R.id.commodity_view)).setMessage(new MsgCommodityByTime(0 , 10)) ;
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar) ;
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.search);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        SearchView searchview = (SearchView)getSupportActionBar().getCustomView().findViewById(R.id.searchview) ;
        searchview.setIconified(true);
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
        Permission.verifyStoragePermissions(this) ;
    }
    protected void onStart() {
        super.onStart() ;
        ((BottomTitleLayout)findViewById(R.id.bottomtitle)).redrawPicture(0);
    }
}
