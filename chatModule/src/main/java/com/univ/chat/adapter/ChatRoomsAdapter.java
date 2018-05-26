package com.univ.chat.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.univ.chat.R;
import com.univ.chat.model.Chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ViewHolder> {

    private View expended = null ;
    private int pos = 0 ;
    public boolean remove_expended() {
        if(expended != null) {
            expended.findViewById(R.id.delete_button).setVisibility(View.GONE);
            expended = null ;
            pos = 0 ;
            return true ;
        }
        return false ;
    }
    public void setExpended(View view , int pos) {
        expended = view ;
        this.pos = pos ;
    }

    private Context mContext;
    private ArrayList<Chat> chatArrayList;
    private static String today;
    static public abstract class onDeleteCallback {
        public abstract void onDelete(int pos) ;
    }
    private onDeleteCallback ondelete = null ;
    public void setOnDeleteCallback(onDeleteCallback delete) {
        ondelete = delete ;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message, timestamp, count;
        public Button del ;
        public int pos ;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            count = (TextView) view.findViewById(R.id.count);
            del = (Button) view.findViewById(R.id.delete_button) ;
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ondelete != null) ondelete.onDelete(pos);
                }
            });
        }
    }


    public ChatRoomsAdapter(Context mContext, ArrayList<Chat> chatArrayList) {
        this.mContext = mContext;
        this.chatArrayList = chatArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_module_chat_rooms_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = chatArrayList.get(position);
        holder.name.setText(chat.getName());
        holder.pos = position ;
        holder.message.setText(chat.getLastMessage());
        if (chat.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(chat.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }

        holder.timestamp.setText(getTimeStamp(chat.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ChatRoomsAdapter.ClickListener clickListener;
        private RecyclerView recyclerView ;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ChatRoomsAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            this.recyclerView = recyclerView ;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            ChatRoomsAdapter adapter = (ChatRoomsAdapter)recyclerView.getAdapter() ;
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                if(child == adapter.expended && child.dispatchTouchEvent(e)){
                    return false ; /// 直接传递给了Button_Del。否则执行ItemOnClick ;
                }
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
