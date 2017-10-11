package com.kf.data.web.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/***
 * 
 * @Title: MultipleDataSourceAspectAdvice.java
 * @Package com.kf.data.web.configuration
 * @Description: 根据不同的包名进行数据源的切换
 * @author liangyt
 * @date 2017年10月11日 下午2:46:20
 * @version V1.0
 */
@Aspect
@Component
public class MultipleDataSourceAspectAdvice {

	@Before("execution(* com.kf.data.service.crawler.**.*(..))")
	public void setCrawlerDataSourceKey(JoinPoint point) {
		System.out.println("转换为爬虫数据源");
		DatabaseContextHolder.clearCustomerType();
		DatabaseContextHolder.setDateSourceType(DateSourceType.CRAWLER);
	}

	@Before("execution(* com.kf.data.service.online.**.*(..))")
	public void setOnlineDataSourceKey(JoinPoint point) {
		System.out.println("转换为线上数据源");
		DatabaseContextHolder.clearCustomerType();
		DatabaseContextHolder.setDateSourceType(DateSourceType.ONLINE);
	}

	@Before("execution(* com.kf.data.service.mid.**.*(..))")
	public void setMidDataSourceKey(JoinPoint point) {
		System.out.println("转换为中间数据源");
		DatabaseContextHolder.clearCustomerType();
		DatabaseContextHolder.setDateSourceType(DateSourceType.MID);
	}

	@Before("execution(* com.kf.data.service.crawler_neeq.**.*(..))")
	public void setCrawlerNeeqDataSourceKey(JoinPoint point) {
		System.out.println("转换为爬虫NEEQ数据源");
		DatabaseContextHolder.clearCustomerType();
		DatabaseContextHolder.setDateSourceType(DateSourceType.CRAWLERNEEQ);
	}

	@Before("execution(* com.kf.data.service.crawler_his.**.*(..))")
	public void setCrawlerHisDataSourceKey(JoinPoint point) {
		System.out.println("转换为爬虫NEEQ数据源");
		DatabaseContextHolder.clearCustomerType();
		DatabaseContextHolder.setDateSourceType(DateSourceType.CRAWLER_HIS);
	}

}