package cn.edu.jnu.ie.crawl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.pageUtil.UserFolloweePage;
import cn.edu.jnu.ie.parser.Followee;
import cn.edu.jnu.ie.parser.UserFolloweeParser;

public class FetchItem {
private int depth;
private Integer fansCnt;
private Followee user;
public static final Logger LOG=LoggerFactory.getLogger(FetchItem.class);
public FetchItem(long uid){
	Followee u=new Followee(uid);
	setUser(u);
	setUid(uid);
	setDepth(1);
}
public FetchItem(Followee user){
	setUser(user);
	setUid(user.getId());
	setDepth(1);
}
public FetchItem(Followee user,int depth){
	this.setUser(user);
	this.setDepth(depth);
}
public FetchItem(long uid,int depth){
	Followee u=new Followee(uid);
	setUser(u);
	setUid(uid);
	setDepth(depth);
}
public long getUid(){
	return this.user.getId();
}
public void setUid(long uid){
	this.user.setId(uid);
}
public Integer getFansCnt(){
	if(fansCnt==null){
		setFansCnt();
	}
	return fansCnt;
}
public void setFansCnt(){
	try{
	UserFolloweePage page=new UserFolloweePage(getUid());
	UserFolloweeParser p = page.getParser();
	this.fansCnt=p.parseCount();
	}
	catch(NumberFormatException e){
		LOG.error("number is more than 100,000 ,id"+getUid());
	}
}
public int getPageCnt(){
	return getFansCnt()/10;
}
public int getDepth() {
	return depth;
}
public void setDepth(int depth) {
	this.depth = depth;
}
public String getScreenName() {
	return this.user.getScreen_name();
}
public void setScreenName(String screenName) {
	this.user.setScreen_name(screenName);
}
public Followee getUser() {
	return user;
}
public void setUser(Followee user) {
	this.user = user;
}
@Override
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }
    if (anObject instanceof FetchItem) {
        FetchItem anotherObject = (FetchItem)anObject;
        if (anotherObject.getUid()==this.getUid()){
        return true;
        }
        else return false;
    }
    return false;
    }
@Override
public int hashCode(){
	return (int)user.getId();
}
}