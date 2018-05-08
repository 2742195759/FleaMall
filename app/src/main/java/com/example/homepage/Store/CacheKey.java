package com.example.homepage.Store;

import java.util.HashMap;

/*
    封装了key的数据结构，和获取类型。
 */
public abstract class CacheKey {
    static HashMap<CacheKey , CacheData> table = new HashMap<CacheKey , CacheData>();
    public abstract CacheData getCacheData() ; /// null means not found ;
    public abstract CacheData newCacheData();
    public void insertCacheData(CacheData data) {
        table.put(this , data) ;
    }
}
