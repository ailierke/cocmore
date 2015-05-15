package com.yunzo.cocmore.core.function.controller.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDistrict;
import com.yunzo.cocmore.core.function.model.mysql.YBasicEmployee;
import com.yunzo.cocmore.core.function.service.DistrictServiceI;
import com.yunzo.cocmore.core.function.service.EmployeeServiceI;

/** 
 *Description: <区域服务控制层>. <br>
 * @date:2014年12月2日 下午3:47:30
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/dis")
public class DistrictController {
	private static final Logger logger = Logger.getLogger(DistrictController.class);
	
	@Resource(name = "disService")
	private DistrictServiceI disService;
	
	@RequestMapping("/findAllDis")
	@ResponseBody
	@SystemControllerLog(description=" 查询全部的District")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllDis(){
		logger.info("YBasicDistrict  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		List<YBasicDistrict> list = null;
		try {
			list = disService.findAll();
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
}
