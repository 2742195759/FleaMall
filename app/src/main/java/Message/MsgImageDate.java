package Message;

import java.sql.Connection;

import Respond.Respond;
import Respond.RspDate;

public class MsgImageDate extends Message{
	private String cno ; 
	int pno ; 
	public MsgImageDate(String cno , int pno) {
		super(null, null);
		this.cno = cno ; this.pno = pno ; 
	}
	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.println("MsgImageDate\n");
	}
	@Override
	protected Respond handle(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		String path = Message.getPhotePath(cno , pno) ; 
		RspDate rsp = new RspDate() ; 
		rsp.getDateFromFile(path);
		return rsp;
	}
	@Override
	protected Respond exceptHandle(Exception e) {
		// TODO Auto-generated method stub
		return SqlTool.normalExceptionDeal(new RspDate() , e);
	}
	@Override
	protected Authority[] getAuthorityArray() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
