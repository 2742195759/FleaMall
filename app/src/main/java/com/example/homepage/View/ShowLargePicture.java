package com.example.homepage.View;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.homepage.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.homepage.View.PictureShowAdapter.actual_size;
import static com.example.homepage.View.PictureShowAdapter.pictures;

public class ShowLargePicture extends AppCompatActivity {

    //定义一个View的数组
    private List<View> views=new ArrayList<>();
    private ViewPager list_pager;

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
            views.add(imageView);
        }

        //为ViewPager设置适配器
        list_pager.setAdapter(new MyAdapter());
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

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v=views.get(position);
            container.addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v=views.get(position);
            //前一张图片划过后删除该View
            container.removeView(v);
        }


    }
}