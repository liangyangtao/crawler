package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.tools.DocumentSimpler;
import com.kf.data.fetcher.tools.Md5Tools;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.mybatis.entity.PdfReportModelNotice;
import com.kf.data.mybatis.entity.PdfReportTableModel;
import com.kf.data.mybatis.entity.PdfReportTableValue;
import com.kf.data.pdfparser.jdbc.PdfReportModelNoticeStore;
import com.kf.data.pdfparser.jdbc.PdfReportTableModelReader;
import com.kf.data.pdfparser.jdbc.PdfReportTableModelSore;
import com.kf.data.pdfparser.jdbc.PdfReportTableValueStore;

public class PdfTemporary2Parser extends KfPdfParser {

	public Map<String, Object> parserDocument(PdfCodeTable pdfCodeTable, PdfReportLinks pdfCodeLink, Document document,
			String rule) {
		try {
			Element document1 = new DocumentSimpler().simpleDocument(document);
			Elements divElements = Jsoup.parse(document1.toString()).body().children();
			int nodeId = 0;
			int firstId = 0;
			int scondId = 0;
			int threeId = 0;
			int rank = 1;
			String firstNode = null;
			String secondNode = null;
			String threeNode = null;
			boolean isStart = false;
			boolean isEnd = false;
			Element newBody = new Element(Tag.valueOf("div"), "");
			for (Element element : divElements) {
				Elements pElements = element.children();
				if (pElements.size() == 0) {
					continue;
				}
				for (Element element2 : pElements) {
					String elementText = element2.text();
					String text = elementText;
					if (elementText.contains("。")) {
						String texttemp[] = elementText.split("。");
						if (texttemp.length >= 1) {
							text = texttemp[texttemp.length - 1];
						}
					}
					// 一级
					if (text.matches(
							"\\s{0,100}第[一|二|三|四|五|六|七|八|九|十|十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|二十一|二十二|二十三|二十四|二十五]+节.*")
							&& text.length() < 20) {
						nodeId++;
						firstNode = text;
						firstId = nodeId;
						rank = 1;
						if (isStart) {
							isEnd = true;
						}
					}

					if (text.matches("\\s{0,100}.*财务报表附注") && text.length() < 20) {
						nodeId++;
						firstNode = text;
						firstId = nodeId;
						rank = 1;
						if (isStart) {
							isEnd = true;
						}
					}

					// 二级
					if (text.matches(
							"\\s{0,100}[一|二|三|四|五|六|七|八|九|十|十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|二十一|二十二|二十三|二十四|二十五]+、.*")) {
						nodeId++;
						secondNode = text;
						scondId = nodeId;
						rank = 2;
						if (isStart) {
							isEnd = true;
						}
					}

					// 二级
					if (text.matches("【.*】") && text.length() < 25) {
						// System.out.println(text);
						nodeId++;
						secondNode = text;
						scondId = nodeId;
						if (isStart) {
							isEnd = true;
						}
						rank = 2;
					}

					// 三级
					if (text.matches(
							"\\s{0,100}（[一|二|三|四|五|六|七|八|九|十|十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|二十一|二十二|二十三|二十四|二十五]+）.*")) {
						// System.out.println(text);
						nodeId++;
						threeNode = text;
						threeId = nodeId;
						rank = 3;
						if (isStart) {
							isEnd = true;
						}
					}
					// 四级 er
					if (text.matches(
							"\\s{0,100}[1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25]+、.*")) {
						// System.out.println(text);
						nodeId++;
						if (rank == 3) {
							threeNode = text;
						} else if (rank == 2) {
							secondNode = text;
							threeId = nodeId;
						}
						if (isStart) {
							isEnd = true;
						}
					}
					if (firstNode != null && firstNode.contains("股本变动及股东情况") && secondNode != null
							&& secondNode.contains("股东情况")) {
						isStart = true;
					}
					if (isEnd) {
						break;
					}
					if (isStart) {
						newBody.appendChild(element2);
					}
				}
				/// 循环结束
			}
			Element resultTable = new Element(Tag.valueOf("table"), "");
			Elements childElements = newBody.children();
			for (Element element : childElements) {
				if (element.tagName().equals("table")) {
					Elements trElements = element.select("tr");
					for (Element trElement : trElements) {
						resultTable.appendChild(trElement);
					}
				}
			}
			Elements trElements = resultTable.select("tr");
			if (trElements.size() == 0) {
				return null;
			}
			// 第一行
			Element firstTrElement = trElements.get(0);
			Elements firstTdElements = firstTrElement.select("td");
			String text = firstTrElement.text().trim();
			String md5 = Md5Tools.GetMD5Code(text);

			List<PdfReportTableModel> pdfReportTableModels = new PdfReportTableModelReader()
					.readPdfReportTableModelByModelId(md5);
			if (pdfReportTableModels.size() == 0) {
				for (int i = 0; i < firstTdElements.size(); i++) {
					try {
						Element tdElement = firstTdElements.get(i);
						PdfReportTableModel pdfReportTableModel = new PdfReportTableModel();
						pdfReportTableModel.setColName(tdElement.text().trim());
						pdfReportTableModel.setColNum(i + 1);
						pdfReportTableModel.setModelId(md5);
						pdfReportTableModel.setProperty(null);
						pdfReportTableModel.setReportType(pdfCodeTable.getPdfType());
						pdfReportTableModel.setTableName(pdfCodeTable.getTableName());
						new PdfReportTableModelSore().savePdfReportTableModel(pdfReportTableModel);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				pdfReportTableModels = new PdfReportTableModelReader().readPdfReportTableModelByModelId(md5);
			}
			try {
				PdfReportModelNotice pdfReportModelNotice = new PdfReportModelNotice();
				pdfReportModelNotice.setModelId(md5);
				pdfReportModelNotice.setNoticeId(pdfCodeLink.getNoticeId());
				pdfReportModelNotice.setReportType(pdfCodeTable.getPdfType());
				new PdfReportModelNoticeStore().savePdfReportModelNotice(pdfReportModelNotice);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (int i = 1; i < trElements.size(); i++) {
				Element trElement = trElements.get(i);
				Elements tdElements = trElement.select("td");
				if (tdElements.size() == 0) {
					continue;
				}
				if (tdElements.first().text().isEmpty() || tdElements.first().text().equals("-")) {
					continue;
				}

				for (int index = 0; index < tdElements.size(); index++) {
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
					int modelAutoId = 0;
					for (PdfReportTableModel pdfReportTableModel : pdfReportTableModels) {
						int colNum = pdfReportTableModel.getColNum();
						if (colNum - 1 == index) {
							modelAutoId = pdfReportTableModel.getId();
							break;
						}
					}
					PdfReportTableValue pdfReportTableValue = new PdfReportTableValue();
					pdfReportTableValue.setModelAutoId(modelAutoId);
					pdfReportTableValue.setNoticeId(pdfCodeLink.getNoticeId());
					pdfReportTableValue.setReportType(pdfCodeTable.getPdfType());
					pdfReportTableValue.setTask(0);
					pdfReportTableValue.setUpTime(new Date());
					pdfReportTableValue.setValue(value);
					new PdfReportTableValueStore().savaPdfReportTableValue(pdfReportTableValue);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
