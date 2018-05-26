package com.example.homepage.Store;

import java.util.Date;
public abstract class CacheData implements Cloneable{
    static abstract class CallBack {
        public Object arg1 , arg2  ;
        public Object cachedata ;
        public abstract void callback(Object o) ;
        public CallBack setCacheKey(CacheKey k) {arg1 = k ; return this ; }
        public CallBack setCacheCallBack(CacheCallBack k) {arg2 = k ; return this ; }
        public CallBack setCacheData(CacheData k) {cachedata = k ; return this ; }
        public CacheData getCacheData() {return (CacheData)cachedata ; }
        public CacheKey getCacheKey() {return (CacheKey) arg1 ; }
        public CacheCallBack getCacheCallBack() {return (CacheCallBack) arg2 ; }

    }


    CacheCallBack cachecallback = null ;
    CallBack datecallback = null;
    //CacheData THIS = this ;
    public Date cnt_date = null;
    public abstract void getFromDataBase(CacheCallBack callback) ; /// get the lastest info and update data ;
    public abstract void getDateFromDataBase (CallBack callback) ;  /// compare the cache data with the database update date .
    public void  isOutDate (CallBack outdata , CallBack notoutdata) {
        CallBack cb = new CallBack() {
            @Override
            public void callback(Object o) {
                Date server_date = (Date) o ;
                if(cnt_date == null || cnt_date.before(server_date)) {
                    ((CallBack)arg1).callback(server_date);
                }
                else {
                    ((CallBack)arg2).callback(server_date);
                }
            }
        } ;
        cb.arg1 = outdata  ;
        cb.arg2 = notoutdata ;
        getDateFromDataBase(cb) ;
    }

    protected abstract boolean available();
}
