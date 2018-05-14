package Message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Respond.Respond;
import Respond.RspMultiRow;

public class MsgCommodityByTime extends Message{
	int page_start , page_num ;
	String keyword = null ;
	public MsgCommodityByTime(int start_index , int num) {
		super(null, null); //不需要验证身份.
		page_start = start_index ;
		page_num = num ;
		// TODO Auto-generated constructor stub
	}
	public MsgCommodityByTime(String keyword) {
		super(null, null); //不需要验证身份.
		page_start = -1 ;
		page_num = -1 ;
		this.keyword = keyword ;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgCommodityByTime:\n") ;
		if(keyword != null) System.out.print("keyword:" + keyword);
		else System.out.print("keyword:" + "NULL");
	}

	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		RspMultiRow rmr = new RspMultiRow() ;
		Statement stm = conn.createStatement() ;

		String data_base_keyword = "" ;
		String limit_sql = "" ;
		if(keyword != null) data_base_keyword = String.format(" and Commodity.brief like \'%s%s%s\' ", "%" , keyword , "%") ;
		if(page_start > -1 && page_num > -1) limit_sql = String.format(
				" LIMIT %d OFFSET %d " , page_num , page_start) ;
		String sql =
				"select "
						+ "Commodity.*,sno,flea_date from Commodity,Selling "
						+ "where Commodity.cno = Selling.cno "
						+ data_base_keyword
						+ "order by flea_date desc " + limit_sql ;
		System.out.print(sql);
		ResultSet rs =
				stm.executeQuery(sql
				) ;
		rmr.AddMultiRow(rs ,"sno" , "cno" , "detail" , "brief" , "price" , "addr" , "flea_date");
		System.out.printf("size = %d\n", rmr.size()) ;
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
