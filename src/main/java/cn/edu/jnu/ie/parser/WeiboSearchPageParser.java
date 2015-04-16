package cn.edu.jnu.ie.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiboSearchPageParser  {
  private String html;
	public boolean isExistResult() {
		boolean isExist =  true;
		Pattern pExist = Pattern.compile("抱歉，没有找到.+?span>相关的结果");//（表示指定页没有结果）
		Matcher mExist = pExist.matcher(html);
		if(mExist.find()){
			isExist = false;
		}
		return isExist;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
}
