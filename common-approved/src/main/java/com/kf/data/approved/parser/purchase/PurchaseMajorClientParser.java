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

/***
 * 
 * @Title: PurchaseMajorClientParser.java
 * @Package com.kf.data.approved.parser.purchase
 * @Description: 关联销售解析
 * @author liangyt
 * @date 2018年1月9日 上午10:35:35
 * @version V1.0
 */
public class PurchaseMajorClientParser extends BaseParser {

	/****
	 * 
	 * @param 关联销售
	 */
	public List<Map<String, Object>> paserMajorClient(Document document) {
		List<Map<String, Object>> clients = new ArrayList<>();

		Elements pElements = document.select("div").first().children();
		List<Element> result = fillMajorClientResult(pElements);
		if (result == null) {
			return clients;
		}
		if (result.size() == 0) {
			return clients;
		}
		Element resultTable = new Element(Tag.valueOf("table"), "");
		for (int k = 0; k < result.size(); k++) {
			Element element = result.get(k);
			Elements trElements = element.select("tr");
			trElements = element.select("tr");
			for (Element trElement : trElements) {
				resultTable.appendChild(trElement);
			}
		}
		resultTable = new TableSpliter().splitTable(resultTable, false, null);
		Elements trElements = resultTable.select("tr");
		Elements firstTdElements = trElements.first().select("td");

		for (int j = 1; j < trElements.size(); j++) {
			Element trElement = trElements.get(j);
			Elements tdElements = trElement.select("td");
			if (tdElements.size() == 0) {
				continue;
			}
			String sales_amount = null;
			// String sales_amount_ratio = null;
			String client_name = null;
			String date = null;

			for (int k = 0; k < tdElements.size(); k++) {
				String key = firstTdElements.get(k).text().trim();
				String value = tdElements.get(k).text().trim();

				if (value.contains("合计") || value.contains("序号") || value.contains("合  计")) {
					break;
				}

				if (key.contains("关联方")) {
					client_name = value;
				} else if (key.contains("年金额")) {
					sales_amount = value;
					date = key.replace("年金额", "");
					try {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("client_name", client_name);
						map.put("sales_amount", sales_amount);
						map.put("date", date);
						clients.add(map);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}
		}
		return clients;

	}

	/****
	 * 寻找符合条件的表格
	 * 
	 * @param elements
	 * @return
	 */
	public List<Element> fillMajorClientResult(Elements elements) {
		List<Element> result = new ArrayList<Element>();
		int colNum = 0;
		for (int j = 0; j < elements.size(); j++) {
			Element childElement = elements.get(j);
			if (childElement.tagName().equals("table")) {
				Elements firstTrElements = childElement.select("tr");
				if (firstTrElements == null || firstTrElements.size() == 0) {
					continue;
				}
				Element firstTrElement = firstTrElements.first();
				String firstTrText = firstTrElement.text();
				firstTrText = replacekong(firstTrText);
				if (firstTrText.contains("关联方") && firstTrText.contains("金额")) {
					if (j - 1 > 0) {
						Element preElement = elements.get(j - 1);
						if (preElement.text().contains("关联销售")) {
							result.add(childElement);
							Elements trElements = childElement.select("tr");
							// 是表头的那个表
							for (Element trElement : trElements) {
								Elements tdElements = trElement.select("td,th");
								if (colNum < tdElements.size()) {
									colNum = tdElements.size();
								}
							}
						} else {
							if (j - 2 > 0) {
								Element preElement2 = elements.get(j - 2);
								if (preElement2.text().contains("关联销售")) {
									result.add(childElement);
									Elements trElements = childElement.select("tr");
									for (Element trElement : trElements) {
										Elements tdElements = trElement.select("td,th");
										if (colNum < tdElements.size()) {
											colNum = tdElements.size();
										}
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
