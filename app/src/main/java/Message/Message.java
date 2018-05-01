package Message;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Respond.Respond;
import Respond.RspMultiRow;
class Authority{
	static private String[] authority_string = {"1_" , "_1"} ;
	
	
	public String type; ///String [User][Comment][Commodity][Nodify] , 一个约束只可以有一个范围。这样便于管理，增加限制可以简化问题。
	public int authority = 0 ;///低位->高位		R W
	public String UUID ;
	///eg : 111 / 110 等
	public String other ; ///自动填充，不需要设置。
	Authority(String ttype , String tUUID , String tauthority){
		setAuthority(tauthority) ;
		UUID = tUUID ; 
		type = ttype ; 
	}
	void print() {
		System.out.print(type);
		System.out.print(authority);
		System.out.print(other);
	}
	//注意aut的格式是
	///生成者调用的。R W分别表示读写
	public String getArea() {
		if(type == "User") return "1___" ; 
		if(type == "Comment") return "_1__" ; 
		if(type == "Commodity") return "__1_" ;
		if(type == "Nodity") return "__1_" ;
		return "____" ; 
	}
	public void setAuthority(String s) {
		for(int i=0;i<s.length();++i) {
			if(s.charAt(i)=='R') authority |= 0x1 ; 
			if(s.charAt(i)=='W') authority |= 0x2 ; 
		}
	}
	public boolean checkSingleStep(Statement stm , String no , int index) throws Exception {
		String sql = String.format("select sno from U_P_A where sno like %s and Area like \'%s\' and other like %s and priority like \'%s\' ", no , getArea() , other , authority_string[index]) ;
		if(stm.executeQuery(sql).next()) return true ; 
		if(UUID == null) return false ; ///如果UUID为null表示直接失败。
		sql = String.format("select sno from U_P_L where sno like %s and type like \'%s\' and other = %s and priority like \'%s\' and UUID = %s" , no , type , other , authority_string[index] , UUID) ;
		if(stm.executeQuery(sql).next()) return true ; 
		return false ;
	}
	public boolean check(Connection conn  , String no) throws Exception {
		print() ; 
		Statement stm = conn.createStatement() ; 
		String sql ; int tmp = authority ;  
		for(int i=0;tmp>0;++i) {
			if((tmp & 0x1) == 1) {
				if(checkSingleStep(stm , no , i) == false) return false ;  
			}
			tmp >>= 1 ; 
		}
		return true ; 
	}
	public static void registerAsType(Statement stm  , String no , String type) throws Exception {
		String sql = null ; 
		if(type.equals("normal")) { ///普通用户的权限.
			sql = String.format("insert into U_P_A (sno , priority , area , other) values (%s,\'11\',\'1111\',\'0\')", no) ; 
			stm.executeUpdate(sql) ; 
			sql = String.format("insert into U_P_A (sno , priority , area , other) values (%s,\'10\',\'1110\',\'1\')", no) ; 
			stm.executeUpdate(sql) ;
		}
		else if(type.equals("root") ) { /// 超级管理员的权限.
			sql = String.format("insert into U_P_A (sno , priority , area , other) values (%s,\'11\',\'1111\',\'0\')", no) ; 
			stm.executeUpdate(sql) ; 
			sql = String.format("insert into U_P_A (sno , priority , area , other) values (%s,\'11\',\'1111\',\'1\')", no) ; 
			stm.executeUpdate(sql) ;
		}
	}
}

