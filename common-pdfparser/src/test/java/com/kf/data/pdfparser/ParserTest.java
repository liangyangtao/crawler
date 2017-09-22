package com.kf.data.pdfparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.kf.data.fetcher.Fetcher;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.parser.KfPdfParser;

public class ParserTest {

	public static void main(String[] args) {
		String url = "https://static.kaifengdata.com/neeq/bc379225a0414e372e65b5addbb3e11a/345658.pdf.html";
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html);
		PdfCodeTable pdfCodeTable = new PdfCodeTable();
		pdfCodeTable.setPdfType("年报_股东");
		PdfReportLinks pdfReportLinks = new PdfReportLinks();
		System.out.println(new KfPdfParser().parserPdfHtmlByPdfTypeAndLink(pdfCodeTable, pdfReportLinks, document));

	}
}
