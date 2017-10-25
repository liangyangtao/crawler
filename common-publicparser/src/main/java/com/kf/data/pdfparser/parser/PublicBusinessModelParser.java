package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * @Title: PublicBusinessModelParser.java
 * @Package com.kf.data.pdfparser.parser
 * @Description: 公转书商业模式抽取
 * @author liangyt
 * @date 2017年10月23日 上午10:09:54
 * @version V1.0
 */
public class PublicBusinessModelParser extends PublicBaseParser {

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
			Elements pElements = document.select("div").first().children();
			String[] preTexts = new String[] { "母公司及子公司商业模式", "公司商业模式情况", "公司的商业模式", "公司商业模式", "商业模式" };
			int preIndex = 0;
			int endIndex = pElements.size();
			String preReg = null;
			Map<Integer, Integer> indexs = new HashMap<Integer, Integer>();
			for (int j = 0; j < pElements.size(); j++) {
				Element pElement = pElements.get(j);
				if (pElement.tagName().equals("p")) {
					String pText = pElement.text();
					pText = pText.replace("  ", "");
					pText = pText.replace(" ", "");
					pText = pText.replace("	", "");
					pText = pText.replace(" ", "");
					if (pText.contains(".......")) {
						continue;
					}
					if (pText.length() > 100) {
						continue;
					}
					if (preReg != null) {
						if (pText.contains(preReg)) {
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
						for (String preText : preTexts) {
							if (pText.contains(preText)) {
								if (preText.contains("及子公司商业模式")) {

								} else if (preText.contains("子公司商业模式")) {
									continue;
								}
								if (pText.length() >= preText.length() + 8) {
									continue;
								}
								for (int i = 0; i < titleTags.length; i++) {
									String string = titleTags[i];
									if (pText.contains(string)) {
										if (preIndex < endIndex) {
											preIndex = j;
											if (i + 1 < titleTags.length) {
												preReg = titleTags[i + 1];
											}
											break;
										}
									}
								}
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
					if (childElement.text().contains(".......")) {
						break;
					}
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
			if (sb.length() > 30) {
				Map<String, String> resultInfoMap = new HashMap<String, String>();
				resultInfoMap.put("value", sb.toString());
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
			resultMap.put("state", "ok");
			resultMap.put("info", infoList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

}
