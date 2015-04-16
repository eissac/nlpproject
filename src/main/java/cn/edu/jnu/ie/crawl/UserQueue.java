package cn.edu.jnu.ie.crawl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import cn.edu.jnu.ie.util.Constant;

public class UserQueue {
	private Queue<FetchItem> userqueue=new LinkedList<FetchItem>();
 	private int maxDepth=Constant.MAXDEPTH;
	public UserQueue(){
	}
	public void add(FetchItem e){
		if(!userqueue.contains(e)&&e.getDepth()<=Constant.MAXDEPTH)
		userqueue.add(e);
	}
	public void addAll(Collection<FetchItem> q ,int depth){
		if(depth<maxDepth){
		userqueue.addAll(q);
		}
	}
	public FetchItem poll(){
		return userqueue.poll();
	}
	public boolean contains(FetchItem item) {
		return userqueue.contains(item);
	}
}
