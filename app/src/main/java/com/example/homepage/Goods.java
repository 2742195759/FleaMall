package com.example.homepage;

import Respond.RspMultiRow ;
import Respond.RspSingleRow ;
/**
 * Created by xuan on 2018/3/13.
 */

public class Goods {
    public String cno ;
    public String title;
    public int head_photo; /// head photo
    public String price  ;
    public String detail ;
    public String addr   ;
    public Goods(String in_cno , String tname, int timageId , String in_price , String in_detail , String in_addr) {
        this.cno = in_cno ;
        this.title = tname;
        this.head_photo = timageId;
        this.price = in_price ;
        this.detail = in_detail ;
        this.addr = in_addr ;
    }
    public Goods() {}
    public boolean getAttributeFromSingleRow(RspSingleRow rsr) {
        if(rsr.testKey("brief") != 0) title = rsr.getString("brief") ;else return false ;
        if(rsr.testKey("detail") != 0) detail = rsr.getString("detail") ; else return false ;
        if(rsr.testKey("price") != 0) price = rsr.getString("price") ; else return false ;
        if(rsr.testKey("addr") != 0) addr = rsr.getString("addr") ;else return false ;
        if(rsr.testKey("cno") != 0) cno = rsr.getString("cno") ;else return false ;
        return true ;
    }
    static public Goods getGoodsFromRspSingleRow(RspSingleRow rsr) {
        Goods goods = new Goods() ;
        if(goods.getAttributeFromSingleRow(rsr)) return goods ;
        return null ;
    }
}
