package cn.edu.jnu.ie.pageUtil;

import cn.edu.jnu.ie.crawl.HTTPHandler;
import cn.edu.jnu.ie.parser.UserFolloweeParser;

public class UserFolloweePage extends Page {
	private long uid;
	public UserFolloweePage(long uid,int page){
		setUid(uid);
		setUrl( "http://m.weibo.cn/page/json?containerid=100505"+uid+"_-_FANS&page="+page);
		setType("UserFollowee");
	}
	public UserFolloweePage(long uid){
		setUid(uid);
		setUrl( "http://m.weibo.cn/page/json?containerid=100505"+uid+"_-_FANS&page=1");
		setType("UserFollowee");
	}
	private void setUid(long uid) {
		this.uid=uid;
	}
	@Override
	public UserFolloweeParser getParser() {
		return new UserFolloweeParser(getContent());
	}
	@Override
	public String getContent(){
		if(content == null)
			content = HTTPHandler.getHTMLwithCookie(url);
		return content;
	}

	@Override
	void setType(String atype) {
		// TODO Auto-generated method stub
		
	}
	
}
