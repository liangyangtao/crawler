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
import com.kf.data.mybatis.entity.TycHumans;

/****
 * 
 * @Title: HumansApp.java
 * @Package com.kf.data.tianyancha
 * @Description: 爬取天眼查人名
 * @author liangyt
 * @date 2017年10月11日 下午1:56:50
 * @version V1.0
 */
public class HumansApp {

	public static String supNameIndex = "赵";
	public static boolean isSupCrawler = false;

	/***
	 * 程序入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		KfConstant.init();
		try {
			String path = HumansApp.class.getClassLoader().getResource("").toURI().getPath();
			File file = new File(path + File.separator + "surname.txt");
			if (file.exists()) {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
				String line = "";
				line = br.readLine();
				supNameIndex = line;
				br.close();
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String url = "https://www.tianyancha.com/humans";
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html);
		Elements surNameElements = document.select(".personNum.float-left.c9");
		for (int i = 0; i < surNameElements.size(); i++) {
			Element element = surNameElements.get(i);
			String surName = element.text().trim();
			if (supNameIndex.equals(surName)) {
				isSupCrawler = true;
			}
			if (isSupCrawler) {
				String surHref = element.absUrl("href");
				parserList(surName, surHref);
			}

		}

	}

	/****
	 * 解析列表页
	 * 
	 * @param surName
	 * @param surHref
	 */
	private static void parserList(String surName, String surHref) {
		String html = Fetcher.getInstance().get(surHref);
		Document document = Jsoup.parse(html);
		parserDetail(surName, document);

		Elements pageElements = document.select(".company_pager");
		int pageNum = 0;
		if (pageElements.size() == 1) {
			Elements totalElements = pageElements.first().select(".total");
			String pageNumStr = totalElements.first().text();
			pageNumStr = pageNumStr.replace("共", "");
			pageNumStr = pageNumStr.replace("页", "");
			pageNum = Integer.parseInt(pageNumStr);
		}
		if (pageNum > 0) {
			for (int i = 2; i <= pageNum; i++) {
				String pageUrl = surHref + "/p" + i;
				String html1 = Fetcher.getInstance().get(pageUrl);
				Document document1 = Jsoup.parse(html1);
				parserDetail(surName, document1);
			}
		}

	}

	/***
	 * 解析详情页
	 * 
	 * @param surName
	 * @param document
	 */
	private static void parserDetail(String surName, Document document) {
		Elements elements = document.select("a.new-c2");
		for (Element element : elements) {
			System.out.println(element);
			TycHumans tycHumans = new TycHumans();
			tycHumans.setName(element.text().trim());
			tycHumans.setSurname(surName);
			tycHumans.setTycLink(element.absUrl("href"));
			sendJson(tycHumans, "tyc_humans");
		}

	}

	/***
	 * 保存姓名
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
