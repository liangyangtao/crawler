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
 * @Title: PurchaseTradeDetailParser.java
 * @Package com.kf.data.approved.parser.purchase
 * @Description: 前六个月买卖股票的情况
 * @author liangyt
 * @date 2018年1月9日 上午11:19:23
 * @version V1.0
 */
public class PurchaseTradeDetailParser extends BaseParser {

	/****
	 * 
	 * @param 收购前股权情况
	 */
	public List<Map<String, Object>> paserTradeDetail(Document document) {
		List<Map<String, Object>> tradeDetails = new ArrayList<>();
		Elements pElements = document.select("div").first().children();
		List<Element> result = fillTradeDetailResult(pElements);
		if (result == null) {
			return tradeDetails;
		}
		if (result.size() == 0) {
			return tradeDetails;
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
			String dt_trans = null;
			String type_tra = null;
			String price_tra = null;
			String vol_tra = null;
			String turnover_tra = null;
			String nm_trader = null;

			for (int k = 0; k < tdElements.size(); k++) {
				String key = firstTdElements.get(k).text().trim();
				String value = tdElements.get(k).text().trim();

				if (value.contains("合计") || value.contains("序号") || value.contains("合  计")) {
					break;
				}
				if (key.contains("交易人")) {
					nm_trader = value;
				} else if (key.contains("成交日期")) {
					dt_trans = value;
				} else if (key.contains("买卖方向")) {
					type_tra = value;
				} else if (key.contains("交易数量")) {
					vol_tra = value;
				} else if (key.contains("交易价格")) {
					price_tra = value;
				} else if (key.contains("交易价格")) {
					price_tra = value;
				}
			}
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("dt_trans", dt_trans);
				map.put("type_tra", type_tra);
				map.put("price_tra", price_tra);
				map.put("vol_tra", vol_tra);
				map.put("turnover_tra", turnover_tra);
				map.put("nm_trader", nm_trader);
				tradeDetails.add(map);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return tradeDetails;

	}

	/****
	 * 寻找符合条件的表格
	 * 
	 * @param elements
	 * @return
	 */
	public List<Element> fillTradeDetailResult(Elements elements) {
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
				if (firstTrText.contains("交易人") && firstTrText.contains("日期") && firstTrText.contains("方向")
						&& firstTrText.contains("数量")) {
					if (j - 1 > 0) {
						Element preElement = elements.get(j - 1);
						String preText = preElement.text();
						preText = replacekong(preText);
						if (preText.contains("前6个月") || preText.contains("前六个月")) {
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
								if (preText2.contains("前6个月") || preText2.contains("前六个月")) {
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
