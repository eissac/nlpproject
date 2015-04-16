package cn.edu.jnu.ie.backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.jnu.ie.util.Constant;
import cn.edu.jnu.ie.util.FileOperation;
import cn.edu.jnu.ie.util.Weibo;

public class HTMLParser {
	public Vector<String> splitHTML(String html){
		Vector<String> pieces = new Vector<String>();
		Pattern p = Pattern.compile("<dl class=\"feed_list\".+?<dd class=\"clear\">");
		Matcher m  = p.matcher(html);
		while(m.find()){
			pieces.add(m.group());
			//System.out.println(m.group());
		}
		return pieces;
	}

	public Vector<Weibo>  getWeibo( String html) throws IOException{
		String onePiece;
		Vector<Weibo> weibos = new Vector<Weibo>();
		Vector<String> pieces = new HTMLParser().splitHTML(html);
      Pattern pcontent = Pattern.compile("feed_list_forwardContent.+?<p class=\"info W_linkb W_textb");
		for(int i = 0; i < pieces.size(); i++){
			boolean containStopWord = false;
			onePiece = pieces.get(i);
			if(onePiece.contains("feed_list_forwardContent")){
				Matcher m = pcontent.matcher(onePiece);
				if(m.find()){
					onePiece = onePiece.replace(m.group(), "");
				}
			}
	      Weibo weiboInfo = new Weibo();
         Pattern pwebchat = Pattern.compile("[0-9]{7,13}");
		   Document doc = Jsoup.parse(onePiece);
		   Elements userInfo = doc.select("dt[class].face > a");
		   Elements dates = doc.select("a[date]");
		   Elements weiboids = doc.select("dl[mid]");
		   Elements weibopieces = doc.select("p > em");
		   Elements forwardNums = doc.select("a:contains(转发)");
		   Elements commentNums = doc.select("a:contains(评论)");
         for(Element weibo : weibopieces){
			    String attr = weibo.text();
			    //根据停词粗暴过滤新闻性微博
				for (String stopword :Constant.STOPWORD){
			  	    if(attr.contains(stopword)) {
			  	    	containStopWord = true;
				       break;
				      }
				}
			  	if (containStopWord) 
			  		 continue;
			     //粗暴过滤所有微信号xx号微博
			    Matcher mwebchat = pwebchat.matcher(attr);
			    if(mwebchat.find()){
				    continue;
		  	     }
			    //过滤转发内容
			    Pattern p = Pattern.compile("//@[a-zA-Z0-9_\u4e00-\u9fa5]+.*");
			    Matcher m = p.matcher(attr);
			    if(m.find()){
			    	attr = attr.replace(m.group(), "");
			      }
	     	    if(attr.length()>4){
	     	    	weiboInfo.addInfo("comment_content",attr);
	     	    	}
         }
      if(!weiboInfo.containsKey("comment_content") || containStopWord)
    	  continue;
		for(Element item : userInfo){
			String name = item.attr("title");
			String home = item.attr("href");
			String img = item.childNode(1).attr("src");
			weiboInfo.addInfo("user_name", name);
			weiboInfo.addInfo("user_homepage", home);
			weiboInfo.addInfo("user_img", img);
		}
		for(Element date : dates){
			String attr = date.attr("date");
			weiboInfo.addInfo("comment_time", attr);
		}
		for(Element weiboid : weiboids){
			String attr = weiboid.attr("mid");
			weiboInfo.addInfo("id", attr);
		}
		for(Element forwardNum : forwardNums){
			String attr = forwardNum.text();
			if(attr.equals("转发")){
				attr = "0";
			}
			else{
				if(!attr.contains("转发(")){
					attr = "0";
				}
				else{
					attr = attr.substring(attr.indexOf("转发(")+3, attr.indexOf(")"));
				}
			}
			//System.out.println(attr);
			weiboInfo.addInfo("repost_count", attr);
		}
		for(Element commentNum : commentNums){
			String attr = commentNum.text();
			if(attr.equals("评论")){
				//System.out.println(attr);
				attr = "0";
			}
			else{
				if(!attr.contains("评论(")){
					attr = "0";
				}
				else{
					attr = attr.substring(attr.indexOf("评论(")+3, attr.indexOf(")"));
				}
			}
			//System.out.println(attr);
			weiboInfo.addInfo("follow_comment_count", attr);
		}
		 weibos.add(weiboInfo);
		}
		return weibos;
	}

	//过滤并提取微博写入到文件saveTXTPath中，并返回微博集合weibos
	public Vector<Weibo> writeWeibo2txt(String searchword, String dirPath, String saveTXTPath) throws IOException{
		Vector<Weibo> weibos = new Vector<Weibo>();
		File dir = new File(dirPath);
		if(dir.exists()){
		File[] files = dir.listFiles();
		for (File ff : files) {
		    String html = FileOperation.html2String(ff);
			 weibos.addAll(getWeibo(html));
			 FileOperation.writeVector(weibos, saveTXTPath);
		}
    }
		    return weibos;
	}
	public Vector<NutchDocument>getZOLComment(String html){
      Vector<NutchDocument> comments = new Vector<NutchDocument>();
		Pattern p = Pattern.compile("<li class=\"comment-item.*");
		return comments;
	}
	public Vector<NutchDocument> writeZOL2txt(String dirPath, String saveTXTPath) throws IOException{
		Vector<NutchDocument> comments = new Vector<NutchDocument>();
		FileWriter fw = new FileWriter(saveTXTPath);
		BufferedWriter bw = new BufferedWriter(fw);
		File dir = new File(dirPath);
		if(dir.exists()) {
			File[] files = dir.listFiles();
			for(File ff:files) {
				String html = FileOperation.html2String(ff);
				comments = getZOLComment(html);
				for(NutchDocument comment : comments) {
					bw.write(comment.toString());
					}
				}
			bw.close();
		}
		return comments;
	}
}

