package com.kf.data.pdfparser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.thread.PdfReportLinkPaserWorker;
import com.kf.data.pdfparser.thread.PdfReportLinkReaderWorker;

/**
 * @Title: App.java
 * @Package com.kf.data.pdf2html
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月20日 下午4:06:11
 * @version V1.0
 */
public class App {

	private static LinkedBlockingQueue<PdfReportLinks> pdfcodeLinkQueue = new LinkedBlockingQueue<PdfReportLinks>();

	public static void main(String[] args) {
		KfConstant.init();
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new PdfReportLinkReaderWorker(pdfcodeLinkQueue));
		for (int i = 0; i < 16; i++) {
			executor.execute(new PdfReportLinkPaserWorker(pdfcodeLinkQueue));
		}
		executor.shutdown();

	}
}
