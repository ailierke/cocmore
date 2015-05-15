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
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.util.ResponseCode;

/**
 * 社会团体通知app接口
 * @author ailierke
 *
 *
 */
@RestController
@RequestMapping("/mobileapi/inform")
public class GroupNoticeAppController {
	@Resource
	GroupsInformService groupsInformService;
	private static final Logger logger = Logger.getLogger(GroupNoticeAppController.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	
	
	/**
	 * 获取商会通知详情 通过通知id
	 * @param info
	 * @return
	 */
	@RequestMapping("/getInformInfo")
	@SystemControllerLog(description="APP 获取商会通知详情 通过通知id")
	public void getDynamicInfo(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node =  objectMapper.readTree(infoMap);
			String fid = node.get("noticeId").textValue();//通知id
			String tel = node.get("userName")==null?null:node.get("userName").textValue();//用户id
			String businessId = node.get("businessId")==null?null:node.get("businessId").textValue();
			resultMap = groupsInformService.getInformById(fid,tel,businessId);//通过动态id来获取详细点赞数评论数
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
	 * 获取商会通知列表
	 * @param info
	 * @return
	 */
	@RequestMapping("/getInformList")
	@SystemControllerLog(description="APP 获取商会通知列表")
	public void getInformList(String infoMap,HttpServletResponse response,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
		
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node =  objectMapper.readTree(infoMap);
			String fid = node.get("noticeId")==null?null:node.get("noticeId").textValue();//通知id
			int pageSize =node.get("pageSize").intValue();//如果noticeid为空就查用户所在商会的最新pageSize通知
			String businessId = node.get("businessId")==null?null:node.get("businessId").textValue();
			String tel = node.get("userName")==null?null:node.get("userName").textValue();//电话号码
			resultMap = groupsInformService.getInformList(fid,pageSize,businessId,tel,request);//通过动态id来获取详细点赞数评论数
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
	 * 获取商会通知点赞
	 * @param info
	 * @return
	 */
	@RequestMapping("/saveInformSupport")
	@SystemControllerLog(description="APP 获取商会通知点赞")
	public void saveInformSupport(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
			boolean flag = true;
			String fid ="" ;
			String tel="";
			String businessId =null;
			int type=0;
			JsonNode node;
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				node = objectMapper.readTree(infoMap);
				fid = node.get("noticeId").textValue();//通知id
				type = node.get("isSupport").intValue()==0?0:1;//点赞操作类型(0赞，1取消点赞) 0点赞  1取消  2点不赞同
				tel = node.get("mid").textValue();
				businessId = node.get("businessId").textValue();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			flag = groupsInformService.pointLike(fid,tel,businessId,type);//通过动态id来获取详细点赞数评论数
			if(flag==true){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			    map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			    map.put(ResponseCode.MSGR.msg(), "");
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				map.put(ResponseCode.MSGR.msg(), "");
			}
			 COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * 获取商会通知点不赞同
	 * @param info
	 * @return
	 */
	@RequestMapping("/setInformNotSupport")
	@SystemControllerLog(description="APP 获取商会通知点不赞同")
	public void setInformNotSupport(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
			boolean flag = true;
			String fid ="" ;
			String tel="";
			String businessId =null;
			int type=0;
			JsonNode node;
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				node = objectMapper.readTree(infoMap);
				fid = node.get("noticeId").textValue();//通知id
				type = node.get("isNotSupport").intValue()==0?2:1;//点赞操作类型(0点不赞同，1取消不赞tong) 0点赞  1取消  2点不赞同
				tel = node.get("mid").textValue();//用户id
				businessId = node.get("businessId").textValue();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			flag = groupsInformService.pointLike(fid,tel,businessId,type);//通过动态id来获取详细点赞数评论数
			if(flag==true){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			    map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			    map.put(ResponseCode.MSGR.msg(), "");
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				map.put(ResponseCode.MSGR.msg(), "");
			}
			 COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 是否参加商会通知
	 * @param info 	
	 * @return
	 */
	@RequestMapping("/saveInformJoin")
	@SystemControllerLog(description="APP 是否参加商会通知")
	public void saveInformJoin(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
			boolean flag = true;
			String fid ="" ;
			String tel="";
			String isJoin="0";
			String businessId =null;
			JsonNode node;
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				node = objectMapper.readTree(infoMap);
				fid = node.get("noticeId").textValue();//通知id
				isJoin = node.get("isJoin").intValue()==0?"0":"1";//是否参加商会通知(0参加，1取消参加)
				tel = node.get("mid").textValue();//用户id
				businessId = node.get("businessId").textValue();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			flag = groupsInformService.saveInformJoin(fid,tel,businessId,isJoin);
			if(flag==true){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			    map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			    map.put(ResponseCode.MSGR.msg(), "");
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				map.put(ResponseCode.MSGR.msg(), "");
			}
			 COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取商会通知评论回复列表（分页）根据当前评论查前pageSize的
	 * @param info
	 * @return
	 */
	@RequestMapping("/getInformReplyList")
	@SystemControllerLog(description="APP获取商会通知评论回复列表")
	public void getInformReplyList(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode  node = objectMapper.readTree(infoMap);
				String noticeId = node.get("noticeId").textValue();//动态id
				String reviewId = node.get("reviewId").textValue();//动态评论id
				int pageSize = node.get("pageSize").intValue();//分页大小 
			
			   List<Map<String,Object>> resultList = groupsInformService.getInformReplyList(noticeId,reviewId,pageSize);//通过动态id来获取 指定的评论数 的所有回复
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
	 * 删除商会通知评论
	 * @param info
	 * @return
	 */
	@RequestMapping("/delInformReview")
	@SystemControllerLog(description="APP删除商会通知评论")
	public void delInformReview(String infoMap,HttpServletResponse response){
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
			   flag  = groupsInformService.delInformReview(reviewId);//删除评论
			   if(flag==true){
				   map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				    map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				    map.put(ResponseCode.MSGR.msg(), "");
			   }else{
				   map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
					map.put(ResponseCode.MSGR.msg(), "");
			   }
			   COC_APPResponseResult.responseToGJson(map, response);
	}
	
	
	/**
	 * 添加通知评论
	 * @param info
	 * @return
	 */
	@RequestMapping("/addInformReview")
	@SystemControllerLog(description="APP 添加通知评论")
	public void addInformReview(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
			String reviewContent ="";
			String noticeId ="";
			String tel ="";
			String businessId =null;
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode  node = objectMapper.readTree(infoMap);
				reviewContent = node.get("reviewContent").textValue();//动态评论id
				noticeId = node.get("noticeId").textValue();//动态评论id
				tel = node.get("mid").textValue();//tel
				businessId = node.get("businessId").textValue();
			
			      groupsInformService.addInformReview(tel,businessId,noticeId,reviewContent);//添加评论
				   map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				    map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				    map.put(ResponseCode.MSGR.msg(), "");
			} catch (Exception e) {
				 map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
					map.put(ResponseCode.MSGR.msg(), "");
				e.printStackTrace();
			}
			 COC_APPResponseResult.responseToGJson(map, response);
	}
}
