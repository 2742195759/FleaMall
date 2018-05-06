package com.example.homepage.Store;

import java.util.Date;

public abstract class CacheData{
    Date date = null;
    public abstract void getFromDataBase(CacheCallBack callback) ; /// get the lastest info and update data ;
    public abstract boolean   outDate () ;  /// compare the cache data with the database update date .
    public boolean   isOutDate () {
        if(date == null) return true ;
        else return outDate() ;
    }
    /// if(data == null) return true ;
}
