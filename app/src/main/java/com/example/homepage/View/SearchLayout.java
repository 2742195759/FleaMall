package com.example.homepage.View;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.homepage.R;

/**
 * Created by xuan on 2018/3/13.
 */

public class SearchLayout extends LinearLayout{
    public SearchLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.search,this);
        //(findViewById(R.id.search_button)).getLayoutParams().width = Account.global_width / 14 ;
        //(findViewById(R.id.view_block)).getLayoutParams().height = Account.global_width / 14 ;
        SearchView search = (SearchView) findViewById(R.id.searchview) ;
        search.setIconified(false);
        search.setQueryHint("输入你想要的商品");
        search.setSubmitButtonEnabled(true);
    }
}
