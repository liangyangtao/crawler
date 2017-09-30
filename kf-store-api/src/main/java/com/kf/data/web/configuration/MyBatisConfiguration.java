package com.kf.data.web.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.kf.data" })
@PropertySource(value = { "classpath:application.properties" })
@MapperScan({ "com.kf.data.mybatis.mapper" })
public class MyBatisConfiguration {

	@Autowired
	private Environment environment;

	@Bean
	public DataSource dataSourceMid() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.etl.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.etl.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.etl.password"));
		dataSource.setMaxIdle(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxIdle")));
		dataSource.setMaxActive(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxActive")));
		dataSource.setMaxWait(Integer.parseInt(environment.getRequiredProperty("jdbc.maxWait")));
		dataSource.setRemoveAbandoned(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.removeAbandoned")));
		dataSource.setRemoveAbandonedTimeout(
				Integer.parseInt(environment.getRequiredProperty("jdbc.removeAbandonedTimeout")));
		dataSource.setTimeBetweenEvictionRunsMillis(
				Integer.parseInt(environment.getRequiredProperty("jdbc.timeBetweenEvictionRunsMillis")));
		dataSource.setMinEvictableIdleTimeMillis(
				Integer.parseInt(environment.getRequiredProperty("jdbc.minEvictableIdleTimeMillis")));
		dataSource.setTestWhileIdle(true);
		dataSource.setNumTestsPerEvictionRun(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxActive")));
		dataSource.setTestOnBorrow(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.testOnBorrow")));
		dataSource.setValidationQuery(environment.getRequiredProperty("jdbc.validationQuery"));
		
		return dataSource;
	}

	@Bean
	public DataSource dataSourceCrawler() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.crawler.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.crawler.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.crawler.password"));
		dataSource.setMaxIdle(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxIdle")));
		dataSource.setMaxActive(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxActive")));
		dataSource.setMaxWait(Integer.parseInt(environment.getRequiredProperty("jdbc.maxWait")));
		dataSource.setRemoveAbandoned(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.removeAbandoned")));
		dataSource.setRemoveAbandonedTimeout(
				Integer.parseInt(environment.getRequiredProperty("jdbc.removeAbandonedTimeout")));
		dataSource.setTimeBetweenEvictionRunsMillis(
				Integer.parseInt(environment.getRequiredProperty("jdbc.timeBetweenEvictionRunsMillis")));
		dataSource.setMinEvictableIdleTimeMillis(
				Integer.parseInt(environment.getRequiredProperty("jdbc.minEvictableIdleTimeMillis")));
		dataSource.setTestOnBorrow(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.testOnBorrow")));
		dataSource.setValidationQuery(environment.getRequiredProperty("jdbc.validationQuery"));
		return dataSource;
	}

	@Bean
	public DataSource dataSourceCrawlerHis() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.crawlerhis.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.crawlerhis.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.crawlerhis.password"));
		dataSource.setMaxIdle(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxIdle")));
		dataSource.setMaxActive(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxActive")));
		dataSource.setMaxWait(Integer.parseInt(environment.getRequiredProperty("jdbc.maxWait")));
		dataSource.setRemoveAbandoned(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.removeAbandoned")));
		dataSource.setRemoveAbandonedTimeout(
				Integer.parseInt(environment.getRequiredProperty("jdbc.removeAbandonedTimeout")));
		dataSource.setTimeBetweenEvictionRunsMillis(
				Integer.parseInt(environment.getRequiredProperty("jdbc.timeBetweenEvictionRunsMillis")));
		dataSource.setMinEvictableIdleTimeMillis(
				Integer.parseInt(environment.getRequiredProperty("jdbc.minEvictableIdleTimeMillis")));
		dataSource.setTestOnBorrow(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.testOnBorrow")));
		dataSource.setValidationQuery(environment.getRequiredProperty("jdbc.validationQuery"));
		return dataSource;
	}

	@Bean
	public DataSource dataSourceOnline() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.online.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.online.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.online.password"));
		dataSource.setMaxIdle(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxIdle")));
		dataSource.setMaxActive(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxActive")));
		dataSource.setMaxWait(Integer.parseInt(environment.getRequiredProperty("jdbc.maxWait")));
		dataSource.setRemoveAbandoned(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.removeAbandoned")));
		dataSource.setRemoveAbandonedTimeout(
				Integer.parseInt(environment.getRequiredProperty("jdbc.removeAbandonedTimeout")));
		dataSource.setTimeBetweenEvictionRunsMillis(
				Integer.parseInt(environment.getRequiredProperty("jdbc.timeBetweenEvictionRunsMillis")));
		dataSource.setMinEvictableIdleTimeMillis(
				Integer.parseInt(environment.getRequiredProperty("jdbc.minEvictableIdleTimeMillis")));
		dataSource.setTestOnBorrow(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.testOnBorrow")));
		dataSource.setValidationQuery(environment.getRequiredProperty("jdbc.validationQuery"));
		return dataSource;
	}

	@Bean
	public DataSource dataSourceCrawlerNeeq() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.crawlerneeq.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.crawlerneeq.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.crawlerneeq.password"));
		dataSource.setMaxIdle(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxIdle")));
		dataSource.setMaxActive(Integer.parseInt(environment.getRequiredProperty("jdbc.pool.maxActive")));
		dataSource.setMaxWait(Integer.parseInt(environment.getRequiredProperty("jdbc.maxWait")));
		dataSource.setRemoveAbandoned(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.removeAbandoned")));
		dataSource.setRemoveAbandonedTimeout(
				Integer.parseInt(environment.getRequiredProperty("jdbc.removeAbandonedTimeout")));
		dataSource.setTimeBetweenEvictionRunsMillis(
				Integer.parseInt(environment.getRequiredProperty("jdbc.timeBetweenEvictionRunsMillis")));
		dataSource.setMinEvictableIdleTimeMillis(
				Integer.parseInt(environment.getRequiredProperty("jdbc.minEvictableIdleTimeMillis")));
		dataSource.setTestOnBorrow(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.testOnBorrow")));
		dataSource.setValidationQuery(environment.getRequiredProperty("jdbc.validationQuery"));
		return dataSource;
	}

	/**
	 * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
	 * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
	 */
	@Bean
	@Primary
	public DynamicDataSource dataSource(@Qualifier("dataSourceMid") DataSource dataSourceMid,
			@Qualifier("dataSourceCrawler") DataSource dataSourceCrawler,
			@Qualifier("dataSourceOnline") DataSource dataSourceOnline,
			@Qualifier("dataSourceCrawlerNeeq") DataSource dataSourceCrawlerNeeq,
			@Qualifier("dataSourceCrawlerHis") DataSource dataSourceCrawlerHis) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DateSourceType.MID, dataSourceMid);
		targetDataSources.put(DateSourceType.CRAWLER, dataSourceCrawler);
		targetDataSources.put(DateSourceType.ONLINE, dataSourceOnline);
		targetDataSources.put(DateSourceType.CRAWLERNEEQ, dataSourceCrawlerNeeq);
		targetDataSources.put(DateSourceType.CRAWLER_HIS, dataSourceCrawlerHis);
		DynamicDataSource dataSource = new DynamicDataSource();
		dataSource.setTargetDataSources(targetDataSources);
		dataSource.setDefaultTargetDataSource(dataSourceCrawler);
		return dataSource;
	}

	@Bean
	public SqlSessionFactory sessionFactory(DynamicDataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setTypeAliasesPackage("com.kf.data.mybatis.entity");
		return sessionFactoryBean.getObject();
	}

	@Bean
	@Autowired
	DataSourceTransactionManager dataSourceTransactionManager(DynamicDataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);
		return dataSourceTransactionManager;
	}


}
