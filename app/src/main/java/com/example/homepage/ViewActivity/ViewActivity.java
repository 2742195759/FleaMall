package com.example.homepage.ViewActivity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.UUID;
import com.example.homepage.Activity.ActivityView ;


/**
 * Created by Administrator on 2018/5/2.
 */

/*
    这个是一个掌控了一个功能的class
 */
class ViewCounter {
    static private int cnt = 0 ;
    static public int getCounter() {
        return cnt ++ ;
    }
}

public abstract class ViewActivity extends LinearLayout{
    private int id = ViewCounter.getCounter() ;
    private boolean is_init = false ;
    public ViewActivity(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void init(int r , ActivityView activityView) {
        is_init = true ;
        setLayout(r);
        register(activityView) ;
    }
    public int getViewId() {
        return id ;
    }

    protected void register(ActivityView activityView) {
        activityView.registerView(this) ;
    }
    protected abstract void setLayout(int resource) ;

    public abstract void onRequestPermissionsResult(String[] permissions, int[] res);
}
