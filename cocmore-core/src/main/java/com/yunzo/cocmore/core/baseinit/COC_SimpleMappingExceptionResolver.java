package com.yunzo.cocmore.core.baseinit;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/** 
 *Description: <COC框架 全局异常处理类>. <br>
 *<p>
	<主要用于全局处理异常操作>
 </p>
 *	Makedate:2014年11月18日 上午4:26:59 
 * @author xiaobo 
 * @version V1.0                             
 */
public class COC_SimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {
	Logger log= Logger.getLogger(COC_SimpleMappingExceptionResolver.class);
	@Override
	protected ModelAndView getModelAndView(String viewName, Exception ex,
			HttpServletRequest request) {
		logger.error("run time exception:", ex);//将异常信息打印到日志中
		return super.getModelAndView(viewName, ex, request);
	}
}
