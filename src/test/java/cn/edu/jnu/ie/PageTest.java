package cn.edu.jnu.ie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cn.edu.jnu.ie.pageUtil.Page;
import cn.edu.jnu.ie.pageUtil.UserWeiboPage;
import cn.edu.jnu.ie.parser.Parser;
import cn.edu.jnu.ie.parser.WeiboJsonParser;

public class PageTest {
	UserWeiboPage infoPage;
	@Before
	public void setUp(){
		infoPage=Page.getUserWeiboPage("2173497972");
	}
	@Test
	public void testGetType() {
		String type = infoPage.getType();
		assertEquals("UserWeibo",type);
	}
	//@Test
	public void testUrl() {
		String url = infoPage.getUrl();
		assertEquals(url,"http://weibo.com/p/1005052173497972/info?mod=pedit_more");
	}
	@Test
	public void testGetDomain(){
		String domain=infoPage.getDomain();
		assertEquals("m.weibo.cn",domain);
	}
	@Test
	public void testGetContent(){
		String content=infoPage.getContent();
		System.out.println(content);
		//assertTrue(content.contains("注册时间"));

		
	}
	@Test
	public void testGetParser(){
		Parser p = infoPage.getParser();
		assertTrue ( p instanceof WeiboJsonParser);
		p.parse();
	}
}
