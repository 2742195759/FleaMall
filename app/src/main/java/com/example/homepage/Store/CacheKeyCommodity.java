package com.example.homepage.Store;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CacheKeyCommodity extends CacheKey {
    static HashMap<CacheKeyCommodity , Commodity> table = new HashMap<CacheKeyCommodity , Commodity>();
    public String cno ;
    @Override
    public CacheData getCacheData() {
        return table.get(this) ;
    }

    @Override
    public CacheData newCacheData() {
        Commodity data  = new Commodity(cno) ;
        table.put(this , data) ;
        return data ;
    }
    public CacheKey setCno (String cno) {
        this.cno = cno ;
        return this ;
    }
}
