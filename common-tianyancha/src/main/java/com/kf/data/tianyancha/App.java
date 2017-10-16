package com.kf.data.tianyancha;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.tianyancha.core.TianyanchaCrawler;
import com.kf.data.tianyancha.thread.CompanyDownloadWorker;
import com.kf.data.tianyancha.thread.TycApiCompanyReaderWorker;
import com.kf.data.tianyancha.watch.PidRecorder;

/***
 * 
 * @Title: App.java
 * @Package com.kf.data.tianyancha
 * @Description: 程序入口
 * @author liangyt
 * @date 2017年10月11日 下午2:25:38
 * @version V1.0
 */
public class App {
	public static LinkedBlockingQueue<Object> companyQueue = new LinkedBlockingQueue<Object>();
	public static TianyanchaCrawler tianyanchaCrawler = new TianyanchaCrawler();
	static PidRecorder pidRecorder = new PidRecorder("", "SPIDERPID");

	public static void main(String[] args) {
		// 初始化程序
		KfConstant.init();
		pidRecorder.start();
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
