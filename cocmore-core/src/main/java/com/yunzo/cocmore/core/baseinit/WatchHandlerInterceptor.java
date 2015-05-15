package com.yunzo.cocmore.core.baseinit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/**
 * 性能监控
 * 记录一下请求的处理时间，得到一些慢请求（如处理时间超过500毫秒）
 * 在测试时需要把stopWatchHandlerInterceptor放在拦截器链的第一个(放置拦截器的执行顺序)，这样得到的时间才是比较准确的。
 * NamedThreadLocal：Spring提供的一个命名的ThreadLocal实现。由于拦截器是单例的，所以使用线程安全的变量
 * @author ailierke
 *Filter是最通用的、最先应该使用的。如登录这种拦截器最好使用Filter来实现
 */
public class WatchHandlerInterceptor extends HandlerInterceptorAdapter{
	private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
		long beginTime = System.currentTimeMillis();//1、开始时间
		startTimeThreadLocal.set(beginTime);//线程绑定变量（该数据只有当前请求的线程可见）
		return true;//继续流程
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,Object handler, Exception ex) throws Exception {
		long endTime = System.currentTimeMillis();//2、结束时间
		long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
		long consumeTime = endTime - beginTime;//3、消耗的时间
		if(consumeTime > 500) {//此处认为处理时间超过500毫秒的请求为慢请求
			//TODO 记录到日志文件
			System.out.println(
					String.format("%s consume %d millis", request.getRequestURI(), consumeTime));
		}
	}
}
