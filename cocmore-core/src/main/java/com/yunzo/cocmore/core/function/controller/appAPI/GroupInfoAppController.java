package com.yunzo.cocmore.core.function.controller.appAPI;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.service.GroupsService;
import com.yunzo.cocmore.core.function.service.PositionServiceI;
import com.yunzo.cocmore.core.function.util.ResponseCode;
/**
 * 
 * @author ailierke
 *
 *商会信息管理APP端接口
 */
@RestController
@RequestMapping("/mobileapi/business")
public class GroupInfoAppController {
	@Resource
	GroupsService groupsService;
	//职位service
	@Resource(name = "posService")
	private PositionServiceI posService;
	
	private static final Logger logger = Logger.getLogger(GroupInfoAppController.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * 获取我的所属商会
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getBusinessInfo")
	@SystemControllerLog(description="APP 获取我的所属商会")
	public void getGroupOfMine(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		  try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String userName = node.get("userName").textValue();
			Map<String,Object> resultMap = groupsService.getGroupByUserId(userName);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			map.put(ResponseCode.MSGR.msg(), resultMap);
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			map.put(ResponseCode.MSGR.msg(), "");
			e.printStackTrace();
		} 
		  COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取我的所属商会
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/businessInfo")
	@SystemControllerLog(description="APP 获取我的所属商会")
	public void getGroup(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		  try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			Map<String,Object> resultMap = null;
			String aBusinessId = node.get("aBusinessId")==null?null:node.get("aBusinessId").textValue();
			if(aBusinessId!=null&&!aBusinessId.equals("")){
				resultMap = groupsService.getBygroupId1(aBusinessId);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), resultMap);
			}else{
				resultMap  = new HashMap<String,Object> ();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SIGNWRONGPARAM.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SIGNWRONGPARAM.msg());
				map.put(ResponseCode.MSGR.msg(), resultMap);
			}
			
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			map.put(ResponseCode.MSGR.msg(), "");
			e.printStackTrace();
		} 
		  COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * 获取所有商会列表
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getBusinessList")
	@SystemControllerLog(description="APP 获取所有商会列表")
	public void getAllGroup(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> reslutList = null;
		  try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
			  reslutList = groupsService.getAllGroup();
			  map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			  map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			  map.put(ResponseCode.MSGR.msg(), reslutList);
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			e.printStackTrace();
		} 
		  COC_APPResponseResult.responseToGJson(map, response);
	}
	
	
	
	/**
	 * 获取所有商会列表
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getRegionBusinessList")
	@SystemControllerLog(description="APP 获取所有商会列表")
	public void getRegionBusinessList(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> reslutList = null;
		  try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
			  reslutList = groupsService.getRegionBusinessList(infoMap);
			  map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			  map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			  map.put(ResponseCode.MSGR.msg(), reslutList);
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			e.printStackTrace();
		} 
		  COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * 商会活动详情网页
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getBusinessActivityPage")
	@SystemControllerLog(description="APP 商会活动详情网页")
	public void getGroupActivityDetailUrl(String infoMap,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("##########"+request.getServletContext().getContextPath()+"/webApp/shhdxq.html");
		String reslutList ="webApp/shhdxq.html";
		  try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
			  map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			  map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			  map.put(ResponseCode.MSGR.msg(), reslutList);
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			e.printStackTrace();
		} 
		  COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 我的商会列表是否有更新
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getBusinessVersions")
	@SystemControllerLog(description="APP  我的商会列表是否有更新")
	public void getBusinessVersions(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		  try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
			  Map<String, Object> jsonObj = objectMapper.readValue(infoMap,Map.class);
				String businessId = (String) jsonObj.get("businessId");
				int CacheVersion = (int) jsonObj.get("localContactCacheVersion");
				map.put(ResponseCode.MSGR.msg(), posService.getPositionByVersion(businessId, CacheVersion));
				map.put(ResponseCode.MSGC.msg(), "100");
				map.put(ResponseCode.MSGM.msg(), "查询成功");
			} catch (Exception e) {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				map.put(ResponseCode.MSGR.msg(), "");
				e.printStackTrace();
			} 
		  COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 通过模糊查询获取所有商会列表
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/fuzzyQueryBusinessList")
	@SystemControllerLog(description="APP 获取所有商会列表")
	public void fuzzyQueryBusinessList(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> reslutList = null;
		  try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
			  reslutList = groupsService.fuzzyQueryBusinessList(infoMap);
			  map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			  map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			  map.put(ResponseCode.MSGR.msg(), reslutList);
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			e.printStackTrace();
		} 
		  COC_APPResponseResult.responseToGJson(map, response);
	}
}
