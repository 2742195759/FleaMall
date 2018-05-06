package com.example.homepage.Store;

/*
    封装了key的数据结构，和获取类型。
 */
public abstract class CacheKey {
    public abstract CacheData getCacheData() ; /// null means not found ;
    public abstract CacheData newCacheData();
}
