package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfCodeTemporary;
import com.kf.data.mybatis.entity.PdfReportLinks;

/***
 * 
 * @author meidi
 *
 */
public class PdfTemporary1Parser extends KfPdfParser {

	public Map<String, Object> parserDocument(PdfCodeTable pdfCodeTable, PdfReportLinks pdfCodeLink, Document document,
			List<PdfCodeTemporary> pdfCodeTemporarys1) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Set<String>> tables = new HashMap<String, Set<String>>();
			Map<String, List<PdfCodeTemporary>> propertyRules = new HashMap<String, List<PdfCodeTemporary>>();
			for (PdfCodeTemporary pdfCodeTemporary : pdfCodeTemporarys1) {
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
			// 行间属性存储
			List<List<Map<String, String>>> infoList = new ArrayList<List<Map<String, String>>>();
			// 开始解析 行间属性
			Set<String> tablekeys = tables.keySet();
			// 一个 表一个表的解析
			for (String tableName : tablekeys) {
				Set<String> propertys = tables.get(tableName);
				String propertyName = null;
				// 每个字段比带的属性
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
				noticeIdMap.put("value", pdfCodeLink.getNoticeId()+"");
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
					String content = document.body().text();
					content = content.replace(" ", "###");
					content = content.replace("	", "###");
					content = content.replace(" ", "###");
					List<String> preResults = new ArrayList<String>();

					for (PdfCodeTemporary pdfCodeTemporary : table_property_Rules) {
						String pre = pdfCodeTemporary.getBeginPosition();
						String end = pdfCodeTemporary.getEndPosition().trim();
						propertyName = pdfCodeTemporary.getPropertyName().trim();
						if (pre == null) {
							String result = null;
							if (content.contains("关于同意") && content.contains("股票在")) {
								result = StringUtils.substringBetween(content, "关于同意", "股票在");
							} else if (content.contains("关于同意") && content.contains("在全国中小企业")) {
								result = StringUtils.substringBetween(content, "关于同意", "在全国中小企业");
							} else {
								result = StringUtils.substringBefore(content, "：");
								if (result.length() > 60) {
									result = StringUtils.substringBefore(content, ":");
								}
							}

							if (result != null) {
								if (result.contains("你公司报送的")) {
									result = StringUtils.substringBetween(content, "关于同意", "在全国中小企业");
								}
								if (result.contains("你公司报送的")) {
									result = StringUtils.substringBetween(content, "关于同意", "股票");
								}
								if (result.contains("股票")) {
									result = StringUtils.substringBefore(result, "股票");
								}
								preResults.add(result);
							}
						} else {
							pre = pre.trim();
							// 行间提取
							List<String> result = getStrByReg(pre, end, content);
							while (result == null || result.size() == 0) {
								if (end.endsWith("###")) {
									pre += "###";
									if (content.contains(pre)) {
										result = getStrByReg(pre, end, content);
										if (result != null && result.size() != 0) {
											break;
										}
									} else {
										break;
									}
								} else {
									break;
								}
							}
							// 如果解析结果为空或只有一个字 就不保存了
							if (result == null || result.size() == 0) {
								continue;
							}
							for (String string : result) {
								string = string.replace(pre, " ");
								string = string.replace(end, " ");
								string = string.replace("###", "");
								string = string.trim();
								if (string.length() >= 2) {
									preResults.add(string);
								}
							}
						}
					}

					// 预选结果
					if (preResults.size() > 0) {
						System.out.println(Arrays.toString(preResults.toArray()));
						Map<Character, Integer> wordCount = new HashMap<Character, Integer>();
						for (int i = 0; i < preResults.size(); i++) {
							String string = preResults.get(i);
							char[] word = string.toCharArray();
							for (char c : word) {
								if (wordCount.get(c) == null) {
									wordCount.put(c, 1);
								} else {
									wordCount.put(c, (wordCount.get(c) + 1));
								}
							}
						}
						int index = preResults.size();
						index = index / 2;
						List<Character> condition = new ArrayList<Character>();
						Set<Character> keys = wordCount.keySet();
						for (Character character : keys) {
							int count = wordCount.get(character);
							if (count < index) {
								condition.add(character);
							}
						}
						List<String> results = new ArrayList<String>();
						for (String sencene : preResults) {
							boolean isNeed = true;
							for (Character character : condition) {
								if (sencene.contains(character + "")) {
									isNeed = false;
									break;
								}
							}
							if (isNeed) {
								results.add(sencene);
							}
						}
						System.out.println(Arrays.toString(results.toArray()));
						String value = preResults.get(0);
						if (results.size() > 0) {

							// 预选结果如何选？

							value = results.get(0);
							int valueCount = Collections.frequency(results, value);
							for (String string : results) {
								int strCount = Collections.frequency(results, string);
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
								for (String string : results) {
									if (string.length() < value.length()) {
										value = string;
									}
								}
							}

						} else {
							// 如果比较不出来就取最小的那个
							for (String string : preResults) {
								if (string.length() < value.length()) {
									value = string;
								}
							}
						}
						if (value.contains("MirrorFailed") || value.contains("NoSuchKey") || value.contains("2")
								|| value.contains("Scanned")) {
							continue;
						}
						if (value.isEmpty()) {
							continue;
						}
						System.out.println(property + "最后选定的值是 ：" + value);
						Map<String, String> resultInfoMap = new HashMap<String, String>();
						value = formatvalue(value);
						resultInfoMap.put("value", value);
						resultInfoMap.put("tableName", tableName);
						resultInfoMap.put("property", property);

						infoEntity.add(resultInfoMap);

					}

				}
				if (infoEntity.size() > 0) {
					Map<String, String> propertyNameMap = new HashMap<String, String>();
					propertyNameMap.put("value", propertyName);
					propertyNameMap.put("tableName", tableName);
					propertyNameMap.put("property", "propertyName");

					infoEntity.add(propertyNameMap);
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
