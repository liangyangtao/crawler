package com.kf.data.tianyancha;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.tianyancha.core.TianyanchaCrawler;
import com.kf.data.tianyancha.thread.CompanyDownloadWorker;
import com.kf.data.tianyancha.thread.TycApiCompanyReaderWorker;

public class App {
	public static LinkedBlockingQueue<Object> companyQueue = new LinkedBlockingQueue<Object>();
	public static TianyanchaCrawler tianyanchaCrawler = new TianyanchaCrawler();

	public static void main(String[] args) {
		KfConstant.init();
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		ExecutorService executor = Executors.newCachedThreadPool();
		// 读取公司名称
		executor.execute(new TycApiCompanyReaderWorker(companyQueue));
		// 爬取公司信息
		for (int i = 0; i < 1; i++) {
			executor.execute(new CompanyDownloadWorker(companyQueue, tianyanchaCrawler));
		}
		executor.shutdown();
	}
}
