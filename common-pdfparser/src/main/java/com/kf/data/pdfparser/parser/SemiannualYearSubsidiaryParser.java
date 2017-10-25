package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.TableSpliter;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;

/****
 * 
 * @Title: SemiannualYearSubsidiaryParser.java
 * @Package com.kf.data.pdfparser.parser
 * @Description: 半年报_子公司
 * @author liangyt
 * @date 2017年10月20日 下午2:05:25
 * @version V1.0
 */
public class SemiannualYearSubsidiaryParser extends PdfTemporary2Parser {

	/****
	 * 解析半年报 子公司
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

			List<List<Map<String, String>>> infoList = new ArrayList<List<Map<String, String>>>();
			// 存储开始位置
			List<String> begins = new ArrayList<String>();
			begins.add("同一控制下企业合并获得的子公司");
			begins.add("企业的构成");
			begins.add("同一控制下企业合并获得的子公司");
			begins.add("在其他主体中的权益");
			begins.add("在其他主体中的权益");
			begins.add("在其他主体中的权益");
			begins.add("在其他主体中的权益");
			begins.add("在其他主体中的权益");
			begins.add("在其他主体中的权益");
			begins.add("在其他主体中的权益");
			begins.add("新成立及并购");

			// 存储结束位置
			List<String> ends = new ArrayList<String>();

			ends.add("重要的非全资子公司");
			ends.add("新成立及并购");
			ends.add("与金融工具相关的风险");
			ends.add("与金融工具相关风险");
			ends.add("关联方关系及其交易");
			ends.add("关联方及关联交易");
			ends.add("关联方及其交易");
			ends.add("在子公司的所有者权益份额发生变化");
			ends.add("重要的非全资子公司");
			ends.add("货币资金");
			sortPreAndEnd(begins, ends);
			for (int i = 0; i < begins.size(); i++) {
				Elements pElements = document.select("div").first().children();
				String preText = begins.get(i);
				String endText = ends.get(i);
				if (document.toString().contains(preText) && document.toString().contains(endText)) {
				} else {
					continue;
				}
				List<Element> result = fillResult(pElements, pdfCodeTable.getPdfType(), begins, ends, preText, endText);
				if (result == null) {
					continue;
				}
				if (result.size() == 0) {
					continue;
				}
				Element resultTable = new Element(Tag.valueOf("table"), "");
				int colNum = 0;
				for (int k = 0; k < result.size(); k++) {
					Element element = result.get(k);
					Elements trElements = element.select("tr");
					if (k == 0) {
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
					} else if (k > 0) {
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

				}

				//
				resultTable = new TableSpliter().splitTable(resultTable, true, null);
				Elements trElements = resultTable.select("tr");
				for (int j = 1; j < trElements.size(); j++) {
					List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();
					Element trElement = trElements.get(j);
					Elements tdElements = trElement.select("td");
					if (tdElements.size() == 0) {
						continue;
					}
					if (tdElements.size() == 6) {
						for (int k = 0; k < tdElements.size(); k++) {
							String value = tdElements.get(k).text().trim();
							if (k == 0 && value.contains("子公司名称")) {
								break;
							}
							Map<String, String> resultInfoMap = new HashMap<String, String>();
							resultInfoMap.put("value", value);
							resultInfoMap.put("tableName", tableName);
							String property = null;
							if (k == 0) {
								property = "company_name";
							} else if (k == 1) {
								property = "business_address";
							} else if (k == 2) {
								property = "";
							} else if (k == 0) {
								property = "";
							} else if (k == 0) {
								property = "";
							} else if (k == 0) {
								property = "";
							}
							resultInfoMap.put("property", property);
							infoEntity.add(resultInfoMap);
						}
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

			resultMap.put("state", "ok");
			resultMap.put("info", infoList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

}
