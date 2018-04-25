package Message;

import java.sql.Connection;
import java.sql.Statement;

import Respond.Respond;
/*
 * [作用]
 * 这个是用户需要更改个人信息时发送的消息。
 * [参数]
 * 1.String acc . 表示学号(唯一标志学生).
 * 2.用户填写的个人信息。
 * 顺序是:nick[Str]{新昵称}, pword[Str]{新密码}, addr[Str]{新地址} , conway[Str]{新联系方式} ， 传入构造函数中就可以。
 * [返回]
 * 一个Respond对象,使用getState可以获得消息的消息,如果是success,表示更新成功,否则表示错误信息.
 * [注意]
 * 如果用户没有更新所有的信息，那么把维持原样(用户没有请求更新)的位置传入null .
 * [简单样例]
 * new MsgUserUpdate("201492275" , "小明","password","4舍210",null) ; 
 * 表示这个Msg会把数据库中的201492275表示的帐号设置为nick -> 小明 , pword -> "password" , addr -> 4舍 , conway -> 不变,数据库内容不会改变.  
 **/
public class MsgUserUpdate extends Message{
	String tacc = null ;
	String tn , tp , ta , tc ; 
	///如果为了版本兼容,那么只好添加额外的构造函数来实现兼容.
	public MsgUserUpdate(String acc , String pword , String nick , String newpword , String addr , String conway) {
		super(acc , pword) ; 
		tn = nick  ; tp = newpword ; ta = addr ; tc = conway ;
		tacc = acc ; 
	}
	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("[MsgUserUpdate]:\nMessage Update User Information\n");
	}
	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		Respond rsp = new Respond() ;
		String[] sid = {"nick" , "pword" , "addr" , "conway"} ; 
		String[] sval = {tn , tp , ta , tc} ; 
		String[] wid = {"sno"} ; 
		String[] wval = {tacc} ; 
		Statement stm = conn.createStatement() ;
		int r = stm.executeUpdate(SqlTool.genUpdateRow("User" , sid , sval, wid, wval)) ;
		//System.out.print(new String(SqlTool.genUpdateRow("User" , sid , sval, wid, wval).getBytes() , "UTF-8"));
		rsp.setExtra("" + r);
		return rsp;
	}
	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		Respond r = SqlTool.normalExceptionDeal(new Respond() , e) ;
		r.setState(e.getMessage());
		return r ; 
	}
	@Override
	protected Authority[] getAuthorityArray() {
		Authority[] a = { new Authority("User" , null , "W") } ;  
		return a;
	}
	
}
