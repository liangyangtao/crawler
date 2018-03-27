package com.kf.data.approved.parser.purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.kf.data.approved.parser.BaseParser;
import com.kf.data.fetcher.tools.TableSpliter;

/****
 * 
 * @Title: PurchaseLiabilitiesParser.java
 * @Package com.kf.data.approved.parser.purchase
 * @Description: 负债表
 * @author liangyt
 * @date 2018年1月10日 上午11:37:38
 * @version V1.0
 */
public class PurchaseLiabilitiesParser extends BaseParser {

	/****
	 * 解析利润表
	 * 
	 * @param pdfCodeTable
	 * @param pdfReportLinks
	 * @param document
	 * @return
	 */
	public List<Map<String, Object>> getLiabilitiesResult(Document document) {
		List<Map<String, Object>> resultMap = new ArrayList<>();
		try {
			Elements pElements = document.select("div").first().children();
			List<Element> result = fillLiabilitiesResult(pElements);
			if (result == null) {
				return resultMap;
			}
			if (result.size() == 0) {
				return resultMap;
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
			resultTable = new TableSpliter().splitTable(resultTable, false, null);
			Elements trElements = resultTable.select("tr");
			for (int j = 1; j < trElements.size(); j++) {
				Element trElement = trElements.get(j);
				Elements tdElements = trElement.select("td");
				for (int k = 1; k < tdElements.size(); k++) {

					Map<String, Object> categoryMap = new HashMap<String, Object>();
					String category = tdElements.get(0).text().trim();
					if (category.isEmpty()) {
						continue;
					}
					String value = tdElements.get(k).text().trim();
					String public_time = trElements.get(0).select("td").get(k).text().trim();

					if (public_time.isEmpty()) {
						continue;
					}
					if (public_time.contains("@@")) {
						String temp[] = public_time.split("@@");
						public_time = temp[0];
						// String type = temp[1];
					}
					categoryMap.put("category", category);
					categoryMap.put("amount", value);
					categoryMap.put("public_time", public_time);
					resultMap.add(categoryMap);

				}

			}

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
	public List<Element> fillLiabilitiesResult(Elements elements) {
		List<Element> result = new ArrayList<Element>();
		int colNum = 0;
		boolean isFind = false;
		for (int j = 0; j < elements.size(); j++) {
			Element childElement = elements.get(j);
			if (isFind) {
				if (childElement.text().contains("权益变动表") || childElement.text().contains("利润表")
						|| childElement.text().contains("现金流量表 ")) {
					break;
				}
			}
			if (childElement.tagName().equals("table")) {
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
					firstTrText = replacekong(firstTrText);
					if (firstTrText.contains("年")) {
						if (j - 1 > 0) {
							Element preElement = elements.get(j - 1);
							String preText = preElement.text();
							preText = replacekong(preText);
							if (preText.contains("负债表")) {
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
									String preText2 = preElement2.text();
									preText2 = replacekong(preText2);
									if (preText2.contains("负债表")) {
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
