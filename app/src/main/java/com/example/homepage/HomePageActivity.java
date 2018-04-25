package com.example.homepage;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class HomePageActivity extends AppCompatActivity {

    private List<Goods> goodsList = new ArrayList<>();
    //private BottomTitleLayout bottomTitleLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home_page);
        initGoods();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.homepage_recyclerview);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        GoodsAdapter adapter = new GoodsAdapter(goodsList);
        recyclerView.setAdapter(adapter);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
    }

    private void initGoods() {
        for(int i=0;i<2;i++) {
            Goods goods1 = new Goods("1",R.drawable.g1);
            goodsList.add(goods1);
            Goods goods2 = new Goods("2",R.drawable.g2);
            goodsList.add(goods2);
            Goods goods3 = new Goods("3",R.drawable.g3);
            goodsList.add(goods3);
            Goods goods4 = new Goods("4",R.drawable.g4);
            goodsList.add(goods4);
            Goods goods5 = new Goods("5",R.drawable.g5);
            goodsList.add(goods5);
            Goods goods6 = new Goods("6",R.drawable.g6);
            goodsList.add(goods1);
            Goods goods7 = new Goods("7",R.drawable.g7);
            goodsList.add(goods7);
            Goods goods8 = new Goods("8",R.drawable.g8);
            goodsList.add(goods8);
            Goods goods9 = new Goods("9",R.drawable.g9);
            goodsList.add(goods9);
        }
    }
}
