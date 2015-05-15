package com.yunzo.cocmore.core.function.service;

import java.util.Map;


/**
 * Description: <验证码服务接口>. <br>
 * @date:2014年12月17日 上午3:52:54
 * @author beck
 * @version V1.0
 */
public interface VerificationServiceI {
	/**
	 * 短信找回密码，获取验证码 
	 * @param demo
	 */
	public void getSMSAuthcode(String userName);
	
	/**
	 * 短信验证码找回登陆密码 
	 * @param demo
	 */
	public Map<String, Object> findPwdBySMS(String userName,String password,String phoneCheckCode);
	
	
	/**
	 * 获取手机注册短信验证码  
	 * @param demo
	 */
	public Map<String, Object> getPhoneRegSMSCode(String mid,String userName);
}
