package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;

/***
 * 
 * @Title: PublicLiabilitiesParser.java
 * @Package com.kf.data.pdfparser.parser
 * @Description: 公转书利润
 * @author liangyt
 * @date 2017年10月23日 下午3:08:52
 * @version V1.0
 */
public class PublicProfitParser extends PublicBaseParser {

	/****
	 * 解析公转书 利润
	 * 
	 * @param pdfCodeTable
	 * @param pdfReportLinks
	 * @param document
	 * @return
	 */
	public Map<String, Object> getResult(PdfCodeTable pdfCodeTable, PdfReportLinks pdfReportLinks, Document document) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String tableName = pdfCodeTable.getTableName();
			Map<String, String> companyidMap = new HashMap<String, String>();
			companyidMap.put("value", pdfReportLinks.getId() + "");
			companyidMap.put("tableName", tableName);
			companyidMap.put("property", "source_id");
			// link
			Map<String, String> linkMap = new HashMap<String, String>();
			linkMap.put("value", pdfReportLinks.getLink());
			linkMap.put("tableName", tableName);
			linkMap.put("property", "link");
			// pdfType
			Map<String, String> pdfTypeMap = new HashMap<String, String>();
			pdfTypeMap.put("value", pdfCodeTable.getPdfType());
			pdfTypeMap.put("tableName", tableName);
			pdfTypeMap.put("property", "pdfType");
			// 时间
			Map<String, String> timeMap = new HashMap<String, String>();
			timeMap.put("value", formatDate(new Date()));
			timeMap.put("tableName", tableName);
			timeMap.put("property", "up_time");
			// noticeId
			Map<String, String> noticeIdMap = new HashMap<String, String>();
			noticeIdMap.put("value", pdfReportLinks.getNoticeId() + "");
			noticeIdMap.put("tableName", tableName);
			noticeIdMap.put("property", "notice_id");
			// publishDate
			// Map<String, String> publishDateMap = new
			// HashMap<String, String>();
			// publishDateMap.put("value", publishDate);
			// publishDateMap.put("tableName", tableName);
			// publishDateMap.put("property", "publish_date");
			// report_date
			Map<String, String> reportDateMap = new HashMap<String, String>();
			reportDateMap.put("value", new SimpleDateFormat("yyyy-MM-dd").format(pdfReportLinks.getReportDate()));
			reportDateMap.put("tableName", tableName);
			reportDateMap.put("property", "report_date");

			List<List<Map<String, String>>> infoList = new ArrayList<List<Map<String, String>>>();
			Elements pElements = document.select("div").first().children();
			List<Element> result = fillResult(pElements);
			if (result == null) {
				return null;
			}
			if (result.size() == 0) {
				return null;
			}
			Element resultTable = new Element(Tag.valueOf("table"), "");
			for (int k = 0; k < result.size(); k++) {
				Element element = result.get(k);
				Elements trElements = element.select("tr");
				trElements = element.select("tr");
				// 是表头的那个表
				for (Element trElement : trElements) {
					resultTable.appendChild(trElement);
				}
			}
			Elements trElements = resultTable.select("tr");

			for (int j = 1; j < trElements.size(); j++) {
				Element trElement = trElements.get(j);
				Elements tdElements = trElement.select("td");
				for (int k = 1; k < tdElements.size(); k++) {
					List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();
					String category = tdElements.get(0).text().trim();
					if (category.isEmpty()) {
						continue;
					}
					Map<String, String> categoryMap = new HashMap<String, String>();
					categoryMap.put("value", category);
					categoryMap.put("tableName", tableName);
					categoryMap.put("property", "category");
					infoEntity.add(categoryMap);

					String value = tdElements.get(k).text().trim();
					String public_time = trElements.get(0).select("td").get(k).text().trim();
					if (public_time.isEmpty()) {
						continue;
					}
					if (public_time.contains("@@")) {
						// 2015年8月31日@@合并
						String temp[] = public_time.split("@@");
						public_time = temp[0];
						String type = temp[1];
						if (!pdfCodeTable.getPdfType().contains(type)) {
							continue;
						}
					}
					Map<String, String> resultInfoMap = new HashMap<String, String>();
					resultInfoMap.put("value", value);
					resultInfoMap.put("tableName", tableName);
					resultInfoMap.put("property", "amount");
					infoEntity.add(resultInfoMap);
					Map<String, String> publicTimeMap = new HashMap<String, String>();
					publicTimeMap.put("value", public_time);
					publicTimeMap.put("tableName", tableName);
					publicTimeMap.put("property", "public_time");
					infoEntity.add(publicTimeMap);
					infoEntity.add(companyidMap);
					// link
					infoEntity.add(linkMap);
					// pdfType
					infoEntity.add(pdfTypeMap);
					// 时间
					infoEntity.add(timeMap);
					// noticeId
					infoEntity.add(noticeIdMap);
					// publishDateMap
					infoEntity.add(reportDateMap);
					infoList.add(infoEntity);

				}

			}

			resultMap.put("state", "ok");
			resultMap.put("info", infoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/****
	 * 寻找符合条件的表格
	 * 
	 * @param elements
	 * @return
	 */
	private List<Element> fillResult(Elements elements) {
		List<Element> result = new ArrayList<Element>();
		int colNum = 0;
		boolean isFind = false;
		for (int j = 0; j < elements.size(); j++) {
			Element childElement = elements.get(j);
			if (isFind) {
				if (childElement.text().contains("合并现金流量表")) {
					break;
				}
			}
			if (childElement.tagName().equals("table")) {
				Elements firstTrElements = childElement.select("tr");
				if(firstTrElements==null || firstTrElements.size() ==0){
					continue;
				}
				Element firstTrElement=	firstTrElements.first();
				
				
				
				if (isFind) {
					int col = 0;
					Elements trElements = childElement.select("tr");
					for (Element trElement : trElements) {
						Elements tdElements = trElement.select("td,th");
						if (col < tdElements.size()) {
							col = tdElements.size();
						}
					}
					if (col == colNum) {
						result.add(childElement);
					} else {
						break;
					}
				} else {
					String firstTrText = firstTrElement.text();
					firstTrText = firstTrText.replace("  ", "");
					firstTrText = firstTrText.replace(" ", "");
					firstTrText = firstTrText.replace("	", "");
					firstTrText = firstTrText.replace(" ", "");
					if (firstTrText.contains("年")) {
						if (j - 1 > 0) {
							Element preElement = elements.get(j - 1);
							if (preElement.text().contains("合并利润表")) {
								result.add(childElement);
								Elements trElements = childElement.select("tr");
								// 是表头的那个表
								for (Element trElement : trElements) {
									Elements tdElements = trElement.select("td,th");
									if (colNum < tdElements.size()) {
										colNum = tdElements.size();
									}
								}
								isFind = true;
							} else {
								if (j - 2 > 0) {
									Element preElement2 = elements.get(j - 2);
									if (preElement2.text().contains("合并利润表")) {
										result.add(childElement);
										Elements trElements = childElement.select("tr");
										// 是表头的那个表
										for (Element trElement : trElements) {
											Elements tdElements = trElement.select("td,th");
											if (colNum < tdElements.size()) {
												colNum = tdElements.size();
											}
										}
										isFind = true;
									} else {

									}
								}
							}
						}

					}
				}
			}
		}
		return result;
	}

}
