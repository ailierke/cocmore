package com.yunzo.cocmore.core.function.controller.basic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.yunzo.cocmore.core.function.model.mysql.YBasicEmployee;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesEmployeedistribution;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.service.EmployeeServiceI;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.UserService;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.MD5Util;

/** 
 *Description: <职员服务控制层>. <br>
 * @date:2014年11月25日 下午4:52:38
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/emp")
public class EmployeeController {
	private static final Logger logger = Logger.getLogger(EmployeeController.class);
	
	@Resource(name = "empService")
	private EmployeeServiceI empService;
	
	@Resource(name = "getNumberService")
	private GetNumberService getNumberService;
	
	@Resource(name = "userInfo")
	private UserService userService;
	
	//加密前段
	private static final String BEGINSTRING="yunzo";
	//加密结束
	private static final String ENDSTRING="coc";
	
	//此处的参数也可以是ServletRequestDataBinder类型 
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {  
	    //注册自定义的属性编辑器 (日期)  
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
	    //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
	    binder.registerCustomEditor(Date.class, dateEditor);     
	}
	
	@RequestMapping("/findEmpById")
	@ResponseBody
	@SystemControllerLog(description=" 查询全部职员")
	public Map<String, Object> doNotNeedSessionAndSecurity_findEmpById(@RequestParam("fid")String fid){
		logger.info("YBasicEmployee  findOrgById");
		Map<String, Object> map = new HashMap<String,Object>();
		YBasicEmployee obj = null;
		try {
			obj = empService.getById(fid);
			map.put("msg", "查询成功");
			map.put("obj", obj);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 分页查询（默认查询全部）
	 * 在参数 searchName不为空 的情况下，根据搜索名称模糊查询
	 * @param searchName
	 * @return
	 */
	@RequestMapping("/findAllEmp")
	@ResponseBody
	@SystemControllerLog(description=" 按条件查询职员")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllEmp(@RequestParam(value="searchName", required=false)String searchName,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit,
			@RequestParam(value="orgId", required=false)String orgId){
		logger.info("YBasicEmployee  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		PagingList<YBasicEmployee> list = null;
		try {
			list = empService.findAll(orgId,searchName,start,limit);
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
	
	@RequestMapping("/saveEmp")
	@ResponseBody
	@SystemControllerLog(description="保存职员")
	public Map<String, Object> doNotNeedSessionAndSecurity_saveEmp(@ModelAttribute("form")YBasicEmployee form){
		logger.info("save YBasicEmployee");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			empService.save(form);
			map.put("msg", "保存成功");
			map.put("success", true);
			getNumberService.addSerialNumber("JC-ZYGL");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "保存失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/updateEmp")
	@ResponseBody
	@SystemControllerLog(description="更新职员")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateEmp(@ModelAttribute("form")YBasicEmployee form){
		logger.info("update YBasicEmployee");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			empService.update(form);
			map.put("msg", "修改成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 审核、反审核、生效和失效操作
	 * @param fid
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateEmpStatus")
	@ResponseBody
	@SystemControllerLog(description="更新职员状态")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateEmpStatus(@RequestParam("fids")String fids,@RequestParam("status")int status){
		logger.info("updateEmpStatus YBasicEmployee");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			for(String id : fids.split(",")){
				YBasicEmployee obj = empService.getById(id);
				obj.setFbillState(status);
				empService.updateStatus(obj);
			}
			map.put("msg", "状态修改成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "状态修改失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 根据职员ID查询职员职位分配的数据
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findEDByPosID")
	@ResponseBody
	@SystemControllerLog(description="根据职员ID查询职员职位分配的数据")
	public Map<String, Object> doNotNeedSessionAndSecurity_findEDByPosID(String fid){
		logger.info("findEDByPosID YBasicentriesEmployeedistribution");
		Map<String, Object> map = new HashMap<String,Object>();
		List<YBasicentriesEmployeedistribution> list = null;
		try {
			list = empService.findEDByPosID(fid);
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
	 * 将职员关联为管理员
	 * @param fid
	 * @return
	 */
	@RequestMapping("/regAdmin")
	@ResponseBody
	@SystemControllerLog(description="将职员关联为管理员")
	public Map<String, Object> doNotNeedSessionAndSecurity_regAdmin(String fids){
		logger.info("findEDByPosID YBasicentriesEmployeedistribution");
		Map<String, Object> map = new HashMap<String,Object>();
		YSystemUsers user = null;
		YBasicEmployee obj = null;
		try {
			String[] array = fids.split(",");
			for (int i = 0; i < array.length; i++) {
				obj = empService.getById(array[i]);
				user = new YSystemUsers();
				user.setFbillState(5);
				user.setFaccount(obj.getFmobilePhone());
				user.setFuserPassword(MD5Util.md5(BEGINSTRING + "888888"));
				user.setFtypeId("1");
				userService.save(user);
			}
			map.put("msg", "关联成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "关联成功");
			map.put("success", false);
		}
		return map;
	}
}
