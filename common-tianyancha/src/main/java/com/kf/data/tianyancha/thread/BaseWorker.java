package com.kf.data.tianyancha.thread;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***
 * 
 * @Title: BaseWorker.java
 * @Package com.kf.data.tianyancha.thread
 * @Description: 线程基础类
 * @author liangyt
 * @date 2017年10月11日 下午2:22:21
 * @version V1.0
 */
public class BaseWorker {

	public static Log logger = LogFactory.getLog(BaseWorker.class);

	/***
	 * 将目标存储到阻塞队列
	 * 
	 * @param queue
	 * @param object
	 */
	public void put(LinkedBlockingQueue<Object> queue, Object object) {
		try {
			queue.put(object);
		} catch (InterruptedException e) {
			logger.info("", e);
		}
	}

	/***
	 * 从阻塞队列中取出目标
	 * 
	 * @param queue
	 * @return
	 */
	public Object take(LinkedBlockingQueue<Object> queue) {
		Object object = null;
		try {
			object = queue.take();
		} catch (InterruptedException e) {
			logger.info("", e);
		}
		return object;
	}

	/***
	 * 线程休眠多长时间
	 * 
	 * @param time
	 */
	public void sleeping(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
