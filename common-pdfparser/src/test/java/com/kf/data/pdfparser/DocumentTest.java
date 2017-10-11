package com.kf.data.pdfparser;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.DocumentSimpler;
import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.fetcher.tools.TableRepairer;
import com.kf.data.pdfparser.entity.PdfLinkEsEntity;
import com.kf.data.pdfparser.es.PdfReportTextReader;

public class DocumentTest {

	public static void main(String[] args) {
		String url = "https://static.kaifengdata.com/neeq/dfb0c010cd00b6ba47234f4e4ed34c93/[%E5%AE%9A%E6%9C%9F%E6%8A%A5%E5%91%8A]%E6%B5%99%E6%B1%9F%E5%A4%A7%E5%86%9C_2017%E5%B9%B4%E5%8D%8A%E5%B9%B4%E5%BA%A6%E6%8A%A5%E5%91%8A.pdf.html";
		url = changeHanzi(url);
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html);
//		KfConstant.init();
//		List<PdfLinkEsEntity> pdfLinkEsEntities = new PdfReportTextReader().readPdfLinkInEsByNoticId(2670640);
//		String html = pdfLinkEsEntities.get(0).getContent();
//		Document document = Jsoup.parse(html);
		document = new DocumentSimpler().simpleDocument(document);
		File file = new File("C:\\Users\\meidi\\Desktop\\re.html");
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			pw.print("<html><head><meta charset=\"utf-8\"></head>");
			pw.print(document.toString());
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
