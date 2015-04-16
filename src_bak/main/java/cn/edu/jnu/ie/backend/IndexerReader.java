package cn.edu.jnu.ie.backend;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.solr.client.solrj.SolrServer;

public class IndexerReader {
	private String SOLRURL="http://localhost:8983/solr";
	private SolrServer solr;
	private Map<String,String> queryMap = new TreeMap<String,String>();
	public IndexerReader(){
	}
	public IndexerReader(String solrurl){
		this.SOLRURL = solrurl;
	}
	public IndexerReader(SolrServer solr){
		this.solr = solr;
	}
   public void open() throws IOException {
	    solr = SolrUtils.getHttpSolrServer(SOLRURL);
    }
   public void addQueryField(String key,String value){
	   queryMap.put(key, value);
   }
   public String buildQuery() {
	   if(queryMap.size()!=0){
	   StringBuilder sb = new StringBuilder();
	   for (String key :queryMap.keySet()){
		  sb.append(key+":"+queryMap.get(key));  
	   }
	   return sb.toString();
	   }
	   else return null;
   }
}
