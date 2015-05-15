package com.yunzo.cocmore.core.config;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.FilterRegistration.Dynamic;

import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.Log4jConfigListener;

import com.yunzo.cocmore.core.baseinit.COC_SpringContext;

/**
 * Description: <安全框架配置>. <br>
 * <p>
 * <所有实现了WebApplicationInitializer接口的类都会在容器启动时自动被加载运行，用@Order注解设定加载顺序
 * 这是servlet3.0+后加入的特性，web.xml中可以不需要配置内容，都硬编码到WebApplicationInitializer的实现类中
 * >
 *
 * </p>
 * Makedate:2014年11月18日 上午4:26:59
 * 
 * @author xiaobo
 * @version V1.0
 */
public class WebInitializer{}
//@Order(3)
//spring DispatcherServlet的配置,其它servlet和监听器等需要额外声明，用@Order注解设定启动顺序
//public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//	 /*
//	  * DispatcherServlet的映射路径
//	  */
//   @Override
//   protected String[] getServletMappings() {
//       return new String[]{"/"};
//   }
//
//   /*
//	  * 应用上下文，除web部分
//	  */
//   @SuppressWarnings({ "unchecked", "rawtypes" })
//	@Override
//   protected Class[] getRootConfigClasses() {
//       return new Class[] {AppConfig.class};//AppConfig里面引用了数据库的配置信息
////       return new Class[] {AppConfig.class};
//   }
//
//   /*
//	  * web上下文
//	  * 这里一定要将spring mvc的配置类导入进来否则检测不到
//	  */
//   @SuppressWarnings({ "unchecked", "rawtypes" })
//	@Override
//   protected Class[] getServletConfigClasses() {
//       return new Class[] {MvcConfig.class};
////	   return null;
//   }
//
//   /*
//	  * 注册过滤器，映射路径与DispatcherServlet一致，路径不一致的过滤器需要注册到另外的WebApplicationInitializer中
//	  */
//   @Override
//   protected Filter[] getServletFilters() {
//       //配置字符集
//	   CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//       characterEncodingFilter.setEncoding("UTF-8");
//       characterEncodingFilter.setForceEncoding(true);
//       	//配置hibernate开启lazy后数据传输
//       OpenSessionInViewFilter hibernateSessionInViewFilter = new OpenSessionInViewFilter();
//       return new Filter[] {characterEncodingFilter,hibernateSessionInViewFilter};
//   }
// 
//
//   /**
//    * 添加监听器log4j
//    */
//   @Override
//	protected void registerContextLoaderListener(ServletContext servletContext) {
//		   servletContext.setInitParameter("log4jConfigLocation", "classpath:config/properties/log4j.properties");
//		   servletContext.addListener(new Log4jConfigListener());
//		super.registerContextLoaderListener(servletContext);
//	}
//}



