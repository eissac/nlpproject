package cn.edu.jnu.ie.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class FileOperation {
	
	public static Vector<String> getLines(String path) throws IOException {
		// TODO Auto-generated method stub
		Vector<String> lines = new Vector<String>();
		File f = new File(path);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String s;
		while((s = br.readLine()) != null){
			lines.add(s);
		}
		br.close();
		
        return lines;
	}
	/**
	 * @param <T>
	 * @param vector
	 * @param savePath
	 * @throws IOException
	 */
	public static <T> void writeVector(Vector< T > vector,String savePath) throws IOException {
File f = new File(savePath);
		if(!f.exists())
			f.createNewFile();
        FileWriter fw = new FileWriter(f,true);
        BufferedWriter bw = new BufferedWriter(fw);
		for ( T  v : vector ) {
	        bw.write(v.toString()+"\n");
		}
		bw.close();
	}
	public static void write2txt(Vector<String> vector, String savePath) throws IOException {
		// TODO Auto-generated method stub
    	File f = new File(savePath);//
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i =0; i < vector.size(); i++){
			bw.write(vector.get(i)+"\n");
			//System.out.println(vector.get(i));
		}
		bw.close();
	}
	
	/**
	 * 把String写到本地文件,如果文件以存在则追加到文件末尾
	 * @param s
	 * @param savePath
	 * @throws IOException
	 */
	public static void writeStringAppend(String s, String savePath) throws IOException {
		// TODO Auto-generated method stub
        File f = new File(savePath);
        FileWriter fw = new FileWriter(f,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(s);
        bw.close();
	}

	public static void writeString(String s, String savePath) throws IOException {
		// TODO Auto-generated method stub
        File f = new File(savePath);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(s);
        bw.close();
	}
	/**
	 * 由html文件得到微博
	 * @param html
	 * @return
	 * @throws IOException
	 */
	public static String html2String(File f) throws IOException{
		StringBuilder html = new StringBuilder();
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String s;
		while((s = br.readLine()) != null){
			html.append(s);
		}
		br.close();
		return html.toString();
	}


	public static void deleteDir(File aDir) {
	  if (aDir.isDirectory()) {
		for (File f : aDir.listFiles()) {
		  f.delete();
		}
		aDir.delete();
	  }
	}

}
