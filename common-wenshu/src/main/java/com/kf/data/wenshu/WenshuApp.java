package com.kf.data.wenshu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.wenshu.core.WenshuCrawler;
import com.kf.data.wenshu.thread.CompanyDownloadWorker;
import com.kf.data.wenshu.thread.CompanyReaderWorker;

/***
 * 
 * @author meidi
 *
 */
public class WenshuApp {

	public static LinkedBlockingQueue<Object> companyQueue = new LinkedBlockingQueue<Object>();
	public static WenshuCrawler wenshuCrawler = new WenshuCrawler();

	public static void main(String[] args) {
		KfConstant.init();
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new CompanyReaderWorker(companyQueue));
		for (int i = 0; i < 1; i++) {
			executor.execute(new CompanyDownloadWorker(companyQueue, wenshuCrawler));
		}
		executor.shutdown();
	}
}
