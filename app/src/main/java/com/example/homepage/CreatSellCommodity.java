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

import com.example.homepage.Store.Cache;
import com.example.homepage.Store.CacheCallBack;
import com.example.homepage.Store.CacheData;
import com.example.homepage.Store.CacheKey;
import com.example.homepage.Store.CacheKeyCommodity;
import com.example.homepage.Store.Commodity;
import com.example.homepage.View.BottomTitleLayout;
import com.example.homepage.View.PictureShowView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import Message.MsgCommodityCreateSell;
import Message.MsgImageSave;
import Respond.*;
import com.example.homepage.View.PictureShowView ;


public class CreatSellCommodity extends AppCompatActivity {
    Activity activity = this ;
    PictureShowView img ;
    static final int REQUEST_IMAGE_CAPTURE = 1 ;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView picture ;
    private Uri imageUri ;
    EditText information ;
    EditText price ;
    EditText address ;
    Goods goods = null ;
    Commodity commodity = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.creat_sell_commodity_layout);
        information =  (EditText) findViewById(R.id.information);
        price =  (EditText) findViewById(R.id.price);
        address =  (EditText) findViewById(R.id.address) ;
//用户选择图片按钮，会下拉出菜单选择相机或相册
        //Button tack_picture = (Button) findViewById(R.id.take_picture);

        if(getIntent().getExtras() != null) {
            String cno = (String)getIntent().getExtras().get("commodity");
            Cache.getCacheData(new CacheKeyCommodity().setCno(cno), new CacheCallBack() {
                    @Override
                public void callback(CacheData data) {
                    commodity = (Commodity) data ;
                    information.setText(commodity.title);
                    price.setText(commodity.price);
                    }
            }) ;

        }

        Button ConfirmButton = (Button) findViewById(R.id.issue);
        //用户发布信息确定按钮
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            static final int ERROR_PRICE_FORMAT = -1 ;
            static final int ERROR_INFORMATION_NULL = -2 ;
            static final int ERROR_IMAGE_NUMBER = -3 ;
            @Override
            public void onClick(View v) {
                int error = 0 ;
                do {
                    String Information = information.getText().toString();
                    String Price = price.getText().toString();
                    String Address = address.getText().toString();
                    if (Price.equals("") || Information.equals("") || Address.equals("")) {
                        error = ERROR_INFORMATION_NULL ; break ;
                    }
                    else if(img.getPictureNumber() == 0) {
                        error = ERROR_IMAGE_NUMBER ; break ;
                    }
                    else {
                        try {
                            int k = Integer.parseInt(Price);
                        } catch (NumberFormatException e) {
                            error = ERROR_PRICE_FORMAT;
                            break;
                        }
                        new MessageAsync<Respond>(new MsgCommodityCreateSell(Account.account, null,
                                Information, Price, Address, Account.password, null)) {
                            @Override
                            public void handle_result(Respond result, int cnt) {
                                if (result.getState().equals("success")) {
                                    Toast.makeText(CreatSellCommodity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreatSellCommodity.this, HomePageActivity.class);
                                    startActivity(intent);
                                    //RspImage[] rsp_image= new RspImage[img.getPictureNumber()] ;
                                    MsgImageSave msgSave = new MsgImageSave(Account.account ,
                                            Account.password , result.getExtra()) ;
                                    for(int i=0;i<img.getPictureNumber();++i) {
                                        msgSave.addImage(new RspImage(img.getPictureByteArray(i)
                                        , -1)) ; /// substitute all the image ;  Not provided change order faculty
                                    }
                                    new MessageAsync<Respond>(msgSave){
                                        @Override
                                        public void handle_result(Respond result, int cnt) {
                                            if(!result.getState().equals("success")) {
                                                Toast.makeText(CreatSellCommodity.this, "图片发布失败", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                    }.excute();
                                } else {
                                    Toast.makeText(CreatSellCommodity.this, "发布失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.excute();
                    }
                }while(false) ;
                /// Error Deal
                switch (error) {
                    case ERROR_INFORMATION_NULL :
                        Toast.makeText(CreatSellCommodity.this, "发布失败:请填写必填字段", Toast.LENGTH_SHORT).show(); break ;
                    case ERROR_PRICE_FORMAT :
                        Toast.makeText(CreatSellCommodity.this, "发布失败:填写正确的Price，必须是数字", Toast.LENGTH_SHORT).show(); break ;
                    case ERROR_IMAGE_NUMBER :
                        Toast.makeText(CreatSellCommodity.this, "发布失败:必须要一张图片", Toast.LENGTH_SHORT).show(); break ;
                }
            }
        });

        img = (PictureShowView) findViewById(R.id.picture_show_list) ;
        img.setActivity(this);
    }

    @Override
    protected void onActivityResult(int req , int res , Intent data) {
        if (req == REQUEST_IMAGE_CAPTURE && res == RESULT_OK) {
            img.onTakePicture();
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
    protected void onStart() {
        super.onStart() ;
        ((BottomTitleLayout)findViewById(R.id.bottomtitle)).redrawPicture(1);
    }

}
