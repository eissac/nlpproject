package cn.edu.jnu.ie.crawl;


public class CrawlDatum {
  private String url;
  private String type;
  private String itemName;
  private int numOfSubPages;
  private CrawlDatum (String url,String type,String itemName,int numOfSubPages) {
	this.url = url;
	this.type = type;
	this.setItemName(itemName);
	this.numOfSubPages = numOfSubPages;
  }
  private CrawlDatum(String url,String type,String itemName){
	this.url = url;
	this.type = type;
	this.itemName = itemName;
	this.numOfSubPages = 1;
  }
  public String toString() {
  return url+"\t"+type+"\t"+String.valueOf(numOfSubPages)+"\t"+itemName;
}
  public String getType() {
	return type;
  }
  public void setType(String type) {
	this.type = type;
  }
  public String getUrl() {
	return url;
  }
  public void setUrl(String url) {
	this.url = url;
  }
  public static CrawlDatum getCrawlDatum(String url,String type,String itemName) {
	return new CrawlDatum(url,type,itemName);
  }
  /**
   * 构造并返回一个CrawlDatum实例，表示一次搜索结果
   * @param url 
   * @param type 页面类型 微博/ZOL
   * @param numOfSubPages 搜索结果下 评论/微博 总页面数
   * @return
   */
  public static CrawlDatum getCrawlDatum(String url,String type,String itemName,int numOfSubPages) {
	return new CrawlDatum(url,type,itemName,numOfSubPages);
  }
  public static CrawlDatum getCrawlDatum(String infoString) {
	String[] crawlDatumInfo = infoString.split("\t");
	String url = crawlDatumInfo[0];
	String type = crawlDatumInfo[1];
	int numOfPages = Integer.parseInt(crawlDatumInfo[2]);
	String itemName = crawlDatumInfo[3];
	return new CrawlDatum(url,type,itemName,numOfPages);
  }
  public int getNumOfSubPages() {
	return numOfSubPages;
  }
  public void setNumOfSubPages(int numOfSubPages) {
	this.numOfSubPages = numOfSubPages;
  }
public String getItemName() {
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}
  
}
