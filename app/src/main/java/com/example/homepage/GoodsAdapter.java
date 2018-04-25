package com.example.homepage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by xuan on 2018/3/13.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    private List<Goods> mGoodsList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImage;
       // View goodsView;
        public ViewHolder(View view) {
            super(view);
            //goodsView = view;
            goodsImage = (ImageView) view.findViewById(R.id.goods_image);
        }
    }
    public GoodsAdapter(List<Goods> goodsList) {
        mGoodsList = goodsList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        /*holder.goodsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Goods goods = mGoodsList.get(position);
                Toast.makeText(v.getContext(),"you clicked view"+goods.getName(),Toast.LENGTH_SHORT).show();
            }
        });*/
        holder.goodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Goods goods = mGoodsList.get(position);
                Toast.makeText(v.getContext(),"you clicked image"+goods.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        Goods goods = mGoodsList.get(position);
        holder.goodsImage.setImageResource(goods.getImageId());
    }
    @Override
    public int getItemCount() {
        return  mGoodsList.size();
    }
}
