package com.example.homepage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.homepage.Store.Commodity;
import com.example.homepage.View.BottomTitleLayout;
import com.example.homepage.View.CommodityView;
import com.example.homepage.View.Permission;
import com.example.homepage.SearchActivity ;

import org.w3c.dom.ls.LSException;

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
        setContentView(R.layout.layout_home_page);
        ((CommodityView)findViewById(R.id.commodity_view)).setHolderSize(Account.global_width / 2 , Account.global_width / 2) ;
        ((CommodityView)findViewById(R.id.commodity_view)).setMessage(new MsgCommodityByTime(0 , 10)) ;
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
        TextView textview = (TextView)findViewById(R.id.search_edittext) ;
        textview.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if(b == true) {
                    Intent intent = new Intent(view.getContext() , SearchActivity.class) ;
                    startActivity(intent);
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
