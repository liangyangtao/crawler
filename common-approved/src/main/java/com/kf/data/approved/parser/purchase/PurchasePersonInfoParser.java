package com.kf.data.approved.parser.purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.approved.parser.BaseParser;

/****
 * 
 * @Title: PurchasePersonInfoParser.java
 * @Package com.kf.data.approved.parser.purchase
 * @Description: 收购人基本信息
 * @author liangyt
 * @date 2018年1月5日 下午1:43:14
 * @version V1.0
 */
public class PurchasePersonInfoParser extends BaseParser {
	/****
	 * 
	 * @param document
	 * @return
	 */
	public Map<String, String> getResult(Document document) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			// 存储开始位置
			List<String> begins = new ArrayList<String>();
			begins.add("收购人的基本情况");
			begins.add("收购人情况");
			begins.add("收购人及其一致行动人基本情况");
			begins.add("基本情况");
			begins.add("公司简介");
			begins.add("简要情况");

			Elements pElements = document.select("div").first().children();
			int preIndex = 0;
			int endIndex = pElements.size();
			String preReg = null;
			Map<Integer, Integer> indexs = new HashMap<Integer, Integer>();
			for (int j = 0; j < pElements.size(); j++) {
				Element pElement = pElements.get(j);
				if (pElement.tagName().equals("p")) {
					String pText = pElement.text();
					pText = replacekong(pText);
					if (pText.contains(".......")) {
						continue;
					}
					if (pText.length() > 100) {
						continue;
					}
					if (preReg != null) {
						if (pText.contains(preReg) || pText.contains("（二）")) {
							if (preIndex == 0) {
								continue;
							}
							if (j > endIndex && endIndex > preIndex) {
								break;
							}
							endIndex = j;
							indexs.put(preIndex, endIndex);
						}
					} else {
						for (String preText : begins) {
							if (pText.contains(preText)) {
								if (pText.length() >= preText.length() + 8) {
									continue;
								}
								preIndex = j;
								preReg = "二、";
								if (preReg != null) {
									break;
								}
							}
						}
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			Set<Integer> preIndexs = indexs.keySet();
			for (Integer preIn : preIndexs) {
				int endIn = indexs.get(preIn);
				for (int j = preIn + 1; j < endIn; j++) {
					Element childElement = pElements.get(j);
					sb.append(childElement.text());
				}
			}
			if (sb.length() > 30) {
				parserInfo(resultMap, sb.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	public void parserInfo(Map<String, String> infoEntity, String value) {
		if (value.contains("男") || value.contains("女")) {
			value = value.replaceAll("一、.{1,5}基本情况", "");
			value = value.replaceAll("（一）.{1,5}基本情况", "");
			value = value.replace("一、收购人情况", "");
			value = value.replace("（一）收购人情况", "");
			value = value.replace("（一）收购人及其一致行动人基本情况", "");
			value = value.replace("收购人一致行动人为", "");
			List<String> results = new ArrayList<>();
			String regEx = "，[男|女]，";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(value);
			while (matcher.find()) {
				String string = matcher.group();
				results.add(string);
			}
			String[] temp = value.split(regEx);
			for (int i = 1; i < temp.length; i++) {
				String name = null;
				if (i == 1) {
					name = temp[0];
					temp[1] = name + results.get(0) + temp[1];
				} else {
					name = temp[i].substring(0, temp[i].indexOf("，"));
				}

				int index = temp[i].lastIndexOf("。");
				if (temp[i].length() - index <= 5) {
					String nextName = temp[i].substring(index + 1);
					temp[i] = temp[i].substring(0, index + 1);
					if (i + 1 < temp.length) {
						temp[i + 1] = nextName + results.get(i) + temp[i + 1];
					}
				}
				infoEntity.put(name, temp[i]);
			}
		}

	}

}
