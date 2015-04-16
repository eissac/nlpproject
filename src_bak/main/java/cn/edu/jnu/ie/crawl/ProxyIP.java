package cn.edu.jnu.ie.crawl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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


public class ProxyIP {
   public static final Logger LOG = LoggerFactory.getLogger(ProxyIP.class);
	public ProxyIP(){
		;
	}
	public static Vector<String> getProxyIPs(String html){
		Document doc = Jsoup.parse(html);
		Elements tbody = doc.getElementsByTag("tbody");
		Vector<String> ips = new Vector<String>();
		for(Element tb : tbody) {
			Elements trs = tb.getElementsByTag("tr");
			for(Element tr : trs) {
		       String ip = tr.child(0).text();
			    String port = tr.child(1).text();
			    ips.add(ip+":"+port);
			}
		}
		return ips;
	}
	 public static Vector<String> getAllProxyIPs(String ipLibURL,int numOfPages) throws ClientProtocolException, IOException, URISyntaxException{  
	        Vector<String> onePageIPs = new Vector<String>();  
	        Vector<String> allIPs = new Vector<String>();  
	        for (int i = 1; i <= numOfPages; i++) {  
	            String url = ipLibURL+String.valueOf(i);  
	            String[] html = new HTTPHandler().getHTML(url);  
	            if(!html[0].equals("404")){  
	                //LOG.info("状态码 "+html[0]);  
	                LOG.debug("start finding proxy IPs under this link: "+url);  
	                onePageIPs = getProxyIPs(html[1]);  
	                for(int j = 0; j < onePageIPs.size(); j++){  
	                    String s = onePageIPs.get(j);  
	                    if(!allIPs.contains(s)){  
	                        allIPs.add(s);  
	                    }  
	                }  
	            }  
	        }  
	        LOG.info("total proxy IP number:： "+allIPs.size());  
	        return allIPs;  
	    }  
	public static Vector<String> getValidIPs(Vector<String> allIPs) throws ClientProtocolException, IOException{
				Vector<String> validIPs = new Vector<String>();
			Vector<String> validHostname = new Vector<String>();
			for(int i = 0 ; i < allIPs.size(); i++){  
	            String ip = allIPs.get(i);  
	            String hostName = ip.split(":")[0];  
	            int port = Integer.parseInt(ip.split(":")[1]);  
	           // String varifyURL = "http://iframe.ip138.com/ic.asp";//http://ip.uee.cn/ http://iframe.ip138.com/ic.asp  
	            String varifyURL = "http://www.whatismyip.com.tw/";//http://ip.uee.cn/ http://iframe.ip138.com/ic.asp  
	            String content = new HTTPHandler().getHTMLbyProxy(varifyURL, hostName, port);  
	            int iReconn=0;
	            while(content.equals("null")){//reconnect 2 times (total 3 times connection)  
	                if(iReconn == 2){  
	                    break;  
	                      }
	                content = new HTTPHandler().getHTMLbyProxy(varifyURL, hostName, port);  
	                iReconn++;  
	                }  
	            Pattern p = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");  
	            Matcher m = p.matcher(content);  
	            String s;  
	            if(m.find()){  
	                s = m.group();  
	                if(!validHostname.contains(s)){  
	                    validHostname.add(s);//  
	                    validIPs.add(s+":"+String.valueOf(port));  
	                    //bw.write(s+"\r\n");//write a valid proxy ip   
	                    LOG.info("valid proxy IP "+ s+":"+String.valueOf(port));  
	                    }  
	                }  
	            else{  
	                LOG.debug("html doesn't contain an IP");
	                }  
	        }  
			return validIPs;
	}
	 public static Vector<String> classifyIPs(Vector<String> validIPs, String plainPath) throws ClientProtocolException, IOException{  
	        final String verificationURL = "http://s.weibo.com/weibo/李雪山hakka&nodup=1&page=1";  
	        FileWriter fw = new FileWriter(plainPath);
	        //Vector<String> utf8IPs = new Vector<String>();  
	        Vector<String> plainIPs = new Vector<String>();  
	        String ip;  
	        //int connectionTime = 0;  
	        for(int i = 0; i < validIPs.size(); i++){  
	            ip = validIPs.get(i);  
	            String html = new HTTPHandler().getHTMLbyProxy(verificationURL, ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));  
	            int iReconn = 0;  
	            while(html.equals("null")){  
	                if(iReconn == 4){  
	                    break;  
	                }  
	                html = new HTTPHandler().getHTMLbyProxy(verificationURL, ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));  
	                iReconn++;  
	                LOG.debug("****"+ip+"is reconnecting the"+iReconn+" time****");  
	                  
	            }  
	                    if(html.contains("version=2012")){  
	                        plainIPs.add(ip);  
	                        LOG.info("plain: "+ip);  
	                        fw.write(ip);
	                    }  
	              
	        }  
	          fw.close();
	        return plainIPs;  
	    }  
	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException{
			//String[] html = new HTTPHandler().getHTML("http://www.kuaidaili.com");
			//Vector<String> allIPs = ProxyIP.getAllProxyIPs("http://www.kuaidaili.com/free/inha/",354 );
		//Vector<String> validIPs = ProxyIP.getValidIPs(allIPs);
		// ProxyIP.classifyIPs(validIPs,"/home/hadoop/NLPProject/nlpproject/data/weibo/plainIPs.txt");
	   String result = new HTTPHandler().getHTMLbyProxy("http://s.weibo.com/weibo/我是歌手&nodup=1&page=3","221.10.102.203",81);
		//System.out.println(result);
	//	FileReader fr = new FileReader(new File("/home/hadoop/NLPproject/nlpproject/source/temp"));
	//	BufferedReader bf = new BufferedReader(fr);
	//	StringBuilder sb = new StringBuilder();
	//	String tempString = bf.readLine();
	//	while(tempString != null) {
	//		sb.append(tempString);
	//		tempString = bf.readLine();
	//	}
		 HTMLParser hp = new HTMLParser();
		 hp.write2txt(result);
		
	
	}

}
