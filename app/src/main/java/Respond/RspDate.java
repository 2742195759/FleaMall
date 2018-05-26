package Respond;

import java.io.File;
import java.util.Date;

public class RspDate extends Respond{
	public Date date ; 
	public void getDateFromFile(String path) {
		File file = new File(path) ; 
		date = new Date(file.lastModified()) ; 
	}
}
