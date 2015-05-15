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
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.service.CityService;


/**
 * @author：jackpeng
 * @date：2014年12月2日上午10:07:57
 * 城市controller类
 */
@Controller
@RequestMapping("/city")
public class CityController {
	private static final Logger logger = Logger
			.getLogger(CityController.class);
	
	@Resource(name = "cityService")
	private CityService cityService;
	
	/**
	 * 查询全部
	 * @return
	 */
	@RequestMapping("/findAllCity")
	@ResponseBody
	@SystemControllerLog(description="查询全部城市")
	public Map<String, Object> findAllCity(){
		logger.info("find YBasicCity");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicCity> list = null;
		try {
			list = cityService.findAll();
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
	 * 根据省份id查询城市
	 * @param id
	 * @return
	 */
	@RequestMapping("/findCityHql")
	@ResponseBody
	@SystemControllerLog(description=" 根据省份id查询城市")
	public Map<String, Object> findCityHql(@RequestParam("id")String id){
		logger.info("find YBasicCity hql");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicCity> list = null;
		String hql = null;
		try {
			hql = "from YBasicCity y where y.YBasicProvince.fid='"+id+"'";
			list = cityService.getByHql(hql);
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
