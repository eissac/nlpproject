package cn.edu.jnu.ie;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import cn.edu.jnu.ie.crawl.HTTPHandler;

public class HTTPHandlerTest {

	@Test
	public void test() {
		String html=HTTPHandler.getHTMLwithCookie("http://weibo.com/p/1005051882362031/follow?relate=fans&from=100505&wvr=6&mod=headfans&current=fans#place");
		Pattern p = Pattern.compile("\\{\"ns\":\"pl.content.followTab.index\".*?\\}");
		Matcher m = p.matcher(html);
		String infoPiece="failed";
		while(m.find()){
			infoPiece=m.group();
		}
		System.out.println(infoPiece);
		Pattern p2 =Pattern.compile("通过.*?关注");
		Pattern uidp=Pattern.compile("&uid=[0-9]+");
		Matcher uidm=uidp.matcher(infoPiece);
		Matcher m2 =p2.matcher(infoPiece);
		String s;
		while(m2.find()){
			s=m2.group();
			System.out.println(s.replaceAll("<[^>]+>", ""));
		}
		while(uidm.find()){
			System.out.println(uidm.group().replaceAll("[^0-9]",""));
		}
	}

}
