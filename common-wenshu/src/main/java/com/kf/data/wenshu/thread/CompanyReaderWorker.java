package com.kf.data.wenshu.thread;

import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.fetcher.Fetcher;

import net.sf.json.JSONObject;

/**
 * @Title: CompanyReaderWorker.java
 * @Package com.kf.data.crawler.tianyancha.worker
 * @Description: 查询neeq_basecompany 的公司名称
 * @author liangyt
 * @date 2017年5月2日 下午4:12:04
 * @version V1.0
 */
public class CompanyReaderWorker extends BaseWorker implements Runnable {

	public LinkedBlockingQueue<Object> companyQueue;

	public CompanyReaderWorker(LinkedBlockingQueue<Object> companyQueue) {
		super();
		this.companyQueue = companyQueue;
	}

	@Override
	public void run() {
		while (true) {
			if (companyQueue.size() <= 0) {
				fillCompanyQueue();
			} else {
				sleeping(10 * 60 * 1000);
			}
		}

	}

	private void fillCompanyQueue() {
		String url = "http://101.201.111.214:8097/company/getNeeqComapny";
		String html = Fetcher.getInstance().get(url);
		JSONObject jsonObject = JSONObject.fromObject(html);
		int err_code = jsonObject.getInt("err_code");
		if (err_code == 200) {
			String companyName = jsonObject.getString("companyName").trim();
			put(companyQueue, companyName);
		}
	}

}
