package cn.edu.jnu.ie.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import cn.edu.jnu.ie.backend.SolrUtils;
import cn.edu.jnu.ie.nlp.Classifier;

public class QueryTest {
public static void main(String[] args) throws Exception{
	String SOLRURL="http://localhost:8983/solr";
	SolrServer solr = SolrUtils.getHttpSolrServer(SOLRURL);
	SolrServer server = new HttpSolrServer( SOLRURL);
	SolrQuery query = new SolrQuery();
	query.setQuery("*:*");
	query.setParam("rows", "9999");
	QueryResponse rsp = solr.query(query);
	SolrDocumentList docs= rsp.getResults();
	Iterator<SolrDocument> it = docs.iterator();
   Collection<Object> comments;
	SolrDocument tempdoc;
	Classifier cl = new Classifier();
      int npos=0;
	   int nneg=0;
	   int nobj=0;
	while(it.hasNext()){
     	int sum=0;
		tempdoc=it.next();
	   int result;
      comments=tempdoc.getFieldValues("comment_content");
	  if(comments != null){
      for(Object comment : comments){
    	  String tempString = (String) comment;
    	  result=cl.classify(tempString); 
    	  sum+=result;
    	  System.out.println(tempString+"      "+result);
      }
	  }
      SolrInputDocument doc = new SolrInputDocument();
      Map<String, String> updatePolar = new HashMap<String, String>(1);
      Map<String, String> updateParsed = new HashMap<String, String>(1);
      updateParsed.put("set", "1");
      doc.addField("id", tempdoc.getFieldValue("id"));
      doc.addField("isParsed", updateParsed);
      if(sum>0){
      System.out.println("------总的来说：   sum= "+sum +"极性："+"pos");
      npos++;
      updatePolar.put("set", "1");
      doc.addField("polar", updatePolar);
      } 
      else if(sum<0){
      System.out.println("------总的来说：   sum= "+sum +"极性："+"neg");
      nneg++;
      updatePolar.put("set", "-1");
      doc.addField("polar", updatePolar);
      }
      else {
    	  System.out.println("------总的来说：   sum= "+sum +"极性："+"obj");
        nobj++;
        updatePolar.put("set", "0");
        doc.addField("polar", updatePolar);
      }
      solr.add(doc);
	}
	System.out.println("总共"+docs.getNumFound()+"条记录");
	System.out.println("其中  pos:"+npos+"  neg:"+nneg+"  obj:"+nobj);
	solr.commit();
}
}
