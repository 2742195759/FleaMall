package Respond;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RspImage extends Respond{ ///一个表示一个图片.
	public byte[] data = null ;
	public int size ; ///000必定是有的,所以可以获得size.
	public int pno = -1 ; ///路径
	public RspImage setPno(int x) {
		pno = x ; 
		return this ; 
	}
	public int size() {
		if(data == null) return -1 ; 
		return data.length ; 
	}
	public int getImageNum() {
		return size ; 
	}
	public RspImage() {
	}
	public RspImage(int pno) {
		this.pno = pno ; 
	}
	public RspImage(byte[] data , int pno) {
		this.data = data ; 
		this.pno = pno ; 
	}
	public RspImage(String path , int pno) throws IOException {
		this.pno = pno ; 
		getImage(path) ; 
	}
	public void getImage(String path) throws IOException {
		FileInputStream fis = new FileInputStream(path) ; 
		data = new byte[fis.available()] ; 
		fis.read(data) ; 
		System.out.print("RspImage.java:45\nFetch Image in " + path);
	}
	public void saveImage(String path) throws IOException {
		FileOutputStream fos = new FileOutputStream(path) ; 
		fos.write(data);
		System.out.print("RspImage.java:45\nSave Image in " + path);
		fos.close(); 
	}
}
