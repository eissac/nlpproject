package cn.edu.jnu.ie.crawl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.parser.Parser;

public class Page {
	public static final Logger LOG = LoggerFactory.getLogger(Page.class);
	private String url;
	private String domain;
	private Pattern p;
	private String type;
	private String content;
	static{
	}
	private Page(String url,String type){
		this.url = url;
		this.type=type;
	}
	private Page(String url){
		this.url = url;
		this.type="WeiboSearchPage";
	}
	public static Page getPage(String url){
		return new Page(url);
	}
	public String getDomain(){
		p = Pattern.compile("//([0-9a-z]+\\.?)+/");
		Matcher m = p.matcher(url);
		domain="";
		while(m.find()){
			domain=m.group().replaceAll("/", "");
		}
		return domain;
	}
	public Parser getParser() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		   return   (Parser) Class.forName(type+"Parser").newInstance();
	}
	public  String getContent(){
		content = HTTPHandler.getHTMLbyProxy(url);
		return content;
	}
	public static void main(String[] args){
		Page p = Page.getPage("http://www.test.com/hkj?fds=fds&fds=32334");
		System.out.println(p.getDomain());
	}
}
