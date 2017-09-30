package com.kf.data.tianyancha.thread;

import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.tianyancha.core.TianyanchaCrawler;

/**
 * @Title: CompanyDownloadWorker.java
 * @Package com.kf.data.crawler.tianyancha.worker
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月4日 下午1:34:34
 * @version V1.0
 */
public class CompanyDownloadWorker extends BaseWorker implements Runnable {

	private LinkedBlockingQueue<Object> companyQueue;
	private TianyanchaCrawler tianyanchaCrawler;

	public CompanyDownloadWorker(LinkedBlockingQueue<Object> companyQueue, TianyanchaCrawler tianyanchaCrawler) {
		super();
		this.companyQueue = companyQueue;
		this.tianyanchaCrawler = tianyanchaCrawler;
	}

	@Override
	public void run() {
		while (true) {
			if (companyQueue.size() > 0) {
				int id = 0;
				try {
					String companyName = (String) take(companyQueue);
					fillTycQueue(companyName);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			} else {
				sleeping(1000);
			}

		}

	}

	private void fillTycQueue(String company) {
		tianyanchaCrawler.parserTycBaseCompany(company);
	}

}
