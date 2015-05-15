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
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupscontact;
import com.yunzo.cocmore.core.function.service.GroupsContactService;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 社会团体关于controller
 * @author jiangxing
 *
 */
@Controller
@RequestMapping("/groupsContact")
public class GroupsContactController {
	
	private static final Logger logger = Logger.getLogger(GroupsContactController.class);
	
	@Resource(name="groupsContactService")
	GroupsContactService groupsContactService;
	
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
	 * @param contact
	 * @return
	 */
	@RequestMapping("/addContact")
	@ResponseBody
	@SystemControllerLog(description = "新增联系我")
	public Map<String,Object> addGroupContact(YBasicSocialgroupscontact contact){
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("addGroupContact start....");
		boolean flag = groupsContactService.addGroupsContact(contact);
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	/**
	 * 查询列表
	 * @param contact
	 * @param start 查询第几条
	 * @param limit 每页显示条数
	 * @return
	 */
	@RequestMapping("/findAllContactPagingList")
	@ResponseBody
	@SystemControllerLog(description = "查询联系我列表")
	public Map<String,Object> findAllContact(Integer start,Integer limit,String groupId){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 查询页面显示和总条数
		 */
		PagingList<YBasicSocialgroupscontact> pageList = new PagingList<YBasicSocialgroupscontact>();
		if(groupId!=null&&!"".equals(groupId)){
			pageList = groupsContactService.getAllGroupsPagingList(start, limit,groupId);
		}
		map.put("success", true);
		map.put("msg","成功");
		map.put("count",pageList.getCount());//总条数
		if(null!= pageList.getList()&& pageList.getList().size()>0){
			map.put("list", pageList.getList().get(0));//分页类容
		}else{
			map.put("list", "");
		}
		return map;
	}
	/**
	 * 修改
	 * @param contact
	 * @return
	 */
	@RequestMapping("/updateContact")
	@ResponseBody
	@SystemControllerLog(description = "修改联系我")
	public Map<String,Object> updateContact(YBasicSocialgroupscontact contact){
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("************"+contact.getUpdateTime());
		boolean flag = groupsContactService.updateGroupsContact(contact);
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	
	/**
	 * 删除
	 * @param contact
	 * @return
	 */
	@RequestMapping("/deleteContact")
	@ResponseBody
	@SystemControllerLog(description = "删除联系我")
	public Map<String,Object> deleteContact(YBasicSocialgroupscontact contact){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = groupsContactService.deleteGroupsContact(contact);
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
}
