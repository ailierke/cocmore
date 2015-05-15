package com.yunzo.cocmore.core.config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
/** 
 *Description: <应用配置类>. <br>
 *<p>
	<负责注册除Controller等web层以外的所有bean，包括aop代理，service层，dao层，缓存>
 </p>
 *	Makedate:2014年11月18日 上午4:26:59 
 * @author xiaobo 
 * @version V1.0                             
 */
public class AppConfig {}
//@Configuration
//@ComponentScan(basePackages = "com.yunzo.cocmore.core.function", excludeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class }) })
//@EnableAspectJAutoProxy(proxyTargetClass=true)
//@Import({CachingConfig.class,DaoConfig.class})
//public class AppConfig {
//	
//}
