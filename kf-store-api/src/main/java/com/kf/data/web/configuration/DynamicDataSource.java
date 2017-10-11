package com.kf.data.web.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/****
 * 
 * @Title: DynamicDataSource.java
 * @Package com.kf.data.web.configuration
 * @Description: 动态切换数据源类
 * @author liangyt
 * @date 2017年10月11日 下午2:46:03
 * @version V1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	protected Object determineCurrentLookupKey() {
		return DatabaseContextHolder.getDateSourceType();
	}
}