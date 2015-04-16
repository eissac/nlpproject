package cn.edu.jnu.ie.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.data.Weibo;
import cn.edu.jnu.ie.nlp.Classifier;
import cn.edu.jnu.ie.util.Constant;

public class WeiboIndexer {
public static final Logger LOG = LoggerFactory.getLogger(WeiboIndexer.class);
private IndexerWriter iw;
public WeiboIndexer() throws IOException{
	iw = new IndexerWriter();
	iw.open();
}
public  void push(String txtPath) throws SolrServerException{
	BufferedReader reader ;
	String tempString;
	try{
	reader = new BufferedReader(new FileReader(new File(txtPath)));
	while((tempString=reader.readLine()) != null){
		push(Weibo.build(tempString));
	}
	reader.close();
	}catch(IOException e){
		LOG.error(new String("can not open file :"+txtPath));
	}
}
public  void push(Weibo weibo) throws IOException, SolrServerException{
	Map<String,String> info;
   NutchDocument doc = new NutchDocument();
   info = weibo.getKeyValues();
  	if(info.containsKey("comment_content") && !info.get("comment_content").trim().equals("")){
  	  for (String key : info.keySet()) {
      	if(key.contentEquals("comment_content")) {
             /*按照句子结束符分割句子*/
             String rawComment = info.get(key);
             commentClassify(doc,rawComment);
          	 }
         doc.add(key,info.get(key));
  	   }
  		doc.add("tstamp",System.currentTimeMillis());
  		iw.write(doc);
    }
}
public void commentClassify(NutchDocument doc ,String rawComment){
	    List<String> comment = new ArrayList<String>();
	    Classifier cl = new Classifier();
	    //过滤话题标签
	    Pattern ptopic = Pattern.compile("#[^#]+#");
	    Matcher mtopic = ptopic.matcher(rawComment);
	    if(mtopic.find()){
		    rawComment = rawComment.replace(mtopic.group(), "");
		    }
	     //过滤@用户
	    Pattern p2 = Pattern.compile("@[a-zA-Z0-9_\u4e00-\u9fa5]+");
	    Matcher m2 = p2.matcher(rawComment);
 	    if(m2.find()){
		     rawComment = rawComment.replace(m2.group(), "");
		     }
         //过滤链接
	    Pattern p3 = Pattern.compile("http://([a-zA-Z0-9]+\\.)+([a-zA-Z0-9]+)(/[a-zA-Z0-9/?%&.=]*)?");
	    Matcher m3 = p3.matcher(rawComment);
 	    if(m3.find()){
		    rawComment = rawComment.replace(m3.group(), "");
		    }
		String regex="[。？！?!]";
	    Pattern p =Pattern.compile(regex);
	    String[] substrs = p.split(rawComment);
	    String temp;
	    int polar;
	    for(String s : substrs) {
	  	  temp=s.trim();
     	  if(temp.length()>4){
	  	   	  comment.add(temp);
			}
	  	 }
	    polar=cl.getPolar(comment);
	    //doc.add("comment_content", comment);
	    doc.add("polar", polar);
	    System.out.print("polar:"+polar);
 }


public static void main(String[] args) throws IOException, SolrServerException{
String[] searchwords = Constant.SEARCHWORDS;
		for (int n = 0; n < searchwords.length; n++) {
		    String searchword = searchwords[n];
		    String dirPath = "data/weibo/getweibo/华为/";
		    String saveTXTPath = "data/weibo/saveweibo/temp";
		    HTMLParser htmlParser = new HTMLParser();
		    Vector<Weibo> weibos = htmlParser.writeWeibo2txt(searchword, dirPath, saveTXTPath);
		    WeiboIndexer wi = new WeiboIndexer();
		    for (Weibo weibo :weibos) {
		        wi.push(weibo);
		    }
		}
}
}
