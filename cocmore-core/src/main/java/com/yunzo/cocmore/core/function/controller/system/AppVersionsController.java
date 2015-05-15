package com.yunzo.cocmore.core.function.controller.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YSystemAppVersions;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationEncodingrules;
import com.yunzo.cocmore.core.function.service.AppVersionsServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * Description: <APP版本信息控制层>. <br>
 * @date:2014年12月13日 下午2:04:50
 * @author beck
 * @version V1.0
 */
@Controller
@RequestMapping("/appvn")
public class AppVersionsController {

	private static final Logger logger = Logger.getLogger(AppVersionsController.class);
	
	@Resource(name = "appVnService")
	private AppVersionsServiceI appVnService;
	
	
	/**
	 * 查询全部
	 * @return
	 */
	@RequestMapping("/findAllAppVn")
	@ResponseBody
	@SystemControllerLog(description = "查询全部APP版本信息")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllAppVn(@RequestParam(value="searchName", required=false)String searchName,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit){
		logger.info("YSystemAppVersions  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		PagingList<YSystemAppVersions> list = null;
		try {
			list = appVnService.findAll(searchName,start,limit);
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
	 * 新增
	 * @return
	 */
	@RequestMapping("/saveAppVn")
	@ResponseBody
	@SystemControllerLog(description = "新增APP版本信息")
	public Map<String, Object> doNotNeedSessionAndSecurity_saveAppVn(@ModelAttribute("form")YSystemAppVersions form){
		logger.info("save YSystemAppVersions");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			appVnService.save(form);
			map.put("msg", "修改成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 修改
	 * @return
	 */
	@RequestMapping("/updateAppVn")
	@ResponseBody
	@SystemControllerLog(description = "修改APP版本信息")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateAppVn(@ModelAttribute("form")YSystemAppVersions form){
		logger.info("update YSystemAppVersions");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			appVnService.update(form);
			map.put("msg", "修改成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败");
			map.put("success", false);
		}
		return map;
	}
}
