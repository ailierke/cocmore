package com.yunzo.cocmore.core.function.controller.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicJoinActivity;
import com.yunzo.cocmore.core.function.service.JoinActivityServiceI;

/**
 * @author：jackpeng
 * @date：2014年12月27日下午2:37:40
 * 上墙活动参与人controller类
 */
@Controller
@RequestMapping("/joinActivity")
public class JoinActivityServiceController {
	
	private static final Logger logger = Logger.getLogger(JoinActivityServiceController.class);
	
	@Resource
	private JoinActivityServiceI joinActivityService;
	
	/**
	 * 根据抽奖设置id查询指定人
	 * @param settingId
	 * @return
	 */
	@RequestMapping("/findJoinActivityId")
	@ResponseBody
	@SystemControllerLog(description = "根据抽奖设置id查询指定人")
	public Map<String, Object> findJoinActivityId(String settingId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicJoinActivity> list = null;
		try {
			list = joinActivityService.getById(settingId);
			if(list != null){
				map.put("obj", list);
				map.put("msg", "查询成功！");
				map.put("success", true);
			}else{
				map.put("obj", null);
				map.put("msg", "查询失败！");
				map.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return map;
	}
}
