package com.yunzo.cocmore.core.function.controller.system;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicIndustry;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationEncodingrules;
import com.yunzo.cocmore.core.function.service.EncodingrulesServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <系统编码规则服务控制层>. <br>
 * @date:2014年11月27日 下午3:58:12
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/enc")
public class EncodingrulesController {
	private static final Logger logger = Logger.getLogger(EncodingrulesController.class);
	
	@Resource(name = "encService")
	private EncodingrulesServiceI encService;
	
	//此处的参数也可以是ServletRequestDataBinder类型 
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {  
	    //注册自定义的属性编辑器 (日期)  
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
	    //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
	    binder.registerCustomEditor(Date.class, dateEditor);     
	}
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findEncById")
	@ResponseBody
	@SystemControllerLog(description = "根据id查询编码规则")
	public Map<String, Object> doNotNeedSessionAndSecurity_findLogById(@RequestParam("fid")String fid){
		logger.info("YSystemconfigurationEncodingrules  findEncById");
		Map<String, Object> map = new HashMap<String,Object>();
		YSystemconfigurationEncodingrules obj = null;
		try {
			obj = encService.getById(fid);
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
	@RequestMapping("/findAllEnc")
	@ResponseBody
	@SystemControllerLog(description = "查询全部编码规则")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllEnc(@RequestParam(value="searchName", required=false)String searchName,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit){
		logger.info("YSystemconfigurationEncodingrules  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		PagingList<YSystemconfigurationEncodingrules> list = null;
		try {
			list = encService.findAll(searchName,start,limit);
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
	@RequestMapping("/updateEnc")
	@ResponseBody
	@SystemControllerLog(description = "修改编码规则")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateEnc(@ModelAttribute("form")YSystemconfigurationEncodingrules form){
		logger.info("update YSystemconfigurationEncodingrules");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			encService.update(form);
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
	 * 审核、反审核、生效和失效操作
	 * @param fid
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateEncStatus")
	@ResponseBody
	@SystemControllerLog(description = "编码规则审核、反审核、生效和失效操作")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateEncStatus(@RequestParam("fid")String fid,@RequestParam("status")int status){
		logger.info("updateIndStatus YSystemconfigurationEncodingrules");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YSystemconfigurationEncodingrules obj = encService.getById(fid);
			obj.setFbillState(status);
			encService.update(obj);
			map.put("msg", "状态修改成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "状态修改失败");
			map.put("success", false);
		}
		return map;
	}
	
}
