package cn.edu.jnu.ie;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import cn.edu.jnu.ie.backend.HTMLParser;
import cn.edu.jnu.ie.backend.WeiboIndexer;
import cn.edu.jnu.ie.util.Constant;
import cn.edu.jnu.ie.util.FileOperation;
import cn.edu.jnu.ie.util.Weibo;

public class ClassifierTest {
	public static void writeWeiboFile2txt(String[] args) throws IOException{
       String[] searchwords = Constant.SEARCHWORDS;
   	 WeiboIndexer wi = new WeiboIndexer();
   	 HTMLParser htmlparser = new HTMLParser();
	    for (int n = 0; n < searchwords.length; n++) {
	        String searchword = searchwords[n];
	        String dirPath = "data/weibo/getweibo/20150308/"+searchword;
	        String savePath ="data/weibo/saveweibo/weibostxt/20150312/"+searchword;
			htmlparser.writeWeibo2txt(searchword, dirPath, savePath);
	        /*if(dirPath.exists()){
	            File[] files = dirPath.listFiles();
	            for (File file :files) {
	        	      String html = FileOperation.html2String(file);
	        	      Vector<Weibo> weibos = htmlparser.getWeibo(html);
	        	      for (Weibo weibo :weibos){
	        	          wi.push(weibo);
	        		       System.out.println(weibo.getContent());
	        	        }
	                }
	        }
	        */
	    }
	}
}
