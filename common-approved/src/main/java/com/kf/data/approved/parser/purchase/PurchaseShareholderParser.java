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
 * @Title: PurchaseShareholderParser.java
 * @Package com.kf.data.parser.purchase
 * @Description: 收购书 股权结构
 * @author liangyt
 * @date 2018年1月4日 下午3:31:10
 * @version V1.0
 */
public class PurchaseShareholderParser extends BaseParser {

	public void parserPurchaseShareholder(Document document) {
		try {
			paserBeforeSholder(document);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			paserAfterSholder(document);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * 
	 * @param 收购后股权情况
	 */
	public List<Map<String, Object>> paserAfterSholder(Document document) {
		List<Map<String, Object>> sholders = new ArrayList<>();
		Elements pElements = document.select("div").first().children();
		List<Element> result = fillAfterShareholderResult(pElements);
		if (result == null) {
			return sholders;
		}
		if (result.size() == 0) {
			return sholders;
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
		Elements firstTdElements = trElements.first().select("td");
		for (int j = 1; j < trElements.size(); j++) {
			Element trElement = trElements.get(j);
			Elements tdElements = trElement.select("td");
			if (tdElements.size() == 0) {
				continue;
			}
			String quantity = null;
			String ratio = null;
			String shareholder = null;
			for (int k = 0; k < tdElements.size(); k++) {
				String key = firstTdElements.get(k).text().trim();
				String value = tdElements.get(k).text().trim();
				if (value.contains("合计") || value.contains("序号") || value.contains("合  计") || value.contains("收购人")
						|| value.contains("股东")) {
					break;
				}
				if (key.contains("前")) {

				} else {
					if (key.contains("股数")) {
						quantity = value;
					} else if (key.contains("比例")) {
						ratio = value;
					} else if ((key.contains("股东") || key.contains("收购人")) && !key.contains("性质")) {
						shareholder = value;
					}
				}

			}
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				if (shareholder == null) {
					continue;
				}
				map.put("shareholder", shareholder);
				map.put("quantity", quantity);
				map.put("ratio", ratio);
				sholders.add(map);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		return sholders;

	}

	/****
	 * 定位股权变动后
	 * 
	 * @param pElements
	 * @return
	 */
	public List<Element> fillAfterShareholderResult(Elements elements) {
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
				if (firstTrText.contains("序号") && firstTrText.contains("股东") && firstTrText.contains("持股")
						&& firstTrText.contains("比例")) {
					if (j - 1 > 0) {
						Element preElement = elements.get(j - 1);
						String preText = preElement.text();
						preText = replacekong(preText);
						if ((preText.contains("收购后") || preText.contains("交割完成后"))
								&& (preText.contains("股权结构") || preText.contains("权益变动") || preText.contains("股份"))) {
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
								String preText2 = preElement2.text();
								preText2 = replacekong(preText2);
								if ((preText2.contains("收购后") || preText2.contains("交割完成后"))
										&& (preText2.contains("股权结构") || preText2.contains("权益变动")
												|| preText2.contains("股份"))) {
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

								}
							}
						}
					}
				}
			}

		}
		return result;

	}

	/****
	 * 
	 * @param 收购前股权情况
	 */
	public List<Map<String, Object>> paserBeforeSholder(Document document) {
		List<Map<String, Object>> sholders = new ArrayList<>();

		Elements pElements = document.select("div").first().children();
		List<Element> result = fillBeforeShareholderResult(pElements);
		if (result == null) {
			return sholders;
		}
		if (result.size() == 0) {
			return sholders;
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
		// System.out.println(resultTable);

		Elements trElements = resultTable.select("tr");
		Elements firstTdElements = trElements.first().select("td");

		for (int j = 1; j < trElements.size(); j++) {
			Element trElement = trElements.get(j);
			Elements tdElements = trElement.select("td");
			if (tdElements.size() == 0) {
				continue;
			}
			String quantity = null;
			String ratio = null;
			String shareholder = null;
			for (int k = 0; k < tdElements.size(); k++) {
				String key = firstTdElements.get(k).text().trim();
				String value = tdElements.get(k).text().trim();
				if (value.contains("合计") || value.contains("序号") || value.contains("合  计") || value.contains("收购人")
						|| value.contains("股东")) {
					break;
				}
				if (key.contains("后")) {

				} else {
					if (key.contains("股数")) {
						quantity = value;
					} else if (key.contains("比例")) {
						ratio = value;
					} else if ((key.contains("股东") || key.contains("收购人")) && !key.contains("性质")) {
						shareholder = value;
					}
				}

			}
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				if (shareholder == null) {
					continue;
				}
				map.put("shareholder", shareholder);
				map.put("quantity", quantity);
				map.put("ratio", ratio);
				sholders.add(map);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		return sholders;

	}

	/****
	 * 寻找符合条件的表格
	 * 
	 * @param elements
	 * @return
	 */
	public List<Element> fillBeforeShareholderResult(Elements elements) {
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
				if (((firstTrText.contains("股东") || firstTrText.contains("收购人")) && firstTrText.contains("持股")
						&& firstTrText.contains("比例"))
						|| (firstTrText.contains("收购") && firstTrText.contains("后") && firstTrText.contains("前"))) {
					if (j - 1 > 0) {
						Element preElement = elements.get(j - 1);
						String preText = preElement.text();
						preText = replacekong(preText);
						if (preText.contains("收购前")
								&& (preText.contains("股权结构") || preText.contains("权益变动") || preText.contains("股份"))) {
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
								String preText2 = preElement2.text();
								preText2 = replacekong(preText2);
								if (preText2.contains("收购前") && (preText2.contains("股权结构") || preText2.contains("权益变动")
										|| preText2.contains("股份"))) {
									result.add(childElement);
									Elements trElements = childElement.select("tr");
									for (Element trElement : trElements) {
										Elements tdElements = trElement.select("td,th");
										if (colNum < tdElements.size()) {
											colNum = tdElements.size();
										}
									}
								} else {

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
