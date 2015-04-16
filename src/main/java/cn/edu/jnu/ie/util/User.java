package cn.edu.jnu.ie.util;

import java.util.List;
import java.util.Vector;

public class User {
	private String id;
	private String name;
	private String homePage;
	private String imgUrl;
	private String person_intro ;
	private String location ;
	private boolean isVerified ;
	private boolean neverAt ;
	private String atNum;
	private List<User> followers;
	private List<User> followees;
	private long followersNum;
	private long followeresNum;
	private Vector<Weibo> weibos;
	private long lastWeiboDate;
	private boolean gender;
	public long getLastWeiboDate() {
		return lastWeiboDate;
	}
	public void setLastWeiboDate(long lastWeiboDate) {
		this.lastWeiboDate = lastWeiboDate;
	}
	public Vector<Weibo> getWeibos() {
		return weibos;
	}
	public void setWeibos(Vector<Weibo> weibos) {
		this.weibos = weibos;
	}
	public long getFolloweresNum() {
		return followeresNum;
	}
	public void setFolloweresNum(long followeresNum) {
		this.followeresNum = followeresNum;
	}
	public long getFollowersNum() {
		return followersNum;
	}
	public void setFollowersNum(long followersNum) {
		this.followersNum = followersNum;
	}
	public List<User> getFollowees() {
		return followees;
	}
	public void setFollowees(List<User> followees) {
		this.followees = followees;
	}
	public List<User> getFollowers() {
		return followers;
	}
	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}
	public String getAtNum() {
		return atNum;
	}
	public void setAtNum(String atNum) {
		this.atNum = atNum;
	}
	public boolean isNeverAt() {
		return neverAt;
	}
	public void setNeverAt(boolean neverAt) {
		this.neverAt = neverAt;
	}
	public boolean isVerified() {
		return isVerified;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPerson_intro() {
		return person_intro;
	}
	public void setPerson_intro(String person_intro) {
		this.person_intro = person_intro;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getHomePage() {
		return homePage;
	}
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean getGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
}
