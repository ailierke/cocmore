package com.yunzo.cocmore.utils.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 
 * 此类获取电话号码归属地
 * 
 * */
public class GetAdressByTel {
	private static final String API = "http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi";
	private static final HttpClient httpClient =new DefaultHttpClient();

	public static void main(String[] args) {

        List<String> str = getAdress("18382911503");
		System.out.println(str.get(0)+"    "+str.get(1));
	}

	public static List<String> getAdress(String tel){
		Map<String ,String > parammap = new HashMap<String,String>();
		parammap.put("chgmobile", tel);
		String charset = "gbk";
		String xmlStr = doGet(API, parammap, charset,charset);
		List<String> returnSrt = new ArrayList<String>(); 
		Document doc  = null;
		try {
			doc = DocumentHelper.parseText(xmlStr);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		  Element rootElt = doc.getRootElement(); // 获取根节点
		  
		  returnSrt.add(rootElt.element("city").getStringValue());
		  returnSrt.add(rootElt.element("province").getStringValue());
		return returnSrt;
	}
	public static String doGet(String url,Map<String,String> params,String paramcharset,String getCotentcharset){
		try {
			if(params != null && !params.isEmpty()){
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for(Map.Entry<String,String> entry : params.entrySet()){
					String value = entry.getValue();
					if(value != null){
						pairs.add(new BasicNameValuePair(entry.getKey(),value));
					}
				}
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, paramcharset));
			}
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Host", "life.tenpay.com");
			httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0");
			httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			httpGet.addHeader("Accept-Encoding", "gzip, deflate");
			httpGet.addHeader("Connection", "keep-alive");
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null){
				result = EntityUtils.toString(entity, getCotentcharset);
			}
			EntityUtils.consume(entity);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
