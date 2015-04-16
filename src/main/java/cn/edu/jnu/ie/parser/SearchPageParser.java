package cn.edu.jnu.ie.parser;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class SearchPageParser {
	private String content;
	public SearchPageParser(String content){
		this.content=content;
	}
	public List<Card> parse10Weibo() {
		WeiboGroup group = JSON.parseObject(content,WeiboGroup.class);
		List<Card> first10Card=group.getCards().get(0).getCard_group();
		return first10Card;
	}
	public ParseResult parse() {
		return null;
	}
	public void setContent(String content) {
		this.content=content;
	}
}
