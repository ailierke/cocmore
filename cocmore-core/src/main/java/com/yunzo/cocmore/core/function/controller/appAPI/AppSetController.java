package com.yunzo.cocmore.core.function.controller.appAPI;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicFeedback;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.service.AppVersionsServiceI;
import com.yunzo.cocmore.core.function.service.FeedbackServiceI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.OrganizationService;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.ResponseCode;
import com.yunzo.cocmore.utils.base.MD5Util;
import com.yunzo.cocmore.utils.base.StringChangeCharset;
import com.yunzo.cocmore.utils.base.YunzoCocSignCode;

/**
 * Description: <APP设置模块访问控制层>. <br>
 * @date:2014年12月12日 下午5:39:01
 * @author beck
 * @version V1.0
 */
@RestController
@RequestMapping("/mobileapi/appset")
public class AppSetController {
	private static final Logger logger = Logger.getLogger(AppSetController.class);
	
	//Json字符串转换为Map
	ObjectMapper mapper = new ObjectMapper(); 
	
	//会员service
	@Resource(name = "memberService")
	private MemberServiceI memberService;
	
	//意见反馈service
	@Resource(name = "feedbackService")
	private FeedbackServiceI feedbackService;
	
	//APP版本信息service
	@Resource(name = "appVnService")
	private AppVersionsServiceI appVnService;
	
	//组织service
	@Resource(name = "orgService")
	private OrganizationService orgService;
	
	/**
	 * 获取最新更新版本信息
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/setMd5info")
	@ResponseBody
	@SystemControllerLog(description="APP 获取最新更新版本信息")
	public void doNotNeedSessionAndSecurity_setAppTest(String infoMap,HttpServletResponse response){
//		logger.info("APP appset/setMd5info");
//		Map<String, Object> map = new HashMap<String,Object>();
//			try {
//				infoMap = URLDecoder.decode(infoMap, "UTF-8");
//				Map<String, Object> objList = appVnService.getAppVersions();
//				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
//				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
//				map.put(ResponseCode.MSGR.msg(), objList);
//			} catch (Exception e) {
//				e.printStackTrace();
//				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
//				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
//			}
//			COC_APPResponseResult.responseToGJson(map, response);
	}
	
	
	/**
	 * 获取最新更新版本信息 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getAppVersions")
	@ResponseBody
	@SystemControllerLog(description="APP 获取最新更新版本信息 ")
	public void doNotNeedSessionAndSecurity_getAppVersions(String infoMap,HttpServletResponse response){
		logger.info("APP appset/getAppVersions");
		Map<String, Object> map = new HashMap<String,Object>();
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
				
				//获取版本号和渠道号
				String appVersion = (String) jsonObj.get("appVersion");
				String appChannelNo = (String) jsonObj.get("appChannelNo");
				
				Map<String, Object> objList = appVnService.getAppVersions(appVersion,appChannelNo);
				if(objList != null){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
					map.put(ResponseCode.MSGR.msg(), objList);
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
					map.put(ResponseCode.MSGR.msg(), "没有最新的版本信息！");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
			COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 修改会员密码
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/updateUserPwd")
	@ResponseBody
	@SystemControllerLog(description="APP 修改会员密码")
	public void doNotNeedSessionAndSecurity_updateMember(String infoMap,HttpServletResponse response){
		logger.info("APP appset/updateUserPwd");
		Map<String, Object> map = new HashMap<String,Object>();
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				String phone = (String) jsonObj.get("userName");
				String oldPassword = (String) jsonObj.get("oldPassword");
				List<YBasicMember> objList = (List<YBasicMember>) memberService.findByHql("from YBasicMember y where y.fmobilePhone = '" + phone + "' and y.fpassword ='"+oldPassword+"'");
				if(objList!=null&&objList.size()>0){
					for(YBasicMember ymember : objList ){
						ymember.setFpassword((String)jsonObj.get("newPassword"));
						memberService.updateStatus(ymember);
					}
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.USERGETSUCCESS.msg());
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(),ResponseCode.PASSWORDWRONG.msg());
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
			COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 保存意见反馈
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/saveFeedback")
	@ResponseBody
	@SystemControllerLog(description="APP 保存意见反馈")
	public void doNotNeedSessionAndSecurity_saveFeedback(String infoMap,HttpServletResponse response){
		logger.info("YBasicFeedback  savefeedback");
		Map<String, Object> map = new HashMap<String,Object>();
		//验证请求是否有效

			YBasicFeedback back = new YBasicFeedback();
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				back.setFmessage((String) jsonObj.get("feedback"));
				back.setFcontactInfo((String) jsonObj.get("contactInfo"));
				feedbackService.save(back);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
			COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 *	用户协议  
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getUserAgreementForUrl")
	@ResponseBody
	@SystemControllerLog(description="APP 用户协议")
	public void doNotNeedSessionAndSecurity_getUserAgreementForUrl(String infoMap,HttpServletRequest request,HttpServletResponse response){
		logger.info("APP appset/getUserAgreementForUrl");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
			
			StringBuffer sb = request.getRequestURL();	//获取请求全路径
			String url = null;
			
			//获取渠道号
			String no = (String) jsonObj.get("appChannelNo");
			if(no.equals("0") || no.equals("1") || no.equals("2")){		//云筑圈
				url = sb.substring(0, sb.indexOf("mobileapi")) + "webApp/yhxy.html";
			}else{														//狮子会				
				url = sb.substring(0, sb.indexOf("mobileapi")) + "webApp/yhxySQ380.html";
			}

			map.put(ResponseCode.MSGR.msg(), url);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
		
	}
	
	
	/**
	 *	关于我们 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getAboutUsForUrl")
	@ResponseBody
	@SystemControllerLog(description="APP 关于我们 ")
	public void doNotNeedSessionAndSecurity_getAboutUsForUrl(String infoMap,HttpServletRequest request,HttpServletResponse response){
		logger.info("APP appset/getAboutUsForUrl");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			StringBuffer sb = request.getRequestURL();	//获取请求全路径
			String url = sb.substring(0, sb.indexOf("mobileapi")) + "webApp/gyzz.html";
			map.put(ResponseCode.MSGR.msg(), url);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
}
