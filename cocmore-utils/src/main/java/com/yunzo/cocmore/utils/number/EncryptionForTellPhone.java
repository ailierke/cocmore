package com.yunzo.cocmore.utils.number;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class EncryptionForTellPhone {
	//加密头
		private static final String APPBEGIN = "Y";
		//加密尾
		private static final String APPEND = "C";

		/**
		 * 加密
		 * @param encinfo 要加密的信息
		 * @return String 加密后字符串
		 */
		public static String encryptToABC(String encinfo) {
			char[] cnum = changeNumberToAscii(encinfo).toCharArray();//存放电话号码
			// 转码十六进制
			encinfo = stringToHex(APPBEGIN+arrayToBackward(cnum)+APPEND);
			return encinfo;
		}
		/**
		 * 解密
		 * @param decinfo 密文
		 * @return  返回明文
		 */
		public static String decryptByABC(String decinfo) {
			//将密文转成字母
			decinfo = hexToString(decinfo);
			//截取中间电话号码字符串
			decinfo = decinfo.substring(APPBEGIN.length(), decinfo.length() - APPEND.length());
			//转换成字符数组
			char[] tr = decinfo.toCharArray();
			decinfo = changeAsciiToNumber(arrayToBackward(tr));
			return decinfo;
		}
		
		/**
		 * 倒序数组
		 * @param tr 数组
		 * @return
		 */
		private static String arrayToBackward(char[] tr)
		{
			String value="";
			if(tr.length>0)
			{
				char t;
				//侄序
				for (int i = 0; i < tr.length / 2; i++) {
					t = tr[i];
					tr[i] = tr[tr.length - 1 - i];
					tr[tr.length - 1 - i] = t;
				}
				value=new String(tr);
			}
			return value;
			
		}

		/**
		 * 转化字符串为十六进制编码
		 * @param 传入要转化的字符串
		 * @return 返回转化后的字符串
		 */
		public static String stringToHex(String sinfo) {
			String hex = "";
			for (int i = 0; i < sinfo.length(); i++) {
				int ch = (int) sinfo.charAt(i);
				String chenge = Integer.toHexString(ch);
				hex += chenge;
			}
			return hex;
		}

		/**
		 * 转化十六进制编码为字符串
		 * @param 传入要转化的字符串
		 * @return 返回转化后的字符串
		 */
		public static String hexToString(String sinfo) {
			byte[] baKeyword = new byte[sinfo.length() / 2];
			try {
				for (int i = 0; i < baKeyword.length; i++) {
					baKeyword[i] = (byte) (0xff & Integer.parseInt(sinfo.substring(i * 2, i * 2 + 2), 16));
				}
				sinfo = new String(baKeyword, "utf-8");// UTF-16le:Not
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return sinfo;
		}

		/**
		 * 利用ascii转码 加密
		 * 
		 * @param str
		 * @return
		 */
		public static String changeNumberToAscii(String str) {
			String changeStr = "";
			for (int i = 0; i < str.length(); i++) {
				int ci = str.charAt(i); // 获取字符串每个字符ascii
				int change = ci + 17;
				char changeChar = (char) change; // 转换
				changeStr += changeChar;
			}
			return changeStr;
		}
		/**
		 * 利用ascii转码 解密
		 * 
		 * @param str
		 * @return
		 */
		public static String changeAsciiToNumber(String str) {
			String changeStr = "";
			for (int i = 0; i < str.length(); i++) {
				int ci = str.charAt(i); // 获取字符串每个字符ascii
				int change = ci - 17;
				char changeChar = (char) change; // 转换
				changeStr += changeChar;
			}
			return changeStr;
		}
		/**
		 * 替换方法
		 * @param str 要替换内容
		 * @return 替换后的内容
		 */
		public static String changeValue(String str) {
			char start = 'A';
			char end = 'J';
			for (int i = 0; i < (int) (end - start + 1); i++) {
				str = str.replaceAll(String.valueOf((char) (start + (char) i)),
						String.valueOf(i));
			}
			return str;
		}
		
		public static void main(String[] args) {
			// TODO Auto-generated method stub
//			long startTime=System.currentTimeMillis();   //获取开始时间   
//			doSomeThing();  //测试的代码段   
//			long endTime=System.currentTimeMillis(); //获取结束时间   
//			System.out.println("程序运行时间： "+(endTime-startTime)+"ms");  
			
			System.out.println(decryptByABC("59444a4942414a42494a444243"));
			System.out.println(encryptToABC("18780098721"));
		}
		
		public static void doSomeThing()
		{
			for(int i=0;i<10;i++)
			{
				if(i>=0&&i<=9)
				{
					String ff = encryptToABC("1354014843"+i);
					System.out.println("加密过后的数据：("+i+")" + ff);
					System.out.println("解密过后的数据：("+i+")"+ decryptByABC(ff));
				}else if(i>=10&&i<=99)
				{
					String ff = encryptToABC("135401484"+i);
					System.out.println("加密过后的数据：("+i+")" + ff);
					System.out.println("解密过后的数据：("+i+")"+ decryptByABC(ff));
				}else
				{
					String ff = encryptToABC("13540148"+i);
					System.out.println("加密过后的数据：("+i+")" + ff);
					System.out.println("解密过后的数据：("+i+")"+ decryptByABC(ff));
				}
			}
		}
}
