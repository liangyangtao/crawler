package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.TableSpliter;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;

/****
 * 
 * @Title: PublicMajorClientParser.java
 * @Package com.kf.data.pdfparser.parser
 * @Description: 公转书 主要客户
 * @author liangyt
 * @date 2017年10月24日 下午5:16:46
 * @version V1.0
 */
public class PublicMajorClientParser extends PublicBaseParser {

	/****
	 * 解析公转书 主要客户
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

			for (int l = 0; l < result.size(); l++) {
				Element element = result.get(l);
				if (element.tagName().equals("table")) {
					String time = null;
					if (l - 1 > 0) {
						Element preElement = result.get(l - 1);
						if (preElement.tagName().equals("p")) {
							String preText = preElement.text();
							time = StringUtils.substringBefore(preText, "公司前五名客户");
						}
					}

					Element resultTable = element;
					Elements trElements = resultTable.select("tr");
					Elements firstTdElements = trElements.first().select("td");
					if (time == null) {
						if (trElements.text().contains("@@")) {
							for (Element element2 : firstTdElements) {
								if (element2.text().contains("@@")) {
									time = StringUtils.substringBefore(element2.text().trim(), "@@");
									break;
								}
							}
						}

					}
					Map<String, String> salesInfoMap = new HashMap<String, String>();
					salesInfoMap.put("value", time);
					salesInfoMap.put("tableName", tableName);
					salesInfoMap.put("property", "sales_date");

					for (int j = 1; j < trElements.size(); j++) {

						List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();
						Element trElement = trElements.get(j);
						Elements tdElements = trElement.select("td");
						if (tdElements.size() == 0) {
							continue;
						}
						for (int k = 0; k < tdElements.size(); k++) {
							String key = firstTdElements.get(k).text().trim();
							String value = tdElements.get(k).text().trim();
							Map<String, String> resultInfoMap = new HashMap<String, String>();
							if (value.contains("合计") || value.contains("客户名称") || value.contains("合  计")) {
								break;
							}
							String property = null;
							
							if (key.contains("名称")) {
								property = "client_name";
							} else if (key.contains("比例")) {
								property = "sales_amount_ratio";
							} else if (key.contains("收入") || key.contains("销售额")) {
								property = "sales_amount";
							} else if(key.contains("序号")){
								property = "num";
							}
							if (property != null) {
								resultInfoMap.put("value", value);
								resultInfoMap.put("tableName", tableName);
								resultInfoMap.put("property", property);
								infoEntity.add(resultInfoMap);

							}
						}
						if (infoEntity.size() != 0) {
							infoEntity.add(companyidMap);
							// link
							infoEntity.add(linkMap);
							// pdfType
							infoEntity.add(pdfTypeMap);
							// 时间
							infoEntity.add(timeMap);
							// noticeId
							infoEntity.add(noticeIdMap);
							// report
							infoEntity.add(reportDateMap);
							//
							infoEntity.add(salesInfoMap);
							infoList.add(infoEntity);
						}

					}
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
			if (childElement.tagName().equals("table")) {
				childElement = new TableSpliter().splitTable(childElement, false, null);
				Elements firstTrElements = childElement.select("tr");
				if (firstTrElements == null || firstTrElements.size() == 0) {
					continue;
				}
				Element firstTrElement = firstTrElements.first();
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
					System.out.println(firstTrText);
					if (firstTrText.contains("客户名称") && (firstTrText.contains("收入") || firstTrText.contains("销售额"))
							&& firstTrText.contains("比例")) {
						if (j - 1 > 0) {
							Element preElement = elements.get(j - 1);
							if (preElement.text().contains("前五名客户")) {
								result.add(preElement);
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
									if (preElement2.text().contains("前五名客户")) {
										result.add(preElement);
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
