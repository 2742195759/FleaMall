package com.example.homepage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.DocumentsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import Message.MsgCommodityCreateSell;
import Respond.Respond;


public class CreatSellCommodity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1 ;
    Activity activity = this ;
    ImageView img ;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView picture;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_sell_commodity_layout);
        Button ConfirmButton = (Button) findViewById(R.id.issue);
        final EditText information =  (EditText) findViewById(R.id.information);
        final EditText price =  (EditText) findViewById(R.id.price);
        final EditText address =  (EditText) findViewById(R.id.address);
        //用户发布信息确定按钮
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Information = information.getText().toString();
                String Price = price.getText().toString();
                String Address = address.getText().toString();

                new MessageAsync<Respond>( new MsgCommodityCreateSell(Account.account, null, Information, Price,
                        Address, Account.password, null)) {
                    @Override
                    public void handle_result( Respond result){

                        if(result.getState().equals("success") )
                        {
                            Toast.makeText(CreatSellCommodity.this,"发布成功",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(CreatSellCommodity.this,"发布失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }.excute();

            }
        });


        //用户选择图片按钮，会下拉出菜单选择相机或相册
        Button tack_picture = (Button) findViewById(R.id.take_picture);
        tack_picture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });


        setContentView(R.layout.creat_sell_commodity_layout);
        Button button = (Button) findViewById(R.id.photograph) ;
        img = (ImageView) findViewById(R.id.phtot_commodity) ;
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


        Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreatSellCommodity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreatSellCommodity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
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


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}
