package com.example.homepage;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.example.homepage.View.CommodityView;
import com.example.homepage.View.Permission;

import org.w3c.dom.Text;

import Message.Message;
import Message.MsgCommodityByTime;
import Respond.RspMultiRow;

public class SearchActivity extends AppCompatActivity{
    SearchView searchview  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
        searchview = (SearchView)findViewById(R.id.searchview) ;
        SearchView search = (SearchView) findViewById(R.id.searchview) ;
        search.setIconified(false);
        search.setQueryHint("输入你想要的商品");
        search.setSubmitButtonEnabled(true);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String search_text = query ;
                Message msg = new MsgCommodityByTime(search_text) ;
                Intent intent = new Intent(searchview.getContext() , SearchShowActivity.class) ;
                intent.putExtra("msg" , msg) ;
                startActivity(intent);
                return true ;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        Permission.verifyStoragePermissions(this) ;
    }
}
