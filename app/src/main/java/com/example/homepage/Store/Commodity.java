package com.example.homepage.Store;

import android.graphics.Bitmap;

import com.example.homepage.MessageAsync;

import Message.MsgCommodityByCno;
import Respond.RspMultiRow;
import Respond.RspSingleRow;

/**
 * Created by Administrator on 2018/5/3.
 */

public class Commodity extends CacheData{
    public String cno ;
    public String title;
    public Picture head_photo = null ; /// head photo
    public String price  ;
    public String detail ;
    public String addr   ;
    public Commodity commodity = this ;

    public Commodity(String in_cno , String tname , String in_price , String in_detail , String in_addr) {
        this.cno = in_cno ;
        this.title = tname;
        this.price = in_price ;
        this.detail = in_detail ;
        this.addr = in_addr ;
    }
    public Commodity(String in_cno) { cno = in_cno ; }
    public boolean getAttributeFromSingleRow(RspSingleRow rsr) {
        if(rsr.testKey("brief") != 0) title = rsr.getString("brief") ;else return false ;
        if(rsr.testKey("detail") != 0) detail = rsr.getString("detail") ; else return false ;
        if(rsr.testKey("price") != 0) price = rsr.getString("price") ; else return false ;
        if(rsr.testKey("addr") != 0) addr = rsr.getString("addr") ;else return false ;
        if(rsr.testKey("cno") != 0) cno = rsr.getString("cno") ;else return false ;
        return true ;
    }

    @Override
    public void getFromDataBase(final CacheCallBack callback) {
        new MessageAsync <RspSingleRow> (new MsgCommodityByCno(cno)) {
            @Override
            public void handle_result(RspSingleRow result, int cnt) {
                commodity.getAttributeFromSingleRow(result) ;
                callback.callback(commodity);
            }
        }.excute();
    }

    @Override
    public boolean outDate() {
        return true;
    }
}
