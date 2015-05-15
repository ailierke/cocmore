package com.yunzo.cocmore.core.config;
import org.apache.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
/** 
 *Description: <启用缓存>. <br>
 *<p>
	<缓存>
 </p>
 *	Makedate:2014年11月18日 上午4:26:59 
 * @author xiaobo 
 * @version V1.0                             
 */
public class CachingConfig {}
//@Configuration
//@EnableCaching//<!-- 启用缓存注解 --> <cache:annotation-driven cache-manager="cacheManager" />
//public class CachingConfig {
//	private static final Logger logger = Logger.getLogger(CachingConfig.class);
//	@Bean
//	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
//		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource(
//				"config/ehcache.xml"));
//		return ehCacheManagerFactoryBean;
//	}
//
//	@Bean
//	public CacheManager cacheManager() {
//		logger.info("EhCacheCacheManager");
//		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
//		cacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
//		return cacheManager;
//	}
//	
//}
