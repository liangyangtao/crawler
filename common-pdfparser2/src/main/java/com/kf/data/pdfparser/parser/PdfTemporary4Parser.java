package com.kf.data.pdfparser.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;

import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfCodeTemporary;
import com.kf.data.mybatis.entity.PdfReportLinks;

public class PdfTemporary4Parser extends KfPdfParser {

	public Map<String, Object> parserDocument(PdfCodeTable pdfCodeTable, PdfReportLinks pdfCodeLink, Document document,
			List<PdfCodeTemporary> pdfCodeTemporarys4) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<List<Map<String, String>>> infoList = new ArrayList<List<Map<String, String>>>();
			Set<String> propertys = new HashSet<String>();
			Map<String, List<PdfCodeTemporary>> propertyRules = new HashMap<String, List<PdfCodeTemporary>>();
			for (PdfCodeTemporary pdfCodeTemporary : pdfCodeTemporarys4) {
				if (pdfCodeTemporary.getOrderId() == 2) {
					continue;
				}
				String property = pdfCodeTemporary.getProperty().trim();
				String propertyName = pdfCodeTemporary.getPropertyName().trim();
				propertys.add(propertyName + "@@@" + property);
				List<PdfCodeTemporary> pdfCodeTemporaryTempList = null;
				if (propertyRules.get(propertyName + "@@@" + property) == null) {
					pdfCodeTemporaryTempList = new ArrayList<PdfCodeTemporary>();
				} else {
					pdfCodeTemporaryTempList = propertyRules.get(propertyName + "@@@" + property);
				}
				pdfCodeTemporaryTempList.add(pdfCodeTemporary);
				propertyRules.put(propertyName + "@@@" + property, pdfCodeTemporaryTempList);
			}

			for (String propertyName_property : propertys) {

				String property = propertyName_property.split("@@@")[1];

				List<PdfCodeTemporary> table_property_Rules = propertyRules.get(propertyName_property);
				// 判断解析规则的类型
				String tableName = table_property_Rules.get(0).getTableName();
				String content = document.body().text();
				content = replacekong(content);
				/////////////////
				String pre = null;
				String end = null;
				String propertyName = null;
				////////////////
				String nextPre = null;
				String nextEnd = null;
				String nextpropertyName = null;
				String nextproperty = null;
				////////////////
				List<String> preResults1 = new ArrayList<String>();
				List<String> preResults2 = new ArrayList<String>();
				String sentence = null;
				for (int i = 0; i < table_property_Rules.size(); i++) {
					PdfCodeTemporary pdfCodeTemporary = table_property_Rules.get(i);
					propertyName = pdfCodeTemporary.getPropertyName().trim();
					PdfCodeTemporary nextPdfCodeTemporary = null;
					for (PdfCodeTemporary temp : pdfCodeTemporarys4) {
						if (temp.getGroupId().equals(pdfCodeTemporary.getGroupId())
								&& !temp.getOrderId().equals(pdfCodeTemporary.getOrderId())
								&& temp.getPropertyName().equals(pdfCodeTemporary.getPropertyName().trim())) {
							nextPdfCodeTemporary = temp;
							break;
						}
					}
					if (nextPdfCodeTemporary == null) {
						continue;
					}
					nextpropertyName = nextPdfCodeTemporary.getPropertyName().trim();
					nextproperty = nextPdfCodeTemporary.getProperty().trim();
					if (pdfCodeTemporary.getBeginPosition().equals("undefined")
							&& nextPdfCodeTemporary.getBeginPosition().equals("undefined")) {

					} else if (pdfCodeTemporary.getBeginPosition().equals("undefined")
							&& !nextPdfCodeTemporary.getBeginPosition().equals("undefined")) {
						List<String> preRsult = getStrByReg(nextPdfCodeTemporary.getBeginPosition().trim(),
								nextPdfCodeTemporary.getEndPosition().trim(), content);
						for (String string : preRsult) {
							String resultSentence = nextPdfCodeTemporary.getBeginPosition().trim() + string
									+ nextPdfCodeTemporary.getEndPosition().trim();
							if (sentence == null) {
								sentence = resultSentence;
							}
							if (resultSentence.length() <= sentence.length()) {
								sentence = resultSentence;
								pre = pdfCodeTemporary.getBeginPosition().trim();
								end = pdfCodeTemporary.getEndPosition().trim();
								nextPre = nextPdfCodeTemporary.getBeginPosition().trim();
								nextEnd = nextPdfCodeTemporary.getEndPosition().trim();
							}
						}

					} else {
						// 是一对组合
						List<String> preRsult = getStrByReg(pdfCodeTemporary.getBeginPosition(),
								nextPdfCodeTemporary.getEndPosition(), content);
						for (String string : preRsult) {
							String preEnd = pdfCodeTemporary.getEndPosition();
							preEnd = replacekong(preEnd);
							////////////////////////////////////////
							String nextBegion = nextPdfCodeTemporary.getBeginPosition();
							nextBegion = replacekong(nextBegion);
							if (string.contains(preEnd) && string.contains(nextBegion)) {
								String resultSentence = pdfCodeTemporary.getBeginPosition() + string
										+ nextPdfCodeTemporary.getEndPosition();
								if (sentence == null) {
									sentence = resultSentence;
								}
								if (resultSentence.length() <= sentence.length()) {
									sentence = resultSentence;
									pre = pdfCodeTemporary.getBeginPosition().trim();
									end = pdfCodeTemporary.getEndPosition().trim();
									nextPre = nextPdfCodeTemporary.getBeginPosition().trim();
									nextEnd = nextPdfCodeTemporary.getEndPosition().trim();
								}
							}
						}
					}

				}
				if (sentence != null && !sentence.isEmpty()) {
					sentence = replacekong(sentence);
					preResults1 = getStrByReg(pre, end, sentence);
					preResults2 = getStrByReg(nextPre, nextEnd, sentence);
				}
				String result = null;
				if (preResults1.size() > 0) {
					result = preResults1.get(0);
					for (String string : preResults1) {
						if (string.length() < result.length()) {
							result = string;
						}
					}
				}

				String result2 = null;
				if (preResults2.size() > 0) {
					result2 = preResults2.get(0);
					for (String string : preResults2) {
						if (string.length() < result2.length()) {
							result2 = string;
						}
					}
				}
				if (result == null || result.isEmpty()) {
					continue;
				}
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

				Map<String, String> resultInfoMap = new HashMap<String, String>();
				result = formatvalue(result);
				resultInfoMap.put("value", result);
				resultInfoMap.put("tableName", tableName);
				resultInfoMap.put("property", property);

				Map<String, String> propertyNameMap = new HashMap<String, String>();
				propertyNameMap.put("value", propertyName);
				propertyNameMap.put("tableName", tableName);
				propertyNameMap.put("property", "propertyName");
				List<Map<String, String>> infoEntity = new ArrayList<Map<String, String>>();
				// result
				infoEntity.add(resultInfoMap);
				// properName
				infoEntity.add(propertyNameMap);
				// id
				infoEntity.add(companyidMap);
				// link
				infoEntity.add(linkMap);
				// pdfType
				infoEntity.add(pdfTypeMap);
				// 时间
				infoEntity.add(timeMap);
				// noticeId
				infoEntity.add(noticeIdMap);
				Map<String, String> resultInfoMap2 = new HashMap<String, String>();
				result2 = formatvalue(result2);
				resultInfoMap2.put("value", result2);
				resultInfoMap2.put("tableName", tableName);
				resultInfoMap2.put("property", nextproperty);
				infoEntity.add(resultInfoMap2);
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
