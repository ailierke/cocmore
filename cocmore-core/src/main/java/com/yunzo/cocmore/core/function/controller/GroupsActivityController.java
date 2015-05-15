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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsactivity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.GroupsActivityService;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.IMGSize;
/**
 * 社会团体活动controller
 * @author jiangxing
 *
 */
@Controller
@RequestMapping("/groupsActivity")
public class GroupsActivityController {
	
	private static final Logger logger = Logger.getLogger(GroupsActivityController.class);
	@Resource
	GetNumberService getNumberService;
	@Resource(name="groupsActivityService")
	GroupsActivityService groupsActivityService;
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {  
	    //注册自定义的属性编辑器 (日期)  
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
	    //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
	    binder.registerCustomEditor(Date.class, dateEditor);     
	}
	
	/**
	 * 根据id查询活动
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	@SystemControllerLog(description = "根据id查询团体活动")
	public Map<String, Object> findById(String id){
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicSocialgroupsactivity groupsActivity = null;
		try {
			groupsActivity = groupsActivityService.getById(id);
			if(groupsActivity != null){
				map.put("obj", groupsActivity);
				map.put("msg", "查询成功！");
				map.put("success", true);
			}else{
				map.put("obj", null);
				map.put("msg", "查询失败！");
				map.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 新增
	 * @param activity
	 * @return
	 */
	@RequestMapping("/addActivity")
	@ResponseBody
	@SystemControllerLog(description = "新增团体活动")
	public Map<String,Object> addGroupActivityInfo(YBasicSocialgroupsactivity activity,MultipartHttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("addGroupActivityInfo start....");
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				activity.setFimages(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		if(activity.getFnumber()==null||activity.getFnumber().equals("")){
			Map<String, Object> map1 = getNumberService.checkExist("XX-SHTTHD");
			activity.setFnumber((String)map1.get("serialNumber"));
		}
		boolean flag = groupsActivityService.addActivity(activity);
		if(flag==true){
			getNumberService.addSerialNumber("XX-SHTTHD");
		}
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	/**
	 * 审核、反审核
	 * @param activity
	 * @return
	 */
	@RequestMapping("/auditActivity")
	@ResponseBody
	@SystemControllerLog(description = "审核、反审核团体活动")
	public Map<String,Object> auditGroupActivityInfo(YBasicSocialgroupsactivity activity){
		
		logger.info("auditGroupActivityInfo start....");
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = groupsActivityService.auditActivity(activity);
		
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	/**
	 *生效、失效
	 * @param activity
	 * @return
	 */
	@RequestMapping("/effectActivity")
	@ResponseBody
	@SystemControllerLog(description = "生效，失效团体活动")
	public Map<String,Object> effectGroupActivityInfo(YBasicSocialgroupsactivity activity){
		logger.info("effectGroupActivityInfo start....");
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = groupsActivityService.effectActivity(activity);
		
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	
	/**
	 * 作废
	 * @param fid
	 * @param fbillState
	 * @param comment
	 * @return
	 */
	@RequestMapping("/Invalid")
	@ResponseBody
	@SystemControllerLog(description = "作废团体活动")
	public Map<String, Object> doNotNeedSessionAndSecurity_Invalid(String fid,String fbillState,String comment){
		logger.info("Invalid YBasicSocialgroupsactivity");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicSocialgroupsactivity activity=new YBasicSocialgroupsactivity();
			activity=groupsActivityService.getById(fid);
			activity.setFbillState(Integer.parseInt(fbillState));
			activity.setFcomment(comment);
			groupsActivityService.update(activity);
			map.put("msg", "作废成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "作废失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 删除
	 * @param activity
	 * @return
	 */
	@RequestMapping("/deleteActivity")
	@ResponseBody
	@SystemControllerLog(description = "删除团体活动")
	public Map<String,Object> deleteActivity(YBasicSocialgroupsactivity activity){
		Map<String,Object> map = new HashMap<String,Object>();
		groupsActivityService.deleteActivity(activity);
		map.put("success",true);
		map.put("msg","成功");
		return map;
	}
	/**
	 * 查询列表
	 * @param activity
	 * @param start 查询第几页页数-1  extjs传来的
	 * @param limit 每页显示条数
	 * @return
	 */
	@RequestMapping("/findAllActivityPagingList")
	@ResponseBody
	@SystemControllerLog(description = "查询所有团体活动，并分页")
	public Map<String,Object> findAllActivity(Integer start,Integer limit,String groupId,String fheadline){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 查询页面显示和总条数
		 */
		PagingList<YBasicSocialgroupsactivity> pageList = new PagingList<YBasicSocialgroupsactivity>();
		if(groupId!=null&&!"".equals(groupId)){
			pageList = groupsActivityService.getAllActivityPagingList(start, limit,groupId,fheadline);
		}
		map.put("success", true);
		map.put("msg","成功");
		map.put("count",pageList.getCount());//总条数
		map.put("list", pageList.getList());//分页类容
		return map;
	}
	/**
	 * 修改
	 * @param activity
	 * @return
	 */
	@RequestMapping("/updateActivity")
	@ResponseBody
	@SystemControllerLog(description = "修改团体活动")
	public Map<String,Object> updateActivity(YBasicSocialgroupsactivity activity,MultipartHttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				activity.setFimages(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		boolean flag = groupsActivityService.updateActivity(activity);
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
}
