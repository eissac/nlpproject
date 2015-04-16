package cn.edu.jnu.ie.parser;

import java.util.List;

import com.alibaba.fastjson.JSON;

public  class WeiboJsonParser implements Parser{
	String content;
	public WeiboJsonParser(String content){
		this.content=content;
	}
	public void parsetest(){
		WeiboGroup group = JSON.parseObject(content,WeiboGroup.class);
		System.out.println(group.toString());
	}
	public List<Card> parse10Weibo() {
		WeiboGroup group = JSON.parseObject(content,WeiboGroup.class);
		List<Card> first10Card=group.getCards().get(0).getCard_group();
		return first10Card;
	}
	
	public ParseResult parse() {
		//TODO 
		return null;
	}
	public void setContent(String content) {
		// TODO Auto-generated method stub
		
	}
}
