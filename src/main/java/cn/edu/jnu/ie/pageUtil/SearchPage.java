package cn.edu.jnu.ie.pageUtil;

import java.util.List;

import org.mortbay.util.UrlEncoded;

import cn.edu.jnu.ie.crawl.HTTPHandler;
import cn.edu.jnu.ie.parser.Card;
import cn.edu.jnu.ie.parser.Cards;
import cn.edu.jnu.ie.parser.SearchPageParser;

public class SearchPage {
private String url;	
private String searchword;
private String content;
public SearchPage(String searchword){
	setSearchword(searchword);
	this.url=buildUrl(searchword);
}
public String  buildUrl(String searchword){
	String query="=7&q="+searchword+"&weibo_type=filter_hasori&cardid=weibo_page";
	String encode = UrlEncoded.encodeString(query);
	String url="http://m.weibo.cn/page/pagejson?containerid=100103type"+encode;
	//http://m.weibo.cn/page/pagejson?containerid=100103type%3D7%26q%3D%E5%8D%8E%E4%B8%BA%26weibo_type%3Dfilter_hasori&cardid=weibo_page
	System.out.println(url);
	return url;
}
public String getContent(){
	if(content == null)
		content = HTTPHandler.getHTMLwithCookie(url);
	return content;
}
public String getSearchword() {
	return searchword;
}
public void setSearchword(String searchword) {
	this.searchword = searchword;
}
public SearchPageParser getParser(){
	return new SearchPageParser(getContent());
}
public static void main(String[] args){
	SearchPage s = new SearchPage("华为");
	System.out.println(s.getContent());
	SearchPageParser p = s.getParser();
	List<Card> weibos= p.parse10Weibo();
	System.out.println(weibos.get(0).getMblog().getUser().getId());

}
}
