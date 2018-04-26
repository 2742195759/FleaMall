package Message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Respond.Respond;
/*
 * 注册的请求,返回一个Respond,state表示消息提示.[检测两次密码是否相同在客户端检测]
 */
public class MsgRegister extends Message {
	/**
	 * 
	 */
	String acc , pword ; 
	public MsgRegister(String tacc , String tpword) {
		super(tacc , null) ; ///必须要传入null,否则会导致权限检验失败.
		acc = tacc ; pword = tpword ; 
	}
	public void print() {
		System.out.print("MsgRegister:\n");
	}
	protected Respond handle(Connection conn) throws Exception {
		Respond rs = new Respond() ; 
		conn.setAutoCommit(false);
		try {
		Statement stm = conn.createStatement() ;
		ResultSet rset = stm.executeQuery(String.format("select sno from User where sno = \'%s\'" , acc)) ; 
		if(rset.next()) {
			throw new Exception("AlreadyExist") ; 
		}
		String[] cols = {"sno" , "pword"} ;
		String[] vals = {acc , pword} ; 
		String sql_stm = SqlTool.genInsert("User", cols , vals) ;
//		System.out.print(sql_stm);
		int r = stm.executeUpdate(sql_stm) ;
		rs.setExtra("" + r);
		Authority.registerAsType(stm, acc, "normal");
		conn.commit();
		}
		catch(Exception e) {
			conn.rollback();
			throw e ; 
		}
		return rs ; 
	}
	protected Respond exceptHandle(Exception e) {
		return SqlTool.normalExceptionDeal(new Respond() , e) ; 
	}
	@Override
	protected Authority[] getAuthorityArray() {
		// TODO 不需要任何权限。
		return null;
	}
}
