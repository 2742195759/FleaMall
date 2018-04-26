package Respond;
///作为一个消息的存储,使用Table作为结果存储就可以了.
///当然还要存储一些错误信息,主要是这些信息,然后共用了一个Respond.
///还有就是作为serializable的载体.
///Respond不用使用多态,只是简单的解码和编码,因为client know detail
public class Respond implements java.io.Serializable{
	///错误处理,一些;具体的内容和协议还是需要看子类的实现
	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	protected String state ;
	protected String extra ; 
	public Respond() {
		setState("success") ; 
		extra = "" ; 
	}
	public Respond(String s) {
		setState(s) ;
		extra = "" ; 
	}
	public String getState() {
		return state;
	}
	public String getExtra() {
		return extra;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setExtra(String state) {
		this.extra = state;
	}
	public Boolean success() {
		return state.equals("success") ; 
	}
	public final void print_state() {
		System.out.print("state : " + state + "\n");
		System.out.print("extra : " + extra + "\n");
	}
	public void print()	{
		System.out.print("Basic rsp class\n") ; 
		print_state() ;
	}
}