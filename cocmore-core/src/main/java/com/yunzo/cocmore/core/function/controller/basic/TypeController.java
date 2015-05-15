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
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.service.TypeService;

/**
 * @author：jackpeng
 * @date：2014年12月13日下午2:48:32
 * 类型controller类
 */
@Controller
@RequestMapping("/type")
public class TypeController {
	private static final Logger logger = Logger
			.getLogger(CityController.class);
	@Resource(name = "typeService")
	private TypeService typeService;
	
	@RequestMapping("/findTypeHql")
	@ResponseBody
	@SystemControllerLog(description="/findTypeHql")
	public Map<String, Object> findTypeHql(String fmodelId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicType> list = null;
		String hql = null;
		try {
			hql = "from YBasicType y where y.fmoduleId = '"+fmodelId+"' and fynexhibition = '1'";
			list = typeService.getTypeHql(hql);
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
