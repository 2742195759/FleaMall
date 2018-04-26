package Message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Respond.Respond;

public class MsgAccountDelete extends Message{
	String sno = null ; 
	public MsgAccountDelete(String tsno , String pword) {
		super(tsno , pword) ; 
		sno = tsno ; 
	}
	public void print() {
		System.out.print("MsgDeleteAccout\n");
	}
	protected Respond handle(Connection conn) throws Exception {
		///因为设置了级联删除，所以不用管。
		Respond rs = new Respond() ;
		Statement stm = conn.createStatement() ;
		conn.setAutoCommit(false);
		try {
		String[] cols = {"sno"} ; 
		String[] vals = {sno} ;
		///删除Commodity,因为这个不会级联删除,所以必须要手动删除.很简单.一个语句搞定
		stm.executeUpdate(String.format(
				"delete from Commodity where cno in (select cno from Selling where sno = %s)" 
				, sno)) ; 
		String sql_stm = SqlTool.genDeleteRow("User", cols , vals) ; 
		int r = stm.executeUpdate(sql_stm) ;
		rs.setExtra("" + r);
		conn.commit();
		}
		catch(Exception e){
		conn.rollback(); 
		}
		return rs ; 
	}
	protected Respond exceptHandle(Exception e) {
		return SqlTool.normalExceptionDeal(new Respond() , e) ; 
	}
	@Override
	protected Authority[] getAuthorityArray() {
		// TODO Auto-generated method stub
		Authority[] a = { new Authority("User" , null , "W") } ;  
		return a;
	}
}
