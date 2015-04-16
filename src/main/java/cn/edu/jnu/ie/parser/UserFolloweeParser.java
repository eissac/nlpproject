package cn.edu.jnu.ie.parser;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class UserFolloweeParser implements Parser{
	private String content;
    public static final Logger LOG=LoggerFactory.getLogger(UserFolloweeParser.class);
	public UserFolloweeParser(String content){
		setContent(content);
	}
	public void setContent(String content){
		this.content=content;
	}
	public List<Card> parseFans() throws JSONException{
		List<Card> first10Card = null;
		WeiboGroup group = JSON.parseObject(content,WeiboGroup.class);
		first10Card=group.getCards().get(0).getCard_group();
				return first10Card;
	}
public int parseCount() {
    WeiboGroup group = null;
	try{
		group = JSON.parseObject(content,WeiboGroup.class);
	}catch( Exception e){
		LOG.error("新手引导微博号");
		return 0;
	}
		return group.getCount();
	}
	public ParseResult parse() {
		// TODO Auto-generated method stub
		return null;
	}
}
