package com.kf.data.wenshu.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.WenshucourtContentWithBLOBs;
import com.kf.data.mybatis.entity.WenshucourtDataWithBLOBs;

/**
 * Author:杨庆辉 Time:2017年7月17日 下午1:38:41 Desp:
 */
public class WenshuParser {

	private static String webUrl = "http://wenshu.court.gov.cn";

	/**
	 * 解析列表页面
	 * 
	 * @param html
	 * @return
	 */
	public List<WenshucourtDataWithBLOBs> parserList(String html) {
		List<WenshucourtDataWithBLOBs> wscds = new ArrayList<WenshucourtDataWithBLOBs>();
		Document document = Jsoup.parse(html);
		Element resulitList = document.getElementById("resultList");
		if (resulitList == null) {
			return wscds;
		}
		Elements dataItems = resulitList.select("div.dataItem");
		if (dataItems == null || dataItems.size() == 0) {
			return wscds;
		}
		for (Element dataItem : dataItems) {
			WenshucourtDataWithBLOBs wscd = new WenshucourtDataWithBLOBs();
			Element labelDiv = dataItem.select("div.label").first();
			if (labelDiv != null) {
				String label = labelDiv.text();
				wscd.setLabel(label);
			}
			Element docA = dataItem.getElementsByTag("a").first();
			if (docA != null) {
				String detailedUrl = webUrl + docA.attr("href");
				String title = docA.text();
				wscd.setDetailedUrl(detailedUrl);
				wscd.setTitle(title);
			}
			Element infoDiv = dataItem.getElementsByClass("fymc").first();
			if (infoDiv != null) {
				String info = infoDiv.text();
				// System.out.println("信息："+info);
				wscd.setInfo(info);
				// 获取时间
				try {
					String timeString = matcherString(info, "\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}");
					if (timeString != null) {
						wscd.setPubTime(new SimpleDateFormat("yyyy-MM-dd").parse(timeString));
					}
				} catch (Exception e) {
                       e.printStackTrace();
                       
				}
				// 获取文书号
				String caseId = matcherString(info, "(\\（)([\\d]{4})([\\s\\S]*?)(\\号)");
				wscd.setCaseId(caseId);
				// 获取法院
				String justiceName = matcherString(info, "([\\s\\S]*?)(\\法院)");
				wscd.setJusticeName(justiceName);
				wscd.setInfotime(new Date());
			}
			wscds.add(wscd);
		}
		return wscds;
	}

	/**
	 * 处理详情页
	 * 
	 * @param html
	 * @return
	 */
	public WenshucourtContentWithBLOBs parserContent(String html) {
		Document document = Jsoup.parse(html);
		Element divContent = document.getElementById("DivContent");
		if (divContent != null) {
			WenshucourtContentWithBLOBs wenshucourtContent = new WenshucourtContentWithBLOBs();
			wenshucourtContent.setContent(divContent.text());
			wenshucourtContent.setContentOrgDiv(divContent.toString());
			return wenshucourtContent;
		} else {
			return null;
		}

	}

	/**
	 * 正则匹配字符串
	 * 
	 * @param string
	 * @param rgex
	 * @return
	 */
	public String matcherString(String string, String rgex) {
		String result = null;
		Pattern pattern = Pattern.compile(rgex);
		Matcher matcher = pattern.matcher(string);
		while (matcher.find()) {
			result = matcher.group(0);
		}
		return result;
	}

}
