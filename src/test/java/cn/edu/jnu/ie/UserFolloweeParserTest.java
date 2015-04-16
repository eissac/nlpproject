package cn.edu.jnu.ie;

import static org.junit.Assert.assertTrue;
import cn.edu.jnu.ie.pageUtil.UserFolloweePage;
import cn.edu.jnu.ie.parser.Parser;
import cn.edu.jnu.ie.parser.UserInfoPageParser;

public class UserFolloweeParserTest {
	UserFolloweePage infoPage;
	Parser p;
//	@Before
	public void setUp(){
		infoPage=new UserFolloweePage(1762231544);

	}
	//@Test
	public void testFactory() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		p=infoPage.getParser();
		assertTrue(p instanceof UserInfoPageParser );
	}

}
