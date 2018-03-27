package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.TableSpliter;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfCodeTemporary;
import com.kf.data.mybatis.entity.PdfReportLinks;

/***
 * 
 * @Title: PdfTemporary2Parser.java
 * @Package com.kf.data.pdfparser.parser
 * @Description: 解析表格类型
 * @author liangyt
 * @date 2017年10月11日 上午11:34:43
 * @version V1.0
 */
public class PdfTemporary2Parser extends KfPdfParser {

	/***
	 * 解析表格类型
	 * 
	 * @param pdfCodeTable
	 *            pdf分类
	 * @param pdfCodeLink
	 *            pdf 链接
	 * @param document
	 *            pdfdom
	 * @param pdfCodeTemporarys2
	 *            解析规则
	 * @return 解析结果
	 */
	public Map<String, Object> parserDocument(PdfCodeTable pdfCodeTable, PdfReportLinks pdfCodeLink, Document document,
			List<PdfCodeTemporary> pdfCodeTemporarys2) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 读取数据解析规则
			/***
			 * 读取这种pdf 下 所有的表以及每个表中的属性 的解析规则
			 */
			// 存储表以及 每个表的字段
			Map<String, Set<String>> tables = new HashMap<String, Set<String>>();
			// 存储字段的解析规则 table_propertys 规则list
			Map<String, Set<String>> propertyNameRules = new HashMap<String, Set<String>>();
			// 存储属性在表格哪一列
			Map<String, Integer> keyIndex = new HashMap<String, Integer>();
			// 存储表格里面的标题
			Set<String> keyRules = new HashSet<String>();
			// 存储开始位置
			List<String> begins = new ArrayList<String>();
			// 存储结束位置
			List<String> ends = new ArrayList<String>();

