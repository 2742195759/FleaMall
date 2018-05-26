package Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import Respond.Respond;
import Respond.RspImage;
import java.sql.Statement; 

public class MsgImageSave extends Message{
	String cno , sno , pword ; 
	ArrayList<RspImage> image = new ArrayList<RspImage>(); 
	public MsgImageSave(String sno , String pword , String cno) {
		super(sno , pword); 
		this.cno = cno ; 
		this.sno = sno ; 
		this.pword = pword ; 
		// TODO Auto-generated constructor stub
	}
	public MsgImageSave addImage(RspImage img) {
		image.add(img) ;
		return this ; 
	}
	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.print("MsgImageSave\n") ; 
	}
	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		Respond r = new Respond() ; 
		conn.setAutoCommit(false);
		try {
		Statement stm = conn.createStatement() ;
		Message.createPhoteDir(cno) ; 
		int next = 0;
		ResultSet rs = stm.executeQuery(String.format("select photo_num as size from Commodity "
				+ "where cno = \'%s\'" , cno)) ; rs.next() ;  
		int old_size = rs.getInt("size") ; 
		for(int i=0;i<image.size();++i) {
			RspImage rimg = image.get(i) ; 
			String path = Message.getPhotePath(cno, i) ; 
			if(rimg.pno == -1) {
				rimg.saveImage(path) ;
			}
			else if(rimg.pno >= 0){
				Message.swapImage(conn , path , Message.getPhotePath(cno,
						rimg.pno)) ; 
			}
		}
		String[] set_id = {"photo_num"} ; Object[] set_val = {image.size()} ; 
		String[] where_id = {"cno"} ; Object[] where_val = {cno} ;
		stm.executeUpdate(SqlTool.genUpdateRow("Commodity", set_id, set_val, where_id, where_val)) ;
		///删除之前的.
		for(int i=image.size();i<old_size;++i) {
			File file = new File(Message.getPhotePath(cno, i)) ; 
			if(file.exists()) file.delete();
		}
		conn.commit();
		}
		catch(Exception e) {
			conn.rollback();
			throw e ; 
		}
		return r;
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
	
	@Override
	protected boolean isOwner(Connection conn, String no2) throws Exception { 
		// TODO Auto-generated method stub
		return conn.createStatement().executeQuery(String.format("select * from Selling where sno="
				+ "\'%s\' and cno=\'%s\'"
				, no2 , cno)).next();
	}

}
