package com.example.homepage;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import Respond.RspMultiRow;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout ;
import java.util.ArrayList;
import java.util.List;

import Message.* ;
public class CommodityView extends LinearLayout {
    Message msg ;
    private Context CONTEXT ;
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
    }
    private void setRecycleViewContent() {
        GoodsAdapter adapter = new GoodsAdapter(goodsList);
        recyclerView.setAdapter(adapter);
    }
    /*
        一定要是Respond是MultiRow_commodity的Msg
     */
    public void setMessage(Message msg) {
        this.msg = msg ;
    }
    private void refleshGoods() {
        new MessageAsync<RspMultiRow>(msg) {
            @Override
            public void handle_result(RspMultiRow result){
                if(result != null && result.getState().equals("success")) {
                    goodsList.clear(); ;
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
                }
                else    {
                    new SweetAlertDialog(CONTEXT, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("获取商品失败,点击|重试|")
                            .show();
                }
                swipe.setRefreshing(false);
            }
        }.excute() ;
    }

}
