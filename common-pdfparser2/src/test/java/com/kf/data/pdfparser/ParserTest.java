package com.kf.data.pdfparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.es.PdfReportTextReader;
import com.kf.data.pdfparser.parser.KfPdfParser;

public class ParserTest {

	public static void main(String[] args) {
		KfConstant.init();
		String html = new PdfReportTextReader().readPdfLinkInEsByNoticId(2665630).get(0).getContent() ;
		Document document = Jsoup.parse(html );
		PdfCodeTable pdfCodeTable = new PdfCodeTable();
		pdfCodeTable.setPdfType("年报_股东");
		PdfReportLinks pdfReportLinks = new PdfReportLinks();
		System.out.println(new KfPdfParser().parserPdfHtmlByPdfTypeAndLink(pdfCodeTable, pdfReportLinks, document));

	}
}
