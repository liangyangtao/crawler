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
		String url = "https://static.kaifengdata.com/neeq/d22ac8dc4e553c5a5dec7ae72670491a/359818.pdf.html";
		String chagelink = changeHanzi(url);
		String html = Fetcher.getInstance().get(chagelink, "gbk");
		Document document = Jsoup.parse(html);
		document = new DocumentSimpler().simpleDocument(document);
//		System.out.println(document);
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
