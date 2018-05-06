package com.example.homepage;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by xuan on 2018/3/13.
 * Update by Xiongkun on 2018/4/28
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    private List<Goods> mGoodsList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        String cno = null ;
        ImageView goodsImage;
        TextView goodsPrice ;
        TextView goodsTitle ;
        View goodsView;
        public ViewHolder(View view) {
            super(view);
            //goodsImage = (ImageView)view;
            goodsView = view ;
            goodsImage = goodsView.findViewById(R.id.goods_image) ;
            goodsPrice = goodsView.findViewById(R.id.goods_price) ;
            goodsTitle = goodsView.findViewById(R.id.goods_title) ;
        }
    }
    public GoodsAdapter(List<Goods> goodsList) {
        mGoodsList = goodsList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.goodsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend = new Intent(v.getContext() , CreatSellCommodity.class) ;
                intend.putExtra("commodity" , holder.cno) ;
                v.getContext().startActivity(intend) ;
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        Goods goods = mGoodsList.get(position);
        if(goods.head_photo == null) holder.goodsImage.setImageResource(R.drawable.g1);
        else                         holder.goodsImage.setImageBitmap(goods.head_photo);
        holder.goodsTitle.setText(goods.title);
        holder.cno = goods.cno ;
        if(goods.price!=null) holder.goodsPrice.setText(goods.price+"￥");
        else holder.goodsPrice.setText("联系商议") ;
    }
    @Override
    public int getItemCount() {
        return  mGoodsList.size();
    }
}
