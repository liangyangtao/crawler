package com.kf.data.tianyancha.watch;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.apache.log4j.Logger;

/****
 * 
 * @Title: ProgressReader.java
 * @Package com.kf.data.tianyancha.watch
 * @Description: 进程操作类
 * @author liangyt
 * @date 2017年10月16日 上午11:41:09
 * @version V1.0
 */
public class ProgressReader {
	static Logger logger = Logger.getLogger(ProgressReader.class);

	// 获取进程当前pid
	public static int getMyPid() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName(); // format: "pid@hostname"
		try {
			return Integer.parseInt(name.substring(0, name.indexOf('@')));
		} catch (Exception e) {
			return -1;

		}
	}

	// 关闭进程
	public static void skillProcess(int pid) {
		try {
			Runtime.getRuntime().exec("tskill " + pid);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	// 重启守护
	public static void startBatch(String batName) {
		Runtime run = Runtime.getRuntime();
		String cmd = "cmd /c start " + System.getProperty("user.dir") + "/" + batName;
		try {
			run.exec(cmd);
		} catch (IOException e) {
			logger.error(e);
		}
	}

}
