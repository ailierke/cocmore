package com.yunzo.cocmore.core.function.controller;

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
import com.yunzo.cocmore.core.function.model.mysql.YFunctionentriesFunctionjournalentry;
import com.yunzo.cocmore.core.function.service.YFunctionentriesFunctionjournalentryService;



/**
 * @ClassName: YFunctionentriesFunctionjournalentryController 
 * @Description: TODO 功能Controller 
 * @date 2014年11月24日 下午6:11:24 
 * @author Ian
 *
 */
@Controller
@RequestMapping("/functionentriesFunctionjournalentry")
public class YFunctionentriesFunctionjournalentryController {
private static final Logger logger = Logger.getLogger(YFunctionentriesFunctionjournalentryController.class);
	
	@Resource(name = "functionentriesFunctionjournalentryService")
	private YFunctionentriesFunctionjournalentryService functionentriesFunctionjournalentryService;
	
	@RequestMapping(value="/save")
	@ResponseBody
	@SystemControllerLog(description="YFunctionentriesFunctionjournalentryService save")
	public Map<String, Object> save(@ModelAttribute("form")YFunctionentriesFunctionjournalentry functionentriesFunctionjournalentry){
		logger.info("YFunctionentriesFunctionjournalentryService save");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			functionentriesFunctionjournalentryService.save(functionentriesFunctionjournalentry);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping(value="/delete")
	@ResponseBody
	@SystemControllerLog(description="YFunctionentriesFunctionjournalentryService delete")
	public Map<String, Object> delete(@ModelAttribute("form")YFunctionentriesFunctionjournalentry functionentriesFunctionjournalentry){
		logger.info("YFunctionentriesFunctionjournalentryService delete");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			functionentriesFunctionjournalentryService.delete(functionentriesFunctionjournalentry);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping(value="/findByFunctionID")
	@ResponseBody
	@SystemControllerLog(description="YFunctionentriesFunctionjournalentryService findByFunctionID")
	public Map<String, Object> findByFunctionID(@RequestParam("functionId")String functionId){
		logger.info("YFunctionentriesFunctionjournalentryService findByFunctionID");
		List<YFunctionentriesFunctionjournalentry> list=null;
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			list=functionentriesFunctionjournalentryService.findByFunctionID(functionId);
			map.put("msg", "成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败");
			map.put("success", false);
			
		}
		return map;
	}
}
