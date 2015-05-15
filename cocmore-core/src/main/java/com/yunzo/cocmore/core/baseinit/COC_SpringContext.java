package com.yunzo.cocmore.core.baseinit;

import org.springframework.context.ApplicationContext;

/** 
 *Description: <COC框架 全局资源>. <br>
 *<p>
	<全局资源>
 </p>
 *	Makedate:2014年11月18日 上午4:26:59 
 * @author xiaobo 
 * @version V1.0                             
 */
public class COC_SpringContext {

	private static ApplicationContext applicationContext;

	private static COC_SpringContext springContext;

	private COC_SpringContext() {
	}

	public static COC_SpringContext getInstance() {
		if (springContext == null) {
			springContext = new COC_SpringContext();
		}
		return springContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}

	/**                                                          
	* 描述 : <获得applicationContext中的对象>. <br> 
	*<p> 
		<使用方法说明>  
	 </p>                                                                                                                                                                                                                                                
	* @param beanName
	* @return                                                                                                      
	*/  
	public static Object getBean(String beanName) {
		return COC_SpringContext.getInstance().getApplicationContext().getBean(beanName);
	}
}
