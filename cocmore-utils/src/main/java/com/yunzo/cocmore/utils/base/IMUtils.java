package com.yunzo.cocmore.utils.base;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 环信接口(原来的接口)
 * 
 * @author star
 *
 */
public class IMUtils {
	// 环信AppKey
	private static String HOST = "a1.easemob.com";
	private static String GRANT_TYPE = "client_credentials";
//	private static String APPKEY = "yunzo#test";
//	private static String CLIENT_ID = "YXA6EvR38HUSEeSwi8H9H3KMwg";
//	private static String CLIENT_SECRET = "YXA6kWwqdD3-Jo8pemX_TkTOUXmBtT4";
//	private static String APPKEY = "yunzo#yunzotest";
//	private static String CLIENT_ID = "YXA6efFaIEBxEeSxMS-Oyo-LHQ";
//	private static String CLIENT_SECRET = "YXA6uLB5gCMWZUzXyoEBz0zIKuRfs9E";
	//以下是生产环境
	private static String APPKEY ="yunzo#cocproduce";
	private static String CLIENT_ID = "YXA6Ux5cIEC-EeSU99V0m81n1w";
	private static String CLIENT_SECRET = "YXA64wd-rx-ZVDj7oXr33YRZcv98UOc";


	// 提交方式
	private static String Method_GET = "GET";
	private static String Method_POST = "POST";
	private static String Method_PUT = "PUT";
	private static String Method_DELETE = "DELETE";

	// 管理员token
	private static String orgAdminToken = "";
	static {
		// 设置获取令牌的属性并获取token
		Map<String, Object> getAccessTokenPostBody = new HashMap<String, Object>();
		getAccessTokenPostBody.put("grant_type", GRANT_TYPE);
		getAccessTokenPostBody.put("client_id", CLIENT_ID);
		getAccessTokenPostBody.put("client_secret", CLIENT_SECRET);
		orgAdminToken = getAccessToken(HOST, APPKEY, false,
				getAccessTokenPostBody);
	}

	// 发送消息类型
	public static final String TARGETTYPE_USERS = "users";
	public static final String TARGETTYPE_CHATGROUPS = "chatgroups";

	/**
	 * send SSL request
	 * 
	 * @param reqURL
	 * @param token
	 *            口令
	 * @param body
	 *            提交JSON体
	 * @param method
	 *            提交方式
	 * @return 环信返回值
	 */
	private static Map<String,Object> sendSSLRequest(String reqURL, String token,
			String body, String method) {
		Map<String,Object> map = new HashMap<String,Object>();
		String responseContent = null;
		int statusCode = 200;
		HttpClient httpClient = new DefaultHttpClient();

		// X509 证书验证
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { xtm }, null);
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
			httpClient.getConnectionManager().getSchemeRegistry()
					.register(new Scheme("https", 443, socketFactory));
			HttpResponse response = null;

			if (method.equals(Method_POST)) {
				HttpPost httpPost = new HttpPost(reqURL);
				httpPost.setEntity(new StringEntity(body, "UTF-8"));
				if (token != null) {
					httpPost.setHeader("Authorization", "Bearer " + token);
				}
				response = httpClient.execute(httpPost);
			} else if (method.equals(Method_PUT)) {
				HttpPut httpPut = new HttpPut(reqURL);
				httpPut.setEntity(new StringEntity(body, "UTF-8"));
				if (token != null) {
					httpPut.setHeader("Authorization", "Bearer " + token);
				}
				response = httpClient.execute(httpPut);
			} else if (method.equals(Method_GET)) {
				HttpGet httpGet = new HttpGet(reqURL);
				if (token != null) {
					httpGet.setHeader("Authorization", "Bearer " + token);
				}
				response = httpClient.execute(httpGet);
			} else if (method.equals(Method_DELETE)) {
				HttpDelete httpDelete = new HttpDelete(reqURL);
				if (token != null) {
					httpDelete.setHeader("Authorization", "Bearer " + token);
				}
				response = httpClient.execute(httpDelete);
			}

			HttpEntity entity = response.getEntity();
			statusCode = response.getStatusLine().getStatusCode();

			if (null != entity) {
				responseContent = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		map.put("statusCode", statusCode);
		map.put("responseContent", responseContent);
		return map;
	}

	/**
	 * get access token
	 * 
	 * @param host
	 *            IP或者域名
	 * @param appKey
	 *            easemob-demo#chatdemo
	 * @param isAdmin
	 *            org管理员token true, app管理员和IM用户 token false
	 * @param postBody
	 *            POST请求体
	 * @return
	 */
	private static String getAccessToken(String host, String appKey,
			Boolean isAdmin, Map<String, Object> postBody) {
		String result = null;
		String orgName = appKey.substring(0, appKey.lastIndexOf("#"));
		String appName = appKey.substring(appKey.lastIndexOf("#") + 1);
		String accessToken = "";
		String rest = "management/token";
		if (!isAdmin) {// org管理员token true, app管理员和IM用户 token false
			rest = orgName + "/" + appName + "/token";
		}
		String reqURL = "https://" + host + "/" + rest;
		Map<String,Object> map = sendSSLRequest(reqURL, null,
				JSONObject.toJSONString(postBody), Method_POST);
		if((Integer)map.get("statusCode")==200){
			result = (String)map.get("responseContent");
		}
		Map<String, Object> resultMap = JSONObject.parseObject(result);
		accessToken = (String) resultMap.get("access_token");
		return accessToken;
	}

