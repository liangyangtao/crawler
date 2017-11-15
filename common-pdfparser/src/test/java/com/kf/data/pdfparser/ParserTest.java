package com.kf.data.pdfparser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.DocumentSimpler;
import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.entity.PdfLinkEsEntity;
import com.kf.data.pdfparser.es.PdfReportTextReader;
import com.kf.data.pdfparser.parser.KfPdfParser;

public class ParserTest {

	public static void main(String[] args) {
		KfConstant.init();
		// List<PdfLinkEsEntity> pdfLinkEsEntities = new
		// PdfReportTextReader().readPdfLinkInEsByNoticId(1627189);
		// String html = pdfLinkEsEntities.get(0).getContent();

		String url = "https://static.kaifengdata.com/neeq/37943ffed0f282dba46c71281a6e7442/[%E5%AE%9A%E6%9C%9F%E6%8A%A5%E5%91%8A]%E6%B0%B8%E4%B8%B0%E5%B0%8F%E8%B4%B7_2017%E5%8D%8A%E5%B9%B4%E5%BA%A6%E6%8A%A5%E5%91%8A.pdf.html";
		String chagelink = changeHanzi(url);
		String html = Fetcher.getInstance().get(chagelink, "gbk");
		Document document = Jsoup.parse(html);
		document = new DocumentSimpler().simpleDocument(document);
		PdfCodeTable pdfCodeTable = new PdfCodeTable();
		pdfCodeTable.setPdfType("半年报_商业模式");
		PdfReportLinks pdfReportLinks = new PdfReportLinks();
		System.out.println(new KfPdfParser().parserPdfHtmlByPdfTypeAndLink(pdfCodeTable, pdfReportLinks, document));

	}

	public static String changeHanzi(String url) {
		char[] tp = url.toCharArray();
		String now = "";
		for (char ch : tp) {
			if (ch >= 0x4E00 && ch <= 0x9FA5) {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == '[') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == ']') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == ' ') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				now += ch;
			}

		}
		return now;
	}
}
