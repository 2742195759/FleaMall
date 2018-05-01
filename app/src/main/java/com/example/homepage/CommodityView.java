package com.example.homepage;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import Respond.RspImage;
import Respond.RspMultiRow;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout ;
import java.util.ArrayList;
import java.util.List;

import Message.* ;
public class CommodityView extends LinearLayout {
    Message msg ;
    private Context CONTEXT ;
    GoodsAdapter recyclerAdapter = null ;
    RecyclerView recyclerView = null ;
    WaveSwipeRefreshLayout swipe = null ;
    private List<Goods> goodsList = new ArrayList<>();
    public CommodityView (final Context context, AttributeSet attrs) {
        super(context , attrs) ;
        LayoutInflater.from(context).inflate(R.layout.layout_commodity_view,this);
        recyclerView = this.findViewById(R.id.recyclerview) ;
        setRecycleView();
        CONTEXT = context ;
        swipe = this.findViewById(R.id.main_swipe) ;
        swipe.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refleshGoods();
            }
        });
    }
    private void setRecycleView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new GoodsAdapter(goodsList);
        recyclerView.setAdapter(recyclerAdapter);
    }
    private void setRecycleViewContent() {
        recyclerAdapter.notifyDataSetChanged();
    }
    /*
        一定要是Respond是MultiRow_commodity的Msg
     */
    public CommodityView setMessage(Message msg) {
        this.msg = msg ;
        return this ;
    }
    /*
        使用Msg请求Goods并且填入属性.必要时可以获取图片.其实应该使用异步来实现,
        使用一个后台任务,一直在处理,然后把结果填入list中就可以了,因为,RecycleView会利用后台数据开始绑定.
    */
    private void refleshGoods() {
        new MessageAsync<RspMultiRow>(msg) {
            @Override
            public void handle_result(RspMultiRow result , int cnt){
                if(result != null && result.getState().equals("success")) {
                    goodsList.clear();
                    for(int i=0;i<result.size();++i) {
                        Goods good = Goods.getGoodsFromRspSingleRow(result.getSingleRow(i)) ;
                        if(good != null) goodsList.add(good);
                        if(good == null) {
                            new SweetAlertDialog(CONTEXT, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("商品解析失败")
                                    .show();
                            break ;
                        }
                    }
                    setRecycleViewContent() ;
                    Message[] msg_img = new Message[goodsList.size()] ;
                    for(int i=0;i<goodsList.size();++i) {
                        msg_img[i] = new MsgImageFetch(Account.account , Account.password ,
                                goodsList.get(i).cno , 0) ;
                    }

                    new MessageAsync<RspImage>(msg_img) {
                        @Override
                        public void handle_result(RspImage img , int cnt){
                            if(img!=null && img.getState().equals("success")) {
                                goodsList.get(cnt).head_photo = BitmapFactory.decodeByteArray(img.data,
                                        0, img.data.length);
                                recyclerAdapter.notifyItemChanged(cnt); //更新控件的图片.
                            }
                            else {
                                new SweetAlertDialog(CONTEXT, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("获取商品图片失败,下拉刷新重试")
                                        .show();
                            }
                        }
                    }.excute();
                }
                else    {
                    new SweetAlertDialog(CONTEXT, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("获取商品失败,下拉刷新重试")
                            .show();
                }
                swipe.setRefreshing(false);
            }
        }.excute() ;
    }

}
