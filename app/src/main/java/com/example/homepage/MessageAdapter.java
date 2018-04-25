package com.example.homepage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xuan on 2018/3/14.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message1> mMessageList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView messageImage;
        TextView messageName;
        public ViewHolder(View view) {
            super(view);
            messageImage = (ImageView) view.findViewById(R.id.message_image);
            messageName = (TextView) view.findViewById(R.id.message_name);
        }
    }
    public MessageAdapter(List<Message1> messageList) {
        mMessageList = messageList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        Message1 message = mMessageList.get(position);
        holder.messageImage.setImageResource(message.getImageId());
        holder.messageName.setText(message.getName());
    }
    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
