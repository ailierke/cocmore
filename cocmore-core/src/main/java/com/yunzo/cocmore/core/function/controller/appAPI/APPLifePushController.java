package com.yunzo.cocmore.core.function.controller.appAPI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.service.LifePushinfoSerciveI;
import com.yunzo.cocmore.core.function.service.impl.LifePushinfoSerciveImpl;
import com.yunzo.cocmore.core.function.vo.PushVo;
import com.yunzo.cocmore.core.thread.PushThread;

/**
 * Description: <生活推送记录controller>. <br>
 * @date:2015年3月18日 下午12:14:46
 * @author beck
 * @version V1.0
 */
@Controller
@RequestMapping("/mobileapi/lifepush")
public class APPLifePushController {
	private static final Logger logger = Logger
			.getLogger(APPLifePushController.class);
	
	//生活推送记录service
	@Resource(name = "lifePushService")
	private LifePushinfoSerciveI lifePushService;
	
	@Resource
	private GroupsInformService groupsInformService;
	
	/**
	 * 新增生活项目之后保存记录并推送
	 */
	@RequestMapping("/lifePushAndSave")
	@ResponseBody
	public void lifePushAndSave(@RequestParam(value="lifeId")String lifeId){
		//System.out.println(lifeId);
		try {
			//执行推送操作和保存记录,获取推送的电话号码
			Set<String> tels = lifePushService.getTels();
			Map<String,PushVo> deviceIdMap  =  groupsInformService.getOutRepeat(tels);
			/**
			 * 开启推送线程，进行推送行为的记录和系统日志的记录  app端的推送
			 */
			PushThread pushThread = new PushThread(new Integer(3), deviceIdMap, "有新产品上架了", lifeId, null,lifePushService);
			pushThread.start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取项目是否已读标识
	 */
	@RequestMapping("/isReadByTel")
	@ResponseBody
	public void isReadByTel(@RequestParam(value="tel")String tel,@RequestParam(value="lifeId")String lifeId
			,HttpServletResponse response){
		System.out.println(tel + "," + lifeId);
		try {
			//执行推送操作和保存记录,获取推送的电话号码
			int num = lifePushService.isReadByTel(tel, lifeId);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("isRead", num);
			COC_APPResponseResult.responseToGJson(map, response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
