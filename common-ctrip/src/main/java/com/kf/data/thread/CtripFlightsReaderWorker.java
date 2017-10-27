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
				try {
					fillCompanyQueue();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			} else {
				sleeping(1 * 60 * 1000);
			}

		}

	}

	/***
	 * 通过API 获取航线URL
	 */
	private void fillCompanyQueue() {
		String url = KfConstant.serverIp;
		String html = Fetcher.getInstance().get(url);
		if (html.startsWith("{")) {
			JSONObject jsonObject = JSONObject.fromObject(html);
			int err_code = jsonObject.getInt("err_code");
			if (err_code == 200) {
				String fightUrl = jsonObject.getString("url").trim();
				if (fightUrl == null || fightUrl.isEmpty()) {
					logger.info("获取公司名称为API为空");
					return;
				}
				logger.info("获取到航线信息" + fightUrl);
				put(companyQueue, fightUrl);
			}
		} else {
			logger.info("通过API获取航线信息失败");
		}
	}

}
