package Message;

import java.sql.Statement;
import java.sql.Connection;

import Respond.Respond;
/*
 * [简介]
 * 执行一个特定的sql Update语句,注意不要乱用,主要用于调试使用.客户端使用前商议.会开发特定的接口.
 * [参数]
 * sql 需要执行的sql语句.需要对底层数据库有所了解.如果出错可以查看服务器的出错信息.
 * [返回]
 * Respond r.正常使用.
 * [注意]
 * 不要滥用,主要用于调试,如果要干别的.最好使用封装之后的Msg,因为会进行各种检验.这个主要是方便编写测试用例.
 */
public class MsgSqlStatement extends Message{
	String sql ; 
	public MsgSqlStatement(String sql) {
		super(null , null);
		this.sql = sql ; 
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgSqlStatement");
	}

	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		Statement stm = conn.createStatement() ; 
		stm.executeUpdate(sql) ; 
		return new Respond();
	}

	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Authority[] getAuthorityArray() {
		// TODO Auto-generated method stub
		return null;
	}

}
