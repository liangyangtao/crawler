package com.kf.data.pdfparser;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
		String url = "https://static.kaifengdata.com/neeq/66f3aed83b525af886c4e46dc4ad802b/[%E5%AE%9A%E6%9C%9F%E6%8A%A5%E5%91%8A]%E9%87%91%E8%BE%89%E7%89%A9%E6%B5%81%202017%E5%B9%B4%E5%8D%8A%E5%B9%B4%E5%BA%A6%E6%8A%A5%E5%91%8A.pdf.htm";
		url = changeHanzi(url);
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html);
		Element newBody = new Element(Tag.valueOf("div"), "");
		Elements divElements = document.body().children();
		for (Element divElement : divElements) {
			Elements childElements = divElement.children();
			for (Element childElement : childElements) {
				if (childElement.tagName().equals("table")) {
					childElement = new TableRepairer().repairTable(childElement);
					newBody.appendChild(childElement);
				} else if (childElement.tagName().equals("w:sdt")) {
					Elements pElements = childElement.children();
					for (Element element : pElements) {
						if (element.tagName().equals("table")) {
							element = new TableRepairer().repairTable(element);
							newBody.appendChild(element);
						} else {
							Element pElement = new Element(Tag.valueOf("p"), "");
							pElement.text(element.text().trim());
							newBody.appendChild(pElement);
						}
					}

				} else if (childElement.tagName().equals("div")) {
					Elements pElements = childElement.children();
					for (Element element : pElements) {
						if (element.tagName().equals("table")) {
							element = new TableRepairer().repairTable(element);
							newBody.appendChild(element);
						} else {
							Element pElement = new Element(Tag.valueOf("p"), "");
							pElement.text(element.text().trim());
							newBody.appendChild(pElement);
						}
					}

				} else {
					Element pElement = new Element(Tag.valueOf("p"), "");
					pElement.text(childElement.text().trim());
					newBody.appendChild(pElement);
				}
			}

		}

		File file = new File("C:\\Users\\meidi\\Desktop\\re.html");
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			pw.print("<html><head><meta charset=\"utf-8\"></head>");
			pw.print(newBody.toString());
			pw.print("</html>");
			pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
