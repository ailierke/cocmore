package com.yunzo.cocmore.core.function.controller.appAPI;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMemberbymemberhide;
import com.yunzo.cocmore.core.function.service.MBMHideServiceI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.MembercompanyServiceI;
import com.yunzo.cocmore.core.function.service.PositionServiceI;
import com.yunzo.cocmore.core.function.util.ResponseCode;

/**
 * Description: <APP通讯录控制层>. <br>
 * 
 * @date:2014年12月15日 下午4:50:33
 * @author beck
 * @version V1.0
 */
@Controller
@RequestMapping("/mobileapi/contacts")
public class APPContactController {
	private static final Logger logger = Logger
			.getLogger(APPContactController.class);

	// Json字符串转换为Map
	ObjectMapper mapper = new ObjectMapper();

	// 职位service
	@Resource(name = "posService")
	private PositionServiceI posService;

	// 会员service
	@Resource(name = "memberService")
	private MemberServiceI memberService;

	// 公司service
	@Resource(name = "companyService")
	private MembercompanyServiceI companyService;

	// 会员黑名单service
	@Resource(name = "mbmService")
	private MBMHideServiceI mbmService;

	/**
	 * 检查通讯录信息是否有改动
	 * 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getContactsVersions")
	@ResponseBody
	@SystemControllerLog(description="APP检查通讯录信息是否有改动")
	public void doNotNeedSessionAndSecurity_getContactsVersions(
			String infoMap,HttpServletResponse response) {
		logger.info("APP contacts/getContactsVersions");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
			String businessId = (String) jsonObj.get("businessId");
			int CacheVersion = (Integer) jsonObj
					.get("localContactCacheVersion");
			map.put(ResponseCode.MSGR.msg(),posService.getPositionByVersion(businessId, CacheVersion));
			//无论传入什么参数，临时返回有更新
			//map.put(ResponseCode.MSGR.msg(),1);
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
	 * 获取通讯录信息
	 * 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getContactsInfo")
	@ResponseBody
	@SystemControllerLog(description="APP获取通讯录信息")
	public void doNotNeedSessionAndSecurity_getContactsInfo(String infoMap,
			HttpServletResponse response) throws Exception {
		logger.info("APP contacts/getContactsVersions");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
			// 操作
			String businessId = (String) jsonObj.get("businessId");
			int cacheVersion = (int) jsonObj.get("cacheVersion");
			int pageSize = (Integer) jsonObj.get("pageSize");
			map = posService.getPositionByVersionAndPageSize(businessId, cacheVersion, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}

		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 批量查询用户隐私设置通讯录列表
	 * 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getContactsPrivacyList")
	@ResponseBody
	@SystemControllerLog(description="APP批量查询用户隐私设置通讯录列表")
	public void doNotNeedSessionAndSecurity_getContactsPrivacyList(
			String infoMap,HttpServletResponse response) {
		logger.info("APP contacts/getContactsPrivacyList");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
			// 操作
			String phone = (String) jsonObj.get("mid");
			String businessId = (String) jsonObj.get("businessId");
			map.put(ResponseCode.MSGR.msg(), mbmService.findAllByInId(phone,businessId));
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
	 * 查询会员个人信息接口
	 * 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getContactsOtherPersonInfo")
	@ResponseBody
	@SuppressWarnings("unchecked")
	@SystemControllerLog(description="APP查询会员个人信息接口")
	public void doNotNeedSessionAndSecurity_getContactsOtherPersonInfo(
			String infoMap,HttpServletResponse response) {
		logger.info("APP contacts/getContactsOtherPersonInfo");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
			// 操作
			String userId = (String) jsonObj.get("userId");
			String businessId = (String) jsonObj.get("businessId");
			map.put(ResponseCode.MSGR.msg(), memberService.getContactsOtherPersonInfo(userId,businessId));
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
	 * 设置通讯录隐私
	 * 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/setContactsPrivacy")
	@ResponseBody
	@SystemControllerLog(description="APP设置通讯录隐私")
	public void doNotNeedSessionAndSecurity_setContactsPrivacy(
			String infoMap,HttpServletResponse response) {
		logger.info("APP contacts/setContactsPrivacy");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			mbmService.setContactsPrivacy(infoMap);
			
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
	 * 查询会员企业信息接口
	 * 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getMemberEnterpriseInfo")
	@ResponseBody
	@SystemControllerLog(description="APP查询会员企业信息接口")
	public void doNotNeedSessionAndSecurity_getMemberEnterpriseInfo(
			String infoMap,HttpServletResponse response) {
		logger.info("APP contacts/getMemberEnterpriseInfo");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
			// 操作
			String phone = (String) jsonObj.get("userid");
			String businessId = (String) jsonObj.get("businessId");
			map.put(ResponseCode.MSGR.msg(), companyService.findAllByUserId(phone,businessId));
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
	 * 检查通讯录职位是否有改动
	 * 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getPositionVersions")
	@ResponseBody
	@SystemControllerLog(description="APP检查通讯录职位是否有改动")
	public void doNotNeedSessionAndSecurity_getPositionVersions(
			String infoMap,HttpServletResponse response) {
		logger.info("APP contacts/getPositionVersions");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
			String businessId = (String) jsonObj.get("businessId");
			int CacheVersion = (Integer) jsonObj
					.get("localContactCacheVersion");
			map.put(ResponseCode.MSGR.msg(), posService.getPositionByPositionVersion(businessId, CacheVersion));
			//无论传入什么参数，临时返回有更新
			//map.put(ResponseCode.MSGR.msg(), 1);
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
	 * 获取通讯录职位
	 * 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getPositionInfo")
	@ResponseBody
	@SystemControllerLog(description="APP获取通讯录职位")
	public void doNotNeedSessionAndSecurity_getPositionInfo(
			String infoMap,HttpServletResponse response) {
		logger.info("APP contacts/getPositionInfo");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
			// 操作
			String businessId = (String) jsonObj.get("businessId");
			int cacheVersion = (int) jsonObj.get("localContactCacheVersion");
			map.put(ResponseCode.MSGR.msg(),
					posService.getPositionInfo(businessId, cacheVersion));
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
