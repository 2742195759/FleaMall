package com.example.homepage.Store;

import android.os.Environment;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Cache {
    static String cache_picture_dir = Environment.getExternalStorageDirectory().getAbsolutePath()+
            "/Android/data/com.example.homepage/files/Cache_Picture/" ;
    static public void getCacheData(CacheKey key , CacheCallBack callback) { /// YI步，so , you need a callback to return ;
        CacheData res = key.getCacheData() ;
        if(res == null) res = key.newCacheData() ;
        if(res.outDate()) res.getFromDataBase(callback);
        else callback.callback(res);
    }
}
