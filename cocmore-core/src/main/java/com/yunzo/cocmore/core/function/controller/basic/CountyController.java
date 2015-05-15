package com.yunzo.cocmore.core.function.controller.basic;

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
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.service.CountyService;

/**
 * @author：jackpeng
 * @date：2014年12月2日上午10:22:54
 * 区县的contoller类
 */
@Controller
@RequestMapping("/county")
public class CountyController {
	private static final Logger logger = Logger
			.getLogger(CountyController.class);
	
	@Resource(name = "countyService")
	private CountyService countyService;
	
	/**
	 * 查询全部
	 * @return
	 */
	@RequestMapping("/findAllCounty")
	@ResponseBody
	@SystemControllerLog(description=" 查询全部County")
	public Map<String, Object> findAllCounty(){
		logger.info("find YBasicCounty");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicCounty> list = null;
		try {
			list = countyService.findAll();
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
	 * 根据城市id查询区县
	 * @param id
	 * @return
	 */
	@RequestMapping("/findCountyHql")
	@ResponseBody
	@SystemControllerLog(description=" 根据城市id查询区县")
	public Map<String, Object> findCountyHql(@RequestParam("id")String id){
		logger.info("find YBasicCounty hql");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicCounty> list = null;
		String hql = null;
		try {
			hql = "from YBasicCounty y where y.YBasicCity.fid = '"+id+"'";
			list = countyService.getByHql(hql);
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
