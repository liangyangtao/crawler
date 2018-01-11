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
 * @Title: PurchaseMajorSupplierParser.java
 * @Package com.kf.data.approved.parser.purchase
 * @Description: 关联采购解析
 * @author liangyt
 * @date 2018年1月9日 上午10:34:57
 * @version V1.0
 */
public class PurchaseMajorSupplierParser extends BaseParser {

	/****
	 * 
	 * @param 关联采购
	 */
	public List<Map<String, Object>> paserMajorSupplier(Document document) {
		List<Map<String, Object>> supplies = new ArrayList<>();
		Elements pElements = document.select("div").first().children();
		List<Element> result = fillMajorSupplierResult(pElements);
		if (result == null) {
			return supplies;
		}
		if (result.size() == 0) {
			return supplies;
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
			String purchase_amount = null;
			// String sales_amount_ratio = null;
			String supplier_name = null;
			String date = null;

			for (int k = 0; k < tdElements.size(); k++) {
				String key = firstTdElements.get(k).text().trim();
				String value = tdElements.get(k).text().trim();
				if (value.contains("合计") || value.contains("序号") || value.contains("合  计")) {
					break;
				}
				if (key.contains("关联方")) {
					supplier_name = value;
				} else if (key.contains("年金额")) {
					purchase_amount = value;
					date = key.replace("年金额", "");
					try {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("supplier_name", supplier_name);
						map.put("purchase_amount", purchase_amount);
						map.put("date", date);
						supplies.add(map);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}
		}
		return supplies;

	}

	/****
	 * 寻找符合条件的表格
	 * 
	 * @param elements
	 * @return
	 */
	public List<Element> fillMajorSupplierResult(Elements elements) {
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
						if (preElement.text().contains("关联采购")) {
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
								if (preElement2.text().contains("关联采购")) {
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
