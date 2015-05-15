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
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.service.ProvinceService;

/**
 * @author：jackpeng
 * @date：2014年12月2日上午9:32:37
 * 省份controller类
 */
@Controller
@RequestMapping("/province")
public class ProvinceController {
	
	private static final Logger logger = Logger.getLogger(ProvinceController.class);
	
	@Resource(name = "provinceService")
	private ProvinceService provinceService;
	
	/**
	 * 查询全部
	 * @return
	 */
	@RequestMapping("/findAllProvince")
	@ResponseBody
	@SystemControllerLog(description="查询全部省份")
	public Map<String, Object> findAllProvince(){
		logger.info("find YBasicProvince");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicProvince> list = null;
		try {
			list = provinceService.findAll();
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
	 * 查询区域下边省份
	 * @return
	 */
	@RequestMapping("/findProvinceById")
	@ResponseBody
	@SystemControllerLog(description="查询区域下边省份")
	public Map<String, Object> findProvinceById(String id){
		logger.info("find YBasicProvince by dis");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicProvince> list = null;
		String hql = null;;
		try {
			hql = "from YBasicProvince y where y.YBasicDistrict.fid = '"+id+"'";
			list = provinceService.findByHql(hql);
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
	 * 根据id查询
	 * @param id
	 * @return
	 */
	@RequestMapping("/findProvinceId")
	@ResponseBody
	@SystemControllerLog(description="根据id查询省份")
	public Map<String, Object> findProvinceId(@RequestParam("id")String id){
		logger.info("find YBasicProvince id==" + id);
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicProvince province = null;
		try {
			province = provinceService.getById(id);
			map.put("obj", province);
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
