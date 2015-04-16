package cn.edu.jnu.ie.crawl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class HTMLParser {
	public Vector<String> splitHTML(String html){
		System.out.println(html);
		Vector<String> pieces = new Vector<String>();
		Pattern p = Pattern.compile("<dl class=\"feed_list\".+?<dd class=\"clear\">");
		Matcher m  = p.matcher(html);
		while(m.find()){
			pieces.add(m.group());
			//System.out.println(m.group());
		}
		
		return pieces;
	}
	public String parse(String html){
		String s = "";
		Document doc = Jsoup.parse(html);
		Elements userInfo = doc.select("dt[class].face > a");
		Elements dates = doc.select("a[date]");
		Elements weiboids = doc.select("dl[mid]");
		Elements weibos = doc.select("p > em");
		Elements forwardNums = doc.select("a:contains(转发)");
		Elements commentNums = doc.select("a:contains(评论)");
		for(Element item : userInfo){
			String name = item.attr("title");
			String home = item.attr("href");
			String img = item.childNode(1).attr("src");
			//System.out.println(attr);
			s +=name+"||"+home+"||"+img+"||";
		}
		for(Element date : dates){
			String attr = date.attr("date");
			//System.out.println(attr);
			s += attr+"||";
		}
		for(Element weiboid : weiboids){
			String attr = weiboid.attr("mid");
			//System.out.println(attr);
			s += attr+"||";
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
			s += attr+"||";
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
			s += attr+"||";
		}
		for(Element weibo : weibos){
			String attr = weibo.text();
			//System.out.println(attr);
			s += attr+"||";
		}
		
		System.out.println(s);
	
		
		return s;
	}
	public String parse(String html,String temp){
		String s = "";
		Document doc = Jsoup.parse(html);
		Elements pieces = doc.getElementsByClass("feed_list");
		for (Element piece : pieces ){
        Node dt = piece.childNode(1);
        String userName = dt.childNode(1).attr("title");
        System.out.println(userName);
        Element dd = (Element) piece.childNode(3);
        System.out.println(dd.text().toString());
		}
		return s;
	}
	
	public Vector<String> write2txt( String html) throws IOException{
		String onePiece;
		Vector<String> pieces = new HTMLParser().splitHTML(html);
		Vector<String> weibos = new Vector<String>();
				for(int i = 0; i < pieces.size(); i++){
					onePiece = pieces.get(i);
					if(onePiece.contains("feed_list_forwardContent")){
						Pattern p = Pattern.compile("feed_list_forwardContent.+?<p class=\"info W_linkb W_textb");
						Matcher m = p.matcher(onePiece);
						if(m.find()){
							onePiece = onePiece.replace(m.group(), "");
						}
					}
					String s = new HTMLParser().parse(onePiece);
					weibos.add(s);
					System.out.println(s);
				}
		return weibos;
	}

	public Vector<String> write2txt(String searchword, String dirPath, String saveTXTPath) throws IOException{
		Vector<String> weibos = new Vector<String>();
		String onePiece;
		File f = new File(saveTXTPath);
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		//dirPath = "d:/data/weibo/getweibo/20xxxxxx/xxxx"
		for(int page = 0; page < 50; page++){
			String path = dirPath+"/"+searchword+page+".html";
			File ff = new File(path);
			if(ff.exists()){
				String html = FileOperation.html2String(path);
				Vector<String> pieces = new HTMLParser().splitHTML(html);
				for(int i = 0; i < pieces.size(); i++){
					onePiece = pieces.get(i);
					if(onePiece.contains("feed_list_forwardContent")){
						Pattern p = Pattern.compile("feed_list_forwardContent.+?<p class=\"info W_linkb W_textb");
						Matcher m = p.matcher(onePiece);
						if(m.find()){
							onePiece = onePiece.replace(m.group(), "");
						}
					}
					String s = new HTMLParser().parse(onePiece);
					weibos.add(s);
					bw.write(s+"\n");
				}
			}
		}
		bw.close();
		
		return weibos;
	}
	
	
}

