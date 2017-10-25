package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.DocumentSimpler;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfCodeTemporary;
import com.kf.data.mybatis.entity.PdfReportLinks;

/***
 * 
 * @author meidi
 *
 */
public class PdfTemporary3Parser extends KfPdfParser {

	public Map<String, Object> parserDocument(PdfCodeTable pdfCodeTable, PdfReportLinks pdfCodeLink, Document document,
			List<PdfCodeTemporary> pdfCodeTemporarys3) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			Map<String, Set<String>> tables = new HashMap<String, Set<String>>();
			// 存储字段的解析规则 table_propertys 规则list
			Map<String, List<PdfCodeTemporary>> propertyRules = new HashMap<String, List<PdfCodeTemporary>>();
			for (PdfCodeTemporary pdfCodeTemporary : pdfCodeTemporarys3) {
				// 先看下有多少张表
				String tableName = pdfCodeTemporary.getTableName();
				if (pdfCodeTable.getPdfType().startsWith("半年报")) {
					tableName = tableName.replace("_year_", "_semiannual_");
				}
				String property = pdfCodeTemporary.getProperty();
				Set<String> propertys = null;
				if (tables.get(tableName) == null) {
					propertys = new HashSet<String>();
				} else {
					propertys = tables.get(tableName);
				}
				propertys.add(property);
				tables.put(tableName, propertys);
				String table_property = tableName + "@@@" + property;
				List<PdfCodeTemporary> pdfCodeTemporaryTempList = null;
				if (propertyRules.get(table_property) == null) {
					pdfCodeTemporaryTempList = new ArrayList<PdfCodeTemporary>();
				} else {
					pdfCodeTemporaryTempList = propertyRules.get(table_property);
				}
				pdfCodeTemporaryTempList.add(pdfCodeTemporary);
				propertyRules.put(table_property, pdfCodeTemporaryTempList);
			}
			List<List<Map<String, String>>> infoList = new ArrayList<List<Map<String, String>>>();
			Elements pElements = document.select("div").first().children();
			Set<String> tablekeys = tables.keySet();
			for (String tableName : tablekeys) {
				Set<String> propertys = tables.get(tableName);
				// id
				Map<String, String> companyidMap = new HashMap<String, String>();
				companyidMap.put("value", pdfCodeLink.getId() + "");
				companyidMap.put("tableName", tableName);
				companyidMap.put("property", "source_id");
				// link
				Map<String, String> linkMap = new HashMap<String, String>();
				linkMap.put("value", pdfCodeLink.getLink());
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
				noticeIdMap.put("value", pdfCodeLink.getNoticeId() + "");
				noticeIdMap.put("tableName", tableName);
				noticeIdMap.put("property", "notice_id");

				Map<String, String> reportDateMap = new HashMap<String, String>();
				reportDateMap.put("value", new SimpleDateFormat("yyyy-MM-dd").format(pdfCodeLink.getReportDate()));
				reportDateMap.put("tableName", tableName);
				reportDateMap.put("property", "report_date");

				List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();
				for (String property : propertys) {
					String table_property = tableName + "@@@" + property;
					// 某个字段的解析规则
					List<PdfCodeTemporary> table_property_Rules = propertyRules.get(table_property);
					List<String> result = new ArrayList<>();
					String preText = null;
					String endText = null;
					
					for (PdfCodeTemporary pdfCodeTemporary : table_property_Rules) {
						preText = pdfCodeTemporary.getBeginPosition().trim();
						endText = pdfCodeTemporary.getEndPosition().trim();
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
							resultInfoMap.put("property", property);
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
