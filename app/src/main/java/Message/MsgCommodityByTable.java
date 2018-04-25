package Message;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Message.* ;
import Respond.Respond;
import Respond.RspMultiRow;
import Respond.RspSingleRow; 
/* [简述]
 * 获取用户对应的Commodity的消息
 * [参数]
 * 1.sno表述需要查看的用户的商品信息(必填)
 * 2.pword表示需要的密码,如果如果为null表示请求的是别人的信息.(选填)
 * 3.type表示这个用户请求的商品类型.比如Selling,Loving,Requireing等.
 * 
 * [返回值]
 * RspMultiRow rmr ; 
 * rmr.size()表示总共行数; 
 * rmr.getSingleRow(int index).可以获取第index行的RspSingleRow. 
 * [注意]
 * 客户端需要自己进行一些筛选.比如价格啥的.这样可以减轻服务器的负担,使响应更加快,但是会浪费流量.(还需要讨论)[DISCUSS]
 * 如果需要额外的pattern表示筛选的正则表达式.使用数据库的筛选机制.  %和_
 */
public class MsgCommodityByTable extends Message{
	String sno ; 
	String table_type ; 
	static final public String Sell = "Selling" ; 
	static final public String Require = "Requireing" ;
	static final public String Love = "Loving" ;
	public MsgCommodityByTable(String tsno , String tpword , String type) {
		super(tsno , tpword) ; 
		table_type = type ; 
		sno = tsno ;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgGetCommodity:\n");
	}

	@Override
	protected Respond handle(Connection conn) throws Exception {
		Statement stm = conn.createStatement() ; 
		String sql = String.format("select Commodity.* from User,%s,Commodity where User.sno=%s.sno and Commodity.cno=%s.cno and User.sno=%s",table_type,table_type,table_type,sno) ; 	
		ResultSet rs = stm.executeQuery(sql)  ; 
		RspMultiRow rmr = new RspMultiRow() ; 
		while(rs.next()) {
			RspSingleRow rsr = new RspSingleRow().insertFromResultSet(rs, "cno" , "detail" , "brief" , "price" , "addr" , "head_photo") ; 
			if(!rsr.getState().equals("success")) throw new Exception(rsr.getState());
			rmr.AddSingleRow(rsr) ; 
		}
		return rmr ; 
	}

	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return SqlTool.normalExceptionDeal(new RspMultiRow(""), e);
	}

	@Override
	protected Authority[] getAuthorityArray() {
		Authority[] a = { new Authority("Commodity", null , "R") } ;  ///需要对商品的可读权限,以后还可以有个筛选啥的.可以作为另一个权限的表现.
		return a;
	}
}
