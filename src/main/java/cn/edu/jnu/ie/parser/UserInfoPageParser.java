package cn.edu.jnu.ie.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.backend.NutchField;
import cn.edu.jnu.ie.util.Trimer;

public class UserInfoPageParser implements Parser {
	public static Logger LOG  = LoggerFactory.getLogger(UserInfoPageParser.class);
	private String content;
	private String uid;
	private User u;
	public UserInfoPageParser(){

	}
	public UserInfoPageParser(String uid,String content){
		setContent(content);
		u = User.withId(Long.valueOf(uid));
	}

	public ParseResult parse() {
		// TODO Auto-generated method stub
		return null;
	}
	public User parseUser() throws UselessUserException { 
		u.addInfo(getBasicInfos());
		Map<String ,NutchField> m = getMoreInfos();
		u.addInfo(m);
		//u.addInfo(getVerifyInfo());
		return u;
		}
	/**
	public   Map<String,NutchField> getVerifyInfo(){
		Map<String ,NutchField> ainfomap = new HashMap<String ,NutchField>();
		Pattern p = Pattern.compile("\\{\"ns\":\"pl\\.header\\.head\\.index\".*?\\}");
		Matcher m = p.matcher(content);
		String infoPiece="failed";
		while(m.find()){
			infoPiece=m.group();
		}
		ainfomap.put("verified", new NutchField(infoPiece.contains("approve")));
		return ainfomap;
	}
	 * @throws UselessUserException 
	**/
	public  Map<String,NutchField>  getBasicInfos() throws UselessUserException{
		Map<String ,NutchField> basicInfo = new HashMap<String ,NutchField>();
		Pattern p = Pattern.compile("\\{\"ns\":\"\",\"domid\":\"Pl_Core_T8CustomTriColumn__56\".*?\\}");
		Matcher m = p.matcher(content);
		String infoPiece="failed";
		while(m.find()){
			infoPiece=m.group();
		}
		Pattern p2 = Pattern.compile("[0-9]+<\\\\/strong>");
		Matcher countMatcher = p2.matcher(infoPiece);
		Vector<Integer> result = new Vector<Integer>();
		while (countMatcher.find()){
			result.add(Trimer.parseNumber(countMatcher.group()));
		}
		if(result.size()==3){
			int weiboCnt = Integer.valueOf(result.get(2));
			int followeeCnt = Integer.valueOf(result.get(0));
			if(weiboCnt <=15 || followeeCnt<=10){
				throw new UselessUserException();
			}
			basicInfo.put("followeeCnt",new NutchField(followeeCnt ));
			basicInfo.put("fansNum",new NutchField(Integer.valueOf(result.get(1)) ));
			basicInfo.put("weiboCnt",new NutchField(weiboCnt));
		}
		else{
			throw new NullPointerException("Info Piece is broken!");
		}
		return basicInfo;
	}
	public Map<String,NutchField> getMoreInfos(){
		Map<String ,NutchField> ainfomap = new HashMap<String ,NutchField>();
		Pattern p = Pattern.compile("\\{\"ns\":\"\",\"domid\":\"Pl_Official_PersonalInfo__59\".*?\\}");
		Matcher m = p.matcher(content);
		String infoPiece="failed";
		while(m.find()){
			infoPiece=m.group();
		}
		//TODO 取得一系列pt_title 和pt_result 在html标签中
		infoPiece=infoPiece.replaceAll("\\\\/", "\\/");
		infoPiece=infoPiece.replaceAll("\\\\\"", "");
		infoPiece=infoPiece.replaceAll("\\\\t", "");
		infoPiece=infoPiece.replaceAll("\\\\r\\\\n", "");
		Document doc = Jsoup.parse(infoPiece);
		Elements title = doc.select("h4");
		for (Node t:title){
			String infoTitle = t.childNode(0).childNode(0).toString();
			Element infoblock = (Element) t.parentNode().nextSibling();
			if(infoTitle.equalsIgnoreCase("标签信息")){
				Elements tag = infoblock.select("li");
					List<String> tags = new ArrayList<String>();
					for (Element a : tag.select("a")){
						tags.add(a.text());
					}
				ainfomap.put("tags", new NutchField(tags));
			}
			else{
			Elements infos = infoblock.select("li");
			for (Element li : infos){
				ainfomap.put(li.child(0).text(),new NutchField(li.child(1).text()));
			}
		}
		}
					return ainfomap;
	}
	public static UserInfoPageParser  withUid(String uid,String content){
		return new UserInfoPageParser(uid,content);
	}
	public void setContent(String content) {
		this.content=content;
	}

}
