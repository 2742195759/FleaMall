package com.example.homepage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CreatSellCommodity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1 ;
    Activity activity = this ;
    ImageView img ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_sell_commodity_layout);
        Button button = (Button) findViewById(R.id.photograph) ;
        img = (ImageView) findViewById(R.id.imageView) ;
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                if(ContextCompat.checkSelfPermission(activity , Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);

                }else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int req , int res , Intent data) {
        if (req == REQUEST_IMAGE_CAPTURE && res == RESULT_OK) {
            Bundle extras = data.getExtras() ;
            Bitmap image = (Bitmap) extras.get("data") ;

            img.setImageBitmap(image);
        }
    }

    @Override
    public void onRequestPermissionsResult(int req , String[] permissions ,int[] res) {
        if(REQUEST_IMAGE_CAPTURE == req) {
            if(res.length > 0 && res[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }
}
