package com.kf.data.tianyancha.thread;

import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.KfConstant;

import net.sf.json.JSONObject;

/**
 * @Title: CompanyReaderWorker.java
 * @Package com.kf.data.crawler.tianyancha.worker
 * @Description: 查询neeq_basecompany 的公司名称
 * @author liangyt
 * @date 2017年5月2日 下午4:12:04
 * @version V1.0
 */
public class TycApiCompanyReaderWorker extends BaseWorker implements Runnable {

	public LinkedBlockingQueue<Object> companyQueue;

	public TycApiCompanyReaderWorker(LinkedBlockingQueue<Object> companyQueue) {
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

	private void fillCompanyQueue() {
		String url = KfConstant.serverIp;
		String html = Fetcher.getInstance().get(url);
		JSONObject jsonObject = JSONObject.fromObject(html);
		int err_code = jsonObject.getInt("err_code");
		if (err_code == 200) {
			String companyName = jsonObject.getString("companyName").trim();
			if (companyName.isEmpty()) {
				return;
			}
			put(companyQueue, companyName);
		}
		//中建材集团进出口公司
		//周宁县农村信用合作联社
		//杭州来拍网络科技有限公司
		//
//		put(companyQueue, "北京百度网讯科技有限公司");
	}

}
