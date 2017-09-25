package com.kf.data.pdfparser.thread;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kf.data.fetcher.tools.DocumentSimpler;
import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.entity.PdfLinkEsEntity;
import com.kf.data.pdfparser.es.PdfReportTextReader;
import com.kf.data.pdfparser.jdbc.DynamicDataStore;
import com.kf.data.pdfparser.jdbc.PdfCodetableReader;
import com.kf.data.pdfparser.jdbc.PdfReportLinksWriter;
import com.kf.data.pdfparser.parser.KfPdfParser;

/**
 * @Title: PdfLinkPaserWorker.java
 * @Package com.kf.data.web.task.thread
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月11日 下午5:09:30
 * @version V1.0
 */
public class PdfReportLinkPaserWorker implements Runnable {

	static final Logger logger = LoggerFactory.getLogger(PdfReportLinkPaserWorker.class);

	private LinkedBlockingQueue<PdfReportLinks> pdfcodeLinkQueue;

	public PdfReportLinkPaserWorker(LinkedBlockingQueue<PdfReportLinks> pdfcodeLinkQueue) {
		super();
		this.pdfcodeLinkQueue = pdfcodeLinkQueue;
	}

	@Override
	public void run() {

		while (true) {
			if (pdfcodeLinkQueue.size() > 0) {
				try {
					PdfReportLinks pdfReportLinks = pdfcodeLinkQueue.take();
					int noticeId = pdfReportLinks.getNoticeId();
					List<PdfLinkEsEntity> pdfLinkEsEntities = new PdfReportTextReader()
							.readPdfLinkInEsByNoticId(noticeId);
					String html = null;
					if (pdfLinkEsEntities.size() > 0) {
						html = pdfLinkEsEntities.get(0).getContent();
					}
					if (html == null) {
						continue;
					}
					Document document = Jsoup.parse(html);
					document = new DocumentSimpler().simpleDocument(document);
					List<PdfCodeTable> pdftables = new PdfCodetableReader().readPdfTable();
					for (PdfCodeTable pdfCodeTable : pdftables) {
						if (pdfCodeTable.getTask() == 1) {
							new Thread(new PdfTableThread(pdfCodeTable, pdfReportLinks, document.clone())).start();
						}
					}
					new PdfReportLinksWriter().updatePdfReportRankById(pdfReportLinks.getId(), 2);
					document = null;
					pdfLinkEsEntities.clear();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			} else {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

}

class PdfTableThread implements Runnable {

	PdfCodeTable pdfCodeTable;
	PdfReportLinks pdfReportLinks;
	Document document;

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
		if (pdfType.startsWith(linkPdfType) ) {
			try {
				String json = null;
				json = new KfPdfParser().parserPdfHtmlByPdfTypeAndLink(pdfCodeTable, pdfReportLinks, document);
				System.out.println(json);
				if (json.equals("{}") || json.equals("{\"state\":\"ok\",\"info\":[]}")
						|| json.equals("{\"state\":\"erro\"}") || json.equals("{\"state\":\"ok\",\"info\":[]}")) {
				} else {
					try {
						new DynamicDataStore().doStore(json, linkPdfType);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}