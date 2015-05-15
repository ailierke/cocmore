package com.yunzo.cocmore.utils.base;

/**
 * 
 *	云筑访问验证工具类
 * @author david
 *
 */
public class YunzoCocSignCode {
	//加密前段
	private static final String BEGINSTRING="2014";
	//加密结束
	private static final String ENDSTRING="YunzoCOC";
	/**
	 * 验证请求是否内空APP请求的方法
	 * @param infoMap 请求参数
	 * @param cocSign 验证码
	 * @return 真/假
	 */
	@SuppressWarnings("unused")
	public static Boolean checkAppGetCocSign(String infoMap,String cocSign)
	{
		@SuppressWarnings("restriction")
		sun.misc.BASE64Decoder decoder =(new sun.misc.BASE64Decoder()); 
		Boolean flag=false;
		try {
			@SuppressWarnings("restriction")
			byte[] b = decoder.decodeBuffer(cocSign);
			cocSign = new String(b); 
			flag=MD5Util.md5(BEGINSTRING+infoMap+ENDSTRING).equals(MD5Util.JM(cocSign));
		} catch (Exception e) {
			e.printStackTrace();	
			return flag;
		} 
		return flag;
	}

}
