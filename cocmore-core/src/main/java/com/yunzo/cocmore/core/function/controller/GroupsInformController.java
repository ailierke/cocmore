package com.yunzo.cocmore.core.function.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinform;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinformrecord;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.IMGSize;
/**
 * 社会团体通知controller
 * @author jiangxing
 *
 */
@Controller
@RequestMapping("/groupsInform")
public class GroupsInformController {
	
	private static final Logger logger = Logger.getLogger(GroupsInformController.class);
	@Resource
	GetNumberService getNumberService;
	@Resource(name="groupsInformService")
	GroupsInformService groupsInformService;
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
	 * @param inform
	 * @return
	 */
	@RequestMapping("/addInform")
	@ResponseBody
	@SystemControllerLog(description = "新增通知")
	public Map<String,Object> addGroupInformInfo(YBasicSocialgroupsinform inform,String[] memberIds,MultipartHttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("addGroupInformInfo start....");
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				inform.setFlogoImage(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		try{
			if(inform.getFnumber()==null||inform.getFnumber().equals("")){
				Map<String, Object> map1 = getNumberService.checkExist("XX-SHTTTZ");
				inform.setFnumber((String)map1.get("serialNumber"));
			}
			groupsInformService.addInform(inform,memberIds);
			getNumberService.addSerialNumber("XX-SHTTTZ");
			map.put("success", true);
			map.put("msg","成功");
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("msg","失败");
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return map;
	}
	/**
	 * 审核、反审核
	 * @param inform
	 * @return
	 */
	@RequestMapping("/auditInform")
	@ResponseBody
	@SystemControllerLog(description = "审核、反审核通知")
	public Map<String,Object> auditGroupInformInfo(YBasicSocialgroupsinform inform){
		
		logger.info("auditGroupInformInfo start....");
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = groupsInformService.auditInform(inform);
		
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	/**
	 *生效、失效
	 * @param inform
	 * @return
	 */
	@RequestMapping("/effectInform")
	@ResponseBody
	@SystemControllerLog(description = "生效、失效通知")
	public Map<String,Object> effectGroupInformInfo(YBasicSocialgroupsinform inform){
		logger.info("effectGroupInformInfo start....");
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			 groupsInformService.effectInform(inform);
			 map.put("success", true);
			map.put("msg","成功");
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg","失败");
			e.printStackTrace();
		}
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
	@SystemControllerLog(description = "作废通知")
	public Map<String, Object> doNotNeedSessionAndSecurity_Invalid(String fid,String fbillState,String comment){
		logger.info("Invalid YBasicSocialgroupsinform");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicSocialgroupsinform inform=new YBasicSocialgroupsinform();
			inform=groupsInformService.getById(fid);
			inform.setFbillState(Integer.parseInt(fbillState));
			inform.setFcomment(comment);
			groupsInformService.update(inform);
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
	 * @param inform
	 * @return
	 */
	@RequestMapping("/deleteInform")
	@ResponseBody
	@SystemControllerLog(description = "删除通知")
	public Map<String,Object> deleteInform(YBasicSocialgroupsinform inform){
		Map<String,Object> map = new HashMap<String,Object>();
		groupsInformService.deleteInform(inform);
		map.put("success",true);
		map.put("msg","成功");
		return map;
	}
	/**
	 * 查询列表
	 * @param inform
	 * @param start 查询第几条
	 * @param limit 每页显示条数
	 * @return
	 */
	@RequestMapping("/findAllInformPagingList")
	@ResponseBody
	@SystemControllerLog(description = "查询通知列表，并分页")
	public Map<String,Object> findAllInform(Integer start,Integer limit,String groupId,String fheadline){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 查询页面显示和总条数
		 */
		PagingList<YBasicSocialgroupsinform> pageList = new PagingList<YBasicSocialgroupsinform>();
		if(groupId!=null&&!"".equals(groupId)){
			pageList = groupsInformService.getAllInformPagingList(start, limit,groupId,fheadline);
		}
		map.put("success", true);
		map.put("msg","成功");
		map.put("count",pageList.getCount());//总条数
		map.put("list", pageList.getList());//分页类容
		return map;
	}
	
	/**
	 * 查询列表
	 * @param inform
	 * @param start 查询第几条
	 * @param limit 每页显示条数
	 * @return
	 */
	@RequestMapping("/findAllInformPagingList1")
	@ResponseBody
	@SystemControllerLog(description = "查询通知列表，并分页")
	public Map<String,Object> findAllInform1(Integer start,Integer limit,Integer type){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 查询页面显示和总条数
		 */
		PagingList<YBasicSocialgroupsinform> pageList = new PagingList<YBasicSocialgroupsinform>();
			pageList = groupsInformService.getAllInformPagingList(start, limit,type);
		map.put("success", true);
		map.put("msg","成功");
		map.put("count",pageList.getCount());//总条数
		map.put("list", pageList.getList());//分页类容
		return map;
	}
	/**
	 * 修改
	 * @param inform
	 * @return
	 */
	@RequestMapping("/updateInform")
	@ResponseBody
	@SystemControllerLog(description = "修改通知")
	public Map<String,Object> updateInform(YBasicSocialgroupsinform inform,MultipartHttpServletRequest request,String[] memberIds){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				inform.setFlogoImage(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		try{
			 groupsInformService.deleteInformRecord(inform);
			 groupsInformService.updateInform(inform,memberIds,request);
			 map.put("success", true);
			map.put("msg","成功");
		}catch(Exception e){
			map.put("success", false);
			map.put("msg","失败");
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 获取参加记录
	 * @param inform
	 * @return
	 */
	@RequestMapping("/getGroupsinformRecords")
	@ResponseBody
	@SystemControllerLog(description = "获取参加记录")
	public Map<String,Object> getGroupsinformRecords(YBasicSocialgroupsinform inform){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
		   List<YBasicSocialgroupsinformrecord> recordList= groupsInformService.getGroupsinformRecords(inform);
		    map.put("object",recordList);
			map.put("success", true);
			map.put("msg","成功");
		}catch(Exception e){
			map.put("success", false);
			map.put("msg","失败");
		}
		
		return map;
	}
	
	/**
	 * 获取回复
	 * @param inform
	 * @return
	 */
	@RequestMapping("/findInform")
	@ResponseBody
	@SystemControllerLog(description = "获取回复")
	public Map<String,Object> findInform(YBasicSocialgroupsinform inform){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
		YBasicSocialgroupsinform inform1= (YBasicSocialgroupsinform)groupsInformService.findInform(inform.getFid());
			map.put("object", inform1);
			map.put("success", true);
			map.put("msg","成功");
		}catch(Exception e){
			map.put("success", false);
			map.put("msg","失败");
		}
		
		return map;
	}
	
	/**
	 * 后台通知详情页面加上点赞数和不赞同数
	 * 
	 */
	@RequestMapping("/findPointLikeAndNotLike")
	@ResponseBody
	@SystemControllerLog(description = "后台通知详情页面加上点赞数和不赞同数")
	public Map<String,Object> findPointLikeAndNotLike(String informId){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
		ArrayList<Integer> pointLikeOrNotLikeList= (ArrayList<Integer>)groupsInformService.findPointLikeAndNotLike(informId);
			map.put("pointLike", pointLikeOrNotLikeList.get(0));//点赞数
			map.put("notLike", pointLikeOrNotLikeList.get(1));//不赞同数
			map.put("success", true); 
			map.put("msg","成功");
		}catch(Exception e){
			map.put("success", false);
			map.put("msg","失败");
		}
		return map;
	}
}
