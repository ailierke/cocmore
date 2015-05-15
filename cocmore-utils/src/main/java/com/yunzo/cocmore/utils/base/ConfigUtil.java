package com.yunzo.cocmore.utils.base;

import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * 项目参数工具类
 * 
 * @author xiaobo
 * 
 */

public class ConfigUtil {
	//日历对象
	private static Calendar date;
	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("mail");

	/**
	 * 获得sessionInfo名字
	 * 
	 * @return
	 */
	public static final String getSessionInfoName() {
		return bundle.getString("sessionInfoName");
	}
	
	/**
	 * 通过键获取值
	 * 
	 * @param key
	 * @return
	 */
	public static final String get(String key) {
		return bundle.getString(key);
	}
	
	/**
	 * 生成Fnumber
	 * @param 特定前缀字符串  
	 * @return
	 */
	public static final String getNumberBeforeName(String moudule)
	{
		date=Calendar.getInstance();
	
		return bundle.getString(moudule)+ date.get(Calendar.YEAR)+"_"+(date.get(Calendar.MONTH)+1) + "_" +date.get(Calendar.DAY_OF_MONTH)+ "_";
	}

}
