package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;

/***
 * 
 * @Title: PublicRiskParser.java
 * @Package com.kf.data.pdfparser.parser
 * @Description: 公转书风险提示抽取
 * @author liangyt
 * @date 2017年10月23日 上午10:09:54
 * @version V1.0
 */
public class PublicRiskParser extends PublicBaseParser {

	/****
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

			Map<String, String> reportDateMap = new HashMap<String, String>();
			reportDateMap.put("value", new SimpleDateFormat("yyyy-MM-dd").format(pdfReportLinks.getReportDate()));
			reportDateMap.put("tableName", tableName);
			reportDateMap.put("property", "report_date");
			List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();
			List<List<Map<String, String>>> infoList = new ArrayList<List<Map<String, String>>>();
			// 存储开始位置
			List<String> begins = new ArrayList<String>();
			begins.add("重大事项及风险提示");
			begins.add("重大事项提示");
			// 存储结束位置
			List<String> ends = new ArrayList<String>();
			ends.add("目录");
			ends.add("目录");

			List<String> result = new ArrayList<>();
			Elements pElements = document.select("div").first().children();
			for (int i = 0; i < begins.size(); i++) {
				String preText = begins.get(i);
				String endText = ends.get(i);
				if (document.toString().contains(preText) && document.toString().contains(endText)) {
				} else {
					continue;
				}
				int preIndex = 0;
				int endIndex = pElements.size();
				Map<Integer, Integer> indexs = new HashMap<Integer, Integer>();
				for (int j = 0; j < pElements.size(); j++) {
					Element pElement = pElements.get(j);
					if (pElement.tagName().equals("p")) {
						String pText = pElement.text();
						pText = pText.replace("  ", "");
						pText = pText.replace(" ", "");
						pText = pText.replace("	", "");
						pText = pText.replace(" ", "");
						if (pText.length() > 100) {
							continue;
						}
						if (pText.contains(endText)) {
							if (preIndex == 0) {
								continue;
							}
							if (j > endIndex && endIndex > preIndex) {
								continue;
							}
							endIndex = j;
							indexs.put(preIndex, endIndex);
						}
						if (pText.equals(preText)) {
							preIndex = j;
						} else if (pText.equals("、" + preText)) {
							preIndex = j;
						} else if (pText.contains(preText)) {
							for (String string : titleTags) {
								if (pText.contains(string)) {
									if (preIndex < endIndex) {
										preIndex = j;
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
						if (childElement.tagName().equals("table")) {
							Elements trElements = childElement.children();
							for (Element trElement : trElements) {
								Elements tdElements = trElement.children();
								for (Element tdElement : tdElements) {
									Elements tdChildElements = tdElement.children();
									if (tdChildElements.size() > 0) {
										for (Element tdChildElement : tdChildElements) {
											if (tdChildElement.tagName().equals("table")) {
												sb.append("	暂无表格	");
											} else if (tdChildElement.tagName().equals("img")) {
												sb.append("	暂无图片	");
											} else {
												sb.append(tdChildElement.text().trim());
											}
										}
									} else {
										sb.append(tdElement.text().trim());
									}

								}

							}

							// }
						} else if (childElement.tagName().equals("img")) {
							sb.append("	暂无图片	");
						} else {
							Elements tbodyElements = childElement.children();
							if (tbodyElements.size() > 0) {
								for (Element tdChildElement : tbodyElements) {
									if (tdChildElement.tagName().equals("table")) {
										sb.append("	暂无表格	");
									} else if (tdChildElement.tagName().equals("img")) {
										sb.append("	暂无图片	");
									} else {
										sb.append(tdChildElement.text().trim());
									}
								}
							} else {
								sb.append(childElement.text().trim());
							}
						}
					}
				}
				if (sb.toString().length() > 0) {
					result.add(sb.toString());
				}

			}
			// 多个规则循环结束 进行比较
			if (result.size() > 0) {
				String value = result.get(0);
				int valueCount = Collections.frequency(result, value);
				for (String string : result) {
					int strCount = Collections.frequency(result, string);
					if (value.contains(string)) {
						value = string;
						if (strCount > valueCount) {
							valueCount = strCount;
						}
					} else {
						if (strCount > valueCount) {
							value = string;
							valueCount = strCount;
						}
					}

				}
				// 如果都比不出来 就取最小的那个
				if (valueCount == 1) {
					for (String string : result) {
						if (string.length() < value.length()) {
							value = string;
						}
					}
				}
				if (value.length() > 30) {
					Map<String, String> resultInfoMap = new HashMap<String, String>();
					resultInfoMap.put("value", value == null ? "" : value);
					resultInfoMap.put("tableName", tableName);
					resultInfoMap.put("property", "value");
					infoEntity.add(resultInfoMap);
					infoEntity.add(companyidMap);
					// link
					infoEntity.add(linkMap);
					// pdfType
					infoEntity.add(pdfTypeMap);
					// 时间
					infoEntity.add(timeMap);
					// noticeId
					infoEntity.add(noticeIdMap);
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

}
