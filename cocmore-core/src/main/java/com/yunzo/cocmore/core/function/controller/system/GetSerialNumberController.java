package com.yunzo.cocmore.core.function.controller.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.service.GetNumberService;

/** 
 *Description: <系统编码规则服务控制层>. <br>
 * @date:2014年11月27日 下午3:58:12
 * @author ailierke
 * @version V1.0                             
 */
@Controller
@Transactional
@RequestMapping("/serialNumber")
public class GetSerialNumberController {
	private static final Logger logger = Logger.getLogger(GetSerialNumberController.class);
	@Resource
	GetNumberService getNumberService;
	/**
	 * 获取编号
	 * @param prefix
	 * @return
	 */
	@RequestMapping("/getSerialNumber")
	@ResponseBody
	@SystemControllerLog(description = "获取编号")
	public Map<String, Object> getSerialNumber(String prefix){
		Map<String,Object> map= getNumberService.checkExist(prefix);
		return map;
	}
	/**
	 * 增加编码规则中的流水
	 * @param prefix
	 * @return
	 */
	@RequestMapping("/addSerialNumber")
	@ResponseBody
	@SystemControllerLog(description = "增加编码规则中的流水")
	public Map<String, Object> addSerialNumber(String prefix){
		Map<String,Object> map=new HashMap<String,Object>();
		boolean flag = true;
		 try {
			 getNumberService.addSerialNumber(prefix);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		 map.put("success",flag);
		return map;
	}
}
