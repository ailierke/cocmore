package com.yunzo.cocmore.core.function.controller.appAPI;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.service.AppdeviceServiceI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.VerificationServiceI;
import com.yunzo.cocmore.core.function.util.ResponseCode;
import com.yunzo.cocmore.utils.base.YunzoCocSignCode;

/**
 * Description: <APP登录访问控制层>. <br>
 * @date:2014年12月13日 下午3:55:12
 * @author beck
 * @version V1.0
 */
@RestController
@RequestMapping("/mobileapi/user")
public class AppLoginController {
		
	private static final Logger logger = Logger.getLogger(AppLoginController.class);
	
	//Json字符串转换为Map
	ObjectMapper mapper = new ObjectMapper();
	
	//会员service
	@Resource(name = "memberService")
	private MemberServiceI memberService;
	
	//验证码service
	@Resource(name = "vfService")
	private VerificationServiceI vfService;
	
	//设备信息service
	@Resource(name = "deviceService")
	private AppdeviceServiceI deviceService;
	
	/**
	 * 登录 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	@SuppressWarnings("unchecked")
	@SystemControllerLog(description="APP登录  ")
	public void doNotNeedSessionAndSecurity_login(String infoMap,String cocSign,
			HttpSession session,HttpServletResponse response){
		logger.info("APP user/getAppVersions");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			//验证请求是否有效
			if(YunzoCocSignCode.checkAppGetCocSign(infoMap,cocSign)){
			
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				map = memberService.login(jsonObj);
				if(map.get(ResponseCode.MSGC.msg()).equals("100")){
					String fmobilePhone = (String) jsonObj.get("userName");
					YBasicMember member = memberService.getByMobilePhone(fmobilePhone);
					session.setAttribute("member", member);
				}
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);

	}
	
	/**
	 * 短信验证码找回登陆密码 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/findPwdBySMS")
	@ResponseBody
	@SystemControllerLog(description="APP短信验证码找回登陆密码 ")
	public void doNotNeedSessionAndSecurity_findPwdBySMS(String infoMap,String cocSign,HttpServletResponse response){
		logger.info("APP user/findPwdBySMS");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			if(YunzoCocSignCode.checkAppGetCocSign(infoMap,cocSign)){
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				//操作
				String userName = (String) jsonObj.get("phone");
				String password = (String) jsonObj.get("password");
				String phoneCheckCode = (String) jsonObj.get("phoneCheckCode");
				map = vfService.findPwdBySMS(userName, password, phoneCheckCode);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 修改登陆密码 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/updatePwd")
	@ResponseBody
	@SuppressWarnings("unchecked")
	@SystemControllerLog(description="APP修改登陆密码 ")
	public void doNotNeedSessionAndSecurity_updatePwd(String infoMap,String cocSign,HttpServletResponse response){
		logger.info("APP user/updatePwd");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			if(YunzoCocSignCode.checkAppGetCocSign(infoMap,cocSign)){
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				//操作
				String userName = (String) jsonObj.get("userName");
				String password = (String) jsonObj.get("password");
				String oldPassword = (String) jsonObj.get("oldPassword");
				if(memberService.updatePassword(userName, oldPassword, password)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "用户名或密码输入错误");
				}
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), "用户名或密码输入错误");
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 短信找回密码，获取验证码 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getSMSAuthcode")
	@ResponseBody
	@SystemControllerLog(description="APP短信找回密码，获取验证码")
	public void doNotNeedSessionAndSecurity_getSMSAuthcode(String infoMap,String cocSign,HttpServletResponse response){
		logger.info("APP user/getSMSAuthcode");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			if(YunzoCocSignCode.checkAppGetCocSign(infoMap,cocSign)){
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				//操作
				String userName = (String)jsonObj.get("phone");
				vfService.getSMSAuthcode(userName);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 游客注册 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/saveGust")
	@ResponseBody
	@SystemControllerLog(description="APP游客注册")
	public void doNotNeedSessionAndSecurity_saveGust(String infoMap,String cocSign,HttpServletResponse response){
		logger.info("APP user/saveGust");
		Map<String, Object> map = new HashMap<String,Object>();
		String deviceId = "";
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			if(YunzoCocSignCode.checkAppGetCocSign(infoMap,cocSign)){
				//操作
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				YAppdevice obj = new YAppdevice();
				
				//获取渠道号,存入设备表
				String no = (String) map.get("appChannelNo");
				String userName = (String) map.get("userName");
				if("0".equals(no)){
					deviceId = (String) jsonObj.get("clientID");
				}else{
					deviceId = (String) jsonObj.get("cocDevicetoken");
				}
				obj.setFappChannelNo(no);
				obj.setFclient((String) jsonObj.get("clientID"));
				obj.setFdeviceId(deviceId);
				obj.setFuserName(userName);
				deviceService.save(obj);
				
				map.put(ResponseCode.MSGR.msg(), memberService.saveGust());
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 用户注册 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/regUser")
	@ResponseBody
	@SystemControllerLog(description="APP用户注册")
	public void doNotNeedSessionAndSecurity_regUser(String infoMap,String cocSign,HttpServletResponse response){
		logger.info("APP user/regUser");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			if(YunzoCocSignCode.checkAppGetCocSign(infoMap,cocSign)){
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				map = memberService.appRegMember(jsonObj);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	
	/**
	 * 绑定个推clientID 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/bindingClientID")
	@ResponseBody
	@SystemControllerLog(description="APP 绑定个推clientID")
	public void doNotNeedSessionAndSecurity_bindingClientID(String infoMap,String cocSign,HttpServletResponse response){
		logger.info("APP user/bindingClientID");
		Map<String, Object> map = new HashMap<String,Object>();
		String deviceId = "";
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			if(YunzoCocSignCode.checkAppGetCocSign(infoMap,cocSign)){
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				YAppdevice obj = new YAppdevice();
				//获取渠道号
				String no = (String) map.get("appChannelNo");
				String userName = (String) map.get("userName");
				if("0".equals(no)){
					deviceId = (String) jsonObj.get("clientID");
				}else{
					deviceId = (String) jsonObj.get("cocDevicetoken");
				}
				obj.setFappChannelNo(no);
				obj.setFclient((String) jsonObj.get("clientID"));
				obj.setFdeviceId(deviceId);
				obj.setFuserName(userName);
				deviceService.save(obj);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	
	/**
	 * 获取手机注册短信验证码  
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getPhoneRegSMSCode")
	@ResponseBody
	@SystemControllerLog(description="APP getPhoneRegSMSCode")
	public void doNotNeedSessionAndSecurity_getPhoneRegSMSCode(String infoMap,String cocSign,HttpServletResponse response){
		logger.info("APP user/getPhoneRegSMSCode");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			if(YunzoCocSignCode.checkAppGetCocSign(infoMap,cocSign)){
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				//操作
				String mid = (String)jsonObj.get("mid");
				String userName = (String)jsonObj.get("phone");
				map = vfService.getPhoneRegSMSCode(mid,userName);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 用户注册并申请加入商会
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/regUserAndApplyBusiness")
	@ResponseBody
	@SystemControllerLog(description="APP 用户注册并申请加入商会")
	public void doNotNeedSessionAndSecurity_regUserAndApplyBusiness(String infoMap,String cocSign,HttpServletResponse response){
		logger.info("APP user/regUserAndApplyBusiness");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			if(YunzoCocSignCode.checkAppGetCocSign(infoMap,cocSign)){
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				map = memberService.appRegMember(jsonObj);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 注册用户申请加入商会
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/applyBusiness")
	@ResponseBody
	@SystemControllerLog(description="APP 注册用户申请加入商会")
	public void doNotNeedSessionAndSecurity_applyBusiness(String infoMap,String cocSign,HttpServletResponse response){
		logger.info("APP user/applyBusiness");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			if(YunzoCocSignCode.checkAppGetCocSign(infoMap,cocSign)){
				Map<String, Object> jsonObj = mapper.readValue(infoMap,Map.class);
				map = memberService.applyBusiness(jsonObj);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
}
