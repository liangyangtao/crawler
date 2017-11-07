package com.kf.data.tianyancha;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.HttpClientBuilder;
import com.kf.data.fetcher.tools.KfConstant;

/****
 * 
 * @Title: CompanyApp.java
 * @Package com.kf.data.tianyancha
 * @Description: 天眼查公司采集
 * @author liangyt
 * @date 2017年11月7日 下午1:45:08
 * @version V1.0
 */
public class CompanyApp2 {
	static CloseableHttpClient httpClient;

	public static void main(String[] args) {
		KfConstant.init();
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		BasicCookieStore cookieStore = new BasicCookieStore();
		HttpClientBuilder httpClientBuilder = new HttpClientBuilder(poolingHttpClientConnectionManager, cookieStore);
		httpClient = httpClientBuilder.getHttpClient();
		new CompanyApp2().crawlerCitys();
	}

	private RequestConfig requestConfig() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
		return requestConfig;
	}

	private void fillHeader(String url, HttpRequestBase httpGet) {
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:29.0) Gecko/20100101 Firefox/29.0");
		httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate");
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader("Referer", url);
		httpGet.setHeader("Cache-Control", "no-cache");
		httpGet.setHeader("Host", getDomain(url));
		httpGet.setHeader("Cookie",
				"TYCID=b430a1f0bfa311e7b3059df514467444; uccid=2e304bf3e59f1e913151768e20397e0c; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1509954257,1510017925,1510018522,1510033414; ssuid=349698225; tyc-user-info=%257B%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNTAwMTMwMzI2MSIsImlhdCI6MTUwOTYwOTU2MywiZXhwIjoxNTI1MTYxNTYzfQ.uP6KQuAk9UIQtB0QXPVekko7nrO3loujH9qAqk9kY7i2wmAxpJ8KN_o02s343JI28509W1qYTZuQBKnj9JAegw%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522onum%2522%253A%25220%2522%252C%2522mobile%2522%253A%252215001303261%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNTAwMTMwMzI2MSIsImlhdCI6MTUwOTYwOTU2MywiZXhwIjoxNTI1MTYxNTYzfQ.uP6KQuAk9UIQtB0QXPVekko7nrO3loujH9qAqk9kY7i2wmAxpJ8KN_o02s343JI28509W1qYTZuQBKnj9JAegw; RTYCID=5c058144fb5a417186dccefaefb8a43d; aliyungf_tc=AQAAAMvFlX00Sw4A8tvKAU9gspCkpgaL; csrfToken=CTh16s4CtwhP-FZTy6lr_xpz; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1510036232; bannerFlag=true");
	}

	/***
	 * 
	 * @param url
	 * @return
	 */
	private static String getDomain(String url) {
		String domain = "";
		try {
			URL u = new URL(url);
			domain = u.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return domain;
	}

	/***
	 * 
	 * @param url
	 * @return
	 */
	public String get(String url) {
		HttpClientContext context = HttpClientContext.create();
		HttpGet httpGet = new HttpGet(url);
		fillHeader(url, httpGet);
		httpGet.setConfig(requestConfig());
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet, context);
			try {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, "utf-8");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 爬取城市列表
	 */
	private void crawlerCitys() {
		String url = "https://www.tianyancha.com/companies/cities";
		String html = get(url);
		Document document = Jsoup.parse(html, url);
		Elements citys = document.select("a.baseFirstLine,a.new-c2");
		for (Element element : citys) {
			String city = element.text();
			String href = element.attr("href");
			System.out.println(city);
			crawlerType(city, href);
		}
	}

	/****
	 * 爬取分类列表
	 * 
	 * @param city
	 * @param href
	 */
	private void crawlerType(String city, String url) {
		String html = get(url);
		Document document = Jsoup.parse(html, url);
		Elements typeElements = document.select(".industryBox > a");
		for (Element element : typeElements) {
			String href = element.attr("href");
			String type = element.text();
			System.out.println(type);
			crawlerCompany(city, type, href);
		}

	}

	/***
	 * 爬取公司信息
	 * 
	 * @param city
	 * @param type
	 * @param href
	 */
	private void crawlerCompany(String city, String type, String url) {
		String html = get(url);
		Document document = Jsoup.parse(html, url);
		Elements hotElements = document.select(".hotCompany");
		if (hotElements.size() > 0) {
			Elements elements = hotElements.first().select("a");
			for (Element element : elements) {
				String company = element.text();
				System.out.println(company);
				String href = element.attr("href");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("city", city);
				map.put("company", company);
				map.put("type", type);
				map.put("url", href);
				sendJson(map, "tyc_companys");
			}

		}

	}

	/***
	 * 保存公司名称
	 * 
	 * @param object
	 * @param type
	 */
	public static void sendJson(Object object, String type) {
		String url = KfConstant.saveJsonIp;
		Map<String, String> params = new HashMap<String, String>();
		Gson gson = new GsonBuilder().create();
		params.put("json", gson.toJson(object));
		params.put("type", type);
		Fetcher.getInstance().postSave(url, params, null, "utf-8");
	}

}
