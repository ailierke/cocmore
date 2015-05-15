package com.yunzo.cocmore.core.function.controller.group;

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
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.service.AssurancecontentService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月25日下午6:13:13
 * 供应担保内容controller类
 */
@Controller
@RequestMapping("/content")
public class AssurancecontentController {
	
	private static final Logger logger = Logger.getLogger(AssurancecontentController.class);
	
	@Resource(name = "contentService")
	private AssurancecontentService contentService;
	
	//此处的参数也可以是ServletRequestDataBinder类型 
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {  
	    //注册自定义的属性编辑器 (日期)  
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
	    //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
	    binder.registerCustomEditor(Date.class, dateEditor);     
	}
	
	/**
	 * 查询全部担保内容并分页
	 * @param start
	 * @param limit
	 * @param groupId
	 * @param content
	 * @return
	 */
	@RequestMapping("findContentPage")
	@ResponseBody
	@SystemControllerLog(description = "查询全部担保内容并分页")
	public Map<String, Object> findContentPage(Integer start,Integer limit,String groupId,String content){
		logger.info("find YBasicAssurancecontent");
		Map<String, Object> map = new HashMap<String, Object>();
		//查询页面现显示和总条数
		PagingList<YBasicAssurancecontent> pagingList = null;
		try {
			pagingList = contentService.getAllControllerPagingList(start, limit, groupId, content);
			map.put("count", pagingList.getCount());//总条数
			map.put("list", pagingList.getList());//每页显示数据
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
	 * 查询全部供应担保内容
	 * @return
	 */
	@RequestMapping("/findAll")
	@ResponseBody
	@SystemControllerLog(description = "查询全部供应担保内容")
	public Map<String, Object> findAllContent(){
		logger.info("find YBasicAssurancecontent");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicAssurancecontent> list = null;
		try {
			list = contentService.findAll();
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
	 * 根据id查询供应担保内容
	 * @param id
	 * @return
	 */
	@RequestMapping("/findContentId")
	@ResponseBody
	@SystemControllerLog(description = "根据id查询供应担保内容")
	public Map<String, Object> findContentId(@RequestParam("id")String id){
		logger.info("find YBasicAssurancecontent fid==" + id);
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicAssurancecontent content = null;
		try {
			content = contentService.getById(id);
			map.put("obj", content);
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
	 * 新增供应担保内容
	 * @param content
	 * @return
	 */
	@RequestMapping("/saveContent")
	@ResponseBody
	@SystemControllerLog(description = "新增供应担保内容")
	public Map<String, Object> saveContent(@ModelAttribute("content")YBasicAssurancecontent content){
		logger.info("save YBasicAssurancecontent");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			contentService.save(content);
			map.put("msg", "新增成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "新增失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 删除供应担保内容
	 * @param content
	 * @return
	 */
	@RequestMapping("/deleteContent")
	@ResponseBody
	@SystemControllerLog(description = "删除供应担保内容")
	public Map<String, Object> deleteContent(@RequestParam("fids")String fids){
		logger.info("delete YBasicAssurancecontent");
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicAssurancecontent content = null;
		try {
			for(String id:fids.split(",")){
				content = contentService.getById(id);
				contentService.delete(content);
			}
			map.put("msg", "删除成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "删除失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 修改供应担保内容
	 * @param content
	 * @return
	 */
	@RequestMapping("/updateContent")
	@ResponseBody
	@SystemControllerLog(description = "修改供应担保内容")
	public Map<String, Object> updateContent(@ModelAttribute("content")YBasicAssurancecontent content){
		logger.info("update YBasicAssurancecontent");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			contentService.update(content);
			map.put("msg", "修改成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败！");
			map.put("success", false);
		}
		return map;
	}
}
