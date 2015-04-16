package cn.edu.jnu.ie;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Vector;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.edu.jnu.ie.backend.HTMLParser;
import cn.edu.jnu.ie.backend.WeiboIndexer;
import cn.edu.jnu.ie.crawl.HTTPHandler;
import cn.edu.jnu.ie.crawl.WeiboSearchPageCrawler;
import cn.edu.jnu.ie.crawl.WeiboSearchPageCrawler.NotPossitivePageNumException;
import cn.edu.jnu.ie.util.Constant;
import cn.edu.jnu.ie.util.DateFormater;
import cn.edu.jnu.ie.util.FileOperation;
import cn.edu.jnu.ie.util.Trimer;
import cn.edu.jnu.ie.util.Weibo;

public class WeiboCrawlerTest {

  final static File testdir = new File("build/test/generator-test");

  //@Before
  public void setUp() throws Exception {
	FileOperation.deleteDir(testdir);
  }

 // @After
  public void tearDown() throws IOException{
    FileOperation.deleteDir(testdir);
  }

@Test
public void testParseInteger(){
  String str="f2sfs234fs,4fs";
  assertEquals(22344,Trimer.parseNumber(str));
}
public void testGetPage() throws ClientProtocolException, URISyntaxException, IOException{
	String url = "http://s.weibo.com/weibo/iphone&xsort=time&timescope=custom:2015-03-17-01:2015-03-17-02&nodup=1&page=2";
			HTTPHandler httphandler = new HTTPHandler();
			System.out.println(httphandler.getHTML(url, "124.88.67.13", 82));
}
public void testWeiboCrawler() throws ClientProtocolException, URISyntaxException, IOException, InterruptedException, NotPossitivePageNumException{
  testdir.mkdirs();
  WeiboSearchPageCrawler wc = new WeiboSearchPageCrawler(testdir.getAbsolutePath()+"/IT","%e8%8d%a3%e8%80%80+%e7%95%85%e7%8e%a9");
  wc.crawlUrlAndSave("2015-03-18-20", "2015-03-18-22", 1);
  wc.crawlWeibo(new File(testdir.getAbsolutePath()+"/IT"), testdir.getAbsolutePath()+"/output");
}

@Test
public void temp() throws ClientProtocolException, URISyntaxException, IOException, InterruptedException, NotPossitivePageNumException {
int intervalHour=48;
String beginTimeStr="2015-01-01-00";
String endTimeStr="2015-01-05-00";
String type="IT";
String seedDirPath="data/weibo/seed/"+DateFormater.getDateofToday();
String rawWeiboDirPath="data/weibo/rawWeibo/"+DateFormater.getDateofToday();
String saveWeiboDirPath="data/weibo/saveWeibo/"+DateFormater.getDateofToday();
String[] searchWords = Constant.SEARCHWORDS;
File seedDir = new File(seedDirPath);
File rawWeiboDir=new File(rawWeiboDirPath);
File saveWeiboDir=new File(saveWeiboDirPath);
if(seedDir.exists()){
FileOperation.deleteDir(seedDir);
}
seedDir.mkdirs();
rawWeiboDir.mkdirs();
saveWeiboDir.mkdirs();
String savePath=seedDirPath+"/"+type;
for (String searchword : searchWords) {
  String word=searchword.substring(0, 2);
  String aRawWeiboDirPath = rawWeiboDirPath+"/"+word;
  String saveWeiboResultPath = saveWeiboDirPath+"/"+word;
  WeiboSearchPageCrawler ws = new WeiboSearchPageCrawler(savePath,searchword);
  ws.crawlUrlAndSave(beginTimeStr, endTimeStr, intervalHour);
  ws.crawlWeibo(new File(savePath), rawWeiboDirPath);
  HTMLParser htmlParser = new HTMLParser();
  Vector<Weibo> weibos = htmlParser.writeWeibo2txt(searchword, aRawWeiboDirPath, saveWeiboResultPath);
  WeiboIndexer wi = new WeiboIndexer();
  for (Weibo weibo :weibos) {
      wi.push(weibo);
  }
  }
}



}
