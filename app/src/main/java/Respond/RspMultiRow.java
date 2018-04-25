package Respond;
///检验的时候只需要检查RspMultiRow的state就可以,不需要考虑内部的SingleRow的提示(他们没有用)
public class RspMultiRow extends Respond{
	RspSingleRow[] arr_rsp = null ; 
	int max_size = 5; 
	int size = 0 ; 
	public RspMultiRow(String str) {
		super(str) ;
		if(str.equals("success"))
		arr_rsp = new RspSingleRow[max_size] ; 
	}
	public RspMultiRow() {
		arr_rsp = new RspSingleRow[max_size] ; 
	}
	public RspSingleRow getSingleRow(int i) {
		return arr_rsp[i] ; 
	}
	public void AddSingleRow(RspSingleRow rsr) {
		if(size == max_size) {
			max_size *= 2 ; 
			RspSingleRow[] now = new RspSingleRow[max_size] ; 
			for(int i=0;i<size;++i) {
				now[i] = arr_rsp[i] ; 
			}
			arr_rsp = now ; 
		}
		arr_rsp[size++] = rsr ; 
	}
	public int size() {
		return size ; 
	}
}
