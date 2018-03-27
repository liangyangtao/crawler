package com.kf.data.pdfparser.thread;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.jdbc.PdfReportLinksReader;
import com.kf.data.pdfparser.jdbc.PdfReportLinksWriter;

/**
 * @Title: PdfLinkReaderWorker.java
 * @Package com.kf.data.web.task.thread
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月11日 下午5:09:37
 * @version V1.0
 */
public class PdfReportLinkReaderWorker implements Runnable {
	static final Logger logger = LoggerFactory.getLogger(PdfReportLinkReaderWorker.class);
	private LinkedBlockingQueue<PdfReportLinks> pdfcodeLinkQueue;

	public PdfReportLinkReaderWorker(LinkedBlockingQueue<PdfReportLinks> pdfcodeLinkQueue) {
		super();
		this.pdfcodeLinkQueue = pdfcodeLinkQueue;
	}

	@Override
	public void run() {
		while (true) {
			if (pdfcodeLinkQueue.size() == 0) {
				List<PdfReportLinks> links = new PdfReportLinksReader().readerPdfCodeLinkByTypeAndRank("半年报", 0);
				for (PdfReportLinks pdfReportLinks : links) {
					pdfcodeLinkQueue.add(pdfReportLinks);
					new PdfReportLinksWriter().updatePdfReportRankById(pdfReportLinks.getId(), 3);
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
