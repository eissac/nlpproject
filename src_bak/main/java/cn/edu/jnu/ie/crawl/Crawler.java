package cn.edu.jnu.ie.crawl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTML;

import org.apache.http.client.ClientProtocolException;


public class Crawler {
	/**
	 * 是否存在指定页数的搜索结果页面
	 * @param html
	 * @return
	 */
	public boolean isExistResult(String html){
		boolean isExist =  true;
		Pattern pExist = Pattern.compile("抱歉，没有找到.+?span>相关的结果");//您可以尝试更换关键词，再次搜索。（表示指定页没有结果）
		Matcher mExist = pExist.matcher(html);
		if(mExist.find()){
			isExist = false;
		}
		
		return isExist;
	}
	
	public static String getMyTime(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String today = dateFormat.format(date);
		//System.out.println(today);
		
		return today;
	}
	
	public static void main(String[] args) throws ClientProtocolException, 
	URISyntaxException, IOException, InterruptedException {
		long t1 = System.currentTimeMillis();
/*
 * "苹果","htc","华为", "小米", "中兴", "联想",  "魅族", "酷派",
				"两会", "马航失联","汽车摇号","自贸区",
 */
		String[] searchwords = {"三星", "苹果","htc","华为", "小米", "中兴", "联想",  "魅族", "酷派"};
		//String[] searchwords = {};
		String today = getMyTime();
		System.out.println("Today is "+today);
		System.out.println("");
		File dirGetweiboSub = new File("data/weibo/getweibo/"+today);
		dirGetweiboSub.mkdirs();
		File dirWeibostxtSub = new File("data/weibo/saveweibo/weibostxt/"+today);
		dirWeibostxtSub.mkdirs();
		File dirWeibosxmlSub = new File("data/weibo/saveweibo/weibosxml/"+today);
		dirWeibosxmlSub.mkdirs();
		Vector<String> ip = new Vector<String>();
		ip = FileOperation.getLines("data/weibo/plainIPs.txt");
		int ipNum = ip.size();
		int iIP = 0;
		
		for (int n = 0; n < searchwords.length; n++) {
			
			
			String searchword = searchwords[n];
			String dirPath = "data/weibo/getweibo/"+today+"/"+searchword;
			File f = new File(dirPath);
			f.mkdirs();//创建文件夹，另一方法mkdirs创建多层未创建的文件夹
			int totalPage = 50;//设置想要搜索的页数，则搜索范围为该搜索词下的第1到第totalPage页
			
			System.out.println("****Start getting weibos of the keyword \""+searchword+"\"****");
			//将指定页数的搜索页面html文件爬取下来并保存
			String html;
			
			for(int i = totalPage; i > 0; i--){//开始爬取，先把一个话题下的html都爬下来，再用这些html文件
					String hostName = ip.get(iIP).split(":")[0];
					int port = Integer.parseInt(ip.get(iIP).split(":")[1]);
					html = new HTTPHandler().getHTML("http://s.weibo.com/weibo/"+searchword+"&nodup=1&page="+String.valueOf(i)
							,hostName, port);
					int iReconn = 0;
					while(html.equals("null")){
						html = new HTTPHandler().getHTML("http://s.weibo.com/weibo/"+searchword+"&nodup=1&page="+String.valueOf(i)
								,hostName, port);
						iReconn++;
						System.out.println("****"+ip.get(iIP)+" reconnected "+iReconn+" time(s)****");
						if(iReconn == 4){//4
							break;
						}
					}
					if(html.equals("null")){
						System.out.println("****5 consecutive connections were failed, now using next IP****");
						if(iIP == ipNum-1){
							System.out.println("****All valid proxy IPs have been tried, still can not get all the data. Now trying the valid proxy IP list again.****");
							iIP = 0;
							System.out.println("****Turn to"+ip.get(iIP)+", start connecting****");
						}
						else{
							iIP++;
							System.out.println("****Turn to"+ip.get(iIP)+", start connecting****");
						}
						i++;
					}
					if(html.contains("version=2012")){
						if(!html.contains("可用空格将多个关键词分开")){
							FileOperation.writeString(html, "data/weibo/getweibo/"+today+"/"+searchword+"/"+searchword+String.valueOf(i)+".html");
							System.out.println("\""+searchword+"\""+" No."+i+" page's html have been saved successfully!");
						}
						else{
							System.out.println("****\""+searchword+"\""+"No."+i+" page does not exist****");
						}
					}
			}
			System.out.println("****\""+searchword+"\" crawling has been done!!****");
			System.out.println("****Now writing the weibos to local files (txt & xml)****");
			String saveTXTPath = "data/weibo/saveweibo/weibostxt/"+today+"/"+searchword+".txt";
			HTMLParser htmlParser = new HTMLParser();
			Vector<String> weibos = htmlParser.write2txt(searchword, dirPath, saveTXTPath);
			
			long t2 = System.currentTimeMillis();
			System.out.println((double)(t2 - t1)/60000 + " mins");
		}
	}
}