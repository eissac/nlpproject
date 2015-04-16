package cn.edu.jnu.ie.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Weibo {
	private Map<String,String> weibo;
	private String content;
	private String userid;
	private String comment_date;
	private String userHomePage;
	private String userImg;
	private String comments_cnt;
	private String reposts_cnt;
	private boolean isRepost;
	public Weibo(){
		this.weibo = new HashMap<String,String>();
	}
	public static Weibo build(String rawWeibo){
		Weibo aWeibo = new Weibo();
		String[] weiboInfo = rawWeibo.split("\t");
		for (String info : weiboInfo){
			String[] infoPair = info.split(":");
			if(infoPair.length == 2) {
	  		   aWeibo.addInfo(infoPair[0], infoPair[1]);
			}	
			else 
				System.out.println("Weibo txt in this line is uncomplete !");
		}
		return aWeibo;
	}
	public void addInfo(String key,String value){
		weibo.put(key, value);
	}
	public void deleteInfo(String key,String value){
		if(weibo.containsKey(key)){
		    weibo.remove(key);	
		}
	}
	public boolean containsKey(String key){
	   return weibo.containsKey(key);	
	}
	public Map<String,String> getKeyValues(){
		return this.weibo;
	}
   public String toString(){
	   Set<String> keys = weibo.keySet();
	   StringBuilder sb = new StringBuilder();
	   for(String key :keys){
		   String value = weibo.get(key);
		   sb.append(key+":"+value+"\t");
	   }
	   return sb.toString();
   }
	public String getContent(){
		return this.weibo.get("comment_content");
	}
   public void setContent(String comment_content){
		this.weibo.put("comment_content",comment_content);
	}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String getUserHomePage() {
	return userHomePage;
}
public void setUserHomePage(String userHomePage) {
	this.userHomePage = userHomePage;
}
public String getUserImg() {
	return userImg;
}
public void setUserImg(String userImg) {
	this.userImg = userImg;
}
public String getComments_cnt() {
	return comments_cnt;
}
public void setComments_cnt(String comments_cnt) {
	this.comments_cnt = comments_cnt;
}
public String getReposts_cnt() {
	return reposts_cnt;
}
public void setReposts_cnt(String reposts_cnt) {
	this.reposts_cnt = reposts_cnt;
}
public String getComment_date() {
	return comment_date;
}
public void setComment_date(String comment_date) {
	this.comment_date = comment_date;
}
public boolean isRepost() {
	return isRepost;
}
public void setRepost(boolean isRepost) {
	this.isRepost = isRepost;
}
}
