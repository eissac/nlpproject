package cn.edu.jnu.ie.crawl;

import java.io.IOException;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.util.Constant;
import cn.edu.jnu.ie.util.FileOperation;

public class PlainIPs {
  public static final Logger LOG = LoggerFactory.getLogger(PlainIPs.class);
  private Vector<IPAddress> IPs = new Vector<IPAddress>(); 
  private static final PlainIPs INSTANCE = new PlainIPs();
  private int IPPointer;
  private PlainIPs() {
	this.IPs = getPlainIPs();
	this.IPPointer = 0;
  }
  public boolean hasNext() {
	return (IPPointer < IPs.size());
  }
  public int getSize() {
	return IPs.size();
  }
  public IPAddress get(int index) {
	return IPs.get(index);
  }
  public IPAddress get() {
	IPAddress ip;
	if (hasNext()){
	  ip = IPs.get(IPPointer);
	IPPointer++;}
	else {
	  IPPointer=0;
	  ip=IPs.get(0);
	  LOG.warn("All IPs has tried 4 times ! No IP is valid!");
	}
	return ip;
  }
  private Vector<IPAddress>  getPlainIPs() {
   Vector<IPAddress> IPs = new Vector<IPAddress>(); 
	try {
		IPAddress IP;
		for (String ipStr : FileOperation.getLines(Constant.PLAINIPPATH)) {
			IP = IPAddress.getIPAddress(ipStr);
			IPs.add(IP);
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	return IPs;
}
  public static PlainIPs getInstance() {
	return INSTANCE;
  }
}
