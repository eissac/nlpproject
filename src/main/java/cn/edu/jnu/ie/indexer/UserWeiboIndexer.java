package cn.edu.jnu.ie.indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import cn.edu.jnu.ie.backend.IndexerWriter;
import cn.edu.jnu.ie.backend.NutchDocument;
import cn.edu.jnu.ie.parser.Card;
import cn.edu.jnu.ie.parser.Mblog;
import cn.edu.jnu.ie.parser.Mblog.WeiboPic;
import cn.edu.jnu.ie.parser.User;
import cn.edu.jnu.ie.util.DateFormater;

public class UserWeiboIndexer {
	private IndexerWriter iw;
	public UserWeiboIndexer(IndexerWriter iw){
		this.iw = iw;
	}
	public void index(String id,List<Card> weibos) throws IOException, SolrServerException{
		int flag=0;
		for(Card weibo : weibos){
			Mblog m = weibo.getMblog();
			NutchDocument doc4w = new NutchDocument();
			User u = m.getUser();
			if(flag==0){
			flag=1;
			NutchDocument doc4u = new NutchDocument();
				doc4u.add("id", u.getId());
				 HashMap<String,String> map=new HashMap<String, String>();  
				 map.put("set", String.valueOf(u.isVerified()));
				doc4u.add("verified", map);

				HashMap<String,String> map2=new HashMap<String, String>();  
				 map2.put("set", String.valueOf(u.getVerified_reason()));
				doc4u.add("verified_reason", map2);

				HashMap<String,String> map3=new HashMap<String, String>();  
				 map3.put("set", String.valueOf(u.getScreen_name()));
				doc4u.add("screen_name", map3);
				iw.write(doc4u);
			}
				//alert !! check is repost or not!  
				//if is repost ,get repost text instead (!!!!  id not change !!!!!!).
					doc4w.add("id", m.getId());
					doc4w.add("uid", u.getId());
				if(m.getRetweeted_status()!=null){
					m=m.getRetweeted_status();
					doc4w.add("isRepost","true");
					doc4w.add("repostUid",m.getUser().getId() );
				}
					if(m.getPics()!=null){
						List<WeiboPic>pics = m.getPics();
						List<String> pid = new ArrayList<String>();
						for(WeiboPic pic :pics){
							pid.add(pic.getPid());
						}
						doc4w.add("pics",pid);
					}
				doc4w.add("content", m.getText());
				doc4w.add("repostCnt", Integer.valueOf(m.getReposts_count()));
				doc4w.add("commentCnt", Integer.valueOf(m.getComments_count()));
				doc4w.add("attitudesCnt", Integer.valueOf(m.getAttitudes_count()));
				doc4w.add("createDate", DateFormater.second2date(m.getCreated_timestamp()));
				doc4w.add("bid", m.getBid());
				doc4w.add("source", m.getSource());
				doc4w.add("url_count", m.getUrl_struct().size());
				iw.write(doc4w);
			}
	}
}
