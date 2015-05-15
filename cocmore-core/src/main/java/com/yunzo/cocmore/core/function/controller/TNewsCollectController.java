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
import com.yunzo.cocmore.core.function.model.mysql.TNewsCollect;
import com.yunzo.cocmore.core.function.model.mysql.TNewsHeadline;
import com.yunzo.cocmore.core.function.service.TNewsCollectService;
import com.yunzo.cocmore.core.function.util.PagingList;

@Controller
@RequestMapping("/tnewsCollectController")
public class TNewsCollectController {
private static final Logger logger = Logger.getLogger(TNewsCollectController.class);
	
	@Resource(name = "newsCollectService")
	private TNewsCollectService newsCollectService;
	
	@RequestMapping("/save")
	@ResponseBody
	@SystemControllerLog(description =" TNewsCollectService save")
	public Map<String, Object> save(@ModelAttribute("form")TNewsCollect newsCollect){
		logger.info("TNewsCollectService save");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			newsCollectService.save(newsCollect);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/update")
	@ResponseBody
	@SystemControllerLog(description =" TNewsCollectService update")
	public Map<String, Object> update(@ModelAttribute("form")TNewsCollect newsCollect){
		logger.info("TNewsCollectService update");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			newsCollectService.update(newsCollect);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	@SystemControllerLog(description =" TNewsCollectService delete")
	public Map<String, Object> delete(@ModelAttribute("form")TNewsCollect newsCollect){
		logger.info("TNewsCollectService delete");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			newsCollectService.delete(newsCollect);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/findAll")
	@ResponseBody
	@SystemControllerLog(description =" TNewsCollectService findAll")
	public Map<String, Object> findAll(){
		logger.info("TNewsCollectService findAll");
		Map<String, Object> map = new HashMap<String,Object>();
		List<TNewsCollect> list=null;
		try {
			list=newsCollectService.findAll();
			map.put("msg", "成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/getAllDynamicPagingList")
	@ResponseBody
	@SystemControllerLog(description =" TNewsCollectService getAllDynamicPagingList")
	public Map<String, Object> getAllDynamicPagingList(@RequestParam("page")Integer page,@RequestParam("pageSize")Integer pageSize){
		logger.info("TNewsCollectService getAllDynamicPagingList");
		PagingList<TNewsCollect> pageList =null;
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			pageList = newsCollectService.getAllDynamicPagingList(page, pageSize);
			map.put("msg","成功");
			map.put("obj", pageList);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
}
