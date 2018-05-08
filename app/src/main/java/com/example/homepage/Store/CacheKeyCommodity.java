package com.example.homepage.Store;

import java.util.HashMap;

import javax.sql.CommonDataSource;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CacheKeyCommodity extends CacheKey {
    public String cno ;
    @Override
    public CacheData getCacheData() {
        return table.get(this) ;
    }

    @Override
    public CacheData newCacheData() {
        Commodity data  = new Commodity(cno) ;
        insertCacheData(data);
        return data ;
    }

    public CacheKey setCno (String cno) {
        this.cno = cno ;
        return this ;
    }
}
