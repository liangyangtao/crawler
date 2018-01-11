package com.kf.data.approved;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.kf.data.approved.parser.purchase.AcquisitionParser;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.DocumentSimpler;

public class Test {

	public static void main(String[] args) {
//     String url = "http://static.kaifengdata.com/neeq/868cd5d8789e4179378d5666ef87fd24/[%E4%B8%B4%E6%97%B6%E5%85%AC%E5%91%8A]%E5%9B%BD%E8%8A%AF%E7%A7%91%E6%8A%80_%E6%94%B6%E8%B4%AD%E6%8A%A5%E5%91%8A%E4%B9%A6.pdf.html";
//	    String url ="http://static.kaifengdata.com/neeq/1af869be7c00c41b084146bd2350dbf1/[%E4%B8%B4%E6%97%B6%E5%85%AC%E5%91%8A]%E5%8D%8E%E4%BB%81%E7%89%A9%E4%B8%9A_%E6%94%B6%E8%B4%AD%E6%8A%A5%E5%91%8A%E4%B9%A6.pdf.html";
//		String chagelink = changeHanzi(url);
//		String html = Fetcher.getInstance().get(chagelink, "gbk");
//		Document document = Jsoup.parse(html);
//		document = new DocumentSimpler().simpleDocument(document);
//		new AcquisitionParser().parserAcquisition(document);
//		PdfReportLinks pdfReportLink = new PdfReportLinks();
//		pdfReportLink.setCompanyName("哇棒传媒");
//		pdfReportLink.setCompanyId(430346);
//		pdfReportLink.setPublishDate("2017年12月7日");
//		pdfReportLink.setReportDate(new Date());
//		pdfReportLink.setLink(url);
//		Document document = Jsoup.parse(html);
//		String text = document.text();
//		System.out.println(text);
//	   System.out.println(	new BaseParser().formatMoneyValue("71,000"));
//		new AchievementPreviewParser().parserContent(html, pdfReportLink);
//		String key ="股东性质";
//		System.out.println(!key.contains("性质"));
//		System.out.println((key.contains("股东") || key.contains("收购人") )&& (!key.contains("性质"))));

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
