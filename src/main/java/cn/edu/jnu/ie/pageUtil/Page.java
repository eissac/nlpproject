package cn.edu.jnu.ie.pageUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.crawl.HTTPHandler;
import cn.edu.jnu.ie.parser.Parser;

public  abstract class Page {
	public static final Logger LOG = LoggerFactory.getLogger(Page.class);
	protected String url;
	protected String content;
	protected  String domain;
	protected String type;
	protected boolean hasSubpage;
	private Pattern p;
	private int numOfSubpage;
	private boolean isSubpage;
	
	public abstract Parser getParser();
	public void setNumOfSubpage(int num){
		this.numOfSubpage=num;
	}
	public int getNumOfSubpage(){
		return numOfSubpage;
	}
	static{
	}
public void checkSubPage(){
	Pattern p = Pattern.compile("page=");
	Matcher m = p.matcher(url);
	setSubpage(m.find());
}
	public static Page getWeiboSearchPageWithSearchWord(String url){
		return new WeiboSearchPage(url);
	}
public static UserWeiboPage getUserWeiboPage(String uid){
		return UserWeiboPage.withUid(uid);
	}
	public static UserInfoPage getUserInfoPage(String uid) {
		return UserInfoPage.withUid(uid);
	}
	public String getDomain(){
		p = Pattern.compile("//([0-9a-z]+\\.?)+/");
		Matcher m = p.matcher(getUrl());
		domain="";
		while(m.find()){
			domain=m.group().replaceAll("/", "");
		}
		return domain;
	}
	abstract void setType(String atype);
	public  String  getType(){
		return type;
	}
	public  String getContent(){
		if(content=="null")
			setContent();
		return content;
	}
	public void setContent(){
		content = HTTPHandler.getHTMLbyProxy(url);
	}
	public boolean isSubpage() {
		return isSubpage;
	}
	public void setSubpage(boolean isSubpage) {
		this.isSubpage = isSubpage;
	}
	public void setUrl(String url){
		this.url=url;
	}
	public String getUrl(){
		return url;
	}
	public boolean hasSubpage(){
		return hasSubpage;
	}
	public static void main(String[] args){
	}

}
