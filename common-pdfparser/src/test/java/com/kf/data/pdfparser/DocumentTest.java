package com.kf.data.pdfparser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.TableRepairer;

public class DocumentTest {

	public static void main(String[] args) {
		String url = "https://static.kaifengdata.com/neeq/024318692097744b09a72b881a22e0b7/[%E5%AE%9A%E6%9C%9F%E6%8A%A5%E5%91%8A]%E9%AA%8F%E5%8F%91%E7%94%9F%E7%89%A9%202017%E5%B9%B4%E5%8D%8A%E5%B9%B4%E5%BA%A6%E6%8A%A5%E5%91%8A.pdf.htm";
		url = changeHanzi(url);
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html);
		Elements divElements = document.body().children();
		Element newBody = new Element(Tag.valueOf("div"), "");
		for (Element divElement : divElements) {
			if (divElement.tagName().equals("div")) {
				Elements childElements = divElement.children();
				for (Element childElement : childElements) {
					if (childElement.tagName().equals("table")) {
						childElement = new TableRepairer().repairTable(childElement);
						newBody.appendChild(childElement);
					} else {
						Element pElement = new Element(Tag.valueOf("p"), "");
						pElement.text(childElement.text().trim());
						newBody.appendChild(pElement);
					}
				}

			}

		}
		System.out.println(newBody);

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
