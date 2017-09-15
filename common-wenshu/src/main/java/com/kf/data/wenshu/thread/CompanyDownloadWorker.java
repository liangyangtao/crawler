package com.kf.data.wenshu.thread;

import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.wenshu.core.WenshuCrawler;

/**
 * @Title: CompanyDownloadWorker.java
 * @Package com.kf.data.crawler.tianyancha.worker
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017
 * @version V1.0
 */
public class CompanyDownloadWorker extends BaseWorker implements Runnable {

	private LinkedBlockingQueue<Object> companyQueue;
	private WenshuCrawler wenshuCrawler;

	public CompanyDownloadWorker(LinkedBlockingQueue<Object> companyQueue, WenshuCrawler tianyanchaCrawler) {
		super();
		this.companyQueue = companyQueue;
		this.wenshuCrawler = tianyanchaCrawler;
	}

	@Override
	public void run() {
		while (true) {
			if (companyQueue.size() > 0) {
				try {
					String company = (String) take(companyQueue);
					fillCompanyQueue(company);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				} finally {
				}
			} else {
				sleeping(1000);
			}

		}

	}

	private void fillCompanyQueue(String company) {
		wenshuCrawler.crawlerWenshuByCompanyName(company);
	}

}
