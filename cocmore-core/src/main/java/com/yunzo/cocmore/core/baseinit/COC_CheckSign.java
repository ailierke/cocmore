package com.yunzo.cocmore.core.baseinit;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.util.ResponseCode;
import com.yunzo.cocmore.utils.base.YunzoCocSignCode;

/**
 * Description: <cocsign验证>. <br>
 * <p>
 * <继承于SPRING框架过滤器>
 * </p>
 * Makedate:2014年11月18日 上午4:26:59 验证app端是否登录，验证签名是否正确，验证参数是否正确
 * 
 * @author ailierke
 * @version V1.0
 */
public class COC_CheckSign extends HandlerInterceptorAdapter {
	Logger logger = Logger.getLogger(COC_CheckSign.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	@Resource
	MemberServiceI memberService;

	@Override
	/**
	 * 调用前置处理检查签名验证，将infoMap编码转换
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		request.setCharacterEncoding("utf-8");
		String cocSign = request.getParameter("cocSign");
		String infoMap = request.getParameter("infoMap");
		Map<String, Object> map = new HashMap<String, Object>();
		String results="",tel=null,md5info=null;
		Boolean flag=false;
		if (cocSign != null || infoMap != null) {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			tel = node.get("userName") == null ? null : node.get("userName").textValue();// 电话号码
			md5info = node.get("md5Info") == null ? null : node.get("md5Info").textValue();// 登录时返回的临时密码
			if (tel != null) {
				if (memberService.checkAppUserLogin(tel, md5info)) {// 判断用户是否登录
					String tvalue = checkObjectIsNull(node);
					if(tvalue.equals(""))
					{
						if (YunzoCocSignCode.checkAppGetCocSign(infoMap, cocSign)) {// 判断签名是否正确
							flag=true;
						} else {
							flag=false;
							results+=ResponseCode.MSERROR.msg();
						}
					}else
					{
						flag=false;
						results+=tvalue;
					}
				} else {
					map.put(ResponseCode.MSGC.msg(), ResponseCode.VALIDATEFAILER.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.VALIDATEFAILER.msg());
					COC_APPResponseResult.responseToGJson(map, response);
					return false;
				}

			} else {
				// 转码参数
				if (YunzoCocSignCode.checkAppGetCocSign(infoMap, cocSign)) {// 判断签名是否正确
					flag=true;
				} else {
					flag=false;
					results+=ResponseCode.MSERRORH.msg();
				}
			}
		}else
		{
			flag=false;
			results+=ResponseCode.SIGNWRONGPARAM.msg();
		}
		
		if(flag)
		{
			return true;
		}else
		{
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			map.put(ResponseCode.MSGR.msg(), results);
			COC_APPResponseResult.responseToGJson(map, response);
			return false;
		}
//		return true;
	}

	/**
	 * 拦截器主要业务
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("cocSgin验证通过.........");
	}

	/**
	 * 当用户登录时，基础参数都要有,对每一个infoMap中的基础参数进行拦截,如果有一个没有传就验证失败
	 */
	private String checkObjectIsNull(JsonNode node) {
		int count = 0;
		String results = "errors[";
		if (node.get("mid") == null) {
			count++;
			results += "mid is null,";
		}
		if (node.get("md5Info") == null) {
			count++;
			results += "md5Info is null,";
		}
		if (node.get("deviceId") == null) {
			count++;
			results += "deviceId is null,";
		}
		if (node.get("deviceOs") == null) {
			count++;
			results += "deviceOs is null,";
		}
		if (node.get("deviceType") == null) {
			count++;
			results += "deviceType is null,";
		}
		if (node.get("appVersion") == null) {
			count++;
			results += "appVersion is null,";
		}
		if (node.get("appChannelNo") == null) {
			count++;
			results += "appChannelNo is null,";
		}
		if (node.get("userName") == null) {
			count++;
			results += "userName is null,";
		}
//		if (node.get("businessId") == null) {
//			count++;
//			results += "businessId is null]";
//		}
		if (count == 0) {// 表单验证不正确
			results = "";
		}
		return results;
	}
}
