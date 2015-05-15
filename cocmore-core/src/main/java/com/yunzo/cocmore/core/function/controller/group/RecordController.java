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
import com.yunzo.cocmore.core.function.model.mysql.FAwardsRecord;
import com.yunzo.cocmore.core.function.service.RecordServiceI;

/**
 * @author：jackpeng
 * @date：2014年12月22日下午4:27:45
 * 抽奖记录Controller类
 */
@Controller
@RequestMapping("/record")
public class RecordController {
	
	private static final Logger logger = Logger.getLogger(RecordController.class);
	
	@Resource(name = "recordService")
	private RecordServiceI recordService;
	
	/**
	 * 根据上墙活动id查询是否存在抽奖记录
	 * @param themeId
	 * @return
	 */
	@RequestMapping("/findRecordThemeId")
	@ResponseBody
	@SystemControllerLog(description = "根据上墙活动id查询是否存在抽奖记录")
	public Map<String, Object> findRecordThemeId(String themeId){
		logger.info("find FAwardsRecord hql");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Boolean boo = recordService.getByhql(themeId);
			if(boo==true){
				map.put("msg", "查询成功！");
				map.put("success", true);
			}else{
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
	
	/**
	 * 根据上墙活动id查询抽奖记录信息
	 * @param themeId
	 * @return
	 */
	@RequestMapping("/findByThemeId")
	@ResponseBody
	@SystemControllerLog(description = "根据上墙活动id查询抽奖记录信息")
	public Map<String, Object> findByThemeId(String themeId){
		logger.info("find FAwardsRecord hql");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<FAwardsRecord> list = recordService.getByThemeId(themeId);
			map.put("obj", list);
			map.put("msg", "查询成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return map;
	}
	
}
