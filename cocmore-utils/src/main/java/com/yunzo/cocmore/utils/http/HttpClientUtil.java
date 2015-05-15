package com.yunzo.cocmore.utils.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

/**
 * Description: <类功能描述>. <br>
 * <p>
 * <使用说明>
 * </p>
 * Makedate:2014年9月1日 下午4:59:22
 * 
 * @author Administrator
 * @version V1.0
 */
public class HttpClientUtil {

	private static final Logger logger = Logger.getLogger(HttpClientUtil.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String requestBody = "{\"name\":\"王五\",\"age\":60,\"createDate\":1408377600000,\"modifyDate\":1409068800000}";
		try {
			String result = HttpClientUtil.httpPost(
					"http://localhost:8080/webmvc/demorest/save", requestBody,
					"application/json", "UTF-8");
			System.out.println("result======" + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String httpPost(String url, String data, String contentType,
			String encode) throws Exception {
		long startTime = System.currentTimeMillis();
		InputStream instream = null;
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer("");
		HttpClient httpClient = new HttpClient();
		PostMethod httpPost = new PostMethod(url);
		try {

			httpPost.setRequestHeader("Content-Type", contentType);

			RequestEntity requestEntity = new StringRequestEntity(data,
					contentType, encode);
			httpPost.setRequestEntity(requestEntity);
			httpClient.executeMethod(httpPost);

			if (httpPost.getStatusCode() == HttpStatus.SC_OK) {

				Header headers[] = httpPost.getResponseHeaders();
				for (Header header : headers) {
					logger.info(header.getName() + "||" + header.getValue());
				}
				instream = httpPost.getResponseBodyAsStream();
				if (instream == null) {
					logger.error("Result is NULL. URL:" + url);
				}

				in = new BufferedReader(
						new InputStreamReader(instream, "UTF-8"));

				String line = null;
				while ((line = in.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}

			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("HttpClientUtil-Exception: ", e);
			}
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}

			httpPost.releaseConnection();
		}
		logger.info("the result from HttpClientUtil:result="
				+ (null != sb.toString() ? sb.toString().trim() : null));
		long endTime = System.currentTimeMillis();
		logger.info("结束调用：HttpClientUtil的httpPostWithJSON方法" + " ["
				+ Long.toString(endTime - startTime) + "]");

		return sb.toString();
	}

}
