package Message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Respond.Respond;
import Respond.RspSingleRow;

public class MsgCommodityByCno extends Message { ///不需要权限
	String cno ; 
	public MsgCommodityByCno(String tcno) {
		super(null, null);
		cno = tcno ; 
		// TODO Auto-generated constructor stub
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgCommodityByCno:\n") ; 
	}

	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		Statement stm = conn.createStatement() ; 
		ResultSet rs = stm.executeQuery(String.format("select * from Commodity where cno = \'%s\'", cno)) ; 
		RspSingleRow rsr = new RspSingleRow() ; 
		return rsr.insertFromResultSet(rs, "cno" , "detail" , "brief" , "price" , "addr") ;
	}

	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return SqlTool.normalExceptionDeal(new RspSingleRow(), e);
	}

	@Override
	protected Authority[] getAuthorityArray() {
		// TODO Auto-generated method stub
		// 不需要权限.
		return null;
	}

}
