package com.kf.data.thread;

import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.core.CtripFlightsCrawler;

/**
 * @Title: CtripFlightsDownloadWorker.java
 * @Package com.kf.data.thread
 * @Description: 获取航线信息
 * @author liangyt
 * @date 2017年5月4日 下午1:34:34
 * @version V1.0
 */
public class CtripFlightsDownloadWorker extends BaseWorker implements Runnable {

	CtripFlightsCrawler ctripFlightsCrawler = new CtripFlightsCrawler();
	
	
	private LinkedBlockingQueue<Object> companyQueue;

	public CtripFlightsDownloadWorker(LinkedBlockingQueue<Object> companyQueue) {
		super();
		this.companyQueue = companyQueue;
	}

	@Override
	public void run() {
		while (true) {
			if (companyQueue.size() > 0) {
				try {
					String companyName = (String) take(companyQueue);
					fillTycQueue(companyName);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				} catch (Throwable t) {
					t.printStackTrace();
					continue;
				}
			} else {
				sleeping(1000);
			}

		}

	}

	/***
	 * 输入航线url称获取数据
	 * 
	 * @param url
	 */
	private void fillTycQueue(String url) {
		ctripFlightsCrawler.spider(url);
	}

}
