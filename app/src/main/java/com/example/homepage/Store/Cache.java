package com.example.homepage.Store;

import android.os.Environment;

import com.univ.chat.push.lib.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;

/*
    TODO:
    由于HashMap内部实现是保存引用，所以当程序退出之后，虽然可以复原HashMap但是由于内存中引用不见的原因，所以
    还是没有作用．因此需要把Picture和KeyPicture存入文件，然后生成HashMap才可以实现正真的可持久化．这个可以
    在下一步中继续．很关键哦
 */
public class Cache {
    static String cache_picture_dir = Environment.getExternalStorageDirectory().getAbsolutePath()+
            "/Android/data/com.example.homepage/files/Cache_Picture/" ;

    static public void getCacheData(CacheKey key ,CacheCallBack callback_client) { /// YI步，so , you need a callback to return ;
        if(!isInit) LogUtil.e("Tag" , "You have not init the cache , please init it before you " +
                "use it.");
        CacheData res = null ;
        res = key.getCacheData() ;
        if(res == null) res = key.newCacheData() ;
        res.isOutDate(
                new CacheData.CallBack() {
            @Override
            public void callback(Object o) {
                Date server_date = (Date) o;
                getCacheData().cnt_date = server_date ;
                getCacheData().getFromDataBase(new CacheCallBack() {
                    @Override
                    public void callback(CacheData data) {
                        getCacheKey().insertCacheData(data);
                        getCacheCallBack().callback(data);
                    }
                });
            }
        }.setCacheCallBack(callback_client).setCacheKey(key).setCacheData(res),

                new CacheData.CallBack() {
            @Override
            public void callback(Object o) {
                getCacheCallBack().callback(getCacheData());
            }
        }.setCacheCallBack(callback_client).setCacheData(res)) ;
    }

    static boolean isInit = false ;
    public static void onInit() {
        isInit = true ;
        ///从文件中读取自己table．
        try {
            String cachefile = cache_picture_dir + "cache_table";
            File file = new File(cachefile);
            if (!file.exists()) return ;
            FileInputStream fis = new FileInputStream(file.getAbsolutePath()) ;
            ObjectInputStream ois = new ObjectInputStream(fis) ;
            CacheKey.table = (HashMap<CacheKey, CacheData>) ois.readObject() ;
        }
        catch(Exception e) {
            LogUtil.w("Cache" , "Read Cache File Wrong\n");
            return ;
        }
    }
    public static void onFinish() {
        try{
            String cachefile = cache_picture_dir + "cache_table";
            File file = new File(cachefile);
            if(!file.exists()) file.createNewFile() ;
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile()) ;
            ObjectOutputStream oos = new ObjectOutputStream(fos) ;
            oos.writeObject(CacheKey.table);
        }
        catch(Exception e) {
            LogUtil.w("Cache" , "Wrong on Write Cache File\n");
        }
    }
}
