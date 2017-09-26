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

public class PdfTemporary2Parser extends KfPdfParser {

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
			Map<String, List<String>> beginRules = new HashMap<String, List<String>>();
			Map<String, List<String>> endRules = new HashMap<String, List<String>>();
			Map<String, Integer> keyIndex = new HashMap<String, Integer>();
			Set<String> keyRules = new HashSet<String>();
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
				/////////////////////////////
				List<String> begins = null;
				if (beginRules.get(table_property) == null) {
					begins = new ArrayList<String>();
				} else {
					begins = beginRules.get(table_property);
				}
				begins.add(pdfCodeTemporary.getBeginPosition().trim());
				beginRules.put(table_property, begins);
				////////////////////////////////////
				List<String> ends = null;
				if (endRules.get(table_property) == null) {
					ends = new ArrayList<String>();
				} else {
					ends = endRules.get(table_property);
				}
				ends.add(pdfCodeTemporary.getEndPosition().trim());
				endRules.put(table_property, ends);
				///////////////////////////////////
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
			// 行间属性存储
			List<List<Map<String, String>>> infoList = new ArrayList<List<Map<String, String>>>();
			Set<String> tablekeys = tables.keySet();
			for (String tableName : tablekeys) {
				Set<String> propertys = tables.get(tableName);
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

				if (propertys.size() > 0) {
					Elements pElements = document.select("div").first().children();
					String property = propertys.iterator().next();
					String table_property = tableName + "@@@" + property;
					List<String> begins = beginRules.get(table_property);
					List<String> ends = endRules.get(table_property);
					sortPreAndEnd(begins, ends);
					for (int i = 0; i < begins.size(); i++) {
						String preText = begins.get(i);
						String endText = ends.get(i);
						if (document.toString().contains(preText) && document.toString().contains(endText)) {
						} else {
							continue;
						}
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
							continue;
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
						if (result.size() > 0) {
							boolean istop = false;
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
									// 判断有几行
									// int row =
									// trElements.size();
									// 有几列
									int col = 0;
									for (int j = 0; j < trElements.size(); j++) {
										Element trElement = trElements.get(j);
										Elements tdElements = trElement.select("td,th");
										if (col < tdElements.size()) {
											col = tdElements.size();
										}
									}
									if (trElements.size() == 0) {
										continue;
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
									// 如果是多行的表头,就合并一下，
									if (titleNum > 1) {
										// istop=true;
									}
									if (element.select("tr").size() == 0) {
										continue;
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
								Element firstTable = result.get(firstIndex);
								Elements firstTrElements = firstTable.select("tr");
								Elements firstTdElements = firstTrElements.first().select("td");

								for (int k = firstIndex; k < result.size(); k++) {
									Element element = result.get(k);
									Elements trElements = element.select("tr");
									if (istop) {
										if (k > firstIndex) {
											if (trElements.size() == 0) {
												continue;
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
											if (titleNum > 1) {
												break;
											}
											if (trElements.size() == 1 && firtsTdElements.size() == 1) {
												break;
											}
											element = new TableSpliter().splitTable(element, false, null);
											trElements = element.select("tr");
											Elements tdElements = trElements.first().select("td");
											boolean isAppend = false;
											int emptyNum = 0;
											for (Element element2 : tdElements) {
												if (element2.text().trim().contains("-")
														|| element2.text().matches(".*\\d+.*")
														|| element2.text().contains("�")
														|| element2.text().trim().contains("_")) {
													isAppend = true;
													break;
												} else if (element2.text().trim().isEmpty()) {
													emptyNum++;
												}
											}
											if (!isAppend) {
												if (emptyNum > 1) {
													isAppend = true;
												}
											}
											if (!isAppend) {

												boolean check = true;
												for (int j = 0; j < tdElements.size(); j++) {
													Element tdElement = tdElements.get(j);
													Element firstTdElement = firstTdElements.get(j);
													if (!tdElement.text().equals(firstTdElement.text())) {
														check = false;
														break;
													}
												}
												if (check) {
													isAppend = true;
												}

											}
											//
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
										} else {
											element = new TableSpliter().splitTable(element, false, null);
											trElements = element.select("tr");
											// 是表头的那个表
											for (Element trElement : trElements) {
												if (trElement.text().contains(endText)) {
													break;
												}
												resultTable.appendChild(trElement);
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
											Set<String> propertyNameTemps = propertyNameRules
													.get(tableName + "@@@" + propertyTemp);
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

									// 如果是多行表头

									////////////////////////// for
									////////////////////////// jiesu
									if (keyIndex.size() <= 1) {
										//////////////// left
										//////////////// 来确定
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
												Set<String> propertyNameTemps = propertyNameRules
														.get(tableName + "@@@" + propertyTemp);
												for (String string : propertyNameTemps) {
													string = replacekong(string);
													string = string.replace("###", "");
													if (elementText.equals(string)) {
														isLeft = true;
														Map<String, String> resultInfoMap = new HashMap<String, String>();
														resultInfoMap.put("value",
																leftThElement.select("td").get(1).text().trim());
														resultInfoMap.put("tableName", tableName);
														resultInfoMap.put("property", propertyTemp);
														infoEntity.add(resultInfoMap);
														break;
													}

												}

											}

										}
										if (!isLeft) {
											continue;
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

										////////////////////////
									} else {
										istop = true;
									}

									if (istop) {

										for (int j = 1; j < trElements.size(); j++) {
											List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();

											Element trElement = trElements.get(j);
											Elements tdElements = trElement.select("td");
											if (tdElements.size() == 0) {
												continue;
											}
											if (tdElements.first().text().isEmpty()
													|| tdElements.first().text().equals("-")) {
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
}
