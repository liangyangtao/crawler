package com.kf.data.pdfparser.thread;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.jdbc.PdfCodetableReader;
import com.kf.data.pdfparser.jdbc.PdfReportLinksReader;
import com.kf.data.pdfparser.jdbc.PdfReportLinksWriter;

/**
 * @Title: PdfLinkReaderWorker.java
 * @Package com.kf.data.web.task.thread
 * @Description: 从数据库中读取要解析的链接
 * @author liangyt
 * @date 2017年5月11日 下午5:09:37
 * @version V1.0
 */
public class PdfReportLinkReaderWorker implements Runnable {
	static final Logger logger = LoggerFactory.getLogger(PdfReportLinkReaderWorker.class);
	private LinkedBlockingQueue<PdfReportLinks> pdfcodeLinkQueue;
	private PdfReportLinksReader pdfReportLinksReader;
	private PdfReportLinksWriter pdfReportLinksWriter;

	public PdfReportLinkReaderWorker(LinkedBlockingQueue<PdfReportLinks> pdfcodeLinkQueue,
			PdfReportLinksReader pdfReportLinksReader, PdfReportLinksWriter pdfReportLinksWriter,
			PdfCodetableReader pdfCodetableReader) {
		this.pdfcodeLinkQueue = pdfcodeLinkQueue;
		this.pdfReportLinksReader = pdfReportLinksReader;
		this.pdfReportLinksWriter = pdfReportLinksWriter;

	}

	@Override
	public void run() {
		while (true) {
			if (pdfcodeLinkQueue.size() == 0) {
				// 不管rank 是什麼的都得讀取
				List<PdfReportLinks> links = pdfReportLinksReader.readerPdfCodeLinkByTypeAndRank("公转书", 0);
				for (PdfReportLinks pdfReportLinks : links) {
					pdfcodeLinkQueue.add(pdfReportLinks);
					pdfReportLinksWriter.updatePdfReportRankById(pdfReportLinks.getId(), 3);
				}
				links.clear();
				links = null;
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
