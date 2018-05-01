package Message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Respond.Respond;
import Respond.RspMultiRow;

public class MsgCommodityByTime extends Message{
	int page_start , page_num ; 
	public MsgCommodityByTime(int start_index , int num) {
		super(null, null); //不需要验证身份.
		page_start = start_index ; 
		page_num = num ; 
		// TODO Auto-generated constructor stub
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgCommodityByTime:\n") ; 
	}

	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		RspMultiRow rmr = new RspMultiRow() ; 
		Statement stm = conn.createStatement() ;
		ResultSet rs = 
				stm.executeQuery(String.format(
						"select "
						+ "Commodity.*,sno,flea_date from Commodity,Selling "
						+ "where Commodity.cno = Selling.cno "
						+ "order by flea_date desc LIMIT %d OFFSET %d", 
				page_num , page_start)) ;
		rmr.AddMultiRow(rs ,"sno" , "cno" , "detail" , "brief" , "price" , "addr" , "flea_date");
		return rmr ; 
	}

	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return SqlTool.normalExceptionDeal(new RspMultiRow(), e);
	}

	@Override
	protected Authority[] getAuthorityArray() {
		// TODO Auto-generated method stub
		// 读取推送的Commodity不需要任何其他的权限.
		return null;
	}

}
