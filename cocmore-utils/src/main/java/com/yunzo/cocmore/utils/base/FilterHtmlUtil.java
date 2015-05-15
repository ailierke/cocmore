package com.yunzo.cocmore.utils.base;

public class FilterHtmlUtil {
	/**
	 * 过滤html标签
	 * @param s
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String filterHtml(String s){
		String str = null; 
		if(!s.equals("")||s!=null){
			str=s.replaceAll("<[.[^<]]*>","");
			// 去除字符串中的空格 回车 换行符 制表符 等
			str = str.replaceAll("\\s*|\t|\r|\n", "");
			// 去除空格
			str = str.replaceAll("&nbsp;", "");
			return str;
		}else{
			return str;
		}
	}
	
	public static void main(String[] args){
		String s ="<p>深圳市四川商会是商会专属的社交平台，集微博和微信功能于一体，随时随地不论用任何设备，商会成员之间都能任意沟通。商会动态消息和成员活动分享经验可以及时获取，<style>既方便又及时</style>，让您在任何时刻也不会错过重要的消息。</p><div><br /></div><p><br /></p>";
		String returnStr = filterHtml(s);
		System.out.println(returnStr);
	}
}
