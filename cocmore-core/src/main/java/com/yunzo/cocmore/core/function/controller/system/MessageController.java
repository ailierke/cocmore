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
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.service.MessageServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <系统消息服务控制层>. <br>
 * @date:2014年11月27日 下午4:29:26
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/msg")
public class MessageController {
private static final Logger logger = Logger.getLogger(MessageController.class);
	
	@Resource(name = "msgService")
	private MessageServiceI msgService;
	
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
	@RequestMapping("/findMsgById")
	@ResponseBody
	@SystemControllerLog(description = "根据ID查询系统消息")
	public Map<String, Object> doNotNeedSessionAndSecurity_findMsgById(@RequestParam("fid")String fid){
		logger.info("YSystemconfigurationMessage  findMsgById");
		Map<String, Object> map = new HashMap<String,Object>();
		YSystemconfigurationMessage obj = null;
		try {
			obj = msgService.getById(fid);
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
	@RequestMapping("/findAllMsg")
	@ResponseBody
	@SystemControllerLog(description = "查询全部系统消息")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllMsg(@RequestParam(value="searchName", required=false)String searchName,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit){
		logger.info("YSystemconfigurationMessage  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		PagingList<YSystemconfigurationMessage> list = null;
		try {
			list = msgService.findAll(searchName,start,limit);
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
	 * 保存
	 * @param form
	 * @return
	 */
	@RequestMapping("/saveMsg")
	@ResponseBody
	@SystemControllerLog(description = "保存系统消息")
	public Map<String, Object> doNotNeedSessionAndSecurity_saveMsg(@ModelAttribute("form")YSystemconfigurationMessage form){
		logger.info("save YSystemconfigurationMessage");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			msgService.save(form);
			map.put("msg", "保存成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "保存失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@RequestMapping("/deleteMsg")
	@ResponseBody
	@SystemControllerLog(description = "删除系统消息")
	public Map<String, Object> doNotNeedSessionAndSecurity_deleteMsg(String fid){
		logger.info("delete YSystemconfigurationMessage");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YSystemconfigurationMessage obj = msgService.getById(fid);
			msgService.delete(obj);
			map.put("msg", "删除成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "删除失败");
			map.put("success", false);
		}
		return map;
	}
}
