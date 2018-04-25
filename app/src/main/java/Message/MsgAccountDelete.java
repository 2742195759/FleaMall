package Message;

import java.sql.Connection;
import java.sql.Statement;

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
		String[] cols = {"sno"} ; 
		String[] vals = {sno} ; 
		String sql_stm = SqlTool.genDeleteRow("User", cols , vals) ;  
		int r = stm.executeUpdate(sql_stm) ;
		rs.setExtra("" + r);
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
