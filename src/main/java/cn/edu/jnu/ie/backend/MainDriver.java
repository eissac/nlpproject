package cn.edu.jnu.ie.backend;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.solr.client.solrj.SolrServerException;

import cn.edu.jnu.ie.indexer.UserIndexer;
import cn.edu.jnu.ie.indexer.UserWeiboIndexer;
import cn.edu.jnu.ie.pageUtil.UserInfoPage;
import cn.edu.jnu.ie.pageUtil.UserWeiboPage;
import cn.edu.jnu.ie.parser.Card;
import cn.edu.jnu.ie.parser.UselessUserException;
import cn.edu.jnu.ie.parser.User;
import cn.edu.jnu.ie.parser.UserInfoPageParser;
import cn.edu.jnu.ie.parser.WeiboJsonParser;
import cn.edu.jnu.ie.util.FileOperation;

public class MainDriver {
public static void main(String[] args) throws SolrServerException, IOException{
	Vector<String>ids = FileOperation.getLines(args[0]);
	IndexerWriter iw ;
	iw = new IndexerWriter(5000);
	iw.open();
	for(String id : ids){
		System.out.println("parsing:\t"+id);
		UserInfoPage infopage;
		UserInfoPageParser infoParser;
		User u = null;
		UserWeiboPage weibopage;
		UserWeiboPage weibopage2;
		WeiboJsonParser weiboparser;
		WeiboJsonParser weiboparser2;
		List<Card> weibos=null;
		UserIndexer ui;
		UserWeiboIndexer uwi;
		try{
			infopage = UserInfoPage.withUid(id);
			 infoParser = infopage.getParser();
			 try{
				 u = infoParser.parseUser();
				 weibopage = UserWeiboPage.withUid(id);
				 weiboparser = weibopage.getParser();
				 weibos = weiboparser.parse10Weibo();
				 weibopage2 = new UserWeiboPage(id,2);
				 weiboparser2=weibopage2.getParser();
				 weibos.addAll(weiboparser2.parse10Weibo());
			 } catch(NullPointerException e){
				 	System.out.println("*********no such user or cookie invaild!************");
				 	throw new NoSuchUserException();
			 	}catch(UselessUserException e){
				 	System.out.println("*********id :"+id+ "  user is useless ************");
				 	throw new NoSuchUserException();
			 	}
			 try{
				 ui = new UserIndexer(iw);
				 ui.index(u);
				 uwi = new UserWeiboIndexer(iw);
				 uwi.index(id, weibos);
			 }catch (NullPointerException e){
				 System.out.println("!!!!!!!!!!!index faild!!!!!!!!!!!!!!!!!");
			 }
		}catch (NoSuchUserException e){
			}
	}
				 iw.close();
}
}
