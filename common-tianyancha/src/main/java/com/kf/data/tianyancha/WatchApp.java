package com.kf.data.tianyancha;

import com.kf.data.tianyancha.watch.PidRecorder;

/***
 * 
 * @Title: WatchApp.java
 * @Package com.kf.data.tianyancha
 * @Description: 守护进程
 * @author liangyt
 * @date 2017年10月16日 上午10:48:34
 * @version V1.0
 */
public class WatchApp {

	public static void main(String[] args) {
		new PidRecorder("QuartzStartup.bat", "WARCHPID").start();
	}
}
