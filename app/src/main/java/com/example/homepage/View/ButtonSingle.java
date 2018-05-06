package com.example.homepage.View;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.homepage.R;

/**
 * Created by Administrator on 2018/5/5.
 */

public class ButtonSingle extends LinearLayout {
    static final int[] selected_image = {R.drawable.home1 , R.drawable.add1
            , R.drawable.message1 , R.drawable.man1 } ;
    static final int[] unselected_image = {R.drawable.home0 , R.drawable.add0
            , R.drawable.message0 , R.drawable.man0 } ;
    static final String[] text = {"主页" , "发布" , "消息" , "个人中心"} ;

    ImageView img = null;
    TextView textView = null ;


    int id = 0 ;
    public ButtonSingle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.buttom_single , this) ;
        img = (ImageView) this.findViewById(R.id.imageView) ;
        textView = (TextView)this.findViewById(R.id.textView) ;
    }
    public void setImageResource(int res) {
        img.setImageResource(res);
    }
    ///Init Method ;
    public void setId(int id) {
        this.id = id ;
        textView.setText(text[this.id]);
        unSelected() ;
    }
    public void onSelected() {
        img.setImageResource(selected_image[id]);
        ((TextView)this.findViewById(R.id.textView)).setTextColor(getResources().getColor(R.color.buttom_single_text_color));
    }
    public void unSelected() {
        img.setImageResource(unselected_image[id]);
        ((TextView)this.findViewById(R.id.textView)).setTextColor(Color.parseColor("#ff000000"));
    }
}
