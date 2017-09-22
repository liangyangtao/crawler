package com.kf.data.pdfparser.thread;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kf.data.mybatis.entity.PdfCodeTable;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.jdbc.PdfCodeLinkWriter;
import com.kf.data.pdfparser.jdbc.PdfCodetableReader;
import com.kf.data.pdfparser.jdbc.PdfReportLinksReader;

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
				// 不管rank 是什麼的都得讀取
				List<PdfCodeTable> pdftables = new PdfCodetableReader().readPdfTable();
				for (PdfCodeTable pdfCodeTable : pdftables) {
					String pdftype = pdfCodeTable.getPdfType();
					if (pdfCodeTable.getTask() == 1) {
						List<PdfReportLinks> links = new PdfReportLinksReader().readerPdfCodeLinkByTypeAndRank(pdftype,
								0);
						for (PdfReportLinks pdfReportLinks : links) {
							pdfcodeLinkQueue.add(pdfReportLinks);
							new PdfCodeLinkWriter().updatePdfCodeRankById(pdfReportLinks.getId(), 3);
						}
					}
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
