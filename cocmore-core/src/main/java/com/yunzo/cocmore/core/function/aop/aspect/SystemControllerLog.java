package com.yunzo.cocmore.core.function.aop.aspect;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
/** 
 *自定义注解 拦截Controller 
 */  
@Target({ElementType.PARAMETER, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface SystemControllerLog {
	String description()  default "";  
}
