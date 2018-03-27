package com.kf.data.web.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/****
 * 
 * @Title: AppInitializer.java
 * @Package com.kf.data.web.configuration
 * @Description: 请求过滤器
 * @author liangyt
 * @date 2017年10月11日 下午2:44:28
 * @version V1.0
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
