package com.example.homepage.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homepage.Account;
import com.example.homepage.CreatSellCommodity;
import com.example.homepage.R;
import com.example.homepage.Store.Commodity;

import java.util.List;

/**
 * Created by xuan on 2018/3/13.
 * Update by Xiongkun on 2018/4/28
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    private List<Commodity> mGoodsList;
    public int holder_h , holder_w ;
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
            goodsImage = (ImageView)goodsView.findViewById(R.id.goods_image) ;
            goodsPrice = (TextView)goodsView.findViewById(R.id.goods_price) ;
            goodsTitle = (TextView)goodsView.findViewById(R.id.goods_title) ;
        }
    }
    public GoodsAdapter(List<Commodity> goodsList) {
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
        Commodity goods = mGoodsList.get(position);
        if(goods.head_photo == null) holder.goodsImage.setImageResource(R.drawable.g1);
        else  {
            Bitmap bitmap = goods.head_photo.getBitmapInBound(holder_w , holder_h) ;
            holder.goodsImage.setImageBitmap(bitmap);
            Log.d("DEBUG" , "" + bitmap.getByteCount() / 1024 + "kb") ;
        }
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
