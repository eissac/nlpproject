package cn.edu.jnu.ie.crawl;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mortbay.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.databaseutil.ConnectDB;
import cn.edu.jnu.ie.pageUtil.UserFolloweePage;
import cn.edu.jnu.ie.parser.Card;
import cn.edu.jnu.ie.parser.Followee;
import cn.edu.jnu.ie.parser.UserFolloweeParser;
import cn.edu.jnu.ie.util.Constant;
import cn.edu.jnu.ie.util.FileOperation;

import com.alibaba.fastjson.JSONException;
public class RelationCrawler {
	private UserQueue queue;
	private int maxDepth;
	private int maxItem;
	private FetchItem seed;
	public ConnectDB con ;
	public static final Logger LOG=LoggerFactory.getLogger(RelationCrawler.class);
	public static final int MINFANSPAGE=2;
	public RelationCrawler(FetchItem seed){
	    this.con = ConnectDB.getConnection();
		this.seed=seed;
		queue=new UserQueue();
	}
	public UserQueue getQueue() {
		return queue;
	}
	public void setQueue(UserQueue queue) {
		this.queue = queue;
	}
	public int getMaxDepth() {
		return maxDepth;
	}
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	public String buildUrl(long uid,int page){
		return "http://m.weibo.cn/page/json?containerid=100505"+uid+"_-_FOLLOWERS&page="+page;
	}
	public int getMaxItem() {
		return maxItem;
	}
	public void setMaxItem(int maxItem) {
		this.maxItem = maxItem;
	}
	public FetchItem getSeed() {
		return seed;
	}
	public void setSeed(FetchItem seed) {
		this.seed = seed;
	}
	public void fetch() throws IOException{
		queue.add(seed);
		FetchItem item ;
		while((item=queue.poll())!=null){
			fetch(item);
		}
	}
	public Vector<String> fetchSourceList(FetchItem aseed){
		long id = aseed.getUid();
		Vector<String> sourcelist=new Vector<String>();
		int numOfPage = (aseed.getFansCnt()-1)/20+1;
		String url;
		for(int i =1;i<=numOfPage;i++){
		//	url="http://weibo.com/p/100505"+id+"/follow?"+UrlEncoded.encodeString("pids=Pl_Official_HisRelation__61&relate=fans&page=")+i;
			url="http://weibo.com/p/100505"+id+"/follow?"+UrlEncoded.encodeString("relate=fans&page="+i);
		String html=HTTPHandler.getHTMLwithCookie(url);
		System.out.println(url);
				Pattern p = Pattern.compile("\\{\"ns\":\"pl.content.followTab.index\".*?\\}");
		Matcher m = p.matcher(html);

		String infoPiece="failed";
		while(m.find()){
			infoPiece=m.group();
			System.out.println(infoPiece);
		}
		Pattern p2 =Pattern.compile("通过.*?关注");
		Matcher m2 =p2.matcher(infoPiece);
		String s;	
		while(m2.find()){
			s=m2.group().replaceAll("<[^>]+>", "");
			sourcelist.add(s);
		}
		}
		return sourcelist;
	}
	public void fetch(FetchItem aseed) throws IOException{
		if(aseed.getFansCnt()==0){
			return;
		}
		long id = aseed.getUid();
		int depth = aseed.getDepth();
		int numOfPage = (aseed.getFansCnt())/10+1;
	//	Vector<String> sourcelist=fetchSourceList(aseed);
		LOG.info("fetching :"+id+"\t"+"total Page:"+numOfPage);
		if(numOfPage <MINFANSPAGE){
			return;
		}
		UserFolloweePage apage;
		UserFolloweeParser aparser;
		StringBuilder sb = new StringBuilder();
		for (int pagePointer =1;pagePointer <= numOfPage;pagePointer++){
			apage=new UserFolloweePage(id,pagePointer);
			try{
			aparser=apage.getParser();
			List<Card> cards= aparser.parseFans();
			Followee followee;
			int cnt=0;
			for(Card card : cards){
				followee = card.getUser();
				queue.add(new FetchItem(followee.getId(),depth+1));
				 //con.write2FansTable(id, card.getUser().getId());
				sb.append(id+"\t"+followee.getId()+"\t"+followee.getStatuses_count()+"\t"+followee.getFansNum()+"\t"+followee.getScreen_name()+"\n");
				cnt++;
			}
			}catch(JSONException e){
				LOG.error("failed parsing +"+id);
			}
			FileOperation.writeStringAppend(sb.toString(), "source/spammer");
		}
	}
	public static void main(String[] args) throws IOException{
		//5116925624
		Long uid=Long.valueOf("5337155638");
		FetchItem aseed= new FetchItem(uid.longValue());
		RelationCrawler rc=new RelationCrawler(aseed);
		rc.fetch();
	}
	}