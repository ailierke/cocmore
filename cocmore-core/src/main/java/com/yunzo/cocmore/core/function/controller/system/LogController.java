package com.yunzo.cocmore.core.function.controller.system;

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
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationLog;
import com.yunzo.cocmore.core.function.service.LogServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <日志控制层>. <br>
 * @date:2014年11月27日 上午11:58:05
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/log")
public class LogController {
	private static final Logger logger = Logger.getLogger(LogController.class);
	
	@Resource(name = "logService")
	private LogServiceI logService;
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findLogById")
	@ResponseBody
	@SystemControllerLog(description = "根据ID查询日志")
	public Map<String, Object> doNotNeedSessionAndSecurity_findLogById(@RequestParam("fid")String fid){
		logger.info("YSystemconfigurationLog  findLogById");
		Map<String, Object> map = new HashMap<String,Object>();
		YSystemconfigurationLog obj = null;
		try {
			obj = logService.getById(fid);
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
	@RequestMapping("/findAllLog")
	@ResponseBody
	@SystemControllerLog(description = "查询全部日志")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllLog(@RequestParam(value="searchName", required=false)String searchName,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit){
		logger.info("YSystemconfigurationLog  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		PagingList<YSystemconfigurationLog> list = null;
		try {
			list = logService.findAll(searchName,start,limit);
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
}
