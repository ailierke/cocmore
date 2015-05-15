package com.yunzo.cocmore.core.function.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsabout;
import com.yunzo.cocmore.core.function.service.GroupsAboutService;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 社会团体关于controller
 * @author jiangxing
 *
 */
@Controller
@RequestMapping("/groupsAbout")
public class GroupsAboutController {
	
	private static final Logger logger = Logger.getLogger(GroupsAboutController.class);
	
	@Resource(name="groupsAboutService")
	GroupsAboutService groupsAboutService;
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {  
	    //注册自定义的属性编辑器 (日期)  
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
	    //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
	    binder.registerCustomEditor(Date.class, dateEditor);     
	}
	/**
	 * 新增
	 * @param about
	 * @return
	 */
	@RequestMapping("/addAbout")
	@ResponseBody
	@SystemControllerLog(description = "新增关于我")
	public Map<String,Object> addGroupAbout(YBasicSocialgroupsabout about){
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("addGroupAbout start....");
		boolean flag = groupsAboutService.addGroupsAbout(about);
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	/**
	 * 查询列表
	 * @param about
	 * @param start 查询第几页页数-1
	 * @param limit 每页显示条数
	 * @return
	 */
	@RequestMapping("/findAllAboutPagingList")
	@ResponseBody
	@SystemControllerLog(description = "查询全部关于我，并分页")
	public Map<String,Object> findAllAbout(Integer start,Integer limit,String groupId){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 查询页面显示和总条数
		 */
		
		PagingList<YBasicSocialgroupsabout> pageList = groupsAboutService.getAllGroupsPagingList(start, limit,groupId);
		System.out.println(pageList);
		map.put("success", true);
		map.put("msg","成功");
		map.put("count",pageList.getCount());//总条数
		map.put("list", pageList.getList());//分页类容
		return map;
	}
	/**
	 * 修改
	 * @param about
	 * @return
	 */
	@RequestMapping("/updateAbout")
	@ResponseBody
	@SystemControllerLog(description = "修改关于我")
	public Map<String,Object> updateAbout(YBasicSocialgroupsabout about){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = groupsAboutService.updateGroupsAbout(about);
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	
	/**
	 * 删除
	 * @param about
	 * @return
	 */
	@RequestMapping("/deleteAbout")
	@ResponseBody
	@SystemControllerLog(description = "删除关于我")
	public Map<String,Object> deleteAbout(YBasicSocialgroupsabout about){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = groupsAboutService.deleteGroupsAbout(about);
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	/**
	 * 根据id查询对象
	 * @param about
	 * @return
	 */
	@RequestMapping("/getByAboutId")
	@ResponseBody
	@SystemControllerLog(description = "根据id查询关于我")
	public Map<String,Object> getByAboutId(String  id){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			YBasicSocialgroupsabout about=new YBasicSocialgroupsabout();
			about  = groupsAboutService.getByAboutId(id);
			map.put("Object", about);
			map.put("success", true);
			map.put("msg","成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg","失败");
		}
		
		return map;
	}
}
