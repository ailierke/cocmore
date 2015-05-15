package com.yunzo.cocmore.core.function.controller.appAPI;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.service.IMService;
import com.yunzo.cocmore.core.function.util.ResponseCode;

/**
 * IM App端接口
 * @author ailierke
 *
 *
 */
@RestController
@RequestMapping("/mobileapi/im")
public class IMAppController {
	@Resource
	IMService imService;
	private static final Logger logger = Logger.getLogger(IMAppController.class);
	/**
	 * 批量获取用户属性
	 * @param info
	 * @return
	 */
	@RequestMapping("/getUsersList")
	@SystemControllerLog(description="APP 批量获取用户属性")
	public void getUsersList(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			resultList = imService.getUserList(infoMap);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			map.put(ResponseCode.MSGR.msg(), resultList);
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			map.put(ResponseCode.MSGR.msg(), "");
			e.printStackTrace();
		}
		 COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 批量获取群组属性
	 * @param info
	 * @return
	 */
	@RequestMapping("/getGroupProperty")
	@SystemControllerLog(description="APP 批量获取群组属性")
	public void getGroupProperty(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			resultMap = imService.getGroupProperty(infoMap);
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
	 * 设置群组属性
	 * @param info
	 * @return
	 */
	@RequestMapping("/saveGroupProperty")
	@SystemControllerLog(description="APP 设置群组属性")
	public void saveGroupProperty(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			imService.saveGroupProperty(infoMap);//通过动态id来获取详细点赞数评论数
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		} 
		map.put(ResponseCode.MSGR.msg(), "");
		 COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * 创建群
	 * @param info
	 * @return
	 */
	@RequestMapping("/createGroup")
	@SystemControllerLog(description="APP 创建群")
	public void createGroup(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			imService.createGroup(infoMap);//通过动态id来获取详细点赞数评论数

			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			e.printStackTrace();
		} 
		map.put(ResponseCode.MSGR.msg(), "");
		 COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 群组加人
	 * @param info 	
	 * @return
	 */
	@RequestMapping("/addGroupPersons")
	@SystemControllerLog(description="APP 群组加人")
	public void addGroupPersons(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			imService.addGroupPersons(infoMap);

			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			e.printStackTrace();
		}
		map.put(ResponseCode.MSGR.msg(), "");
		 COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 群删人
	 * @param info
	 * @return
	 */
	@RequestMapping("/delGroupPersons")
	@SystemControllerLog(description="APP 群删人")
	public void delGroupPersons(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");

			imService.delGroupPersons(infoMap);//通过动态id来获取 指定的评论数 的所有回复
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			map.put(ResponseCode.MSGR.msg(), "");
		}catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			map.put(ResponseCode.MSGR.msg(), "");
			e.printStackTrace();
		} 
		 COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 删除群
	 * @param info
	 * @return
	 */
	@RequestMapping("/delGroupById")
	@SystemControllerLog(description="APP 删除群")
	public void delGroupById(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			imService.delGroupById(infoMap);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			map.put(ResponseCode.MSGR.msg(), "");
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			map.put(ResponseCode.MSGR.msg(), "");
		}
		 COC_APPResponseResult.responseToGJson(map, response);
	}
}
