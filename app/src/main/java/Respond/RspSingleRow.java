package Respond;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
/*
 *	服务器开发使用:insertFromResultSet函数,传入sql返回的resultset,然后是需要传递给rsp的列名字.就完成了信息的转移.
 *	客户端开发使用:	testKey() 测试key是什么类型,是NULL还是无效.
 *					getXXX() 在test之后被,如果返回KEY_IS_READY,那么可以调用,返回这个键的值.注意这类函数不做任何检测.(用户自己使用testKey函数检测,)
 *
*/
public class RspSingleRow extends Respond {
	final static int KEY_NOT_FOUND = 0 ; 
	final static int KEY_IS_NULL = 1 ; 
	final static int KEY_IS_READY = 2 ;
	final static int KEY_IS_WRONG_TYPE = 3;
	HashMap<String,Object> hmap = new HashMap<String,Object>() ;
	HashMap<String,String> is_null = new HashMap<String,String>();
	public RspSingleRow(String string) {
		// TODO Auto-generated constructor stub
		super(string) ; 
	}
	public RspSingleRow() {
		// TODO Auto-generated constructor stub
		super() ;
	}
	public int testKey(String key) { /// 
		if(is_null.get(key) != null) return KEY_IS_NULL ;  
		if(hmap.get(key) == null) return KEY_NOT_FOUND ; 
		return KEY_IS_READY ; 
	}
	public int getInt(String key) {
		return (int) hmap.get(key) ; 
	}
	public float getFloat(String key) {
		return (float) hmap.get(key) ; 
	}
	public String getString(String key) {
		return (String) hmap.get(key) ; 
	}
	public RspSingleRow insertFromResultSet(ResultSet rs , String ... s) {
		// TODO Auto-generated method stub
		try {
		for(String k : s) {
			Object x = rs.getObject(k) ; 
			if(x == null) is_null.put(k, "null") ; 
			else hmap.put(k , x) ; 
		}
		}
		catch(SQLException e) {
			state = "Server Error : FromRS have get a error string column name\n" ; 
		}
		return this ; 
	}
	@Override
	public void print() {
		System.out.print("RspSingleRow\n");
		print_state() ; 
		System.out.print(hmap);
	}
}
