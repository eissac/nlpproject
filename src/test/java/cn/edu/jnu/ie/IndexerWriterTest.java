package cn.edu.jnu.ie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import cn.edu.jnu.ie.backend.IndexerReader;
import cn.edu.jnu.ie.backend.IndexerWriter;
import cn.edu.jnu.ie.backend.NutchDocument;

public class IndexerWriterTest {
	private static IndexerWriter iw = new IndexerWriter(2);
public static void main(String[] args){
   File sourceFile = new File(args[0]);
   BufferedReader reader;
	String tempString;
	try{
		iw.open();
      reader = new BufferedReader(new FileReader(sourceFile));
      while ((tempString=reader.readLine())!=null) {
	       NutchDocument doc = new NutchDocument();
    	    List<String> comment = new ArrayList<String>();
          String regEx="[。？！?! ]";
          Pattern p =Pattern.compile(regEx);
          Matcher m = p.matcher(tempString);
          /*按照句子结束符分割句子*/
          String[] substrs = p.split(tempString);
          String temp;
          for(String s : substrs) {
        	  temp=s.trim();
        	  if(!temp.equals("")){
        		  comment.add(temp);
        	  }
          }
          String id=String.valueOf(System.currentTimeMillis())+String.valueOf((int)Math.random()*100);
          doc.add("comment_content", comment);
          doc.add("id", String.valueOf(id));
          doc.add("title", "testSet");
          doc.add("isParsed", 0);
          doc.add("tstamp",System.currentTimeMillis());
		    iw.write(doc);
      }
		SolrServer solr=iw.getServer();
		iw.commit();
		iw.close();
	}catch(Exception e){
		
	}
}
}
