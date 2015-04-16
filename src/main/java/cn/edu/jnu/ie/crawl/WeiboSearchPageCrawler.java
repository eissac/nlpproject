package cn.edu.jnu.ie.crawl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.util.Constant;
import cn.edu.jnu.ie.util.DateFormater;
import cn.edu.jnu.ie.util.FileOperation;
import cn.edu.jnu.ie.util.Trimer;

public class WeiboSearchPageCrawler implements Crawler {
  public static final Logger LOG = LoggerFactory.getLogger(WeiboSearchPageCrawler.class);
  private Vector<CrawlDatum> crawldb = new Vector<CrawlDatum>();
  private PlainIPs plainIPs = PlainIPs.getInstance();
  private IPAddress plainIP;
  private final String searchword;
  private final String savePath;
  private final String PAGETYPE = "WeiboSearchPage";
  /**
   * 
   * @param saveDirPath
   * @param searchword
   */
  public WeiboSearchPageCrawler(String saveDirPath,String searchword) {
	this.savePath = saveDirPath;
	this.searchword = searchword;
  }
  /**
   * 提取时间区间内的有效url，并写入文件中
   * @param savePath 
   * @param endTimeStr 
   * @param beginTimeStr 
   * @throws IOException 
   * @throws NotPossitivePageNumException 
   * @throws ClientProtocolException 
   * 
   */
  public void crawlUrlAndSave(String beginTimeStr, String endTimeStr) throws 
  ClientProtocolException, NotPossitivePageNumException, IOException {
	crawlUrl(beginTimeStr,endTimeStr);
	FileOperation.writeVector(crawldb, savePath);
  }
  /**
   * 提取时间区间内的有效url，并写入文件中
   * @param beginTimeStr
   * @param endTimeStr
   * @param interval
   * @throws ClientProtocolException
   * @throws NotPossitivePageNumException
   * @throws IOException
   */
  public void crawlUrlAndSave(String beginTimeStr, String endTimeStr,int interval) 
	  throws ClientProtocolException, NotPossitivePageNumException, IOException {
	crawlUrl(beginTimeStr,endTimeStr,interval);
	FileOperation.writeVector(crawldb, savePath);
  }
  /**
   * 提取时间区间内的有效url
   * BE CAREFUL！时间区间必须为interval的整数倍
   * @param beginTime 单位：毫秒，距1970-01-01 00:00:00.000毫秒数
   * @param endTime  同上
   * @param interval  小时为单位的搜索结果时间间隔
   * @throws IOException 
   * @throws ClientProtocolException 
   * @throws NotPossitivePageNumException 
   */
  public void crawlUrl(long beginTime ,long endTime,int intervalHour ) 
	  throws ClientProtocolException, IOException, NotPossitivePageNumException {
	long interval  = intervalHour * Constant.ONEHOUR;
	long nowBeginTime = beginTime ;
	long nowEndTime = nowBeginTime+interval;
	plainIP = plainIPs.get();
	while (nowEndTime <= endTime) {
	  String url = buildURL(nowBeginTime,nowEndTime,searchword);
	  String html = HTTPHandler.getHTMLbyProxy(url,plainIP.getHost(),plainIP.getPort());
	  WeiboSearchPage page = new WeiboSearchPage(html);
	  int statuCode = page.getStatuCode();
	  if (statuCode == Constant.PAGESTATUS_FAILDED) {
		  getValidateIP();
				}
	  else if (statuCode == Constant.PAGESTATUS_VALID) {
		addPages(url,page.getNumOfPages());
		nowBeginTime = nowEndTime;
		nowEndTime = nowBeginTime+interval;
		LOG.info("Success! "+"add url:\t"+url);
		//重设interval
		interval  = intervalHour * Constant.ONEHOUR;
		}
	  else if (statuCode == Constant.PAGESTATUS_NORESULT) {
		nowBeginTime = nowEndTime;
		nowEndTime = nowBeginTime+interval;
		LOG.info("No Result ! search word: \t"+searchword+"\t"+" url:\t"+url);
		}
	  else if (statuCode == Constant.PAGESTATUS_RESULTTOOLARGE) {
		if (interval>Constant.ONEHOUR){
		  interval/=2;
		  nowEndTime = nowBeginTime+interval;
		  LOG.info("Reduce Interval .Try : interval = "+interval/Constant.ONEHOUR+" hour");
		  }
		else { 
		  addPages(url,Constant.MAXPAGE);
		  LOG.warn("Too much result has been returned!\nsearch word: \t"+searchword+"\t"+" url:\t"+url);
		  nowBeginTime=nowEndTime;
		  nowEndTime=nowEndTime+interval;
		  }
		}
	  else {
		//TODO
		LOG.error("Status code error!");
		}
	  }
	}
  /**
    * 提取时间区间内的有效url，写入成员变量crawldb中
   * BE CAREFUL!!!!!时间区间应该为32小时的整数倍
   * @param beginTime
   * @param endTime
   * @throws NotPossitivePageNumException 
   * @throws IOException 
   * @throws ClientProtocolException 
   */
  public void crawlUrl(long beginTime,long endTime) throws NotPossitivePageNumException, ClientProtocolException, IOException {
	int interval = Constant.DEFAULT_INTERVAL;  //32小时
	crawlUrl (beginTime,endTime,interval);
	}
  /**
   * 提取时间区间内的有效url,添加到 成员变量crawldb中
   * @param beginTimeStr
   * @param endTimeStr
   * @param interval 时间间隔，小时为单位
   * @throws ClientProtocolException
   * @throws NotPossitivePageNumException
   * @throws IOException
    */
  public void crawlUrl(String beginTimeStr ,String endTimeStr,int interval) throws ClientProtocolException, NotPossitivePageNumException, IOException {
	long beginTime = DateFormater.timeStr2Date(beginTimeStr).getTime();
	long endTime = DateFormater.timeStr2Date(endTimeStr).getTime();
	crawlUrl(beginTime,endTime,interval);
	}
  /**
   * 提取时间区间内的有效url,添加到 成员变量crawldb中
   * @param beginTimeStr
   * @param endTimeStr
   * @throws ClientProtocolException
   * @throws NotPossitivePageNumException
   * @throws IOException
    */
  public void crawlUrl(String beginTimeStr ,String endTimeStr) throws ClientProtocolException, NotPossitivePageNumException, IOException {
	long beginTime = DateFormater.timeStr2Date(beginTimeStr).getTime();
	long endTime = DateFormater.timeStr2Date(endTimeStr).getTime();
	crawlUrl(beginTime,endTime);
	}
  