	/**
	 * 创建用户(批量/单个)
	 * 
	 * @param userMap包含环信用户name 环信password  环信nickName
	 *            要创建的用户列表:
	 * @return responseContent
	 */
	public static String createUser(Map<String,String> userMap) {
		String createResponse = "";
		String reqURL = "https://" + HOST + "/" + APPKEY.replaceFirst("#", "/")
				+ "/users";
		boolean flag = true;
		try {
				Map<String,Object> map = sendSSLRequest(reqURL, orgAdminToken,JSONObject.toJSONString(userMap), Method_POST);	
				if((Integer)map.get("statusCode")==200){
					flag = false;
					createResponse = (String)map.get("responseContent");
				}else{
					
				}
		
//			Map<String,Object> map = sendSSLRequest(reqURL, orgAdminToken,JSONObject.toJSONString(userMap), Method_POST);	
//					if((Integer)map.get("statusCode")==200){
//						createResponse = (String)map.get("responseContent");
//					}
//
//			if((Integer)map.get("statusCode")!=200){//如果返回码不等于200就说明注册用户失败，记录下来
//				logger.info("注册失败用户："+userMap.get("username"));
//			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return createResponse;
	}

	/**
	 * 查询所有用户
	 * 
	 * @return
	 */
	public static String findAllUser(String cursor) {
		String reqURL = "https://" + HOST + "/" + APPKEY.replaceFirst("#", "/")
				+ "/users/?limit=100&cursor="+cursor;
		String result=null;
		Map<String,Object> map= sendSSLRequest(reqURL, orgAdminToken, null,
				Method_GET);
		if((Integer)map.get("statusCode")==200){
			result = (String)map.get("responseContent");
		}
		System.out.println(result);
		return result;
	}
	/**
	 * 查询单个帐号
	 * 
	 * @param username
	 *           
	 * @return
	 */
	public static String deleteUser(String username) {
		String reqURL = "https://" + HOST + "/" + APPKEY.replaceFirst("#", "/")
				+ "/users/" + username;
		String result =null;
		Map<String,Object> map= sendSSLRequest(reqURL, orgAdminToken, null,
				Method_DELETE);
		if((Integer)map.get("statusCode")==200){
			result = (String)map.get("responseContent");
		}
		return result;
	}
	/**
	 * 排序删除多少个
	 * 
	 * @param limit
	 *            删除个数
	 * @return
	 */
	public static String deletesUsers(int limit) {
		String orderBy = "";// 按时间排序
		String reqURL = "https://" + HOST + "/" + APPKEY.replaceFirst("#", "/")
				+ "/users?" + orderBy + "limit=" + limit;
		String result = null;
		Map<String,Object> map = sendSSLRequest(reqURL, orgAdminToken, null,
				Method_DELETE);
		if((Integer)map.get("statusCode")==200){
			result = (String)map.get("responseContent");
		}
		return result;
	}

	/**
	 * 发送推送消息
	 * 
	 * @param targetType
	 *            发送消息对象类型 IMUtils.TARGETTYPE_CHATGROUPS 群组
	 *            IMUtils.TARGETTYPE_USERS 用户组
	 * @param target
	 *            注意这里需要用数组,数组长度建议不大于20, 即使只有一个用户, 也要用数组 ['u1']
	 * @param msgTitle
	 *            消息标题
	 * @param msgContent
	 *            消息内容
	 * @param from
	 *            表示这个消息是谁发出来的, 可以没有这个属性, 那么就会显示是admin, 如果有的话,则会显示是这个用户发出的
	 * @param ext
	 *            扩展属性,由app自己定义.可以没有这个字段， 但是如果有，值不能是“ext:null“这种形式，否则出错
	 * @return
	 */
	public static String msgPush(String targetType, List<String> target,
			String msgTitle, String msgContent, String from,
			Map<Object, Object> ext) {
		String reqURL = "https://" + HOST + "/" + APPKEY.replaceFirst("#", "/")
				+ "/messages";
		Map<String, String> msgMap = new HashMap<String, String>();
		String result =null;
		msgMap.put("type", msgTitle);
		msgMap.put("msg", msgContent);

		Map<String, Object> bodyMap = new HashMap<String, Object>();
		bodyMap.put("target_type", targetType);
		bodyMap.put("target", JSONObject.toJSON(target));
		bodyMap.put("msg", JSONObject.toJSON(msgMap));
		bodyMap.put("from", from);
		bodyMap.put("ext", JSONObject.toJSON(ext));
		Map<String,Object> map = sendSSLRequest(reqURL, orgAdminToken,
				JSONObject.toJSONString(bodyMap), Method_POST);
		if((Integer)map.get("statusCode")==200){
			result = (String)map.get("responseContent");
		}
		return result;
	}
	
	public static void main(String[] args) {
		Map<String,String> userMap = new HashMap<String,String>();
		userMap.put("username", "1515462733");
		userMap.put("password", "21218cca77804d2ba1922c33e0151105");
		createUser(userMap);
	}
}
