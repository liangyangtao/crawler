package com.kf.data.web.configuration;

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
