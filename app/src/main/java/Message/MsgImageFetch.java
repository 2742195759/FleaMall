package Message;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import Respond.Respond;
import Respond.RspImage;

public class MsgImageFetch extends Message{
	String sno , pword , cno; 
	int pno ;
	public MsgImageFetch(String sno, String pword , String cno , int pno) {
		super(sno, pword);
		this.cno = cno ;
		this.pno = pno ; 
		// TODO Auto-generated constructor stub
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgImageFetch\n");
	}

	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		RspImage img = new RspImage(Message.getPhotePath(cno , pno) , pno) ;
		Statement stm = conn.createStatement() ; 
		if(pno == 0) {
			ResultSet rs = stm.executeQuery(String.format("select photo_num as size from Commodity "
					+ "where cno = \'%s\'" , cno)) ; rs.next() ;
			img.size = rs.getInt("size") ;
		}
		return img ; 
	}

	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return SqlTool.normalExceptionDeal(new RspImage(), e);
	}
	@Override
	protected boolean isOwner(Connection conn, String no2) throws Exception { 
		// TODO Auto-generated method stub
		return conn.createStatement().executeQuery(String.format("select * from Selling where sno="
				+ "%s and cno=%s"
				, no2 , cno)).next();
	}
	@Override
	protected Authority[] getAuthorityArray() {
		// TODO Auto-generated method stub
		Authority[] a = { new Authority("Commodity", null , "R") } ;  ///需要对商品的可读权限,以后还可以有个筛选啥的.可以作为另一个权限的表现.
		return a;
	}
}
