package com.yunzo.cocmore.core.function.controller.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.FSmsSendrecord;
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.service.SmsSendServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <短信发送控制层>. <br>
 * @date:2014年11月26日 下午3:57:28
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/sms")
public class SmsSendController {
private static final Logger logger = Logger.getLogger(MembercompanyController.class);
	
	@Resource(name = "smsService")
	private SmsSendServiceI smsService;
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findSmsById")
	@ResponseBody
	@SystemControllerLog(description="根据ID查询短信")
	public Map<String, Object> doNotNeedSessionAndSecurity_findSmsById(@RequestParam("fid")String fid){
		logger.info("FSmsSendrecord  findSmsById");
		Map<String, Object> map = new HashMap<String,Object>();
		FSmsSendrecord obj = null;
		try {
			obj = smsService.getById(fid);
			map.put("msg", "查询成功");
			map.put("obj", obj);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 查询全部
	 * @return
	 */
	@RequestMapping("/findAllSms")
	@ResponseBody
	@SystemControllerLog(description="查询全部短信")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllSms(){
		logger.info("FSmsSendrecord  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		List<FSmsSendrecord> list = null;
		try {
			list = smsService.findAll();
			map.put("msg", "查询成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 根据手机号、姓名、状态查询
	 * @return
	 */
	@RequestMapping("/findAllByPhoneNameStatus")
	@ResponseBody
	@SystemControllerLog(description="根据手机号、姓名、状态查询")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllByPhoneNameStatus(String memberFid,String fmobilePhone,int fbillState){
		logger.info("findAllByPhoneNameStatus  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		List<FSmsSendrecord> list = null;
		try {
			String hql = "from FSmsSendrecord y where y.YBasicMember.fid = '" + memberFid + "' and y.fmobilePhone = '" + fmobilePhone + "' "
					+ "and y.fbillState = " + fbillState;
			list = smsService.findAllByPhoneNameStatus(hql);
			map.put("msg", "查询成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 
	 * @Title: doNotNeedSessionAndSecurity_findSmsByGroupId 
	 * @Description: TODO 根据团体id查询并分页
	 * @param @param groupId
	 * @param @param headline
	 * @param @return    
	 * @return Map<String,Object>   
	 * @throws
	 */
	@RequestMapping("doNotNeedSessionAndSecurity_findSmsByGroupId")
	@ResponseBody
	@SystemControllerLog(description="根据团体id查询并分页")
	public Map<String,Object> doNotNeedSessionAndSecurity_findSmsByGroupId(Integer start,Integer limit,String groupId,String headline){
		logger.info("smsService doNotNeedSessionAndSecurity_findSmsByGroupId");
		Map<String, Object> map = new HashMap<String, Object>();
		//查询页面现显示和总条数
		PagingList<FSmsSendrecord> pagingList = null;
		try {
			pagingList = smsService.getAllControllerPagingList(start, limit, groupId, headline);
			map.put("count", pagingList.getCount());//总条数
			map.put("list", pagingList.getList());//每页显示数据
			map.put("msg", "查询成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 发送短信
	 * @param fid
	 * @return
	 */
	@RequestMapping("/send")
	@ResponseBody
	@SystemControllerLog(description="发送的短信")
	public Map<String, Object> doNotNeedSessionAndSecurity_send(String fids,String msgText,String dateString){
		logger.info("FSmsSendrecord  send");
		Map<String, Object> map = new HashMap<String,Object>();
		
		try {
			if(smsService.send(fids, msgText,dateString)){
				map.put("msg", "发送成功");
				map.put("success", true);
			}else{
				map.put("msg", "查询失败");
				map.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
}
