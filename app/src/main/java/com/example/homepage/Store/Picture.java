package com.example.homepage.Store;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.homepage.Account;
import com.example.homepage.MessageAsync;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Message.MsgImageFetch;
import Respond.RspImage;

/**
 * Created by Administrator on 2018/5/4.
 */

public class Picture extends CacheData{
    Picture THIS = this ;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Cache.cache_picture_dir);
        if (!storageDir.exists()) storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public String mCurrentPhotoPath ;
    public Bitmap bitmap ; /// null means not loaded ;
    public int Max ;
    String cno ; int num ;

    Picture (String cno , int num) {
        this.cno = cno ; this.num = num ;
    }
    @Override
    public void getFromDataBase(final CacheCallBack callback) {
        new MessageAsync<RspImage>(new MsgImageFetch(Account.account , Account.password ,
                cno , num)) {

            @Override
            public void handle_result(RspImage result, int cnt) {
                if(result.getState().equals( "success") ) {
                    Max = result.getImageNum() ;
                    bitmap = BitmapFactory.decodeByteArray(result.data , 0 ,
                            result.data.length) ;
                    /// restore the jpg picture in the cache ;
                    try {
                        callback.callback(THIS);
                        createImageFile(); /// 获得mCurrentPhotoPath
                        result.saveImage(mCurrentPhotoPath);
                    }
                    catch (Exception e) {
                        callback.callback(null);
                        return ;
                    }

                }
                else callback.callback(null);
            }
        }.excute();
    }

    @Override
    public boolean outDate() {
        /// Check the file.exist ;
        return true;
    }
}
