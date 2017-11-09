package com.kf.data.tianyancha;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.fetcher.Fetcher;
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
public class CompanyApp {

	public static String companyCityIndex = "北京";
	public static boolean isSupCrawler = false;

	public static void main(String[] args) {
		KfConstant.init();
		try {
			String path = CompanyApp.class.getClassLoader().getResource("").toURI().getPath();
			File file = new File(path + File.separator + "city.txt");
			if (file.exists()) {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
				String line = "";
				line = br.readLine();
				companyCityIndex = line.trim();
				br.close();
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		new CompanyApp().crawlerCitys();
	}

	/***
	 * 爬取城市列表
	 */
	private void crawlerCitys() {
		String url = "https://www.tianyancha.com/companies/cities";
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html, url);
		Elements citys = document.select("a.baseFirstLine,a.new-c2");
		for (Element element : citys) {
			String city = element.text();
			String href = element.attr("href");
			System.out.println(city);
			if (city.equals(companyCityIndex)) {
				isSupCrawler = true;
			}
			if (isSupCrawler) {
				crawlerType(city, href);
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/****
	 * 爬取分类列表
	 * 
	 * @param city
	 * @param href
	 */
	private void crawlerType(String city, String url) {
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html, url);
		Elements typeElements = document.select(".industryBox > a");
		for (Element element : typeElements) {
			String href = element.attr("href");
			String type = element.text();
			System.out.println(type);
			crawlerCompany(city, type, href);
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		String html = Fetcher.getInstance().get(url);
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
