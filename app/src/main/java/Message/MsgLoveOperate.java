package Message;

import java.sql.Connection;
import java.sql.Statement;

import Respond.Respond;
/*
 * [简述]
 * 这个msg是一个处理Love的msg,包含了create,delete.因为简单,所以写在了一起.
 * [参数]
 * 1.self_no 表示处理的用户.
 * 2.self_pword 表示处理的用户密码啊.如果是null表示是处理他人的.
 * 3.tcno 表示选中的商品.和sno ,cno构造了
 * 4.type 可以是create也可以是delete.
 * [返回]
 * 返回一个简单的Respond,
 * [注意]
 * 没啥注意的.
 */
public class MsgLoveOperate extends Message{
	static public String create = "c" ; 
	static public String delete = "d" ; 
	String acc , cno ; 
	String type ; 
	public MsgLoveOperate(String self_no, String self_pword ,String tcno , String ttype) {
		super(self_no, self_pword); 
		acc = self_no ; cno = tcno ; 
		type = ttype ; 
		// TODO Auto-generated constructor stub
	}
	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgLoveOperate:" + type);
	}
	@Override
	protected Respond handle(Connection conn) throws Exception {
		
		Statement stm = conn.createStatement() ; 
		if(type.equals(create) ) {
			String[] id = {"sno" , "cno" , "flea_date"} ; 
			String[] val = {acc , cno , "now()"} ;
			String sql = SqlTool.genInsert("Loving", id, val) ; 
		
			stm.executeUpdate(sql) ; 
		}
		else if(type.equals(delete)) {
			String[] id = {"sno" , "cno" } ; 
			String[] val = {acc , cno } ;
			stm.executeUpdate(SqlTool.genDeleteRow("Loving", id, val)) ; 
		}
		Respond rs = new Respond() ; 
		return rs;
	}
	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return SqlTool.normalExceptionDeal(new Respond(), e);
	}
	@Override
	protected Authority[] getAuthorityArray() {
		Authority[] a = { new Authority("Commodity" , null , "W") } ;   //需要商品的写权限.
		return a;
	}
	
	
}
