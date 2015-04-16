package cn.edu.jnu.ie.parser;

import java.util.ArrayList;
import java.util.List;

public class WeiboGroup {
private int ok;
private int count;
private int maxPage;
private List<Cards> cards= new ArrayList<Cards>();
public WeiboGroup(){
	
}
public int getOk() {
	return ok;
}
public void setOk(int ok) {
	this.ok = ok;
}
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}
public List<Cards> getCards() {
	return cards;
}
public void setCards(List<Cards> cards) {
	this.cards = cards;
}
public int getMaxPage() {
	return maxPage;
}
public void setMaxPage(int maxPage) {
	this.maxPage = maxPage;
}
}
