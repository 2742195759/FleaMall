package Message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Respond.Respond;
import Respond.RspMultiRow;
import Respond.RspSingleRow;


/*
 * [简介]
 * 这个Message是获取系统推送的消息的.作用就是可以通知用户,但是需要用户主动申请获取Nodify.大部分的注意在[注意]中.
 * [参数]
 * sno表示请求的发起者.pword是密码.和之前的没有任何区别.
 * [返回]
 * RspMultiRow 返回多个Nodify.
 * SingleRow中的key有:
 * 1.cmd:表示cmd的类型.'cmd'定义了服务器需要客户端获取通知之后干的事情
 * 2.title:表示了给用户看(前端)的文本标题
 * 3.content:表示了用户看的内容
 * 4.flea_date:表示了通知发起的时间.(不一定和当前时间一样,因为nodify是一种信箱的形式)
 * [注意]
 * 1.因为这个是一个信箱类似的模型.所以需要用户主动的请求nodify的内容.并且获取.(推荐客户端设计一个定时器,然后一定时间获取一次通知.)
 * 2.Nodify获取之后,服务器会自动删除所哟的Nodify表示你都看了.[切记]
 * 3.cmd表示了需要干的事情,而后面两个表示了要给用户的信息.比如nodify表示只是一个通知,不需要干啥.以后会增加其他的cmd比如更新(update)啥的.
 */
public class MsgNodifyGet extends Message{
	String sno ; 
	public MsgNodifyGet(String self_no, String self_pword) {
		super(self_no, self_pword);
		sno = self_no ; 
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgGetNodity:\n") ; 
	}

	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		Statement stm = conn.createStatement() ; 
		RspMultiRow rmr = new RspMultiRow() ; 
		ResultSet rs = stm.executeQuery(String.format("select * from Nodify where sno=%s order by flea_date", sno)) ; 
		while(rs.next()) {
			RspSingleRow rsr = new RspSingleRow().insertFromResultSet(rs, "cmd" , "title" , "detail" , "flea_date") ; 
			rmr.AddSingleRow(rsr);
		}
		stm.executeUpdate(String.format("delete from Nodify where sno=%s", sno));
		return rmr;
	}

	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return SqlTool.normalExceptionDeal(new RspMultiRow(""), e);
	}

	@Override
	protected Authority[] getAuthorityArray() {
		// TODO Auto-generated method stub
		Authority[] a = { new Authority("Nodify" , null , "R") } ; 
		return a;
	}
	
}
