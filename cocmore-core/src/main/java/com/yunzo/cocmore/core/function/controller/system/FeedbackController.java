package com.yunzo.cocmore.core.function.controller.system;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.yunzo.cocmore.core.function.model.mysql.YBasicFeedback;
import com.yunzo.cocmore.core.function.service.FeedbackServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <意见反馈服务控制层>. <br>
 * @date:2014年12月5日 下午5:49:37
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackController {
	private static final Logger logger = Logger.getLogger(FeedbackController.class);
	
	@Resource(name = "feedbackService")
	private FeedbackServiceI feedbackService;
	
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
	 * 查询全部
	 * @return
	 */
	@RequestMapping("/findAllfeedback")
	@ResponseBody
	@SystemControllerLog(description = "查询全部意见反馈")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllfeedback(@RequestParam(value="searchName", required=false)String searchName,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit){
		logger.info("YBasicFeedback  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		PagingList<YBasicFeedback> list = null;
		try {
			list = feedbackService.findAll(searchName,start,limit);
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
	 * @return
	 */
	@RequestMapping("/savefeedback")
	@ResponseBody
	@SystemControllerLog(description = "保存意见反馈")
	public Map<String, Object> doNotNeedSessionAndSecurity_savefeedback(@ModelAttribute("form")YBasicFeedback form){
		logger.info("YBasicFeedback  savefeedback");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			feedbackService.save(form);
			map.put("msg", "保存成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "保存失败");
			map.put("success", false);
		}
		return map;
	}
}
