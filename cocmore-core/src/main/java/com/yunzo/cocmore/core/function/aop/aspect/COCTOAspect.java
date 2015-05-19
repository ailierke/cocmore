package com.yunzo.cocmore.core.function.aop.aspect;

import net.minidev.json.JSONUtil;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
// 声明这是一个切面Bean
@Aspect
// @Aspect放在类头上，把这个类作为一个切面，但是这个类一定要显式的注册在Spring容器中。
@Order(1)
public class COCTOAspect {
	// 本地异常日志记录对象
	private static final Logger logger = Logger.getLogger(COCTOAspect.class);

//	private YSystemconfigurationLog log;
	// 注入Service用于把日志保存数据库
	
	// Service层切点
	@Pointcut("@annotation(com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog)")
	public void serviceAspect() {
	}

	// Controller层切点
	@Pointcut("@annotation(com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog)")
	public void controllerAspect() {
	}

	/**
	 * 前置通知 用于拦截Controller层记录用户的操作
	 * 
	 * @param joinPoint
	 *            切点
	 */
	// @Around("execution(* org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter.handle(..))") 
	@Before("controllerAspect()")  
	 public void doBefore(JoinPoint joinPoint) {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		// 读取session中的用户
//		 YSystemUsers user = (YSystemUsers) session.getAttribute("user");
//		 YBasicMember member = (YBasicMember) session.getAttribute("member");
		// 请求的IP
		String ip = request.getRemoteAddr();
		try {
			// *========数据库日志=========*//
//			log=new YSystemconfigurationLog();
//			log.setFid(UUID.randomUUID().toString());
//			log.setFnumber((String)map.get("serialNumber"));
//			if(user != null){
//				log.setFuserId(user.getFaccount());
//			}
//			if(member != null){
//				log.setFuserId(member.getFmobilePhone());
//			}
//			log.setFtype(0);
//			log.setFtime(new Date());
//			log.setFcontent(getControllerMethodDescription(joinPoint));
//			log.setFip(ip);
//			log.setFremark((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
			
			// 保存数据库
//			logService.save(log);
//			getNumberService.addSerialNumber("XT-RZGL");
		} catch (Exception e) {
			 //记录本地异常日志  
            logger.error("==前置通知异常==");  
            logger.error(e.getMessage());  
		}
	}

	/**
	 * 异常通知 用于拦截service层记录异常日志
	 * 
	 * @param joinPoint
	 * @param e
	 */
//	@AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		// 读取session中的用户
//		 YSystemUsers user = (YSystemUsers) session.getAttribute("user");
//		 YBasicMember member = (YBasicMember) session.getAttribute("member");
		// 获取请求ip
		String ip = request.getRemoteAddr();

		try {
			/* ==========数据库日志========= */
//			Map<String,Object> map= getNumberService.checkExist("XT-RZGL");
//			log=new YSystemconfigurationLog();
//			log.setFid(UUID.randomUUID().toString());
//			log.setFnumber((String)map.get("serialNumber"));
//			if(user != null){
//				log.setFuserId(user.getFaccount());
//			}
//			if(member != null){
//				log.setFuserId(member.getFmobilePhone());
//			}
//			log.setFtype(1);
//			log.setFtime(new Date());
//			log.setFcontent(getServiceMthodDescription(joinPoint));
//			log.setFip(ip);
//			log.setFremark((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
			
			// 保存数据库
//			logService.save(log);
//			getNumberService.addSerialNumber("XT-RZGL");
			
//			log=new YSystemconfigurationLog();
//			log.setFid(UUID.randomUUID().toString());
//			log.setFcontent(getControllerMethodDescription(joinPoint));
//			log.setFip(ip);
			
			// 保存数据库
//			logService.save(log);
			System.out.println("=====异常通知结束=====");
		} catch (Exception ex) {
			// 记录本地异常日志
			logger.error("==异常通知异常==");
			logger.error(e.getMessage());
		}

	}

	/**
	 * 获取注解中对方法的描述信息 用于service层注解
	 * 
	 * @param joinPoint
	 *            切点
	 * @return 方法描述
	 * @throws Exception
	 */
	public static String getServiceMthodDescription(JoinPoint joinPoint)
			throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String description = "";
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					description = method.getAnnotation(SystemServiceLog.class)
							.description();
					break;
				}
			}
		}
		return description;
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 * 
	 * @param joinPoint
	 *            切点
	 * @return 方法描述
	 * @throws Exception
	 */
	public static String getControllerMethodDescription(JoinPoint joinPoint)
			throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String description = "";
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					description = method.getAnnotation(
							SystemControllerLog.class).description();
					break;
				}
			}
		}
		return description;
	}

}
