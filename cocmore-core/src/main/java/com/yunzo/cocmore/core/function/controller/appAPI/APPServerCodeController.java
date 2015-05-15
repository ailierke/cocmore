package com.yunzo.cocmore.core.function.controller.appAPI;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.service.AppSysComLogServiceI;
import com.yunzo.cocmore.core.function.service.AppSysComTypeServiceI;
import com.yunzo.cocmore.core.function.util.ResponseCode;

/**
 * Description: <APP执行代码controller>. <br>
 * @date:2015年3月12日 下午6:58:48
 * @author beck
 * @version V1.0
 */
@Controller
@RequestMapping("/mobileapi/syscommand")
public class APPServerCodeController {
	private static final Logger logger = Logger
			.getLogger(APPServerCodeController.class);
	
	// Json字符串转换为Map
	ObjectMapper mapper = new ObjectMapper();
	
	//手机执行命令记录service
	@Resource(name = "appSCLogService")
	private AppSysComLogServiceI appSCLogService;
	
	//手机执行命令类型service
	@Resource(name = "appSCTypeService")
	private AppSysComTypeServiceI appSCTypeService;
	
	/**
	 * 客户端定时获取执行代码
	 */
	@RequestMapping("/getSysCommandCode")
	@ResponseBody
	public void timerGetCode(String infoMap,
			HttpServletResponse response) throws Exception {
		logger.info("APPServerCodeController timerGetCode");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
			Map<String, Object> result = appSCTypeService.selectAll(jsonObj);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			map.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取执行代码成功后，完成回调
	 */
	@RequestMapping("/setSysCommandCallback")
	@ResponseBody
	public void getByExeCode(String infoMap,
			HttpServletResponse response) throws Exception{
		logger.info("APPServerCodeController getByExeCode");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
			//操作
			appSCLogService.appSave(jsonObj);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
}
