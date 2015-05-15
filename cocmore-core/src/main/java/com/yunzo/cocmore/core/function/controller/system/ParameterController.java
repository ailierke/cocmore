package com.yunzo.cocmore.core.function.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationEncodingrules;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationLog;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationParameter;
import com.yunzo.cocmore.core.function.service.ParameterServiceI;
/** 
 *Description: <系统参数服务控制层>. <br>
 * @date:2014年11月27日 下午4:16:09
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/par")
public class ParameterController {
private static final Logger logger = Logger.getLogger(ParameterController.class);
	
	@Resource(name = "parService")
	private ParameterServiceI parService;
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findParById")
	@ResponseBody
	@SystemControllerLog(description = "根据ID查询系统参数")
	public Map<String, Object> doNotNeedSessionAndSecurity_findParById(@RequestParam("fid")String fid){
		logger.info("YSystemconfigurationParameter  findParById");
		Map<String, Object> map = new HashMap<String,Object>();
		YSystemconfigurationParameter obj = null;
		try {
			obj = parService.getById(fid);
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
	@RequestMapping("/findAllPar")
	@ResponseBody
	@SystemControllerLog(description = "查询全部系统参数")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllPar(){
		logger.info("YSystemconfigurationParameter  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		List<YSystemconfigurationParameter> list = null;
		try {
			list = parService.findAll();
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
	 * 修改
	 * @return
	 */
	@RequestMapping("/updatePar")
	@ResponseBody
	@SystemControllerLog(description = "修改系统参数")
	public Map<String, Object> doNotNeedSessionAndSecurity_updatePar(@ModelAttribute("form")YSystemconfigurationParameter form){
		logger.info("update YSystemconfigurationParameter");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			parService.update(form);
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
