package com.kf.data.approved;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import com.kf.data.approved.parser.AchievementPreviewParser;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.mybatis.entity.PdfReportLinks;

public class Test {

	public static void main(String[] args) {
		String url = "https://static.kaifengdata.com/neeq/814e0b1e3c9f3b39a87165da208b32b1/[%E4%B8%B4%E6%97%B6%E5%85%AC%E5%91%8A]%E5%93%87%E6%A3%92%E4%BC%A0%E5%AA%92_2017%E5%B9%B4%E5%BA%A6%E4%B8%9A%E7%BB%A9%E9%A2%84%E5%91%8A.pdf.html";
		String chagelink = changeHanzi(url);
		String html = Fetcher.getInstance().get(chagelink, "gbk");
		PdfReportLinks pdfReportLink = new PdfReportLinks();
		pdfReportLink.setCompanyName("哇棒传媒");
		pdfReportLink.setCompanyId(430346);
		pdfReportLink.setPublishDate("2017年12月7日");
		pdfReportLink.setReportDate(new Date());
		pdfReportLink.setLink(url);
		new AchievementPreviewParser().parserContent(html, pdfReportLink);

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
