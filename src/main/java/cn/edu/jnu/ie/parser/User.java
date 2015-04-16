package cn.edu.jnu.ie.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.backend.NutchField;
import cn.edu.jnu.ie.data.Weibo;
import cn.edu.jnu.ie.util.Constant;
import cn.edu.jnu.ie.util.Trimer;

public class User extends ParseResult {
	private String id;
	private String screen_name;
	private String home;
	private String imgUrl;
	private String verified_reason;
	private String description ;
	private String ismember ;
	private String location ;
	private boolean verified ;
	private boolean neverAt ;
	private String atNum;
	private List<User> followers;
	private List<User> followees;
	private long followersCnt;
	private long followeesCnt;
	private Vector<Weibo> weibos;
	private long lastWeiboDate;
	private String gender;
	private String registTime;
	private List<String> tags;
	private Map<String ,NutchField> infomap;
	public static Logger LOG  = LoggerFactory.getLogger(User.class);
	public static  Map < String,String>DICT=  String2Map(Constant.DICSTRING);
	public static Map<String,String>String2Map(String[] strs){
		Map<String,String> dicmap = new HashMap<String,String>();
		for (String str : strs) {
			String[] pair = str.split("\\|");
			dicmap.put(pair[0], pair[1]);
		}
		return dicmap;
	}

	public User(){
	}
	private User(Map<String,NutchField> infoMap){
		this.setInfomap(infoMap);
	}
	public static User  withId(long id){
		Map<String,NutchField> ainfomap = new HashMap<String,NutchField>();
		ainfomap.put("id", new NutchField(id));
		User u=new User(ainfomap);
		return u;
	}
	public static User withName(String username){
		Map<String,NutchField> ainfomap = new HashMap<String,NutchField>();
		ainfomap.put("name", new NutchField(username));
		User u=new User(ainfomap);
		return u;
	}
public static User withInfoMap (Map<String,NutchField> infoMap){
		User u=new User(infoMap);
		return u;
	}
	public void addInfo(String key,NutchField value){
		String newkey = validate(key);
		if(newkey!=null)
		infomap.put(newkey, value);
	}
	public NutchField getInfo(String key){
		if(!getInfomap().containsKey(key)){
			LOG.error("No "+key+" was found !");
			throw new NullPointerException("No "+key+" was found !");
		}
		return getInfomap().get(key);
	}
	public void addInfo( Map<String ,NutchField> ainfomap){
		for (Entry<String, NutchField> s : ainfomap.entrySet()){
			if(s.getValue()!=null)
			this.addInfo(s.getKey(), s.getValue());
		}
	}
public   String validate(String key ){
	String zh = Trimer.parseChinese(key);
	String en=key;
	if(zh !=""){
		en=DICT.get(zh);
	}
	return en;
}
	public static  User buildFromInfoMap(Map<String,NutchField>infomap){
		return  new User(infomap);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id=id;
	}

	public String getName() {
		return getInfomap().get("name").toString();
	}

	public void setName(String name) {
		getInfomap().put("name", new NutchField(name));
	}

	public Map<String ,NutchField> getInfomap() {
		return infomap;
	}

	public void setInfomap(Map<String ,NutchField> infomap) {
		this.infomap = infomap;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsmember() {
		return ismember;
	}

	public void setIsmember(String ismember) {
		this.ismember = ismember;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public boolean isNeverAt() {
		return neverAt;
	}

	public void setNeverAt(boolean neverAt) {
		this.neverAt = neverAt;
	}

	public String getAtNum() {
		return atNum;
	}

	public void setAtNum(String atNum) {
		this.atNum = atNum;
	}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	public List<User> getFollowees() {
		return followees;
	}

	public void setFollowees(List<User> followees) {
		this.followees = followees;
	}

	public long getFollowersCnt() {
		return followersCnt;
	}

	public void setFollowersCnt(long followersCnt) {
		this.followersCnt = followersCnt;
	}

	public long getFolloweesCnt() {
		return followeesCnt;
	}

	public void setFolloweesCnt(long followeesCnt) {
		this.followeesCnt = followeesCnt;
	}

	public Vector<Weibo> getWeibos() {
		return weibos;
	}

	public void setWeibos(Vector<Weibo> weibos) {
		this.weibos = weibos;
	}

	public long getLastWeiboDate() {
		return lastWeiboDate;
	}

	public void setLastWeiboDate(long lastWeiboDate) {
		this.lastWeiboDate = lastWeiboDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRegistTime() {
		return registTime;
	}

	public void setRegistTime(String registTime) {
		this.registTime = registTime;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getVerified_reason() {
		return verified_reason;
	}

	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}



	
}
