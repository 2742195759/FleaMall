package com.example.homepage.Store;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.example.homepage.Account;
import com.example.homepage.MessageAsync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }
    static private int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    public static Bitmap getBitmapFromPath(String path, int width, int height) throws FileNotFoundException {
        int degree = readPictureDegree(path) ;
        BitmapFactory.Options
        return rotaingImageView(degree , BitmapFactory.decodeStream(new FileInputStream(path)));
    }
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
    // public Bitmap bitmap ; /// only restore the mCurrentP洪洞Path ;
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
                    /// restore the jpg picture in the cache ;
                    try {
                        createImageFile(); /// 获得mCurrentPhotoPath
                        result.saveImage(mCurrentPhotoPath); result.data = null ;
                        callback.callback(THIS);
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

    public boolean getBitmapInBound(int width , int height) {
        if(mCurrentPhotoPath == null) {
            return false ;
        }
        getBitmapFromPath(mCurrentPhotoPath , width , height) ;
    }
    public boolean getBitmapInBound() {
        getBitmapInBound(-1 , -1) ;
    }
}
