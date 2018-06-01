package com.example.homepage.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.homepage.Account;
import com.example.homepage.MessageAsync;
import com.example.homepage.R;
import com.example.homepage.Store.Cache;
import com.example.homepage.Store.CacheCallBack;
import com.example.homepage.Store.CacheData;
import com.example.homepage.Store.CacheKeyCommodity;
import com.example.homepage.Store.CacheKeyPicture;
import com.example.homepage.Store.Commodity;
import com.example.homepage.Store.Picture;
import com.google.gson.JsonObject;
import com.univ.chat.helper.GsonHelper;
import com.univ.chat.model.Comm;
import com.univ.chat.util.ChatApplication;
import com.univ.chat.util.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Message.MsgCommodityOff;
import Message.MsgCommodityOwner;
import Respond.*;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class CommodityDetailsActivity extends AppCompatActivity {

    ImageView imageview;
    TextView price,information,address;
    Commodity commodity = null;
    private ViewPager viewPager;  //对应的viewPager
    private MyAdapter adpter;
    List<View>list_view = null ;
    int max;
    private String cno ;
    private RspSingleRow user ;
    static int i;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.commodity_detail_menu , menu);
        return true;
    }

    private void createCommAndStartChatActivity () {
        Comm com = new Comm() ;
        com.setFrom(Account.account);
        com.setTo(user.getString("sno"));
        String url = String.format(URL.CREATE_COMMUNICATIONS);
        org.json.JSONObject body = new org.json.JSONObject() ;
        try {
            body = GsonHelper.toJsonObject(com) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(CommodityDetailsActivity.this , "创建成功" ,
                        Toast.LENGTH_SHORT) .show();
                Intent intent = new Intent(CommodityDetailsActivity.this , ChatRoomActivity.class) ;
                intent.putExtra("chat_room_id" , user.getString("sno")) ;
                intent.putExtra("name" , user.getString("nick")) ;
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CommodityDetailsActivity.this , "Wrong in create comm" ,
                        Toast.LENGTH_SHORT) .show();
            }
        }) ;
        ChatApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    private void offCommodity () {
        new MessageAsync<Respond>(new MsgCommodityOff(Account.account ,
                Account.password , cno)){

            @Override
            public void handle_result(Respond result, int cnt) {
                if(result.getState().equals("success")) {
                    Toast.makeText(CommodityDetailsActivity.this , "success" ,
                            Toast.LENGTH_SHORT).show();
                    finish() ;
                }
            }
        }.excute();
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_delete :
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定删除吗?").setCancelText("取消").setConfirmText("确认删除")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        offCommodity() ;
                        finish() ;
                    }
                }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
                break ;
            case R.id.action_favorite : break  ;
            case R.id.action_update : break ;
            case R.id.action_chat :
                if(user.getString("sno").equals(Account.account)) {
                    Toast.makeText(CommodityDetailsActivity.this , "" +
                            "不可以和自己聊天" , Toast.LENGTH_SHORT).show();
                }
                else
                    createCommAndStartChatActivity();
                break ;
            default:
        }
        return true ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_details);
        ActionBar actionbar = getSupportActionBar() ;
        price = (TextView) findViewById(R.id.price);
        information = (TextView) findViewById(R.id.information);
        address = (TextView) findViewById(R.id.address);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.getLayoutParams().height = Account.global_width ;
        viewPager.getLayoutParams().width = Account.global_width ;
        if(getIntent().getExtras() != null) {
            cno = (String) getIntent().getExtras().get("commodity");
            //获得商品图片信息
            new MessageAsync<RspSingleRow>(new MsgCommodityOwner(cno)){
                @Override
                public void handle_result(RspSingleRow result, int cnt) {
                    if(result.getState().equals("success")) {
                        user = result;
                    }
                    else {
                        Toast.makeText(CommodityDetailsActivity.this , "获取商品" +
                                "owner failed",Toast.LENGTH_SHORT).show() ;
                    }
                }
            }.excute();
            list_view = new ArrayList<>() ;
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
                                ImageView imageview = null ;
                                Picture picture = (Picture) data;
                                if (picture == null) {
                                    imageview = new ImageView(CommodityDetailsActivity.this);
                                    imageview.setImageResource(R.drawable.g1);
                                }
                                else{
                                    imageview = new ImageView(CommodityDetailsActivity.this);
                                    Bitmap bitmap = picture.getBitmapInBound(Account.global_width, Account.global_width) ;
                                    imageview.setImageBitmap(bitmap) ;
                                }
                                ViewPager.LayoutParams l = new ViewPager.LayoutParams() ;
                                l.width = l.height = Account.global_width ;
                                imageview.setLayoutParams(l);
                                list_view.add(imageview);
                                if(list_view.size() == max){
                                    adpter = new MyAdapter(list_view);
                                    viewPager.setAdapter(adpter);
                                    list_view = null ;
                                    adpter = null ;
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
            container.removeView((View)object);
            //list_view.remove(position) ;
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

