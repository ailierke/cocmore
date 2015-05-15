package com.yunzo.cocmore.core.function.controller.appAPI;

import java.net.URLDecoder;
import java.util.ArrayList;
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
import com.yunzo.cocmore.core.function.service.GroupsDynamicService;
import com.yunzo.cocmore.core.function.util.ResponseCode;

@RestController
@RequestMapping("/mobileapi/dynamic")
public class GroupDynamicAppController {
	@Resource
	GroupsDynamicService groupsDynamicService;
	private static final Logger logger = Logger.getLogger(GroupDynamicAppController.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * 获取商会动态详情 通过动态id
	 * @param info
	 * @return
	 */
	@RequestMapping("/getDynamicInfo")
	@SystemControllerLog(description="APP 获取商会动态详情 通过动态id")
	public void getDynamicInfo(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node =  objectMapper.readTree(infoMap);
			String fid = node.get("dynamicId").textValue();//动态id
			String userName = node.get("userName")==null?null:node.get("userName").textValue();//用户id
			String businessId = node.get("businessId")==null?null:node.get("businessId").textValue();//用户id
			resultMap = groupsDynamicService.getDynamicById(fid,userName,businessId);//通过动态id来获取详细点赞数评论数
			map.put("responseCode", ResponseCode.SUCCESS.value());
		    map.put("message", ResponseCode.SUCCESS.msg());
			map.put("result", resultMap);
		} catch (Exception e) {
			map.put("responseCode", ResponseCode.EXCEPTION.value());
			map.put("message", ResponseCode.EXCEPTION.msg());
			map.put("result", "");
			e.printStackTrace();
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取商会动态列表 dynamicId  pageSize  有dynamicId就查询一条，不然查询商会最新pagesize
	 * @param info
	 * @return
	 */
	@RequestMapping("/getDynamicList")
	@SystemControllerLog(description="APP 获取商会动态列表")
	public void getInformList(String infoMap,HttpServletResponse response,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
		
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node =  objectMapper.readTree(infoMap);
			String fid = node.get("dynamicId")==null?null:node.get("dynamicId").textValue();//动态id
			int pageSize =node.get("pageSize").intValue();
			String businessId = node.get("businessId")==null?null:node.get("businessId").textValue();
			String tel = node.get("userName")==null?null:node.get("userName").textValue();//电话号码
			resultMap = groupsDynamicService.getDynamicList(fid,pageSize,businessId,tel,request);
			map.put("responseCode", ResponseCode.SUCCESS.value());
		    map.put("message", ResponseCode.SUCCESS.msg());
			map.put("result", resultMap);
		} catch (Exception e) {
			map.put("responseCode", ResponseCode.EXCEPTION.value());
			map.put("message", ResponseCode.EXCEPTION.msg());
			map.put("result", "");
			e.printStackTrace();
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * 商会动态点赞
	 * @param info
	 * @return
	 */
	@RequestMapping("/saveDynamicSupport")
	@SystemControllerLog(description="APP  商会动态点赞")
	public void saveDynamicSupport(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
			boolean flag = true;String fid ="" ;String tel="";
			String businessId =null;
			int type=0;
			JsonNode node;
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				node = objectMapper.readTree(infoMap);
				fid = node.get("dynamicId").textValue();//通知id
				businessId = node.get("businessId").textValue();
				type = new Integer(node.get("isSupport").textValue())==0?0:1;//参数点赞操作类型(0点不赞同，1取消点赞)  表： 0点赞  1取消  
				tel = node.get("mid").textValue();//用户电话号码
			} catch (Exception e) {
				e.printStackTrace();
			} 
			flag = groupsDynamicService.pointLike(fid,tel,businessId,type);//通过动态id来获取详细点赞数评论数
			if(flag==true){
				map.put("responseCode", ResponseCode.SUCCESS.value());
			    map.put("message", ResponseCode.SUCCESS.msg());
			    map.put("result", "");
			}else{
				map.put("responseCode", ResponseCode.EXCEPTION.value());
				map.put("message", ResponseCode.EXCEPTION.msg());
				map.put("result", "");
			}
			COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取商会动态评论回复列表（分页）根据当前评论查前pageSize的
	 * @param info
	 * @return
	 */
	@RequestMapping("/getDynamicReplyList")
	@SystemControllerLog(description="APP获取商会动态评论回复列表（分页）")
	public void getDynamicReplyList(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode  node = objectMapper.readTree(infoMap);
				String dynamicId = node.get("dynamicId").textValue();//动态id
				String reviewId = node.get("reviewId").textValue();//动态评论id
				int pageSize = new Integer(node.get("pageSize").textValue());//分页大小 
			
			   List<Map<String,Object>> resultList = groupsDynamicService.getDynamicReplyList(dynamicId,reviewId,pageSize);//通过动态id来获取 指定的评论数 的所有回复
				map.put("responseCode", ResponseCode.SUCCESS.value());
			    map.put("message", ResponseCode.SUCCESS.msg());
			    map.put("result", resultList);
			} catch (Exception e) {
				map.put("responseCode", ResponseCode.EXCEPTION.value());
				map.put("message", ResponseCode.EXCEPTION.msg());
				map.put("result", "");
				e.printStackTrace();
			} 
			COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * 删除商会动态评论
	 * @param info
	 * @return
	 */
	@RequestMapping("/delDynamicReview")
	@SystemControllerLog(description="APP  删除商会动态评论")
	public void delDynamicReview(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = true;
			String reviewId ="";
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode  node = objectMapper.readTree(infoMap);
				reviewId = node.get("reviewId").textValue();//动态评论id
			} catch (Exception e) {
				e.printStackTrace();
			}
			   flag  = groupsDynamicService.delDynamicReview(reviewId);//删除评论
			   if(flag==true){
				   map.put("responseCode", ResponseCode.SUCCESS.value());
				    map.put("message", ResponseCode.SUCCESS.msg());
				    map.put("result", "");
			   }else{
				   map.put("responseCode", ResponseCode.EXCEPTION.value());
					map.put("message", ResponseCode.EXCEPTION.msg());
					map.put("result", "");
			   }
			   COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * 添加动态评论
	 * @param info
	 * @return
	 */
	@RequestMapping("/addDynamicReview")
	@SystemControllerLog(description="APP 添加动态评论")
	public void addDynamicReview(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
			String reviewContent ="";
			String dynamicId ="";
			String tel ="";
			String businessId = null; 
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode  node = objectMapper.readTree(infoMap);
				businessId = node.get("businessId").textValue();
				reviewContent = node.get("reviewContent").textValue();//动态评论id
				dynamicId = node.get("dynamicId").textValue();//动态评论id
				tel = node.get("mid").textValue();//动态评论id
			
				groupsDynamicService.addDynamicReview(tel,businessId,dynamicId,reviewContent);//添加评论
				   map.put("responseCode", ResponseCode.SUCCESS.value());
				    map.put("message", ResponseCode.SUCCESS.msg());
				    map.put("result", "");
			} catch (Exception e) {
				 map.put("responseCode", ResponseCode.EXCEPTION.value());
					map.put("message", ResponseCode.EXCEPTION.msg());
					map.put("result", "");
				e.printStackTrace();
			}
			COC_APPResponseResult.responseToGJson(map, response);
	}
}
