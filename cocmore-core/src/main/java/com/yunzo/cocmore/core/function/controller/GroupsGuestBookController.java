package com.yunzo.cocmore.core.function.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdynamic;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsguestbook;
import com.yunzo.cocmore.core.function.service.GroupsGuestBookService;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 社会团体留言controller
 * @author jiangxing
 *
 */
@Controller
@RequestMapping("/groupsGuestbook")
public class GroupsGuestBookController {
	
	private static final Logger logger = Logger.getLogger(GroupsGuestBookController.class);
	
	@Resource(name="groupsGuestbookService")
	GroupsGuestBookService groupsGuestbookService;
	/**
	 * 新增
	 * @param guestbook
	 * @return
	 */
	@RequestMapping("/addGuestbook")
	@ResponseBody
	@SystemControllerLog(description = "新增留言")
	public Map<String,Object> addGroupGuestbookInfo(YBasicSocialgroupsguestbook guestbook){
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("addGroupGuestbookInfo start....");
		boolean flag = groupsGuestbookService.addGuestbook(guestbook);
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	/**
	 * 查询总列表或者根据创建人来查询
	 * @param createUserId 创建人Id
	 * @param start 查询第几条
	 * @param limit 每页显示条数
	 * @return
	 */
	@RequestMapping("/findAllGuestbookPagingList")
	@ResponseBody
	@SystemControllerLog(description = "查询总列表或者根据创建人来查询")
	public Map<String,Object> findAllGuestbookByCreateUser(Integer start,Integer limit,String createUserName,String groupId){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 查询页面显示和总条数
		 */
		PagingList<YBasicSocialgroupsguestbook> pageList = new PagingList<YBasicSocialgroupsguestbook>();
		if(groupId!=null&&!"".equals(groupId)){
			pageList = groupsGuestbookService.getAllGuestbookPagingList(start, limit, createUserName, groupId);
		}
		map.put("success", true);
		map.put("msg","成功");
		map.put("count",pageList.getCount());//总条数
		map.put("list", pageList.getList());//分页类容
		return map;
	}
}
