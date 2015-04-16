package cn.edu.jnu.ie.data;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.crawl.ProxyIP;

public class Configuration {
	public static final Logger LOG = LoggerFactory.getLogger(ProxyIP.class);
	private PropertiesConfiguration prop;
	static Configuration conf = new Configuration();
	private Configuration(){
		try{
			prop=new PropertiesConfiguration("conf/crawl.properties");
			prop.setEncoding("UTF-8");
		}
		catch (ConfigurationException e) {
			System.out.println(e.getMessage());
		}
	}
	static {
		}
	public static Configuration getConf() {
		return conf;
	}
	public String get(String key) {
		return prop.getString(key);
	}
	public List<String> getList(String key){
		@SuppressWarnings("unchecked")
		List<String> list= prop.getList(key);
		return list;
	}
}
