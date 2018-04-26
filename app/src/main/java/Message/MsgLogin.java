package Message;

import java.sql.*;

import Respond.Respond;
import Respond.RspSingleRow;
/*
 * MsgLogin:
 * description:
 * 	目前是测试用户是否存在的函数.
 * input:
 * 	acc --- 表示用户的账户(学号sno) ; 
 * 	pword --- 表示用户的密码(password) ;
 * output:
 * 	返回一个RspSingleRow rsp ; 
 * 	rsp.state.equal("success") 
 * 表示存在这个账户,并且可以使用getXXX来获取set内的信息.
 */
public class MsgLogin extends Message{
	//public static final String fail_acc_or_pword = "No such account\n" ; 
	String acc ; 
	public MsgLogin(String tacc , String tpassword) {
		super(tacc , tpassword) ;
		acc = tacc ; 
	}
	protected Respond handle(Connection conn) throws Exception{
		RspSingleRow rsr = new RspSingleRow() ; 
		SqlTool stool = new SqlTool(conn);
		Statement stm = stool.getSimpleStatement() ;
		ResultSet rs = stm.executeQuery("SELECT * from User where sno = " + acc) ;
		if(rs.next()) {
			rsr.insertFromResultSet(rs , "sno" , "addr" , "conway" , "nick") ; 
			return rsr ;
		}
		return null;  ///这个逻辑是不会走到的.
	// return new RspSingleRow(fail_acc_or_pword) ; 
	}
	public void print() {
		System.out.print("MsgLogin:\naccout"+acc);
	}
	protected Respond exceptHandle(Exception e) {
		return SqlTool.normalExceptionDeal(new RspSingleRow() , e) ; 
	}
	@Override
	protected Authority[] getAuthorityArray() {
		Authority[] a = { new Authority("User" , null , "R") } ; 
		return a;
	}
}
