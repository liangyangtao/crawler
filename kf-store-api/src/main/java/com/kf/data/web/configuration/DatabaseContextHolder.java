package com.kf.data.web.configuration;

/***
 * 
 * @Title: DatabaseContextHolder.java
 * @Package com.kf.data.web.configuration
 * @Description: 动态切换数据源
 * @author liangyt
 * @date 2017年10月11日 下午2:45:06
 * @version V1.0
 */
public class DatabaseContextHolder {

	private static final ThreadLocal<DateSourceType> contextHolder = new ThreadLocal<>();

	public static void setDateSourceType(DateSourceType type) {
		contextHolder.set(type);
	}

	public static DateSourceType getDateSourceType() {
		return contextHolder.get();
	}

	public static void clearCustomerType() {
		contextHolder.remove();
	}
}
