package com.example.homepage.Store;

import java.util.Date;
import java.util.HashMap;

/*
    封装了key的数据结构，和获取类型。
 */
public abstract class CacheKey {
    static HashMap<CacheKey , CacheData> table = new HashMap<CacheKey , CacheData>();
    public abstract CacheData getCacheData() ; /// null means not found ;
    public abstract CacheData newCacheData();
    public void insertCacheData(CacheData data) {
        if(data.cnt_date == null) data.cnt_date = new Date() ;
        table.put(this , data) ;
    }
}
