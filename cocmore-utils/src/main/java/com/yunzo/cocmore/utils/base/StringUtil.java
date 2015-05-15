package com.yunzo.cocmore.utils.base;

/**
 * String工具类
 * 
 * @author xiaobo
 * 
 */
public class StringUtil {

	/**
	 * 格式化字符串
	 * 
	 * 例：formateString("xxx{0}bbb",1) = xxx1bbb
	 * 
	 * @param str
	 * @param params
	 * @return
	 */
	public static String formateString(String str, String... params) {
		for (int i = 0; i < params.length; i++) {
			str = str.replace("{" + i + "}", params[i] == null ? "" : params[i]);
		}
		return str;
	}
	public static boolean isNullOrEmpty(String str)
	{
		if (str==null || str.length()==0)
		return true;
		else return false;
	}
	
	/*
	 * *
	 * 判断字符串是否是浮点数 
	 */ 
	 public static boolean isDouble(String value) {  
		 try {   
			 Double.parseDouble(value); 
			 if (value.contains("."))   
				 return true;  
			 return false;  
			 } catch (NumberFormatException e){   
			 return false;  
			 } 
	 }
	 public static String addZeroForNum(String str, int strLength) {
			int strLen = str.length();
			if (strLen < strLength) {
			while (strLen < strLength) {
			StringBuffer sb = new StringBuffer();
			sb.append("0").append(str);
			// sb.append(str).append("0");
			str = sb.toString();
			strLen = str.length();
			}
			}
			return str;
		}

}
