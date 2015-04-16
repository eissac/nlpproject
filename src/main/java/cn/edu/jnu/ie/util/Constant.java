package cn.edu.jnu.ie.util;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    public static final String TEST_URL = "http://s.weibo.com/weibo/李雪山hakka&nodup=1&page=1" ;
//	 public static final String[] SEARCHWORDS = {"华为 荣耀 差","华为 荣耀  比 好","华为 荣耀  一般","华为 荣耀  不错","华为 荣耀  价格","华为 荣耀  外观","华为 荣耀  性价比"};
	 public static final String[] SEARCHWORDS = {"华为 荣耀 不错"};
    public static final String[] STOPWORD = {"转发","抽奖","微信","WeChat","v信","V信" ,"仅需","私信","联系","【" ,"】"} ;
    public static final String PLAINIPPATH = "source/plainIPs.txt";
    public static final int MAXSEARCHRESULT = 1000;
    public static final int PAGESTATUS_FAILDED = 400;
    public static final int PAGESTATUS_NORESULT = 401;
    public static final int PAGESTATUS_RESULTTOOLARGE = 402;
    public static final int PAGESTATUS_VALID = 200;
    public static final long ONEDAY = 86400000;
    public static final long ONEHOUR = 3600000;
    public static final int MAXRETRY = 4;
    public static final int MAXPAGE = 50;
    public static final int  DEFAULT_INTERVAL= 32 ; //32小时
	public static final String CookiePATH = "source/cookies.txt";
	public static final String[] DICSTRING = {"大学|college","公司|enterprise","QQ|QQ","邮箱|email","昵称|screen_name","简介|description","注册时间|registTime","所在地|location","性别|gender","博客|blog","个性域名|home"};

	public static final String USERSOLRURL ="http://localhost:8983/solr/weibo2";
	public static final String SPAMMERSOLRURL ="http://localhost:8983/solr/weibo2";
	public static final int MAXDEPTH = 2;
	public static final String USER="weibo_root";
	public static final String PASSWORD="jnu123";
	public static final String DATABASE="jdbc:mysql://127.0.0.1:3306/weibo";
	public static final String MINFANSPAGE="2";
}