package com.example.homepage.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.homepage.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.homepage.View.PictureShowAdapter.actual_size;
import static com.example.homepage.View.PictureShowAdapter.pictures;

public class ShowLargePicture extends AppCompatActivity {

    private ViewPager list_pager;
    private List<View> list_view;
    private viewpageAdapter adpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*标题栏 隐去*/
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
        int n= getIntent().getIntExtra("extra_int",0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_large_picture);
        list_pager = (ViewPager)findViewById(R.id.large_viewpager);
        list_view = new ArrayList<>();

        int j=0;
        for(int i=0;i<actual_size;i++)
        {
            ImageView imageView=new ImageView(this);
            if(i<actual_size-n)
            {
                imageView.setImageBitmap(pictures.get(n+i).getBitmapInBound());
            }
            else{
                imageView.setImageBitmap(pictures.get(j++).getBitmapInBound());
            }
            list_view.add(imageView);
        }
        adpter = new viewpageAdapter(list_view);
        list_pager.setAdapter(adpter);
        // 刚开始的时候 吧当前页面是先到最大值的一半 为了循环滑动
        int currentItem = Integer.MAX_VALUE / 2;
        // 让第一个当前页是 0
        currentItem = currentItem - ((Integer.MAX_VALUE / 2) % actual_size);
        list_pager.setCurrentItem(currentItem);

        list_pager.setOnTouchListener(new View.OnTouchListener() {
            int touchFlag = 0;
            float x = 0, y = 0;
            ViewConfiguration configuration = ViewConfiguration.get(getApplicationContext());
            float mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchFlag = 0;
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xDiff = Math.abs(event.getX() - x);
                        float yDiff = Math.abs(event.getY() - y);
                        if (xDiff < mTouchSlop && xDiff >= yDiff)
                            touchFlag = 0;
                        else
                            touchFlag = -1;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (touchFlag == 0) {
                            finish();
                        }
                        break;
                }
                return false;
            }
        });
    }

    public class viewpageAdapter extends PagerAdapter {
        private List<View> list_view;
        public viewpageAdapter(List<View> list_view) {
            this.list_view = list_view;
        }
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list_view.get(position % list_view.size()));
            return list_view.get(position % list_view.size());
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list_view.get(position % list_view.size()));
        }
    }
}