			for (PdfCodeTemporary pdfCodeTemporary : pdfCodeTemporarys2) {
				// 先看下有多少张表
				String tableName = pdfCodeTemporary.getTableName().trim();
				if (pdfCodeTable.getPdfType().startsWith("半年报")) {
					tableName = tableName.replace("_year_", "_semiannual_");
				}
				String property = pdfCodeTemporary.getProperty().trim();
				Set<String> propertys = null;
				if (tables.get(tableName) == null) {
					propertys = new HashSet<String>();
				} else {
					propertys = tables.get(tableName);
				}
				propertys.add(property);
				tables.put(tableName, propertys);
				String table_property = tableName + "@@@" + property;
				// 重复的开始和结束就不放了
				String begin = pdfCodeTemporary.getBeginPosition().trim();
				String end = pdfCodeTemporary.getEndPosition().trim();
				if (begins.contains(begin)) {
					for (int i = 0; i < begins.size(); i++) {
						if (begins.get(i).equals(begin)) {
							if (ends.get(i).equals(end)) {
							} else {
								begins.add(begin);
								ends.add(end);
								break;
							}
						}

					}
				} else {
					begins.add(begin);
					ends.add(end);
				}

				Set<String> propertyNames = null;
				if (propertyNameRules.get(table_property) == null) {
					propertyNames = new HashSet<String>();
				} else {
					propertyNames = propertyNameRules.get(table_property);
				}
				propertyNames.add(pdfCodeTemporary.getPropertyName().trim());
				propertyNameRules.put(table_property, propertyNames);
				keyRules.add(pdfCodeTemporary.getPropertyName().trim());
			}
			List<List<Map<String, String>>> infoList = new ArrayList<List<Map<String, String>>>();
			Set<String> tablekeys = tables.keySet();
			for (String tableName : tablekeys) {
				// 每个字段必须带的属性
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

				// publishDate
				// Map<String, String> publishDateMap = new
				// HashMap<String, String>();
				// publishDateMap.put("value", publishDate);
				// publishDateMap.put("tableName", tableName);
				// publishDateMap.put("property", "publish_date");

				Map<String, String> reportDateMap = new HashMap<String, String>();
				reportDateMap.put("value", new SimpleDateFormat("yyyy-MM-dd").format(pdfCodeLink.getReportDate()));
				reportDateMap.put("tableName", tableName);
				reportDateMap.put("property", "report_date");

				String pdfType = pdfCodeTable.getPdfType();
				Set<String> propertys = tables.get(tableName);
				if (propertys.size() > 0) {
					Elements pElements = document.select("div").first().children();
					sortPreAndEnd(begins, ends);
					for (int i = 0; i < begins.size(); i++) {
						String preText = begins.get(i);
						String endText = ends.get(i);
						if (document.toString().contains(preText) && document.toString().contains(endText)) {
						} else {
							continue;
						}
						List<Element> result = fillResult(pElements, pdfType, begins, ends, preText, endText);
						if (result == null) {
							continue;
						}
						if (result.size() > 0) {
							boolean istop = false;
							Element resultTable = createNewtable(result, endText, keyRules, istop);
							if (Arrays.asList(megerPdfTypes).contains(pdfType)) {
								String key = "";
								if (pdfType.equals("年报_负债") || pdfType.equals("公转书_合并资产负债表")
										|| pdfType.equals("公转书_母公司资产负债表")) {
									key = "liabilities";
								} else if (pdfType.equals("年报_现金") || pdfType.equals("公转书_母公司现金流量表")
										|| pdfType.equals("公转书_合并现金流量表")) {
									key = "cash";
								} else if (pdfType.equals("年报_利润") || pdfType.equals("公转书_母公司利润表")
										|| pdfType.equals("公转书_合并利润表")) {
									key = "profit";
								}
								resultTable = new TableSpliter().splitTable(resultTable, true, null);
							} else {
								resultTable = new TableSpliter().splitTable(resultTable, false, null);
							}

							Elements trElements = resultTable.select("tr");
							if (trElements.size() > 0) {
								if (Arrays.asList(customsTypes).contains(pdfType)) {

									for (int j = 1; j < trElements.size(); j++) {
										Element trElement = trElements.get(j);
										Elements tdElements = trElement.select("td");
										for (int k = 1; k < tdElements.size(); k++) {
											List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();
											String category = tdElements.get(0).text().trim();
											if (category.isEmpty()) {
												continue;
											}
											Map<String, String> categoryMap = new HashMap<String, String>();
											categoryMap.put("value", category);
											categoryMap.put("tableName", tableName);
											categoryMap.put("property", "category");
											infoEntity.add(categoryMap);

											String value = tdElements.get(k).text().trim();
											String public_time = trElements.get(0).select("td").get(k).text().trim();
											if (public_time.isEmpty()) {
												continue;
											}
											if (public_time.contains("@@")) {
												// 2015年8月31日@@合并
												String temp[] = public_time.split("@@");
												public_time = temp[0];
												String type = temp[1];
												if (!pdfType.contains(type)) {
													continue;
												}

											}

											Map<String, String> resultInfoMap = new HashMap<String, String>();
											resultInfoMap.put("value", value);
											resultInfoMap.put("tableName", tableName);
											resultInfoMap.put("property", "amount");
											infoEntity.add(resultInfoMap);
											Map<String, String> publicTimeMap = new HashMap<String, String>();
											publicTimeMap.put("value", public_time);
											publicTimeMap.put("tableName", tableName);
											publicTimeMap.put("property", "public_time");
											infoEntity.add(publicTimeMap);
											infoEntity.add(companyidMap);
											// link
											infoEntity.add(linkMap);
											// pdfType
											infoEntity.add(pdfTypeMap);
											// 时间
											infoEntity.add(timeMap);
											// noticeId
											infoEntity.add(noticeIdMap);
											// publishDateMap
											infoEntity.add(reportDateMap);
											infoList.add(infoEntity);

										}

									}

								} else {
									Element thElement = trElements.first();
									Elements tdElments = thElement.select("td");
									fillKeyIndex(tdElments, propertys, propertyNameRules, tableName, keyIndex, i);
									// 如果是多行表头
									////////////////////////// for
									////////////////////////// jiesu
									if (keyIndex.size() <= 1) {
										//////////////// left
										//////////////// 来确定
										fillLeftTypeTable(trElements, propertys, keyIndex, tableName, companyidMap,
												linkMap, pdfTypeMap, timeMap, noticeIdMap, reportDateMap, infoList,
												istop, propertyNameRules);
										////////////////////////
									} else {
										istop = true;
									}
									if (istop) {
										fillTopTypeTable(trElements, propertys, keyIndex, tableName, companyidMap,
												linkMap, pdfTypeMap, timeMap, noticeIdMap, reportDateMap, infoList);

									}
								}
							}
							break;
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

	/***
	 * 找到数据对应的列
	 * 
	 * @param tdElments
	 * @param propertys
	 * @param propertyNameRules
	 * @param tableName
	 * @param keyIndex
	 * @param i
	 */
	public void fillKeyIndex(Elements tdElments, Set<String> propertys, Map<String, Set<String>> propertyNameRules,
			String tableName, Map<String, Integer> keyIndex, int i) {
		for (int j = 0; j < tdElments.size(); j++) {
			Element element = tdElments.get(j);
			String elementText = element.text().trim();
			elementText = replacekong(elementText);
			elementText = elementText.replace("###", "");
			if (elementText.contains("@@")) {

			} else {
				if (elementText.contains("（")) {
					elementText = elementText.split("（")[0].trim();
				}
				if (elementText.contains("(")) {
					elementText = elementText.split("\\(")[0].trim();
				}
				if (elementText.contains("%")) {
					elementText = elementText.split("%")[0].trim();
				}
			}
			for (String propertyTemp : propertys) {
				Set<String> propertyNameTemps = propertyNameRules.get(tableName + "@@@" + propertyTemp);
				// 属性的列表
				for (String string : propertyNameTemps) {
					string = replacekong(string);
					string = string.replace("###", "");
					if (elementText.isEmpty() && string.endsWith("#")) {
						int keylength = string.length();
						if (i == keylength - 1) {
							keyIndex.put(propertyTemp, j);
							break;
						}
					} else {
						if (string.contains("@@")) {
							String temp[] = string.split("@@");
							boolean isAll = true;
							for (String string2 : temp) {
								if (!elementText.contains(string2)) {
									isAll = false;
									break;
								}
							}
							if (isAll) {
								keyIndex.put(propertyTemp, j);
								break;
							}

						} else {
							if (elementText.contains("@@")) {
								String temp[] = elementText.split("@@");
								String mm = temp[temp.length - 1];
								if (mm.contains("（")) {
									mm = mm.split("（")[0].trim();
								}
								if (mm.contains("(")) {
									mm = mm.split("\\(")[0].trim();
								}
								if (mm.contains("%")) {
									mm = elementText.split("%")[0].trim();
								}
								if (mm.trim().equals(string)) {
									keyIndex.put(propertyTemp, j);
									break;
								}
							} else {

								if (elementText.equals(string)) {
									keyIndex.put(propertyTemp, j);
									break;
								}
							}
						}
					}
				}
			}

		}

	}

	/****
	 * 解析 left 方式的表格
	 * 
	 * @param trElements
	 * @param propertys
	 * @param keyIndex
	 * @param tableName
	 * @param companyidMap
	 * @param linkMap
	 * @param pdfTypeMap
	 * @param timeMap
	 * @param noticeIdMap
	 * @param reportDateMap
	 * @param infoList
	 * @param istop
	 * @param propertyNameRules
	 * @return
	 */
	public boolean fillLeftTypeTable(Elements trElements, Set<String> propertys, Map<String, Integer> keyIndex,
			String tableName, Map<String, String> companyidMap, Map<String, String> linkMap,
			Map<String, String> pdfTypeMap, Map<String, String> timeMap, Map<String, String> noticeIdMap,
			Map<String, String> reportDateMap, List<List<Map<String, String>>> infoList, boolean istop,
			Map<String, Set<String>> propertyNameRules) {
		istop = false;
		boolean isLeft = false;
		List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();

		for (int j = 0; j < trElements.size(); j++) {
			Element leftThElement = trElements.get(j);
			Element element = leftThElement.select("td").get(0);
			String elementText = element.text().trim();
			elementText = replacekong(elementText);
			elementText = elementText.replace("###", "");
			if (elementText.contains("@@")) {

			} else {
				if (elementText.contains("（")) {
					elementText = elementText.split("（")[0].trim();
				}
				if (elementText.contains("(")) {
					elementText = elementText.split("\\(")[0].trim();
				}
				if (elementText.contains("%")) {
					elementText = elementText.split("%")[0].trim();
				}
			}
			for (String propertyTemp : propertys) {
				Set<String> propertyNameTemps = propertyNameRules.get(tableName + "@@@" + propertyTemp);
				for (String string : propertyNameTemps) {
					string = replacekong(string);
					string = string.replace("###", "");
					if (elementText.equals(string)) {
						isLeft = true;
						Map<String, String> resultInfoMap = new HashMap<String, String>();
						resultInfoMap.put("value", leftThElement.select("td").get(1).text().trim());
						resultInfoMap.put("tableName", tableName);
						resultInfoMap.put("property", propertyTemp);
						infoEntity.add(resultInfoMap);
						break;
					}

				}

			}

		}
		if (!isLeft) {

		} else {
			if (infoEntity.size() != 0) {
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
		return isLeft;

	}

	/***
	 * 解析 top 方式的表格
	 * 
	 * @param trElements
	 * @param propertys
	 * @param keyIndex
	 * @param tableName
	 * @param companyidMap
	 * @param linkMap
	 * @param pdfTypeMap
	 * @param timeMap
	 * @param noticeIdMap
	 * @param reportDateMap
	 * @param infoList
	 */

	public void fillTopTypeTable(Elements trElements, Set<String> propertys, Map<String, Integer> keyIndex,
			String tableName, Map<String, String> companyidMap, Map<String, String> linkMap,
			Map<String, String> pdfTypeMap, Map<String, String> timeMap, Map<String, String> noticeIdMap,
			Map<String, String> reportDateMap, List<List<Map<String, String>>> infoList) {
		for (int j = 1; j < trElements.size(); j++) {
			List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();
			Element trElement = trElements.get(j);
			Elements tdElements = trElement.select("td");
			if (tdElements.size() == 0) {
				continue;
			}
			if (tdElements.first().text().isEmpty() || tdElements.first().text().equals("-")) {
				continue;
			}
			for (String propertyTemp : propertys) {
				if (keyIndex.get(propertyTemp) == null) {
					continue;
				}
				int index = keyIndex.get(propertyTemp);
				String value = tdElements.get(index).text().trim();
				String unit = trElements.get(0).select("td").get(index).text().trim();
				if (unit.contains("（元）") || unit.contains("(元)")) {
					unit = "元";
				} else if (unit.contains("（万元）") || unit.contains("(万元)")) {
					unit = "万元";
				} else if (unit.contains("（股）") || unit.contains("(股)")) {
					unit = "股";
				} else if (unit.contains("（万股）") || unit.contains("(万股)")) {
					unit = "万股";
				} else {
					unit = "";
				}
				if (value.isEmpty()) {
					continue;
				}
				Map<String, String> resultInfoMap = new HashMap<String, String>();
				resultInfoMap.put("value", value + unit);
				resultInfoMap.put("tableName", tableName);
				resultInfoMap.put("property", propertyTemp);
				infoEntity.add(resultInfoMap);
			}
			if (infoEntity.size() != 0) {
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

	/****
	 * 找到需要解析的表格
	 * 
	 * @param result
	 * @param endText
	 * @param keyRules
	 * @param istop
	 * @return
	 */
	public Element createNewtable(List<Element> result, String endText, Set<String> keyRules, boolean istop) {
		Element resultTable = new Element(Tag.valueOf("table"), "");
		if (result.size() == 1) {
			Element element = result.get(0);
			Elements trElements = element.select("tr");
			for (Element trElement : trElements) {
				resultTable.appendChild(trElement);
			}
		} else {
			int firstIndex = 0;
			int firstNum = 0;
			// 如果有多个表格 ,排除掉不是需要的表格
			for (int k = 0; k < result.size(); k++) {
				Element element = result.get(k);
				Elements trElements = element.select("tr");
				if (trElements.size() == 0) {
					continue;
				}
				// 有几列
				int col = 0;
				for (int j = 0; j < trElements.size(); j++) {
					Element trElement = trElements.get(j);
					Elements tdElements = trElement.select("td,th");
					if (col < tdElements.size()) {
						col = tdElements.size();
					}
				}

				Element firstElement = trElements.get(0);
				Elements firtsTdElements = firstElement.select("td");
				// 判断表头有几行
				int titleNum = 1;
				for (Element tdElement : firtsTdElements) {
					String rowsStr = tdElement.attr("rowspan");
					if (!rowsStr.isEmpty()) {
						int rowInt = Integer.parseInt(rowsStr);
						if (titleNum < rowInt) {
							titleNum = rowInt;
						}
					}
				}

				// 多个表格怎么合并
				Elements firstTdElements = element.select("tr").first().select("td");
				int proNum = 0;
				for (Element firstTdElement : firstTdElements) {
					String elementText = firstTdElement.text();
					elementText = replacekong(elementText);
					elementText = elementText.replace("###", "");
					for (String propertyname : keyRules) {
						propertyname = replacekong(propertyname);
						propertyname = propertyname.replace("###", "");
						if (propertyname.contains("@@")) {
							String propertyTemp[] = propertyname.split("@@");
							boolean isExit = true;
							for (String string : propertyTemp) {
								if (elementText.contains(string)) {

								} else {
									isExit = false;
								}
							}
							if (isExit) {
								proNum++;
								break;
							}

						} else {
							if (elementText.contains(propertyname)) {
								proNum++;
								break;

							}
						}

					}

					if (proNum > firstNum) {
						firstNum = proNum;
						firstIndex = k;
					}
				}
			}

			if (firstNum > 1) {
				istop = true;
			}
			// 判断列是否一致
			int colNum = 0;
			for (int k = firstIndex; k < result.size(); k++) {
				Element element = result.get(k);
				Elements trElements = element.select("tr");
				if (istop) {
					if (k == firstIndex) {
						// 如果是第一个表格
						trElements = element.select("tr");
						// 是表头的那个表
						for (Element trElement : trElements) {
							if (trElement.text().contains(endText)) {
								break;
							}
							resultTable.appendChild(trElement);
							Elements tdElements = trElement.select("td,th");
							if (colNum < tdElements.size()) {
								colNum = tdElements.size();
							}
						}
					} else if (k > firstIndex) {
						if (trElements.size() == 0) {
							continue;
						}
						Element kTrElement = trElements.get(0);
						Elements kTdElements = kTrElement.select("td");
						// 判断表头有几行
						int titleNum = 1;
						for (Element tdElement : kTdElements) {
							String rowsStr = tdElement.attr("rowspan");
							if (!rowsStr.isEmpty()) {
								int rowInt = Integer.parseInt(rowsStr);
								if (titleNum < rowInt) {
									titleNum = rowInt;
								}
							}
						}
						if (titleNum > 1) {
							// 如果有多行表头 不合并
							break;
						}
						if (trElements.size() == 1 && kTdElements.size() == 1) {
							break;
						}
						boolean isAppend = false;
						int col = 0;
						for (Element trElement : trElements) {
							if (trElement.text().contains(endText)) {
								break;
							}
							Elements tdElements = trElement.select("td,th");
							if (col < tdElements.size()) {
								col = tdElements.size();
							}
						}
						if (col == colNum) {
							isAppend = true;
						}
						if (isAppend) {
							for (Element trElement : trElements) {
								if (trElement.text().contains(endText)) {
									break;
								}
								resultTable.appendChild(trElement);
							}
						} else {
							// 结束
							break;
						}
					}
				} else {
					// 是LEFT 方式提取
					for (Element trElement : trElements) {
						if (trElement.text().contains(endText)) {
							break;
						}
						resultTable.appendChild(trElement);
					}
				}
			}

		}
		return resultTable;

	}

	/****
	 * 判读表格的位置
	 * 
	 * @param pElements
	 * @param pdfType
	 * @param begins
	 * @param ends
	 * @param preText
	 * @param endText
	 * @return
	 */
	public List<Element> fillResult(Elements pElements, String pdfType, List<String> begins, List<String> ends,
			String preText, String endText) {
		int preIndex = 0;
		int endIndex = pElements.size();
		LinkedHashMap<Integer, Integer> indexs = new LinkedHashMap<Integer, Integer>();
		for (int j = 0; j < pElements.size(); j++) {
			Element pElement = pElements.get(j);
			if (pElement.tagName().equals("p")) {
				String pText = pElement.text();
				if (pdfType.contains("负债表") || pdfType.contains("利润表") || pdfType.contains("现金流量表")) {

					if (pText.contains("续") || pText.contains("表主要数据") || pText.contains("表项目变动分析表")
							|| pText.contains("表日后事项")) {
						continue;
					}
					for (String finance : finances) {
						if (pText.contains(finance)) {
							if (begins.contains(finance)) {
								if (preIndex < endIndex) {
									preIndex = j;
								}
								break;
							}
							if (ends.contains(finance)) {
								if (preIndex == 0) {
									continue;
								}
								if (j > endIndex && endIndex > preIndex) {
									continue;
								}
								endIndex = j;
								if (preIndex == endIndex) {
									continue;
								}
								indexs.put(preIndex, endIndex);
								break;
							}

						}

					}

				} else {
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
					if (preIndex < endIndex && pText.contains(preText)) {
						preIndex = j;
					}
				}
			}
		}
		Element newBody = new Element(Tag.valueOf("div"), "");
		Elements chlidElements = pElements;
		if (chlidElements.size() == 0) {
			return null;
		}
		List<Element> result = new ArrayList<Element>();

		if (indexs.size() > 1) {
			Iterator<Integer> indexIterator = indexs.keySet().iterator();
			int k = 0;
			while (indexIterator.hasNext()) {
				int key = indexIterator.next();
				int value = indexs.get(key);
				if (value - key > 50) {
					indexIterator.remove();
					indexs.remove(key);
					continue;
				}
				if (k == 0 && pdfType.contains("母公司")) {
					indexIterator.remove();
					indexs.remove(key);
				}
				if (k == 1 && pdfType.contains("合并")) {
					indexIterator.remove();
					indexs.remove(key);
				}
				k++;

			}
		} else {
			// if (pdfType.contains("母公司") &&
			// preText.contains("母公司")) {
			// continue;
			// }
		}
		Set<Integer> preIndexs = indexs.keySet();
		for (Integer preIn : preIndexs) {
			int endIn = indexs.get(preIn);
			if (endIn - preIn > 50) {
				continue;
			}
			for (int j = preIn; j <= endIn; j++) {
				Element childElement = chlidElements.get(j);
				newBody.appendChild(childElement);
			}
		}
		Elements childElements = newBody.children();
		for (Element element : childElements) {
			if (element.tagName().equals("table")) {
				result.add(element);
			}
		}
		return result;

	}
}
