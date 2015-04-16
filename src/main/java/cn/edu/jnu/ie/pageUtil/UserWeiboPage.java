package cn.edu.jnu.ie.pageUtil;

	import cn.edu.jnu.ie.crawl.HTTPHandler;
import cn.edu.jnu.ie.parser.UserInfoPageParser;
import cn.edu.jnu.ie.parser.WeiboJsonParser;

public class UserWeiboPage extends Page {
		private String uid;
		private UserWeiboPage(String uid){
			setUid(uid);
			setUrl("http://m.weibo.cn/page/json?containerid=100505"+uid+"_-_WEIBO_SECOND_PROFILE_WEIBO&page=1");
			setType("UserWeibo");
		}
		public UserWeiboPage(String uid,int page){
			setUid(uid);
			setUrl("http://m.weibo.cn/page/json?containerid=100505"+uid+"_-_WEIBO_SECOND_PROFILE_WEIBO&page="+page);
			setType("UserWeibo");

		}
		public static UserWeiboPage  withUid(String uid){
			return new UserWeiboPage(uid);
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
		void setType(String atype) {
			type=atype;
		}
		@Override
		public WeiboJsonParser getParser() {
			return  new WeiboJsonParser(this.getContent());
		}

	}

