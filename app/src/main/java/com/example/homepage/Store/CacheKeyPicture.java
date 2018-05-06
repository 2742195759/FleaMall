package com.example.homepage.Store;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/4.
 */

public class CacheKeyPicture extends CacheKey {
    static HashMap<CacheKeyPicture , Picture> table = new HashMap<CacheKeyPicture , Picture>();
    public String cno ;
    public int num    ;
    @Override
    public CacheData getCacheData() {
        return table.get(this) ;
    }

    @Override
    public CacheData newCacheData() {
        Picture data = new Picture(cno , num) ;
        table.put(this , data) ;
        return data ;
    }

    public CacheKeyPicture setCno (String cno) {
        this.cno = cno ;
        return this ;
    }
    public CacheKeyPicture setNum(int num) {
        this.num = num ;
        return this ;
    }
}
