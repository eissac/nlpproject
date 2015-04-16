package cn.edu.jnu.ie.pageUtil;

import cn.edu.jnu.ie.crawl.HTTPHandler;
import cn.edu.jnu.ie.parser.Parser;
import cn.edu.jnu.ie.parser.UserInfoPageParser;

public class UserInfoPage extends Page{
	private String uid;
	private UserInfoPage(String uid){
		setUid(uid);
		setUrl( "http://weibo.com/p/100505"+uid+"/info?mod=pedit_more");
		setType("UserInfo");
	}
	public static UserInfoPage  withUid(String uid){
		return new UserInfoPage(uid);
	}
	public  void setUid(String uid){
		this.uid=uid;
	}
	public String getUid(){
		return uid;
	}
	@Override
	public String  getUrl(){
		return url;
	}
	@Override
	public String getContent(){
		if(content == null)
			content = HTTPHandler.getHTMLwithCookie(url);
		return content;
	}
	@Override
	public boolean hasSubpage(){
		return false;
	}
	@Override
	void setType(String atype) {
		type=atype;
	}
	@Override
	public UserInfoPageParser getParser() {
		String content = this.getContent();
		if(content.equals("302")){
		}
		return  UserInfoPageParser.withUid(getUid(),content);
	}

}
