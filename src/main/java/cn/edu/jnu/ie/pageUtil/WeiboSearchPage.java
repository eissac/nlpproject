package cn.edu.jnu.ie.pageUtil;

import cn.edu.jnu.ie.parser.Parser;
import cn.edu.jnu.ie.parser.WeiboSearchPageParser;

public class WeiboSearchPage extends Page{
	int numOfWeibo;
	public WeiboSearchPage(String url ){
		setType("WeiboSearch");
		setUrl(url);
	}
	public WeiboSearchPage(String url,boolean subpage ){
		setType("WeiboSearch");
		setUrl(url);
	}
	public int getNumOfWeibo(){
		return this.numOfWeibo;
	}
	public void setNumOfWeibo(int numOfWeibo){
		this.numOfWeibo = numOfWeibo;
	}
	@Override
	void setType(String atype) {
		type=atype;
	}
	@Override
	public  Parser getParser() {
		return (Parser) new WeiboSearchPageParser();
	}
}