 public  void getValidateIP() throws ClientProtocolException, IOException {
	 while(!ProxyIP.isPlainIP(plainIP)) {
		  if (plainIPs.hasNext()) {
			plainIP = plainIPs.get();
		  }
		  else 
			LOG.error("No IP valid ! Please run ProxyIP to update IPs");
		  }
 }
		  
 
  /**
   * 把有效的url添加到成员变量crawldb中
   * @param url  搜索结果url
   * @param pages 搜索结果的子页面数
   * @param searchword  搜索词
   */
  public void addPages(String url,int pages) {
	CrawlDatum crawldatum;
	crawldatum = CrawlDatum.getCrawlDatum(url, this.PAGETYPE,searchword,pages);
	crawldb.add(crawldatum);
	}
  
  /**
   * 把获取到的有效url写入savePath文件中
  * @param savePath
   */
  public void writeValidCrawlDatums() throws IOException{
	FileOperation.writeVector(crawldb, savePath);
	LOG.info("写入文件 "+savePath);
	}
  /**
    * * 构造待爬取的url
   * @param beginTime
   * @param endTime
   * @return
 * @throws UnsupportedEncodingException 
   */
  public String buildURL(long beginTime,long endTime,String searchword) throws UnsupportedEncodingException {
	String beginTimeStr = DateFormater.getFormatTime(beginTime);
	String endTimeStr = DateFormater.getFormatTime(endTime);
	String url = "http://s.weibo.com/weibo/"+URLEncoder.encode(searchword+"&xsort=time&timescope=custom:"+beginTimeStr+":"+endTimeStr+"&nodup=1" ,"UTF-8");
	//String url = "http://s.weibo.com/weibo/"+searchword+"&nodup=1&page=1";
	System.out.println(url);
	return url;
	}
  static class WeiboSearchPage {
	private boolean isNoResult = false;
	private boolean isTooMuchResult = false;
	private int numOfPages = 0;
	private String html;
	private WeiboSearchPage(String html) {
	  this.html = html;
	  }
	public static WeiboSearchPage getWeiboSearchPage(String html) {
	  return new WeiboSearchPage(html);
	  }
	public boolean checkIsFailded() {
	  return (html=="failed");
	  }
	public boolean checkIsNoResult() {
	  Pattern p = Pattern.compile("nick-name");
	  Matcher m = p.matcher(html);
	  //不包含nick-name,该页面没有微博内容
	  isNoResult = !m.find();
	  return isNoResult;
	  }
	public boolean checkIsTooMuchResult() throws NotPossitivePageNumException {
	  setNumOfPages();
	  if (numOfPages > 50) {
		this.isTooMuchResult = true;
		}
	  else if (numOfPages <= 0) {
		//throw new NotPossitivePageNumException(); 
		//TODO
		}
	  return isTooMuchResult;
	  }
	
