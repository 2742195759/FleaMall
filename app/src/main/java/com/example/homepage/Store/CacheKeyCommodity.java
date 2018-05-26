package com.example.homepage.Store;

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
        return data ;
    }

    public CacheKey setCno (String cno) {
        this.cno = cno ;
        return this ;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * cno.hashCode() ;
    }

    @Override
    public boolean equals(Object obj) {
        CacheKeyCommodity com = (CacheKeyCommodity) obj ;
        return cno.equals(com.cno) ;
    }
}
