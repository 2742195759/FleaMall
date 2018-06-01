package com.example.homepage.Store;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.homepage.Account;
import com.example.homepage.MessageAsync;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import Message.MsgImageDate;
import Message.MsgImageFetch;
import Respond.RspDate;
import Respond.RspImage;

/**
 * Created by Administrator on 2018/5/4.
 */

public class Picture extends CacheData {

    public abstract class FetchBitmap {
        public abstract Bitmap fetchBitmap(BitmapFactory.Options options) ;

    }
    public class FetchBitmapFromFile extends FetchBitmap {
        String file_path ;

        public FetchBitmapFromFile(String path) {
            file_path = path ;
        }

        @Override
        public Bitmap fetchBitmap(BitmapFactory.Options options) {
            Bitmap bitmap = BitmapFactory.decodeFile(file_path , options) ;
            if(options.inJustDecodeBounds == false) {
                int degree = readPictureDegree(file_path) ;
                return rotaingImageView(degree , bitmap);
            }
            else return bitmap ;
        }
    }
    public class FetchBitmapFromRes extends FetchBitmap {
        Resources res ;
        int resid ;
        public FetchBitmapFromRes(Resources res , int id) {
            resid = id ; this.res = res ;
        }

        @Override
        public Bitmap fetchBitmap(BitmapFactory.Options options) {
            return BitmapFactory.decodeResource(res , resid , options) ;
        }

        public FetchBitmapFromRes(Parcel Par)
        {
           // res = Par.readParcelable( Resources.class.getClassLoader());
            resid = Par.readInt();
        }
    }


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
    private String createImageFile() throws IOException {
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
        return image.getAbsolutePath();
    }

    // public Bitmap bitmap ; /// only restore the mCurrentP洪洞Path ;
    public int Max ;
    String cno = null ; int num ;

    public FetchBitmap fetch = null ;

    public Picture (String path) {
        fetch = new FetchBitmapFromFile(path) ;
    }
    public Picture (String cno , int num) {
        this.cno = cno ; this.num = num ;
    }
    public Picture (Resources res , int id) {
        fetch = new FetchBitmapFromRes(res , id) ;
    }

    @Override
    public void getFromDataBase(CacheCallBack callback) {
        cachecallback = callback ;
        new MessageAsync<RspImage>(new MsgImageFetch(Account.account , Account.password ,
                cno , num)) {

            @Override
            public void handle_result(RspImage result, int cnt) {
                if(result.getState().equals( "success") ) {
                    Max = result.getImageNum() ;
                    /// restore the jpg picture in the cache ;
                    try {
                        String path = createImageFile(); /// 获得mCurrentPhotoPath
                        fetch = new FetchBitmapFromFile(path) ;
                        result.saveImage(path); result.data = null ;
                        cachecallback.callback(THIS);
                    }
                    catch (Exception e) {
                        cachecallback.callback(null);
                        return ;
                    }

                }
                else cachecallback.callback(null);
            }
        }.excute();
    }

    public Bitmap getBitmapInBound(int width , int height) {
        if(fetch == null) {
            return null ;
        }
        BitmapFactory.Options options = new BitmapFactory.Options() ;
        if(width == -1 && height == -1) {
            return fetch.fetchBitmap(options) ;
        }
        Bitmap bitmap ;
        options.inJustDecodeBounds = true ;
        bitmap = fetch.fetchBitmap(options) ;
        //if(null == bitmap) return bitmap ;
        int imgw = options.outWidth ; int imgh = options.outHeight ;
        options.inSampleSize = 1 ;
        int tmp = Math.min(imgw/width , imgh/height) ; /// 由于算法会自动适应,所以不用担心.
        while(2 * options.inSampleSize < tmp) {
            options.inSampleSize *= 2 ;
        }
        options.inJustDecodeBounds  = false ;
        options.outHeight = 0 ; options.outWidth = 0 ;
        return fetch.fetchBitmap(options) ;

    }
    public Bitmap getBitmapInBound() {
        return getBitmapInBound(-1 , -1) ;
    }
    @Override
    public void getDateFromDataBase (CallBack callback) {
        datecallback = callback ;
        new MessageAsync<RspDate>(new MsgImageDate(cno , num)){

            @Override
            public void handle_result(RspDate result, int cnt) {
                if(result.success()) {
                    Date server_date = result.date ;
                    datecallback.callback(server_date);
                }
            }
        }.excute();
    }

    @Override
    protected boolean available() {
        BitmapFactory.Options options = new BitmapFactory.Options() ;
        options.inJustDecodeBounds = true ;
        Bitmap bitmap = fetch.fetchBitmap(options) ;
        return bitmap != null;
    }
    public Picture(Parcel p) {
        // 序列化自定义对象 需要传入上下文类加载器
        THIS = p.readParcelable(Thread.currentThread().getContextClassLoader());
        Max = p.readInt();
        cno = p.readString();
        num = p.readInt();
        fetch = p.readParcelable(Thread.currentThread().getContextClassLoader());
    }
}
