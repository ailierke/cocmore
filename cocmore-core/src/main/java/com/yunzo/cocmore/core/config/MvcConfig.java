package com.yunzo.cocmore.core.config;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.yunzo.cocmore.core.baseinit.COC_CheckSign;
import com.yunzo.cocmore.core.baseinit.COC_InitializingInterceptor;
import com.yunzo.cocmore.core.baseinit.COC_SimpleMappingExceptionResolver;

/**
 * Description: <MVC配置>. <br>
 * <p>
 * <配置MVC>
 * </p>
 * Makedate:2014年11月18日 上午4:26:59
 * 
 * @author xiaobo
 * @version V1.0
 */
public class MvcConfig{}
//@Configuration
//@EnableAspectJAutoProxy(proxyTargetClass=true)
//@EnableWebMvc
//@ComponentScan(basePackages = "com.yunzo.cocmore.core.function", useDefaultFilters = false, includeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class }) })
//public class MvcConfig extends WebMvcConfigurerAdapter  {
//	private static final Logger logger = Logger.getLogger(MvcConfig.class);
//	/**
//	 * 描述 : <注册试图处理器>. <br>
//	 * <p>
//	 * <使用方法说明>
//	 * </p>
//	 * 
//	 * @return
//	 */
//	@Bean
//	public ViewResolver viewResolver() {
//		logger.info("ViewResolver");
//		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//		viewResolver.setPrefix("/function/");
//		viewResolver.setSuffix(".jsp");
//		return viewResolver;
//	}
//	/**
//	 * 描述 : <基于cookie的本地化资源处理器>. <br>
//	 * <p>
//	 * <使用方法说明>
//	 * </p>
//	 * 
//	 * @return
//	 */
//	@Bean(name = "localeResolver")
//	public CookieLocaleResolver cookieLocaleResolver() {
//		logger.info("CookieLocaleResolver");
//		return new CookieLocaleResolver();
//	}
//	/**
//	 * 描述 : <文件上传处理器>. <br>
//	 * <p>
//	 * <使用方法说明>
//	 * </p>
//	 * 
//	 * @return
//	 */
//	@Bean(name = "multipartResolver")
//	public CommonsMultipartResolver commonsMultipartResolver() {
//		logger.info("CommonsMultipartResolver");
//		CommonsMultipartResolver fileupLoadResolver = new CommonsMultipartResolver();
//		fileupLoadResolver.setDefaultEncoding("UTF-8");
//		fileupLoadResolver.setMaxUploadSize(102400);
//		return fileupLoadResolver;
//	}
//	@Override
//    public void addFormatters(FormatterRegistry registry) {
//	};
//
//    @Override
//   public void configureMessageConverters(java.util.List<org.springframework.http.converter.HttpMessageConverter<?>> converters) {
//    	converters.add(new MappingJackson2HttpMessageConverter());
////    	converters.add(new Jaxb2RootElementHttpMessageConverter());
//    };
//    /**
//     * 配置出错页面
//     */
//    @Override
//    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
//    	COC_SimpleMappingExceptionResolver exeptionResolver = new COC_SimpleMappingExceptionResolver();
//    	exeptionResolver.setDefaultErrorView("common_error");
//    	exeptionResolver.setExceptionAttribute("exception");
//		Properties properties = new Properties();
//		properties.setProperty("java.lang.RuntimeException", "common_error");
//		exeptionResolver.setExceptionMappings(properties);
//    	exceptionResolvers.add(exeptionResolver);
//    	super.configureHandlerExceptionResolvers(exceptionResolvers);
//    }
//    
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//    	super.configureViewResolvers(registry);
//    }
//    
//    
//    /**
//     * 添加interceptors
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//    	// TODO Auto-generated method stub
//    			logger.info("addInterceptors start");
//    			registry.addInterceptor(new LocaleChangeInterceptor());
//    			registry.addInterceptor(new  COC_InitializingInterceptor());
//    			//update by ailierke  为每自己的app端接口做拦截cocsign验证,图片有 
//    			registry.addInterceptor(new COC_CheckSign()).addPathPatterns("/mobileapi/*/*").excludePathPatterns("/mobileapi/user/*");
//    			logger.info("addInterceptors end");
//    }
//    /**
//     * 开放静态资源
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    	logger.info("addResourceHandlers");
//		registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/");
//		registry.addResourceHandler("/function/**").addResourceLocations("/function/");
//    }
//}