public abstract class Message implements java.io.Serializable{
	/**
	 * 
	 */
	//static final String server_ip = "127.0.0.1" ;
	//static final int port = 3511 ;
	//static final String image_path = "/home/xopngkun/桌面/image/" ; 
	//static final String path_delm = "/" ; 
	static final String server_ip = "211.159.180.189" ;
	static final int port = 3511 ;
	static final String image_path = "C:\\image\\" ;
	static final String path_delm = "\\" ; 
	protected static String getPhotePath(String cno , int pno) {
		return String.format(image_path + "%s%s%03d.jpg", cno , path_delm , pno) ; 
	}
	protected static void createPhoteDir(String cno) {
		File dir = new File(image_path + cno) ;    
		if(!dir.exists()) {
			dir.mkdirs() ; 
		}
	}
	private String no , pword ; ///表示发起人的sno和pword身份。
	public Message(String self_no , String self_pword) {
		no = self_no ; pword = self_pword ; 
	}
	public final Respond sendAndReturn() {
		Respond res = null ; /// 返回的Rsp.
		try {
			Socket sock = new Socket(server_ip , port) ;
			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream()) ;
			ObjectInputStream ois = new ObjectInputStream(sock.getInputStream()) ;
			oos.writeObject(this);
			res = (Respond) ois.readObject() ;
			sock.close() ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res ; 
	}
	private final boolean checkAuthorityArray(Connection conn, Authority[] ats) throws Exception{
		///如果没有条目，表示验证成功，不需要任何权限。
		if(ats == null || ats.length == 0) return true ;
		for(Authority a : ats) {
			///根据传入的参数和用户名来验证。是否要进行other/self的读写。
			///如果pword==null则相对的r / w -> or , ow ; 需要的权限一般更高。
			///如果pword!=null则r / w 改写为 sr , sw ; 表示对自己修改。
			if(pword == null || no == null) 	a.other = "1" ;
			else if((isOwner(conn , no)))a.other = "0" ; 
			if(a.check(conn , no) == false) 
				return false ; 
		}
		return true ;
	}	
	protected boolean isOwner(Connection conn, String no2) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}
	private static final long serialVersionUID = 0001L;
	public final Respond wrapHandle(Connection conn) {
		///使用handle作为虚函数.作为msg类的多态处理信息.
		try {
			///加入CheckPassword是为了防止非法入侵。和软件外部模拟的Message。
			if(checkPassword(conn) == false) throw new Exception("WrongPassword") ;  
			if(checkAuthorityArray(conn , getAuthorityArray()) == false) throw new Exception("AccessDenied") ; 
			return handle(conn) ; 
		}catch(Exception e) {
			Respond r = exceptHandle(e) ;
			///显示下列的名字。
			if(e.getMessage().equals("WrongPassword")) 
				r.setState("WrongPassword"); 
			if(e.getMessage().equals("AccessDenied")) 
				r.setState("AccessDenied");
			if(e.getMessage().equals("AlreadyExist")) 
				r.setState("AlreadyExist");
			return r ; 
		}
	}
	///必须override的方法
	private boolean checkPassword(Connection conn) throws Exception{
		if(pword == null) return true ; 
		if(no == null) return false ; 
		Statement stm = conn.createStatement() ;
		String sql = String.format("select sno from User where sno=\'%s\' and pword=\'%s\'", no , pword) ;
		ResultSet rs = stm.executeQuery(sql) ;
		return rs.next() ; 
	}
	static public void NodifyUser(ResultSet rs , Statement stm , String cmd , String title , String content) throws Exception{
		ArrayList lis = new ArrayList();
		while(rs.next()) {
			lis.add(rs.getString("sno")) ; 
		}
		for(int i=0;i<lis.size();++i) {
			String[] id = {"sno" , "cmd" , "title" , "content" , "flea_date"} ; 
			String[] val = {lis.get(i).toString() , cmd , title , content , "now()"} ; 
			System.out.print(SqlTool.genInsert("Nodify", id, val));
			stm.execute(SqlTool.genInsert("Nodify", id, val)) ; 
		}
	}
	public abstract void print() ;
	protected abstract Respond handle(Connection conn) throws Exception ;///使用handle作为虚函数.作为msg类的多态处理信息.
	protected abstract Respond exceptHandle(Exception e) ; ///出错了之后的处理.常规是: new Respond 然后
	protected abstract Authority[] getAuthorityArray() ;
	public static void swapImage(Connection conn, String path, String photoPath) throws Exception {
		// TODO Auto-generated method stub
		// A 的内容变为 B ; 文件实现重排.
		File A = new File(path) ; File B = new File(photoPath) ; 
		if(!A.exists() || !B.exists()) throw new Exception("FileError") ;
		File Aold = new File(path+".old") ; File Bold = new File(photoPath + ".old") ;
		if(Aold.exists()) Aold.delete() ; 
		A.renameTo(Aold) ; A.delete() ; 
		B.renameTo(A) ; 
		//数据库改变
		Statement stm = conn.createStatement() ; 
		
		
	}
}