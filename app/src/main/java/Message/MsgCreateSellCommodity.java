package Message;

import java.sql.Connection;
import java.sql.Statement;

import Respond.Respond;

import Message.* ; 
import Respond.* ; 
import java.util.UUID ; 
/*
 * [说明]
 * 用户创建售卖商品的消息.需要传输需要的一切商品信息.分必填和选填
 * [参数]
 * 除了必填项,所有选填项都可以为null,表示特殊意义,详情见每一个.
 * 0.sno 表示创造的学生的学号. {必填}
 * 1.detail 表示商品的详细描述.一边介绍程度.和一些备注{选填}
 * 2.brief 表示商品的标题描述.{必填}
 * 3.price 表示出售者预定的价格. {选填/如果是Null表示互相联系}
 * 4.addr 表示可以出售的地址.{必填}{自动填入用户addr地址,如果不存在,那么一定要填写}
 * 5.password 表示创造了学生的密码 {选填，如果是null则需要更大的权限，需要可以W-Commodity}
 * [返回]
 * Respond rsp ; 
 * rsp.getState() 表示状态. success表示成功,其他表示出粗信息.
 * (int)rsp.getExtra() 表示返回的商品号码.
 * [注意]
 * (brief:<100字符,汉字表示2个字符,以后都是一样的) ;
 * (detail < 10000字符 , 汉字表示2个字符) ; 
 * (price 是字符串类型.所以一定要进行转换.) ; 
 */
public class MsgCreateSellCommodity extends Message{
	/*顺序一定要仔细,严格按照上面的,非null一定不可以为null*/
	String[] s ; 
	public MsgCreateSellCommodity(String ... informations) {
		super(informations[0] , informations[5]) ;
		s = informations ; 
		assert(s.length == 6) ; 
		assert(s[0] != null && s[1] != null && s[4] != null) ; 
	}
	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.println("[MsgCreateShellCommodity]:\n");
	}

	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		///这里使用了事务的处理,不是自动提交了.MySql如果出错,需要回滚到
		///开始的状态.
		Respond rsp = new Respond() ; 
		conn.setAutoCommit(false);
		try {
			Statement stm = conn.createStatement() ;
			//stm.executeQuery("begin") ; 
			String cno = UUID.randomUUID().toString() ; 
			String[] vals = {cno , s[1] , s[2] , s[3] , s[4]} ; 
			String[] cols = {"cno" , "detail" , "brief" , "price" , "addr"} ;
			String sql = SqlTool.genInsert("Commodity", cols , vals) ; 
			int r = stm.executeUpdate(sql) ;
			if(r == 0) rsp.setState("[Warning]:\nNo Row Have Create , Please Check Your Parameters\n");
			rsp.setExtra("" + r);
			String sell_sql = String.format("insert into Selling (cno , sno , flea_date) values (\'%s\' , \'%s\' , now())", cno , s[0]) ;
			System.out.println(sell_sql) ; 
			stm.executeUpdate(sell_sql) ; 
		}
		catch(Exception e) {
			conn.rollback();
			throw e ; 
		}
		conn.commit(); 
		return rsp;
	}

	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return SqlTool.normalExceptionDeal(new Respond() , e) ; 
	}
	@Override
	protected Authority[] getAuthorityArray() {
		// TODO Auto-generated method stub
		Authority[] a = { new Authority("Commodity" , null , "W") } ;  
		return a;
	}
}
