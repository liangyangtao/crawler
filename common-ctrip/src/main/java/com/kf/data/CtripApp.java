package com.kf.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.thread.CtripFlightsDownloadWorker;
import com.kf.data.thread.CtripFlightsReaderWorker;

/***
 * 
 * @Title: CtripApp.java
 * @Package com.kf.data
 * @Description: 携程爬虫入口
 * @author liangyt
 * @date 2017年10月25日 下午4:21:06
 * @version V1.0
 */
public class CtripApp {
	public static LinkedBlockingQueue<Object> companyQueue = new LinkedBlockingQueue<Object>();

	public static void main(String[] args) {
		// 初始化程序
		KfConstant.init();
		int clientNum = 1;
		try {
			String path = CtripApp.class.getClassLoader().getResource("").toURI().getPath();
			File file = new File(path + File.separator + "clientNum.txt");
			if (file.exists()) {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
				String line = "";
				line = br.readLine();
				clientNum = Integer.parseInt(line);
				br.close();
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			clientNum = 1;
		}
		ExecutorService executor = Executors.newCachedThreadPool();
		// 读取公司名称
		executor.execute(new CtripFlightsReaderWorker(companyQueue));
		// 爬取公司信息
		for (int i = 0; i < clientNum; i++) {
			executor.execute(new CtripFlightsDownloadWorker(companyQueue));
		}
		executor.shutdown();
	}

}
