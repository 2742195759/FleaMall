package com.example.homepage;


import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.homepage.Store.Cache;
import com.example.homepage.Store.CacheCallBack;
import com.example.homepage.Store.CacheData;
import com.example.homepage.Store.CacheKeyCommodity;
import com.example.homepage.Store.CacheKeyPicture;
import com.example.homepage.Store.Commodity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.example.homepage.Store.Picture;
import java.util.ArrayList;
import java.util.List;


public class CommodityDetailsActivity extends AppCompatActivity {

    ImageView imageview;
    TextView price,information,address;
    Commodity commodity = null;
    private ViewPager viewPager;  //对应的viewPager
    private List<View> list_view;
    private MyAdapter adpter;
    int max;
    static int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_details);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null) {
            actionbar.hide();
        }
        price = (TextView) findViewById(R.id.price);
        information = (TextView) findViewById(R.id.information);
        address = (TextView) findViewById(R.id.address);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        list_view = new ArrayList<>();

        if(getIntent().getExtras() != null) {
            final String cno = (String) getIntent().getExtras().get("commodity");
            //获得商品图片信息
            Cache.getCacheData(new CacheKeyPicture().setCno(cno).setNum(0), new CacheCallBack() {
                @Override
                public void callback(CacheData data) {
                    Picture picture = (Picture) data;
                    max = picture.Max;
                    //Toast.makeText(CommodityDetailsActivity.this,max,Toast.LENGTH_SHORT).show();
                    for(i = 0;i < max;i++){
                        Cache.getCacheData(new CacheKeyPicture().setCno(cno).setNum(i), new CacheCallBack() {
                            @Override
                            public void callback(CacheData data) {
                                Picture picture = (Picture) data;
                                if (picture == null) {
                                    imageview = new ImageView(MyApplication.getContext());
                                    imageview.setImageResource(R.drawable.g1);
                                }
                                else{
                                    imageview = new ImageView(MyApplication.getContext());
                                    Bitmap bitmap = picture.getBitmapInBound(50 , 50) ;
                                    imageview.setImageBitmap(bitmap);
                                }
                                list_view.add(imageview);
                                if(list_view.size() == max){
                                    adpter = new MyAdapter(list_view);
                                    viewPager.setAdapter(adpter);
                                }
                            }
                        });
                    }
                }
            });
            //获得商品文字信息
            Cache.getCacheData(new CacheKeyCommodity().setCno(cno), new CacheCallBack() {
                @Override
                public void callback(CacheData data) {
                    commodity = (Commodity) data;
                    price.setText("￥" + commodity.price);
                    information.setText(commodity.title);
                    address.setText(commodity.addr);
                }
            });
        }
    }
    public class MyAdapter extends PagerAdapter {
        private List<View> list_view;
        public MyAdapter(List<View> list_view) {
            this.list_view = list_view;
        }
        @Override
        public int getCount() {
            if (list_view != null && list_view.size() > 0) {
                return list_view.size();
            } else {
                return 0;
            }
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = list_view.get(position % list_view.size());
            if (container!=null) {
                container.removeView(v);
            }
            container.addView(v);
            return v;
        }
    }
}

