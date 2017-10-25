package com.kf.data.pdfparser.thread;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.DocumentSimpler;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.jdbc.DynamicDataStore;
import com.kf.data.pdfparser.jdbc.PdfCodetableReader;
import com.kf.data.pdfparser.jdbc.PdfReportLinksWriter;
import com.kf.data.pdfparser.parser.KfPdfParser;

/**
 * @Title: PdfLinkPaserWorker.java
 * @Package com.kf.data.web.task.thread
 * @Description: 解析pdf
 * @author liangyt
 * @date 2017年5月11日 下午5:09:30
 * @version V1.0
 */
public class PdfReportLinkPaserWorker implements Runnable {

	static final Logger logger = LoggerFactory.getLogger(PdfReportLinkPaserWorker.class);

	private LinkedBlockingQueue<PdfReportLinks> pdfcodeLinkQueue;
	private PdfReportLinksWriter pdfReportLinksWriter;
	private PdfCodetableReader pdfCodetableReader;
	DocumentSimpler documentSimpler = new DocumentSimpler();

	public PdfReportLinkPaserWorker(LinkedBlockingQueue<PdfReportLinks> pdfcodeLinkQueue,
			PdfCodetableReader pdfCodetableReader, PdfReportLinksWriter pdfReportLinksWriter) {
		super();
		this.pdfcodeLinkQueue = pdfcodeLinkQueue;
		this.pdfCodetableReader = pdfCodetableReader;
		this.pdfReportLinksWriter = pdfReportLinksWriter;

	}

	@Override
	public void run() {

		while (true) {
			if (pdfcodeLinkQueue.size() > 0) {
				try {
					PdfReportLinks pdfReportLinks = pdfcodeLinkQueue.take();
					System.out.println(pdfReportLinks.getLink());
					String html = null;
					String chagelink = changeHanzi(pdfReportLinks.getLink());
					if (chagelink.endsWith("/")) {
						continue;
					}
					html = Fetcher.getInstance().get(chagelink, "gbk");
					if (html == null) {
						continue;
					}
					if (html.contains("MirrorFailed")) {
						continue;
					}
					Document document = Jsoup.parse(html);
					document = documentSimpler.simpleDocument(document);
					List<PdfCodeTable> pdftables = pdfCodetableReader.readPdfTable();
					for (PdfCodeTable pdfCodeTable : pdftables) {
						if (pdfCodeTable.getTask() == 1) {
							new Thread(new PdfTableThread(pdfCodeTable, pdfReportLinks, document.clone())).start();
						}
					}
					pdfReportLinksWriter.updatePdfReportRankById(pdfReportLinks.getId(), 2);
					document = null;
					html = null;
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public String changeHanzi(String url) {
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

class PdfTableThread implements Runnable {

	private PdfCodeTable pdfCodeTable;
	private PdfReportLinks pdfReportLinks;
	private Document document;
	private KfPdfParser kfPdfParser = new KfPdfParser();
	private DynamicDataStore dynamicDataStore = new DynamicDataStore();

	public PdfTableThread(PdfCodeTable pdfCodeTable, PdfReportLinks pdfReportLinks, Document document) {
		super();
		this.pdfCodeTable = pdfCodeTable;
		this.pdfReportLinks = pdfReportLinks;
		this.document = document;
	}

	@Override
	public void run() {
		String linkPdfType = pdfReportLinks.getPdfType();
		String pdfType = pdfCodeTable.getPdfType();
		System.out.println("解析" + pdfType);
		if (pdfType.startsWith(linkPdfType)) {
			try {
				String json = null;
				json = kfPdfParser.parserPdfHtmlByPdfTypeAndLink(pdfCodeTable, pdfReportLinks, document);
				System.out.println(json);
				if (json.equals("{}") || json.equals("{\"state\":\"ok\",\"info\":[]}")
						|| json.equals("{\"state\":\"erro\"}") || json.equals("{\"state\":\"ok\",\"info\":[]}")) {
				} else {
					try {
						dynamicDataStore.doStore(json, linkPdfType);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				json = null;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}