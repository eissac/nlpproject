package cn.edu.jnu.ie.crawl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.util.Constant;

public class HTTPHandler {
	  private static PlainIPs plainIPs = PlainIPs.getInstance();
	  private static IPAddress plainIP=plainIPs.get();
	  public static final Logger LOG = LoggerFactory.getLogger(HTTPHandler.class);
	public HTTPHandler(){
		;
	}
	/**
	 * 根据url返回页面内容及状态号，不使用代理IP。
	 * 如果获取内容为空，则提示connection time out，返回默认页面内容为“failed”
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String[] getHTML(String url) throws ClientProtocolException, IOException{
		String[] html = new String[2];
		html[1] = "failed";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(10000)//设置socket超时时间
				.setConnectTimeout(10000)//设置connect超时时间
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.build();
		HttpGet httpGet = new HttpGet(url);
		try{
			CloseableHttpResponse response = httpClient.execute(httpGet);
			html[0] =  String.valueOf(response.getStatusLine().getStatusCode());
			html[1] = EntityUtils.toString(response.getEntity(), "utf8");
		} catch(IOException e){
			LOG.warn("connection time out");
		}
		return html;
	}
	
	
	public boolean checkNull(String htmlContent) {
		return htmlContent == null;
	}
	public static  String getHTMLbyProxy(String url)  {
		String html = "failed";
		int failedCount = 0;
		while (html.equals("failed"))
		{
			try {
				html = getHTMLbyProxy(url,plainIP.getHost(),plainIP.getPort());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				LOG.error("error url protocol:"+url+" !");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (failedCount>=Constant.MAXRETRY) {
				failedCount = 0;
				LOG.warn("plainIP :"+plainIP.toString()+"failed 5 times ,try next one");
				plainIP=plainIPs.get();
			}
		}
			return html;
	}
	
	/**
	 *  根据url返回页面内容，使用代理IP。
	 * 如果获取内容为空，则提示connection time out，返回默认页面内容为“failed”
	 * @param targetURL
	 * @param hostName
	 * @param port
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHTMLbyProxy(String targetURL, String hostName, int port) throws ClientProtocolException, IOException{
		HttpHost proxy = new HttpHost(hostName, port);
		String html = "failed";
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(5000)//设置socket超时时间
				.setConnectTimeout(5000)//设置connect超时时间
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setRoutePlanner(routePlanner)
				.setDefaultRequestConfig(requestConfig)
				.build();
		HttpGet httpGet = new HttpGet(targetURL);//"http://iframe.ip138.com/ic.asp"
		try{
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK){
				html = EntityUtils.toString(response.getEntity(), "utf-8");
			}
			response.close();
		} catch(IOException e){
			System.out.println("****Connection time out****");
		}
		
		return html;
	}
public static  String getHTML(String url, String hostName, int port) throws URISyntaxException, ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		//采用用户自定义cookie策略，只是使cookie rejected的报错不出现，此错误仍然存在
		HttpHost proxy = new HttpHost(hostName, port);//58.22.28.133:80
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
			public CookieSpec create(HttpContext context) {
				return new BrowserCompatSpec() {
					@Override
					public void validate(Cookie cookie, CookieOrigin origin)
							throws MalformedCookieException {
						// Oh, I am easy
					}
				};
			}
		};
		Registry<CookieSpecProvider> r = RegistryBuilder
				.<CookieSpecProvider> create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY,
						new BrowserCompatSpecFactory())
				.register("easy", easySpecProvider).build();
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec("easy")
				.setSocketTimeout(16000)//设置socket超时时间
				.setConnectTimeout(16000)//设置connect超时时间
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultCookieSpecRegistry(r)
				.setRoutePlanner(routePlanner)
				.setDefaultRequestConfig(requestConfig).build();
		
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		String html = "failed";//用于验证是否正常取到html
		try{
			CloseableHttpResponse response = httpClient.execute(httpGet);
			System.out.println("statue code:  "+response.getStatusLine().getStatusCode());
			html = EntityUtils.toString(response.getEntity(), "utf8");//
			//System.out.println(html);//打印返回的html
		} catch(IOException e){
			System.out.println("****Connection time out****");
		}
		return html;
	}
	public static void main(String[] args) throws ClientProtocolException, IOException{
		HTTPHandler httphandler = new HTTPHandler();
		PlainIPs plainIPs = PlainIPs.getInstance();
		IPAddress ip = plainIPs.get();
		String html  = httphandler.getHTMLbyProxy("http://s.weibo.com/user/f&Refer=weibo_user",ip.getHost() ,ip.getPort());
		System.out.println(html);
		
	}
}
