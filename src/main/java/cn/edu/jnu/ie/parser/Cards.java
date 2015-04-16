package cn.edu.jnu.ie.parser;

import java.util.ArrayList;
import java.util.List;

public class Cards {
private String mod_type;
private int maxPage;
private int page;
private String url;
private String loadMore;
private  List<Card> card_group= new ArrayList<Card>();
public Cards(){
	
}
public String getMod_type() {
	return mod_type;
}
public void setMod_type(String mod_type) {
	this.mod_type = mod_type;
}
public int getMaxPage() {
	return maxPage;
}
public void setMaxPage(int maxPage) {
	this.maxPage = maxPage;
}
public int getPage() {
	return page;
}
public void setPage(int page) {
	this.page = page;
}
public String getLoadMore() {
	return loadMore;
}
public void setLoadMore(String loadMore) {
	this.loadMore = loadMore;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public List<Card> getCard_group() {
	return card_group;
}
public void setCard_group(List<Card> card_group) {
	this.card_group = card_group;
}

}
