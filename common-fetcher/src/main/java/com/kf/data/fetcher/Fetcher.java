package com.kf.data.fetcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @Title: Fetcher.java
 * @Package com.kf.data.crawler.tianyancha.fetcher
 * @Description: httpclient 工具类， 输入url 返回 html
 * @author liangyt
 * @date 2017年5月2日 下午3:55:42
 * @version V1.0
 */
public class Fetcher {
	private BasicCookieStore cookieStore;
	private CloseableHttpClient httpClient;
	private final String _DEFLAUT_CHARSET = "utf-8";
	private boolean proxy = false;
	public static Fetcher fetcher = Fetcher.getInstance();

	public Fetcher(BasicCookieStore cookieStore, CloseableHttpClient httpClient) {
		this.cookieStore = cookieStore;
		this.httpClient = httpClient;
	}

	public synchronized static Fetcher getInstance() {
		if (fetcher == null) {
			PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
			BasicCookieStore cookieStore = new BasicCookieStore();
			HttpClientBuilder httpClientBuilder = new HttpClientBuilder(poolingHttpClientConnectionManager,
					cookieStore);
			CloseableHttpClient httpClient = httpClientBuilder.getHttpClient();
			fetcher = new Fetcher(cookieStore, httpClient);
		}
		return fetcher;
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
	}

	public String get(String url) {
		return get(url, _DEFLAUT_CHARSET);
	}

	public String get(String url, String charset) {
		return get(url, null, charset);
	}

	public String inputStream2String(InputStream is, String charset) {
		String temp = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int i = -1;
			while ((i = is.read()) != -1) {
				baos.write(i);
			}
			temp = baos.toString(charset);
			if (temp.contains("???") || temp.contains("�")) {
				Pattern pattern = Pattern.compile("<meta[\\s\\S]*?charset=\"{0,1}(\\S+?)\"\\s{0,10}/{0,1}>");
				Matcher matcher = pattern.matcher(temp.toLowerCase());
				if (matcher.find()) {
					charset = matcher.group(1);
				} else {
					charset = "gbk";
				}
				temp = baos.toString(charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return temp;

	}

	public String get(String url, Map<String, String> headers, String charset) {
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		String useCharset = charset;
		if (charset == null) {
			useCharset = _DEFLAUT_CHARSET;
		}
		HttpGet httpGet = new HttpGet(url);
		fillHeader(url, httpGet);
		httpGet.setConfig(requestConfig());
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet, context);
			try {
				HttpEntity entity = response.getEntity();
				return inputStream2String(entity.getContent(), useCharset);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String post(String url, Map<String, String> params, Map<String, String> headers, String charset) {
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		String useCharset = charset;
		if (charset == null) {
			useCharset = _DEFLAUT_CHARSET;
		}
		try {
			HttpPost httpPost = new HttpPost(url);
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpPost.setHeader(key, headers.get(key));
				}
			} else {
				fillHeader(url, httpPost);
			}
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params != null) {
				for (String key : params.keySet()) {
					nvps.add(new BasicNameValuePair(key, params.get(key)));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			}
			httpPost.setConfig(requestConfig());
			CloseableHttpResponse response = httpClient.execute(httpPost, context);
			try {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, useCharset);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String postSave(String url, Map<String, String> params, Map<String, String> headers, String charset) {
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		String useCharset = charset;
		if (charset == null) {
			useCharset = _DEFLAUT_CHARSET;
		}
		try {
			HttpPost httpPost = new HttpPost(url);
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpPost.setHeader(key, headers.get(key));
				}
			} else {
				fillHeader(url, httpPost);
			}
			httpPost.setHeader("token", "kfsave");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params != null) {
				for (String key : params.keySet()) {
					nvps.add(new BasicNameValuePair(key, params.get(key)));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			}
			httpPost.setConfig(requestConfig());
			CloseableHttpResponse response = httpClient.execute(httpPost, context);
			try {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, useCharset);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private RequestConfig requestConfig() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
		return requestConfig;

	}

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

	public void downFile(String url, String pname, String filename) {
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		HttpGet httpGet = new HttpGet(url);
		fillHeader(url, httpGet);
		httpGet.setConfig(requestConfig());
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet, context);
			// 获取请求结果
			HttpEntity httpEntity = response.getEntity();
			// 将结果转为流了
			InputStream inputStream = httpEntity.getContent();
			// 将流写成文件就行了
			String path = Fetcher.class.getClassLoader().getResource("").getPath();

			File file = new File(path + File.separator + pname);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(file.getAbsoluteFile() + File.separator + filename);
			FileOutputStream output = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int l;
			while ((l = inputStream.read(b)) != -1) {
				output.write(b, 0, l);
			}
			output.flush();
			inputStream.close();
			output.close();
			response.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
