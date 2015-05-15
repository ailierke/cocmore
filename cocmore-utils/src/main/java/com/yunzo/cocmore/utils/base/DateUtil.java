package com.yunzo.cocmore.utils.base;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期工具类
 * 
 * @author xiaobo
 * 
 */
public class DateUtil {
	
	private static Calendar date;

	public static void main(String[] args) {
		
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
		return "";
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return dateToString(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 判断两个时间段(24小时制)
	 * date1 是否在date2之前
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isTimeBefore(String date1,String date2){
		  try{
			  DateFormat df = new SimpleDateFormat("HH:mm:ss");
			  return df.parse(date1).before(df.parse(date2)); 
		  }catch(ParseException e){
			  System.out.print(e.getMessage());
			  return false;
		  }
	}
	
	/**
	 * 判断两个日期
	 * date1 是否在date2之前
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isDateBefore(String date1,String date2){
		  try{
			  DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			  return df.parse(date1).before(df.parse(date2)); 
		  }catch(ParseException e){
			  System.out.print(e.getMessage());
			  return false;
		  }
	}
	
	/**
	 * 字符串转日期对象
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
	public static Date StringToDate(String dateStr,String formatStr){
		DateFormat dd=new SimpleDateFormat(formatStr);
		Date date=null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取当前日期
	 * @return
	 */
	public static String getDate(){
		date=Calendar.getInstance();
		return date.get(Calendar.YEAR)+"-"+(date.get(Calendar.MONTH)+1) + "-" +date.get(Calendar.DAY_OF_MONTH)+ "_";
	}

}
