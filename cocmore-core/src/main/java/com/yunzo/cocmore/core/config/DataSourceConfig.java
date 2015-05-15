package com.yunzo.cocmore.core.config;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Description: <加载资源文件>. <br>
 * <p>
 * <加载资源文件>
 * </p>
 * Makedate:2014年11月18日 上午4:26:59
 * 
 * @author xiaobo
 * @version V1.0
 */
public class DataSourceConfig{}
//@Configuration
//// 加载资源文件
//@PropertySource({ "classpath:/config/properties/db.properties" })
//public class DataSourceConfig {
//	private static final Logger logger = Logger
//			.getLogger(DataSourceConfig.class);
//	/*
//	 * 绑定资源属性
//	 */
//	@Value("${jdbc.driver}")
//	String driverClass;
//	@Value("${jdbc.url}")
//	String url;
//	@Value("${jdbc.username}")
//	String userName;
//	@Value("${jdbc.password}")
//	String passWord;
//
//	@Bean(name = "dataSource")
//	public DataSource dataSource() {
//		logger.info("DataSource");
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName(driverClass);
//		dataSource.setUrl(url);
//		dataSource.setUsername(userName);
//		dataSource.setPassword(passWord);
//		return dataSource;
//	}
//}
