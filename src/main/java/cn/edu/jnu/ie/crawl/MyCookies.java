package cn.edu.jnu.ie.crawl;

import java.io.IOException;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.util.Constant;
import cn.edu.jnu.ie.util.FileOperation;

public class MyCookies {
  public static final Logger LOG = LoggerFactory.getLogger(MyCookies.class);
  private Vector<String> Cookies = new Vector<String>(); 
  private static final MyCookies INSTANCE = new MyCookies();
  private int CookiePointer;
  private MyCookies() {
	this.Cookies = getCookies();
	this.CookiePointer = 0;
  }
  public boolean hasNext() {
	return (CookiePointer < Cookies.size());
  }
  public int getSize() {
	return Cookies.size();
  }
  public String get(int index) {
	return Cookies.get(index);
  }
  public String get() {
	String cookie; 
	if (hasNext()){
	  cookie = Cookies.get(CookiePointer);
	CookiePointer++;}
	else {
	  CookiePointer=0;
	  cookie=Cookies.get(0);
	  LOG.warn("All Cookies has tried 4 times ! No Cookie is valid!");
	}
	return cookie;
  }
  private Vector<String>  getCookies() {
   Vector<String> Cookies = new Vector<String>(); 
	try {
		String Cookie;
		for (String cookieStr : FileOperation.getLines(Constant.CookiePATH)) {
			Cookies.add(cookieStr);
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	return Cookies;
}
  public static MyCookies getInstance() {
	return INSTANCE;
  }
}
