package Message;

import java.sql.Connection;
import java.sql.ResultSet;

import Respond.Respond;
import Respond.RspSingleRow;

public class MsgCommodityOwner extends Message{
	String cno ;  
	public MsgCommodityOwner(String cno) {
		super(null , null);
		// TODO Auto-generated constructor stub
		this.cno = cno ; 
	}
	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgOwner");
	}
	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		RspSingleRow rsr = new RspSingleRow() ; 
		java.sql.Statement stm = conn.createStatement() ; 
		ResultSet rs = stm.executeQuery(String.format("select User.* from User , Selling where User.sno like Selling.sno and"
				+ " Selling.cno like \'%s\'", cno)) ; 
		if(rs.next()) {
			rsr.insertFromResultSet(rs, Message.user_query_column) ; 
		}
		else {
			rsr.setState("Commodity Not Found");
		}
		return rsr;
	}
	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return SqlTool.normalExceptionDeal(new RspSingleRow(), e);
	}
	@Override
	protected Authority[] getAuthorityArray() {
		// TODO Auto-generated method stub
		return null;
	}

}
