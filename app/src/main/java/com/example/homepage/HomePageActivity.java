package com.example.homepage;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

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
        setContentView(R.layout.layout_home_page);
        ((CommodityView)findViewById(R.id.commodity_view)).setMessage(new MsgCommodityByTime(0 , 10)) ;
        ((CommodityView)findViewById(R.id.commodity_view)).setHolderSize(Account.global_width / 2 , Account.global_width / 2) ;
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
        Permission.verifyStoragePermissions(this) ;
    }
    protected void onStart() {
        super.onStart() ;
        ((BottomTitleLayout)findViewById(R.id.bottomtitle)).redrawPicture(0);
    }
}
