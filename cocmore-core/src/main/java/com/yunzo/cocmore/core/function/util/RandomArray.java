package com.yunzo.cocmore.core.function.util;


/**
 * Description: <会员6位密码随机生成器>. <br>
 * 
 * @date:2014年12月15日 上午11:09:15
 * @author beck
 * @version V1.0
 */
public class RandomArray {
	
	/**
	 * 转换成字符串
	 * @param RandomNumber
	 * @return
	 */
	public static String getRandom(int RandomNumber){
		StringBuffer sb = new StringBuffer();
		int[] array = getRandomArray(RandomNumber);
		for(Integer temp : array){
			sb.append(temp);
		}
		return sb.toString();
	}
	

	/**
	 * 将新获得的随机数与已产生的其它随机数相比较，若有重复数据，则丢弃，并重来一遍； 否则，将新数存入数组。
	 * 
	 * @param i
	 * @return 随机数组
	 */
	public static int[] getRandomArray(int i) {
		int[] a = new int[i]; 			//随机数数组
		for (int m = 0; m < i; m++) { 	//m为已产生的随机数个数
			int temp = random();
			if (m == 0)
				a[m] = temp;
			else {
				for (int n = 0; n < m; n++) { 	//遍历已产生的随机数
					if (temp == a[n]) {
						temp = random();
						n = -1;			//有重复则重新遍历
					}
				}
				a[m] = temp;
			}
		}
		return a;
	}

	/**
	 * @return 0至9之间的随机整数
	 */
	private static int random() {
		return (int) (10 * Math.random());
	}

}
