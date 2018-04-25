package Message;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;


/* [简述]
 * 这个message的作用就是卖家下架自己的商品,也就是从售卖改为不买了.
 * 允许的原因:1.卖家不想卖了.   2.卖家已经卖出去了.
 * [参数]
 * self_no 表示发起请求的人的sno(必填)
 * self_pword 表示发起请求人的密码,如果是null表示不是主人发起的请求.(可选)
 * tcno 表示需要下架的商品的cno(必填)
 * [返回]
 * Respond rs . 表示成功还是否 . getstate()
 * [注意]
 * 这个函数使用之后会给User发送Nodify.
 * 这个nodify可以通知那些对这个商品具有依赖关系的用户,比如添加了收藏等的用户.
 * 具体作用见
 * MsgGetNodify类.
 */
import Respond.Respond;
///也就是删除当前的Commodity但是显示是下架.
public class MsgCommodityOff extends Message{
	String sno , cno ; 
	public MsgCommodityOff(String self_no, String self_pword , String tcno) {
		super(self_no, self_pword);
		assert(self_no != null && tcno != null) ; 
		sno = self_no ; cno = tcno ; 
		// TODO Auto-generated constructor stub
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgOffCommodity\n");
	}

	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub/
		// 有待优化,现在是表示下架就是删除.其实可以存储的.
		// 以后来改进,当前改进思路是:添加一个列表示下架了.然后如果下架了让客户端处理.哈哈.其实只需要删除联系就可以了.
		conn.setAutoCommit(false);
		Respond rsp = new Respond() ; 
		try {
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery(String.format("select sno from Loving where cno=%s", cno)) ; 
		Message.NodifyUser(rs, stm, "nodify", "收藏物品下架", String.format("你收藏的商品已经下架,已经为您删除了"));
		String[] d_c_id = {"cno"} ; 
		String[] d_c_val = {cno} ; 
		stm.execute(SqlTool.genDeleteRow("Commodity", d_c_id , d_c_val));
		stm.execute(SqlTool.genDeleteRow("Selling", d_c_id , d_c_val));
		conn.commit();
		}
		catch(Exception e) {
			conn.rollback();
			throw e  ;
		}
		return rsp;
	}

	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return SqlTool.normalExceptionDeal(new Respond(), e);
	}

	@Override
	protected Authority[] getAuthorityArray() {
		// TODO Auto-generated method stub
		Authority[] a = { new Authority("Commodity" , null , "W") } ;  
		return a;
	}
	
}
