package com.yunzo.cocmore.core.function.controller.group;

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
import com.yunzo.cocmore.core.function.model.mysql.FAwardSetting;
import com.yunzo.cocmore.core.function.service.SettingService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月26日上午10:27:54
 * 抽奖设置controller类
 */
@Controller
@RequestMapping("/setting")
public class SettingController {
	
	private static final Logger logger = Logger.getLogger(SettingController.class);
	
	@Resource(name = "settingService")
	private SettingService settingService;
	
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
	 * 查询全部并分页
	 * @param start
	 * @param limit
	 * @param activityName
	 * @return
	 */
	@RequestMapping("/findSettingPage")
	@ResponseBody
	@SystemControllerLog(description = "查询全部并分页")
	public Map<String, Object> findSettingPage(Integer start,Integer limit,String activityName,String groupId){
		logger.info("find FAwardSetting");
		Map<String, Object> map = new HashMap<String, Object>();
		PagingList<FAwardSetting> pagingList = null;
		try {
			pagingList = settingService.getAllSettingPagingList(start, limit, activityName,groupId);
			map.put("list", pagingList.getList());
			map.put("count", pagingList.getCount());
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
	 * 查询全部抽奖设置
	 * @return
	 */
	@RequestMapping("/findall")
	@ResponseBody
	@SystemControllerLog(description = "查询全部抽奖设置")
	public Map<String, Object> findAllSetting(){
		logger.info("find FAwardSetting");
		Map<String, Object> map = new HashMap<String, Object>();
		List<FAwardSetting> list = null;
		try {
			list = settingService.findAll();
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
	
	/**
	 * 根据id查询抽奖设置
	 * @param id
	 * @return
	 */
	@RequestMapping("/findSettingId")
	@ResponseBody
	@SystemControllerLog(description = "根据id查询抽奖设置")
	public Map<String, Object> findSettingId(@RequestParam("id")String id){
		logger.info("find FAwardSetting fid=="+id);
		Map<String, Object> map = new HashMap<String, Object>();
		FAwardSetting setting = null;
		try {
			setting = settingService.getById(id);
			map.put("obj", setting);
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
	 * 新增抽奖设置
	 * @param setting
	 * @return
	 */
	@RequestMapping("/saveSetting")
	@ResponseBody
	@SystemControllerLog(description = "新增抽奖设置")
	public Map<String, Object> saveSetting(@ModelAttribute("setting")FAwardSetting setting){
		logger.info("save FAwardSetting");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			settingService.save(setting);
			map.put("msg", "新增成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "新增失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 删除抽奖设置
	 * @param setting
	 * @return
	 */
	@RequestMapping("/deleteSetting")
	@ResponseBody
	@SystemControllerLog(description = "删除抽奖设置")
	public Map<String, Object> deleteSetting(@RequestParam("fids")String fids){
		logger.info("delete FAwardSetting");
		Map<String, Object> map = new HashMap<String, Object>();
		FAwardSetting setting = null;
		try {
			for(String id:fids.split(",")){
				setting = settingService.getById(id);
				settingService.delete(setting);
			}
			map.put("msg", "删除成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "删除失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 修改抽奖设置
	 * @param setting
	 * @return
	 */
	@RequestMapping("/updateSetting")
	@ResponseBody
	@SystemControllerLog(description = "修改抽奖设置")
	public Map<String, Object> updateSetting(@ModelAttribute("setting")FAwardSetting setting){
		logger.info("update FAwardSetting");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			settingService.update(setting);
			map.put("msg", "修改成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败！");
			map.put("success", false);
		}
		return map;
	}
}
