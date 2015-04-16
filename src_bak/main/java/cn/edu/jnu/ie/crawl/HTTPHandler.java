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

public class HTTPHandler {
	public HTTPHandler(){
		;
	}
	public String[] getHTML(String url) throws ClientProtocolException, IOException{
		String[] html = new String[2];
		html[1] = "null";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(2000)//设置socket超时时间
				.setConnectTimeout(2000)//设置connect超时时间
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.build();
		HttpGet httpGet = new HttpGet(url);
		try{
			CloseableHttpResponse response = httpClient.execute(httpGet);
			//System.out.println(response.getStatusLine().getStatusCode());
			html[0] =  String.valueOf(response.getStatusLine().getStatusCode());
			html[1] = EntityUtils.toString(response.getEntity(), "utf8");
			//System.out.println(html);
		} catch(IOException e){
			System.out.println("****Connection time out****");
		}
		
		return html;
	}
	public String getHTMLbyProxy(String targetURL, String hostName, int port) throws ClientProtocolException, IOException{
		HttpHost proxy = new HttpHost(hostName, port);
		String html = "null";
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(2000)//设置socket超时时间
				.setConnectTimeout(2000)//设置connect超时时间
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setRoutePlanner(routePlanner)
				.setDefaultRequestConfig(requestConfig)
				.build();
		HttpGet httpGet = new HttpGet(targetURL);//"http://iframe.ip138.com/ic.asp"
		try{
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			//System.out.println(response.getStatusLine().getStatusCode());
			if(statusCode == HttpStatus.SC_OK){
				html = EntityUtils.toString(response.getEntity(), "utf-8");
			}
			response.close();
			//System.out.println(html);//打印返回的html
		} catch(IOException e){
			System.out.println("****Connection time out****");
		}
		
		return html;
	}
	public String getHTML(String url, String hostName, int port) throws URISyntaxException, ClientProtocolException, IOException {
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
		String html = "null";//用于验证是否正常取到html
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
}
