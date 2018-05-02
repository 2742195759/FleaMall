package com.example.homepage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.homepage.ViewActivity.ViewActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/2.
 */

public class ActivityView extends AppCompatActivity {
    protected ArrayList<ViewActivity> va_list ;
    public void registerView(ViewActivity viewActivity) {
        va_list.add(viewActivity) ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)  ;

    }
    @Override
    public void onRequestPermissionsResult(int req , String[] permissions ,int[] res){
        for(int i=0;i<va_list.size();++i) {
            if(req == va_list.get(i).getViewId())
            va_list.get(i).onRequestPermissionsResult(permissions , res) ;
        }
    }
    @Override
    protected void onActivityResult(int req , int res , Intent data){
        for(int i=0;i<va_list.size();++i) {
            if(req == va_list.get(i).getViewId())
                va_list.get(i).onActivityResult
        }
    }
}
