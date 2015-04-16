package cn.edu.jnu.ie.crawl;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import cn.edu.jnu.ie.util.Constant;
import cn.edu.jnu.ie.util.FileOperation;

public class IPAddress {
	private String host;
	private int port;
	private IPAddress (String host  ,int port ) {
		this.setHost(host);
		this.setPort(port);
	}
	private IPAddress (String host  ,String port ) {
		this.setHost(host);
		this.setPort(port);
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int  getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setPort(String port) {
		this.port = Integer.parseInt(port);
	}
	public static IPAddress getIPAddress(String host,int port) {
		return new IPAddress (host,port);
	}
	public static IPAddress getIPAddress(String ipStr) {
		String[] ipInfo = ipStr.split(":");
		String host = ipInfo[0];
		String port = ipInfo[1];
		return new IPAddress (host,port);
	}
	public String toString() {
		return this.host+":"+this.port;
	}
	 public static Vector<IPAddress>  getIPsFromFile(String inFile) {
		   Vector<IPAddress> IPs = new Vector<IPAddress>(); 
			try {
				IPAddress IP;
				for (String ipStr : FileOperation.getLines(inFile)) {
					IP = IPAddress.getIPAddress(ipStr);
					IPs.add(IP);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return IPs;
		}
	
}
