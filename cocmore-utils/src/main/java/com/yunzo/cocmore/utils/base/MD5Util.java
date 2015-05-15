package com.yunzo.cocmore.utils.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * MD5加密工具类
 * 
 * @author xiaobo
 * 
 */
public class MD5Util {

	public static void main(String[] args) {
		String s = new String("中国");
		String s5="EkUQEUBGF0BDEUxHEEEVFkwRRUMXEkISQBZMQ0xHQEQ=";
		//String s3="%7B%22deviceOsversion%22%3A%228.1%22%2C%22deviceId%22%3A%22f3e849eebcf80ccdd05435bc0952d36666fc0f82%22%2C%22mid%22%3A%2290545e37-70e4-4a92-9b93-04e951f648a3%22%2C%22fTitle%22%3A%22%E4%BA%86%E7%AE%80%E4%BB%8B%22%2C%22isWarrant%22%3A0%2C%22deviceType%22%3A%22iPhone%20Simulator%22%2C%22md5Info%22%3A%2298a67526968129b1803b3ea96b4baa4c%22%2C%22appVersion%22%3A%221%22%2C%22expDateStart%22%3A%222012-12-12%22%2C%22businessId%22%3A%227efde656-4c9d-4cb2-afeb-dfd7431d637f%22%2C%22expDateStop%22%3A%222012-12-12%22%2C%22type%22%3A0%2C%22countryId%22%3A%220f9b50ab-4d11-431b-89bf-b3943916c504%22%2C%22provincialId%22%3A%2211%22%2C%22guaranteeIds%22%3A[]%2C%22cityId%22%3A%2282%22%2C%22contactsPerson%22%3A%22%E4%BA%86%E7%AE%80%E4%BB%8B%22%2C%22tradeID%22%3A%220d90bd77-06a1-463d-9bef-10e6c9750a86%22%2C%22contactsPhone%22%3A%22%E4%BA%86%E7%AE%80%E4%BB%8B%22%2C%22appChannelNo%22%3A%221%22%2C%22deviceOs%22%3A%22iOS%22%2C%22userName%22%3A%2259474442484941484347494243%22%7D";
		//String s3 = "{\"deviceId\":\"acbdf66985d4a92f6aa523c3b9640a6461a7c61d\",\"businessId\":\"7efde656-4c9d-4cb2-afeb-dfd7431d637f\",\"userName\":\"59474442484941484347494243\",\"mid\":\"14f5228d-2e28-4eef-a333-16f4f8112641\",\"appVersion\":\"1\",\"appChannelNo\":\"1\",\"deviceType\":\"iPhone Simulator\",\"deviceOs\":\"iOS\",\"deviceOsversion\":\"8.1\",\"md5Info\":\"3a62d074f34a5499d6235f6024a5df38\"}";
		//String s3 = "{\"mid\": \"\",\"md5Info\": \"\",\"deviceId\": \"13\",\"deviceOs\": \"Android\",\"deviceOsversion\": \"9.0\",\"deviceType\": \"iPhone6\",\"appVersion\": \"8.0\",\"appChannelNo\": \"4\",\"userName\": \"59434345484949434345444243\",\"client\": \"DGGS\",\"businessId\": \"7efde656-4c9d-4cb2-afeb-dfd7431d637f\",\"sessionToken\": \"ABCD\",\"sign\": \"909\",\"localContactCacheVersion\":0}";
		String s3 = "{\"mid\": \"\",\"md5Info\": \"\",\"deviceId\": \"13\",\"deviceOs\": \"Android\",\"deviceOsversion\": \"9.0\",\"deviceType\": \"iPhone6\",\"appVersion\": \"8.0\",\"appChannelNo\": \"4\",\"userName\": \"59434345484949434345444243\",\"client\": \"DGGS\",\"businessId\": \"7efde656-4c9d-4cb2-afeb-dfd7431d637f\",\"sessionToken\": \"ABCD\",\"sign\": \"909\",\"cacheVersion\":0,\"pageSize\":1000}";
		try {
			//URL转码
			s3 = URLDecoder.decode(s3, "UTF-8");
			s3="2014"+s3+"YunzoCOC";
			System.out.println("URL转码后的字符串：" + s3);	
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("原始：" + s);
		//System.out.println("MD5后：" + md5(s));
		String s1=md5(s3);
		System.out.println("MD5后：" +s1);
		s1= KL(s1);
		System.out.println("KL后：" +s1);
		System.out.println("MD5后再加密再BASE64加密：" +getBase64(s1));
		System.out.println("判断两个字符串是否相等：" +getBase64(s1).equals(s5));
		//System.out.println("=========解密："+JM(StringChangeCharset.decodeUnicode(s3)));
		//System.out.println("解密为MD5后的：" + JM(KL(md5(s))));
		//System.out.println("两次解密为MD5后的：" + convertMD5(JM(KL(md5(s)))));
		
	}
	// BASE64加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}
	// BASE64解密  
    public static String getFromBase64(String s) {  
        byte[] b = null;  
        String result = null;  
        if (s != null) {  
            BASE64Decoder decoder = new BASE64Decoder();  
            try {  
                b = decoder.decodeBuffer(s);  
                result = new String(b, "utf-8");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    } 
	/**
	 * md5加密
	 * 
	 * @param inStr
	 * @return
	 */
	public static String md5(String inStr) {
		MessageDigest md5 = null;
		byte[] byteArray=null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byteArray = inStr.getBytes("UTF-8");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	// 可逆的加密算法
	public static String KL(String inStr) {
		// String s = new String(inStr);
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');//每个字符的ASC码与字符t的ASC码进行二进制异或运算
		}
		byte[] k=null;
		try {
			k = new String(a).getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new String(k);
	}

	// 加密后解密
	public static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		byte[] k=null;
		try {
			k = new String(a).getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new String(k);
	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convertMD5(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}

}
