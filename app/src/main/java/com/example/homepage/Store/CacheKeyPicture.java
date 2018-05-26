package com.example.homepage.Store;

/**
 * Created by Administrator on 2018/5/4.
 */

public class CacheKeyPicture extends CacheKey {
    public String cno ;
    public int num    ;
    @Override
    public CacheData getCacheData() {
        return table.get(this) ;
    }

    @Override
    public CacheData newCacheData() {
        Picture data = new Picture(cno , num) ;
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

    @Override
    public int hashCode() {
        return super.hashCode() * cno.hashCode() * num ;
    }

    @Override
    public boolean equals(Object obj) {
        CacheKeyPicture pic = (CacheKeyPicture) obj ;
        return cno.equals(pic.cno) && num == pic.num ;
    }
}
