package com.yunzo.cocmore.utils.base;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author beck
 * @date 2014年10月23日
 * @description 根据地址获取百度地图经纬度工具类
 */
public class LatitudeUtils {
	
	public static final String KEY = "7d9fbeb43e975cd1e9477a7e5d5e192a";
	
	/**
	 * 返回输入地址的经纬度坐标
	 * lng(经度),lat(纬度)
	 */
	public static String getGeocoderLatitude(String address){
		BufferedReader in = null;
		try {
			//将地址转换成utf-8的16进制
			address = URLEncoder.encode(address, "UTF-8");

			//设置百度地图API访问地址 
			URL tirc = new URL("http://api.map.baidu.com/geocoder?address="+ address +"&output=json&key="+ KEY);
			
			in = new BufferedReader(new InputStreamReader(tirc.openStream(),"UTF-8"));
			String res;
			StringBuilder sb = new StringBuilder("");
			while((res = in.readLine())!=null){
				sb.append(res.trim());
			}
			//System.out.println(sb.toString());
			String str = sb.toString();
			
			if(str != null ){
				int lngStart = str.indexOf("lng\":");
				int lngEnd = str.indexOf(",\"lat");
				int latEnd = str.indexOf("},\"precise");
				if(lngStart > 0 && lngEnd > 0 && latEnd > 0){
					String lng = str.substring(lngStart+5, lngEnd);
					String lat = str.substring(lngEnd+7, latEnd);
					return lng + "," + lat;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static void main(String args[]){
		String result = LatitudeUtils.getGeocoderLatitude("成都市天府新谷");
		System.out.println(result);
	}
	
}