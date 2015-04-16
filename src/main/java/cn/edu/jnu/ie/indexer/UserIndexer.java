package cn.edu.jnu.ie.indexer;
import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.backend.IndexerWriter;
import cn.edu.jnu.ie.backend.NutchDocument;
import cn.edu.jnu.ie.backend.NutchField;
import cn.edu.jnu.ie.parser.User;
import cn.edu.jnu.ie.util.Constant;


public class UserIndexer 	extends Indexer {
public static final Logger LOG = LoggerFactory.getLogger(UserIndexer.class);
public static String SOLRURL=Constant.USERSOLRURL;
private IndexerWriter iw;
public UserIndexer(IndexerWriter iw) throws IOException{
	this.iw = iw;
}
	public  void index(User u) throws SolrServerException{
		NutchDocument doc = new NutchDocument();
		Map<String,NutchField> infomap = u.getInfomap();
		for (String key :infomap.keySet()){
			doc.add(key, infomap.get(key));
		}
		doc.add("usertype","spammer");
		doc.add("infoCnt", infomap.size());
		try {
			iw.write(doc);
		} catch (IOException e) {
			LOG.error("indexing faild!for IOException");
			e.printStackTrace();
		}
	}
}
