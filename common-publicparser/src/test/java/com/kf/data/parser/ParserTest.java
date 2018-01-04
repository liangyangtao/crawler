package com.kf.data.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.DocumentSimpler;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.parser.KfPdfParser;

/***
 * 
 * @Title: ParserTest.java
 * @Package com.kf.data.parser
 * @Description: 解析测试
 * @author liangyt
 * @date 2017年10月23日 上午10:35:01
 * @version V1.0
 */
public class ParserTest {

	public static void main(String[] args) {
		String url = "https://static.kaifengdata.com/neeq/13a38de0b34dca72f1caa95446c20ba3/[%E4%B8%B4%E6%97%B6%E6%8A%A5%E5%91%8A]%E6%B3%B0%E4%B8%80%E8%82%A1%E4%BB%BD_%E5%85%AC%E5%BC%80%E8%BD%AC%E8%AE%A9%E8%AF%B4%E6%98%8E%E4%B9%A6.pdf.html";
		String chagelink = changeHanzi(url);
		String html = Fetcher.getInstance().get(chagelink, "gbk");
		Document document = Jsoup.parse(html);
		document = new DocumentSimpler().simpleDocument(document);
		// System.out.println(document);
		PdfCodeTable pdfCodeTable = new PdfCodeTable();
		pdfCodeTable.setPdfType("公转书_主要客户");
		PdfReportLinks pdfReportLinks = new PdfReportLinks();
		pdfReportLinks.setLink(url);
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
