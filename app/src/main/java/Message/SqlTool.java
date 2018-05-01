package Message;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Respond.Respond;

public class SqlTool {
	//public static final String T_User="User" ; 
	protected Connection conn = null ; 
	public SqlTool (Connection c) {
		conn = c ;
	}
	public Statement getSimpleStatement() {
		try {
			return conn.createStatement() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	///这个是发送一个state为"Wrong have happen in server"的消息.
	static public Respond normalExceptionDeal(Respond rsp , Exception e) {
		e.printStackTrace(); 
		rsp.setState("Wrong have happen in server");
		return rsp ; 
	}
	
	///接下来是生成sql语句的命令.
	///插入table中value(o[2i] = o[2i+1]) ; 
	static public String genInsert(String table , String[] id , String[] val) {
		int len = id.length ;
		assert(len == val.length) ;  
		String col = "", vals ="";
		int start = 0 ;
		for(int i=0;i<len;++i) {
			if(val[i] == null) continue ;
			if(start > 0) {col += " , " ; vals += " , " ;}
			++start ;
			col += id[i] ;
			if(val[i].toString().equals("now()")) {
				vals += String.format("%s", val[i].toString()); 
			}
			else 
			vals += String.format("\'%s\'", val[i].toString()); 
		}
		return String.format("insert into %s (%s) values (%s);", table , col , vals) ; 
	}
	///删除table中where o[2*i] = o[2*i+1] ; 
	static public String genDeleteRow(String table , String[] id , Object[] val) {
		int len = id.length ;
		assert(len == val.length) ;
		String where = "";
		int start = 0 ;
		for(int i=0;i<len;++i) {
			if(val[i] == null) continue ;
			if(start > 0) {where += " and " ;}
			++start ;
			where += id[i] + " = " + String.format("\'%s\'", val[i].toString());  ; 
		}
		return String.format("delete from %s where %s ;", table , where) ; 
	}
	///生成EqualStatement , S[i] = o[i] , S[i+1] = o[i+1]  ....
	static public String genEqualStatement(String[] id , Object[] val , String delm) {
		int len = id.length ; 
		assert(len == val.length) ;
		String ret = "" ; 
		int start = 0 ; 
		for(int i=0;i<len;++i) {
			if(val[i] == null) continue ; 
			if(start != 0) ret += String.format(" %s ", delm) ; 
			ret += id[i] + " = \'" + val[i].toString() + '\''; 
			start = 1 ; 
		}
		return ret ; 
	}
	///delete
	static public String genUpdateRow(String table , String[] set_id , Object[] set_val , String[] where_id , Object[] where_val) {
		String set = genEqualStatement(set_id , set_val , ",") ; 
		String where = genEqualStatement(where_id , where_val , "and") ; 
		return String.format("update %s set %s where %s", table , set , where) ;
	}
}