package com.example.homepage.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.homepage.R;

public class ShowLargePicture extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_large_picture);
        Intent intent = getIntent();
        Bitmap picture=intent.getParcelableExtra("picture");
        ImageView img = (ImageView)this.findViewById(R.id.large_image );
        img.setImageBitmap(picture);

        /*String picName;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_large_picture);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            picName = bundle.getString("picName"); //图片名
        }
        ImageView img = (ImageView)this.findViewById(R.id.large_image );
        ImageDownloader.download( picName,img);
        Toast toast = Toast.makeText(this, "点击图片即可返回", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
        img.setOnClickListener(new View.OnClickListener() { // 点击返回
            public void onClick(View paramView) {
                finish();
            }
        });*/
    }
}