	public void setNumOfPages() {
	  Pattern p = Pattern.compile("找到 ([0-9]{1,3},?)+ 条结果");
	  Matcher m = p.matcher(html);
	  while (m.find()) {
		System.out.println(m.group());
		int numOfresult = Trimer.parseNumber(m.group());
		numOfPages = (numOfresult/20)+1;
		}
	  }
	public int getStatuCode() throws NotPossitivePageNumException {
	  if (checkIsFailded()) {
		return Constant.PAGESTATUS_FAILDED;
		}
	  if (checkIsNoResult()) {
		return Constant.PAGESTATUS_NORESULT;
		}
	  if (checkIsTooMuchResult()) {
		return Constant.PAGESTATUS_RESULTTOOLARGE;
		}
	  return Constant.PAGESTATUS_VALID;
	  }
	public int getNumOfPages() {
	  return this.numOfPages;
	  }
	}
  public class NotPossitivePageNumException extends Exception {
	private static final long serialVersionUID = 1L;
	//或者：extends Throwable
	public NotPossitivePageNumException()  {}
	public NotPossitivePageNumException(String message) {
	  super(message);
	  }
	}
  public String getSavePath() {
	return savePath;
	}
  public String getSearchword() {
	return searchword;
	}
  public String parseTimeStr(String url) {
	Pattern p = Pattern.compile("201[4|5](-[0-9]{2}){3}:201[4|5](-[0-9]{2}){3}");
	Matcher m = p.matcher(url);
	while(m.find()){
	  return m.group();
	}
	return "ERROR";
  }
  /**
   * 
   * @param sourceFile
   * @param saveDir
   * @throws ClientProtocolException
   * @throws URISyntaxException
   * @throws IOException
   * @throws InterruptedException
   */
	public void crawlWeibo(File sourceFile,String saveDir) 
		throws ClientProtocolException, URISyntaxException, IOException, InterruptedException {
	  IPAddress ip=plainIPs.get();
	  BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
	  String infoString="";
	  while((infoString=reader.readLine())!=null){
		CrawlDatum datum = CrawlDatum.getCrawlDatum(infoString);
		String baseurl=datum.getUrl();
		int numOfpages=datum.getNumOfSubPages();
		String word=datum.getItemName().substring(0, 2);
		String timeString = parseTimeStr(URLDecoder.decode(baseurl,"UTF-8"));
		String time = timeString.split(":")[0].replaceAll("-", "");
		String dirPath = saveDir+"/"+word+"/";
		File f = new File(dirPath);
		f.mkdirs();//创建文件夹，另一方法mkdirs创建多层未创建的文件夹
		String html;
		String url;
	  long t1 = System.currentTimeMillis();
		for(int i=1;i<=numOfpages;i++) {
		  url=baseurl+URLEncoder.encode("&page="+i,"UTF-8");
		 LOG.info("****Start getting"+url+"****");
		  html = HTTPHandler.getHTMLbyProxy(url,ip.getHost(),ip.getPort());
		  if(html=="failed") {
			while(!ProxyIP.isPlainIP(ip)) {
			  if (plainIPs.hasNext()) {
				ip = plainIPs.get();
				}
			  else 
				LOG.error("No IP valid ! Please run ProxyIP to update IPs");
			  }
			--i;
			continue;
			}
		  else{
			FileOperation.writeString(html, dirPath+time+"page"+i);
			long t2 = System.currentTimeMillis();
			System.out.println((double)(t2 - t1)/60000 + " mins");
			}
		  }
		}
	  reader.close();
	  }
	
	public static void main(String [] args) throws ClientProtocolException, NotPossitivePageNumException, IOException, URISyntaxException, InterruptedException {
	  if(args.length!=4) {
		System.out.println("String beginTimeStr=args[0] "
	  + "String endTimeStr=args[1]"
		  + "String type=args[2];"
	+ "String savePathDir=args[3];");
	}
	String beginTimeStr=args[0];
	String endTimeStr=args[1];
	String type=args[2];
	String saveDirPath=args[3]+"/"+DateFormater.getDateofToday();
	String[] searchWords = Constant.SEARCHWORDS;
	//String saveDirPath="data/weibo/seed/"+DateFormater.getDateofToday();
	File saveDir = new File(saveDirPath);
	if(saveDir.exists()){
	FileOperation.deleteDir(saveDir);
	}
	saveDir.mkdirs();
	String savePath=saveDirPath+"/"+type;
	for (String searchword : searchWords) {
	  WeiboSearchPageCrawler ws = new WeiboSearchPageCrawler(savePath,searchword);
	  //ws.crawlAndSave("2015-01-01-00","2015-01-01-12",12);
	  ws.crawlUrlAndSave(beginTimeStr, endTimeStr, 120);
	  ws.crawlWeibo(new File(savePath), "data/weibo/getweibo");
	  }
	}
}
