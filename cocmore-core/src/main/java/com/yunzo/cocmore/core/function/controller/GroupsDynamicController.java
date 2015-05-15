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
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdynamic;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.GroupsDynamicService;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.IMGSize;
/**
 * 社会团体动态controller
 * @author jiangxing
 *
 */
@Controller
@RequestMapping("/groupsDynamic")
public class GroupsDynamicController {
	
	private static final Logger logger = Logger.getLogger(GroupsDynamicController.class);
	@Resource
	GetNumberService getNumberService;
	@Resource(name="groupsDynamicService")
	GroupsDynamicService groupsDynamicService;
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
	 * @param dynamic
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addDynamic")
	@SystemControllerLog(description = "新增动态")
	public void addGroupDynamicInfo(YBasicSocialgroupsdynamic dynamic,MultipartHttpServletRequest request){
		
		logger.info("addGroupDynamicInfo start....");
		if(null!=request){
			Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
			if((boolean)ImgInfoMap.get("success")==true){
				if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
					dynamic.setFlogoImage(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
					logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				}
			}
		}else{
			//do nothing
		}
		
		if(dynamic.getFnumber()==null||dynamic.getFnumber().equals("")){
			Map<String, Object> map = getNumberService.checkExist("XX-SHTTDT");
			dynamic.setFnumber((String)map.get("serialNumber"));
		}
		boolean flag = groupsDynamicService.addDynamic(dynamic);
		if(flag==true){
			getNumberService.addSerialNumber("XX-SHTTDT");
		}
	}
	/**
	 * 审核、反审核
	 * @param dynamic
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/auditDynamic")
	@SystemControllerLog(description = "审核、反审核动态")
	public Map<String,Object> auditGroupDynamicInfo(YBasicSocialgroupsdynamic dynamic){
		logger.info("auditGroupDynamicInfo start....");
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = true;
		try {
			flag = groupsDynamicService.auditDynamic(dynamic);
		} catch (Exception e) {
			flag=false;
			e.printStackTrace();
		} 
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	/**
	 *生效、失效
	 * @param dynamic
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/effectDynamic")
	@SystemControllerLog(description = "生效、失效动态")
	public Map<String,Object> effectGroupDynamicInfo(YBasicSocialgroupsdynamic dynamic){
		logger.info("effectGroupDynamicInfo start....");
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = true;
		try {
			flag = groupsDynamicService.effectDynamic(dynamic);
		} catch (Exception e) {
			flag =false;
			e.printStackTrace();
		}
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
	@SystemControllerLog(description = "作废动态")
	public Map<String, Object> doNotNeedSessionAndSecurity_Invalid(String fid,String fbillState,String comment){
		logger.info("Invalid YBasicSocialgroupsdynamic");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicSocialgroupsdynamic namic=new YBasicSocialgroupsdynamic();
			namic=groupsDynamicService.getById(fid);
			namic.setFbillState(Integer.parseInt(fbillState));
			namic.setFcomment(comment);
			groupsDynamicService.update(namic);
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
	 * @param dynamic
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteDynamic")
	@SystemControllerLog(description = "删除动态")
	public Map<String,Object> deleteDynamic(YBasicSocialgroupsdynamic dynamic){
		Map<String,Object> map = new HashMap<String,Object>();
		
		groupsDynamicService.deleteDynamic(dynamic);
		
		map.put("success",true);
		map.put("msg","成功");
		return map;
	}
	/**
	 * 查询列表
	 * @param dynamic 根据团体id来查
	 * @param start 查询第几条
	 * @param limit 每页显示条数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllDynamicPagingList")
	@SystemControllerLog(description = "查询动态列表，并分页")
	public Map<String,Object> findAllDynamic(Integer start,Integer limit,String groupId,String fheadline){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 查询页面显示和总条数
		 */
		PagingList<YBasicSocialgroupsdynamic> pageList = new PagingList<YBasicSocialgroupsdynamic>();
		if(groupId!=null&&!"".equals(groupId)){
			pageList = groupsDynamicService.getAllDynamicPagingList(start, limit,groupId,fheadline);
		}
		map.put("success", true);
		map.put("msg","成功");
		map.put("count",pageList.getCount());//总条数
		map.put("list", pageList.getList());//分页类容
		return map;
	}
	
	/**
	 * 查询列表
	 * @param dynamic 根据团体id来查
	 * @param start 查询第几条
	 * @param limit 每页显示条数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllDynamicPagingList1")
	@SystemControllerLog(description = "查询动态列表，并分页")
	public Map<String,Object> findAllDynamic1(Integer start,Integer limit,Integer type){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 查询页面显示和总条数
		 */
		PagingList<YBasicSocialgroupsdynamic> pageList = new PagingList<YBasicSocialgroupsdynamic>();
			pageList = groupsDynamicService.getAllDynamicPagingList(start, limit,type);
		map.put("success", true);
		map.put("msg","成功");
		map.put("count",pageList.getCount());//总条数
		map.put("list", pageList.getList());//分页类容
		return map;
	}
	
	/**
	 * 修改
	 * @param dynamic
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateDynamic")
	@SystemControllerLog(description = "修改动态")
	public void updateDynamic(YBasicSocialgroupsdynamic dynamic,MultipartHttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				dynamic.setFlogoImage(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		 groupsDynamicService.updateDynamic(dynamic);
	}
	
	/**
	 * 查询
	 * @param dynamic
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findDynamic")
	@SystemControllerLog(description = "查询单个动态")
	public Map<String,Object> findDynamic(YBasicSocialgroupsdynamic dynamic){
		Map<String,Object> map = new HashMap<String,Object>();
		 
		 map.put("object", groupsDynamicService.getDynamic(dynamic.getFid()));
		 return map;
	}
}
