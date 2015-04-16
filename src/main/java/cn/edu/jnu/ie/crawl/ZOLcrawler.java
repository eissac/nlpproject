package cn.edu.jnu.ie.crawl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import cn.edu.jnu.ie.util.DateFormater;
import cn.edu.jnu.ie.util.FileOperation;

/**
 * 爬取中关村网站下的产品评论，通过遍历0-9999999，获得有效产品id， 保存该产品下的所有评论页面(可选)
 * @author eissac
 */
public class ZOLcrawler {
long beginProid;
long proNum;
long endProId;
static String[] searchwords;
static PlainIPs IPs;
static int IPNum;
static {
	IPs = PlainIPs.getInstance();
	IPNum = IPs.getSize();
}
public static void crawlZOL(String sourceDir ,String parseDir,int proNum,int beginProId) throws ClientProtocolException, URISyntaxException, IOException {
  int proId = beginProId;
  int iIP = 0;
  IPAddress ip;
  String today = DateFormater.getDateofToday();
  String sourcePath = sourceDir+"/"+today;
  String parsePath = parseDir+"/"+today;
  new File(sourcePath).mkdirs();
  new File(parsePath).mkdirs();
  String html;
  ip = IPs.get(iIP);
  String hostName = ip.getHost();
  int port = ip.getPort();
  for (int j = 0; j < proNum;j++) {
	   StringBuilder sb = new StringBuilder();
		boolean validPage=false;
	   int page=1;
	while(true) {
	    String requestURL= "http://detail.zol.com.cn/xhr3_Review_GetListAndPage_order=1%5EisFilter=1%5EproId="+proId+"%5Epage="+page+".html";
	    html = new HTTPHandler().getHTML(requestURL,hostName, port);
		int iReconn = 0;
		while(html.equals("null")){
	      html = new HTTPHandler().getHTML(requestURL,hostName, port);
			iReconn++;
			System.out.println("****"+ip.toString()+" reconnected "+iReconn+" time(s)****");
			if(iReconn == 4){//4
				break;
			}
		}
		if(html.equals("null")){
			System.out.println("****5 consecutive connections were failed, now using next IP****");
			if(iIP == IPNum-1){
				System.out.println("****All valid proxy IPs have been tried, still can not get all the data. Now trying the valid proxy IP list again.****");
				iIP = 0;
				System.out.println("****Turn to"+IPs.get(iIP)+", start connecting****");
			}
			else{
				iIP++;
				System.out.println("****Turn to"+IPs.get(iIP)+", start connecting****");
			}
		}
	    if(html.length()>4700)
	    {
	    	System.out.println("ok!parsing id:"+proId+"\t page : "+page+"\t length:"+html.length());
	    	sb.append(html+"\n");
		    validPage = true;
		    page++;
	    }
	    else{
		    break;
	    }
	}
	if(validPage){
		FileOperation.writeString(sb.toString(), sourcePath+"/"+String.valueOf(proId)+".html");
	}
	proId++;
    }
}
}
