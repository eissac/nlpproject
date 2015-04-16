package cn.edu.jnu.ie;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.edu.jnu.ie.pageUtil.UserFolloweePage;
import cn.edu.jnu.ie.pageUtil.UserWeiboPage;
import cn.edu.jnu.ie.parser.Card;
import cn.edu.jnu.ie.parser.Followee;
import cn.edu.jnu.ie.parser.Mblog;
import cn.edu.jnu.ie.parser.UserFolloweeParser;
import cn.edu.jnu.ie.parser.WeiboJsonParser;

public class JsonParseTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	UserWeiboPage up;
	WeiboJsonParser pp;
	UserFolloweePage fp;
	UserFolloweeParser ufp;
	@Before
	public void setup(){
	 up = UserWeiboPage.withUid("1857150145");
	 pp=up.getParser();
	 fp = new UserFolloweePage(1857150145);
	 ufp = fp.getParser();
	}
	@Test
	public void weiboTest(){
		List<Card> cards = pp.parse10Weibo();
		Mblog mblog = cards.get(0).getMblog();
		assertTrue(mblog.getId()==1857150145);
	}
	@Test
	public void followeeTest(){
		List<Card> cards=ufp.parseFans();
		Followee f = cards.get(0).getUser();
		System.out.println(f.getId());
	//	assertEquals(f.getId(),Long.valueOf("1340404854"));
	}
}
