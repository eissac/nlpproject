package cn.edu.jnu.ie;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cn.edu.jnu.ie.backend.NutchField;
import cn.edu.jnu.ie.pageUtil.Page;
import cn.edu.jnu.ie.pageUtil.UserInfoPage;
import cn.edu.jnu.ie.parser.UselessUserException;
import cn.edu.jnu.ie.parser.User;
import cn.edu.jnu.ie.parser.UserInfoPageParser;

public class UserInfoPageParserTest {
	UserInfoPageParser parser;
	String infoPiece;

	@Before
	public void setUp(){
		UserInfoPage info=Page.getUserInfoPage("2516683113");
		 parser = info.getParser();
	}
	//@Test
	public void getMoreInfoTest(){
		Map<String,NutchField> infos = parser.getMoreInfos();
		for(String key : infos.keySet()){
			System.out.println("key:"+key+" value:"+infos.get(key));
		}
	//	assertTrue(infos.size()==8);
	}
	//@Test
	public void getBasicInfoTest() throws UselessUserException{
		Map<String,NutchField> infos = parser.getBasicInfos();
		assertEquals(infos.get("followeeCnt").toString(),"140");
		assertEquals(infos.get("followerCnt").toString(),"1403110");
		assertEquals(infos.get("weiboCnt").toString(),"39321");
	}
	//@Test
	public void getUserTest() throws UselessUserException{
		User u = parser.parseUser();
		assertEquals(u.getInfo("home").toString(), "http://weibo.com/102221013http://weibo.com/yoyi" );
		assertEquals(u.getInfo("isVerified").toString(), "true" );
	}
	@Test
	public void getUser() throws UselessUserException{
		User u = parser.parseUser();
		assertEquals(u.getInfo("gender").toString(),"女");
		assertEquals(u.getInfo("location").toString(),"北京 东城区");
		assertEquals(u.getName(),"情书丶心语");
		assertEquals("你是我写不完的情书~~情感微小说分享平台~~",u.getInfo("intro").toString());
		System.out.println(u.getInfo("registTime").toString());
		System.out.println(u.getInfo("tags").toString());
		assertEquals(u.getInfo("isVerified").toString(), "false" );
	}

}
