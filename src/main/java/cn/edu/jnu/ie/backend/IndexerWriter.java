package cn.edu.jnu.ie.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexerWriter {
	public static final Logger LOG = LoggerFactory.getLogger(IndexerWriter.class);
private String SOLRURL="http://localhost:8983/solr";
private int batchSize;
private final List<SolrInputDocument> inputDocs = new ArrayList<SolrInputDocument>();
private SolrServer solr;
public IndexerWriter(){
	this.batchSize=10;
}
public IndexerWriter(int batchSize){
	this.batchSize=batchSize;
}
public IndexerWriter(String solrurl){
	this.SOLRURL=solrurl;
}
public IndexerWriter(int batchSize,String solrurl){
	this.SOLRURL=solrurl;
	this.batchSize=batchSize;
}
public SolrServer getServer(){
	return this.solr;
}
public void open() throws IOException {
	solr = SolrUtils.getHttpSolrServer(SOLRURL);
}
public void close() throws IOException {
        try {
            if (!inputDocs.isEmpty()) {
                LOG.info("Indexing " + Integer.toString(inputDocs.size())
                        + " documents");
                }
         //   UpdateRequest req = new UpdateRequest();
            solr.add(inputDocs);
            solr.commit();
        }catch (final SolrServerException e) {
            throw makeIOException(e);
        }
    }
public void write(NutchDocument doc) throws IOException {
	 final SolrInputDocument inputDoc = new SolrInputDocument();
     for (final Entry<String, NutchField> e : doc) {
         for (final Object val : e.getValue().getValues()) {
             // normalise the string representation for a Date
             Object val2 = val;
             if (val instanceof Date) {
                 val2 = DateUtil.getThreadLocalDateFormat().format(val);
             }
             if (e.getKey().equals("content") || e.getKey().equals("title")) {
                 val2 = SolrUtils.stripNonCharCodepoints((String) val);
             }

             inputDoc.addField(e.getKey(), val2, e.getValue().getWeight());
         }
     }     
     inputDoc.setDocumentBoost(doc.getWeight());
     inputDocs.add(inputDoc);
     System.out.println(inputDocs.size());
     if (inputDocs.size() >=batchSize) {
    	 try{
    		 LOG.info("Indexing" + Integer.toString(inputDocs.size())+" documents");
    		 UpdateRequest req = new UpdateRequest();
    		 req.add(inputDocs);
    		 solr.commit();
    		 req.process(solr);
    	 } catch (final SolrServerException e) {
    		 LOG.error("Can not connect Solr Server");
    	 }
    	 inputDocs.clear();
     }
}
public void write(){
	
}
public void commit() throws IOException {
    try {
        solr.commit();
    } catch (SolrServerException e) {
        throw makeIOException(e);
    }
}
public void commitImmediately() throws SolrServerException, IOException{
	 UpdateRequest req = new UpdateRequest();
	  req.setAction( UpdateRequest.ACTION.COMMIT, false, false );
	  req.add( inputDocs );
	  UpdateResponse rsp = req.process( solr );
}
public static IOException makeIOException(SolrServerException e) {
    final IOException ioe = new IOException();
    ioe.initCause(e);
    return ioe;
}
public void update(NutchDocument doc) throws IOException {
    write(doc);
}



}
