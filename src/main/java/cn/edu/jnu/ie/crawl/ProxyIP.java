package cn.edu.jnu.ie.crawl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.util.Constant;
import cn.edu.jnu.ie.util.FileOperation;


public class ProxyIP {
	public static final Logger LOG = LoggerFactory.getLogger(ProxyIP.class);
	/**
	 * 
	 * @param html
	 * @param outputfile
	 * @return
	 * @throws IOException 
	 */
	public static Vector<IPAddress> getProxyIPs(String html,String outputfile) throws IOException{
		Document doc = Jsoup.parse(html);
		Elements tbody = doc.getElementsByTag("tbody");
		Vector<IPAddress> ips = new Vector<IPAddress>();
		for(Element tb : tbody) {
			Elements trs = tb.getElementsByTag("tr");
			for(Element tr : trs) {
			  try{
		       String ip = tr.child(0).text();
			    String portStr = tr.child(1).text();
			    int port = Integer.parseInt(portStr);
			    ips.add(IPAddress.getIPAddress(ip,port));
			  }catch (Exception e){
				continue;
			  }
			}
		}
	   FileOperation.writeVector(ips,outputfile);
		return ips;
	}
	 public static Vector<IPAddress> getAllProxyIPs(String ipLibURL,int numOfPages,String outputPath) throws ClientProtocolException, IOException, URISyntaxException{  
	        Vector<IPAddress> onePageIPs = new Vector<IPAddress>();  
	        Vector<IPAddress> allIPs = new Vector<IPAddress>(); 
	        for (int i = 1; i <= numOfPages; i++) {  
	            String url = ipLibURL+String.valueOf(i);  
	            String[] html = new HTTPHandler().getHTML(url);  
	            while(html[1]=="faild"){
	              	html = new HTTPHandler().getHTML(url);
	            	}
	            try{
               LOG.info(html[0]+"\t url:"+url);  
	            if(!html[0].equals("404")){  
	            	if(html[1]=="failed") {
	            		continue;
	            	}
	                onePageIPs = getProxyIPs(html[1],outputPath);
	                for(int j = 0; j < onePageIPs.size(); j++){  
	                    IPAddress s = onePageIPs.get(j);  
	                    if(!allIPs.contains(s)){  
	                        allIPs.add(s);  
	                    }  
	                }  
	            }  
	        }catch (NullPointerException e) {
	          System.out.println(html[1]);
	          continue;
	        }
	        }  
	        LOG.info("total proxy IP number:： "+allIPs.size());  
	        return allIPs;  
	    }  
	public static Vector<IPAddress> getValidIPs(Vector<IPAddress> allIPs) throws ClientProtocolException, IOException{
				Vector<IPAddress> validIPs = new Vector<IPAddress>();
			Vector<IPAddress> validHostname = new Vector<IPAddress>();
			File f = new File("source/IPs/validIPs.txt");
	        FileWriter fw = new FileWriter(f);
	        BufferedWriter bw = new BufferedWriter(fw);
	        int cal = allIPs.size();
			for (IPAddress ip : allIPs) {
			  cal--;
	        String hostName = ip.getHost();  
	        int port = ip.getPort();  
	       // String varifyURL = "http://iframe.ip138.com/ic.asp";//http://ip.uee.cn/ http://iframe.ip138.com/ic.asp  
	        String varifyURL = "http://w.1616.net/chaxun/iptolocal.php";//http://ip.uee.cn/ http://iframe.ip138.com/ic.asp  
	        String content = new HTTPHandler().getHTMLbyProxy(varifyURL, hostName, port);  
	        if(content.equals("failed"))
	        {
	          LOG.info("failed ip:"+ip+"\t"+cal+"ips remain");
	          continue;
	        }
	         Pattern p = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");  
	         Matcher m = p.matcher(content);  
	            String s;  
	            while(m.find()){  
	                s = m.group();  
	                if(!validHostname.contains(s)){  
	                    validIPs.add(ip);  
	                    bw.write(ip.toString()+"\n");
	                    LOG.info("valid proxy IP "+ ip.toString());  
	                    }  
	                }  
	        }  
			bw.close();
			return validIPs;
	}
	public static boolean isPlainIP(IPAddress ip) throws ClientProtocolException, IOException {
     int iReconn = 0;  
     String html = new HTTPHandler().getHTMLbyProxy(Constant.TEST_URL, ip.getHost(), ip.getPort());  
     while(html.equals("failed")){  
       if(iReconn == Constant.MAXRETRY){  
    	 	return false;
	      } 
       html = new HTTPHandler().getHTMLbyProxy(Constant.TEST_URL, ip.getHost(), ip.getPort());  
       iReconn++ ;
       LOG.info("****"+ip.toString()+"is reconnecting the"+iReconn+" time****");  
      }
     if(html.contains("version=2012")) {
       return true;
       }
     else return false;
     }
	 public static Vector<IPAddress> classifyIPs(Vector<IPAddress> validIPs, String plainPath) throws ClientProtocolException, IOException{  
	   Vector<IPAddress> plainIPs = new Vector<IPAddress>();  
	   int cal=validIPs.size();
	   for (IPAddress ip : validIPs) {
		 if (isPlainIP(ip)) {
		   plainIPs.add(ip);  
		   LOG.info("plain: "+ip.toString());
		   }
		 else
		   LOG.info(cal+" ip remains");
	    cal--;
		 }  
	   return plainIPs;  
	   }
	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException{
		// String[] html = new HTTPHandler().getHTML("http://www.kuaidaili.com");
		//Vector<IPAddress> allIPs = ProxyIP.getAllProxyIPs("http://www.kuaidaili.com/free/outha/",373,"source/IPs/allIPs.txt");
		Vector<IPAddress> allIPs = IPAddress.getIPsFromFile("source/IPs/allIPs.txt") ;
		Vector<IPAddress> validIPs = ProxyIP.getValidIPs(allIPs);
		//Vector<IPAddress> validIPs = IPAddress.getIPsFromFile("source/IPs/validIPs.txt") ;
		Vector<IPAddress> plainIPs = ProxyIP.classifyIPs(validIPs,"source/IPs/plainIPs.txt");
		FileOperation.writeVector(plainIPs,"source/IPs/plainIPs.txt");
		 
		//System.out.println(result);
	//	FileReader fr = new FileReader(new File("/home/hadoop/NLPproject/nlpproject/source/temp"));
	//	BufferedReader bf = new BufferedReader(fr);
	//	StringBuilder sb = new StringBuilder();
	//	String tempString = bf.readLine();
	//	while(tempString != null) {
	//		sb.append(tempString);
	//		tempString = bf.readLine();
	//	}

		
	}

}
