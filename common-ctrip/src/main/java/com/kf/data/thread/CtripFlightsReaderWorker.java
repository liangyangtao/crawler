package com.kf.data.thread;

import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.KfConstant;

import net.sf.json.JSONObject;

/**
 * @Title: CtripFlightsReaderWorker.java
 * @Package com.kf.data.thread
 * @Description: 通过API 获取解析航线的路线
 * @author liangyt
 * @date 2017年5月2日 下午4:12:04
 * @version V1.0
 */
public class CtripFlightsReaderWorker extends BaseWorker implements Runnable {

	public LinkedBlockingQueue<Object> companyQueue;

	public CtripFlightsReaderWorker(LinkedBlockingQueue<Object> companyQueue) {
		super();
		this.companyQueue = companyQueue;
	}

	@Override
	public void run() {
		while (true) {
			if (companyQueue.size() <= 0) {
				// Calendar now = Calendar.getInstance();
				// int hours = now.get(Calendar.HOUR_OF_DAY);
				// if (hours > 22 || hours < 8) {
				// System.out.println(hours + "点不进行采集");
				// sleeping(10 * 60 * 1000);
				// } else {
				fillCompanyQueue();
				// }
				// 填充公司
			} else {
				sleeping(1 * 60 * 1000);
			}

		}

	}

	/***
	 * 通过API 获取航线URL
	 */
	private void fillCompanyQueue() {
//		String url = KfConstant.serverIp;
//		String html = Fetcher.getInstance().get(url);
//		JSONObject jsonObject = JSONObject.fromObject(html);
//		int err_code = jsonObject.getInt("err_code");
//		if (err_code == 200) {
//			String companyName = jsonObject.getString("companyName").trim();
//			if (companyName.isEmpty()) {
//				logger.info("获取公司名称为API为空");
//				return;
//			}
//			put(companyQueue, companyName);
//		}
		put(companyQueue, "http://flights.ctrip.com/international/round-beijing-macau-bjs-mfm?2017-10-28&2017-11-15&y_s");
	}

}
