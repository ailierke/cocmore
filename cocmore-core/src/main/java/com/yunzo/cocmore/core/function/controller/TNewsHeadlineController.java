package com.yunzo.cocmore.core.function.controller;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.TNewsCollect;
import com.yunzo.cocmore.core.function.model.mysql.TNewsHeadline;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.service.CommentService;
import com.yunzo.cocmore.core.function.service.TNewsHeadlineService;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.IMGSize;

@Controller
@RequestMapping("/tnewsHeadlineController")
public class TNewsHeadlineController {
private static final Logger logger = Logger.getLogger(TNewsHeadlineController.class);
	
	@Resource(name = "newsHeadlineService")
	private TNewsHeadlineService newsHeadlineService;
	
	@Resource(name = "commentService")
	private CommentService commentService;
	
	
	//此处的参数也可以是ServletRequestDataBinder类型 
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {  
	    //注册自定义的属性编辑器 (日期)  
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
	    //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
	    binder.registerCustomEditor(Date.class, dateEditor);     
	}
		
	@RequestMapping("/findNewsHeadlineId")
	@ResponseBody
	@SystemControllerLog(description =" 查询headline")
	public Map<String, Object> findNewsHeadlineId(String id){
		Map<String, Object> map = new HashMap<String, Object>();
		TNewsHeadline newsHeadline = null;
		try {
			newsHeadline = newsHeadlineService.getById(id);
			if(newsHeadline != null){
				map.put("obj", newsHeadline);
				map.put("msg", "成功");
				map.put("success", true);
			}else{
				map.put("obj", newsHeadline);
				map.put("msg", "数据不存在");
				map.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/newsHeadlineSave")
	@ResponseBody
	@SystemControllerLog(description =" TNewsHeadlineService save")
	public Map<String, Object> save(TNewsHeadline newsHeadline,MultipartHttpServletRequest request){
		logger.info("TNewsHeadlineService save");
		Map<String, Object> map = new HashMap<String,Object>();
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				newsHeadline.setFimageUrl(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		try {
			newsHeadlineService.save(newsHeadline);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping("/newsHeadlineUpdate")
	@ResponseBody
	@SystemControllerLog(description ="TNewsHeadlineService update")
	public Map<String, Object> update(@ModelAttribute("form")TNewsHeadline newsHeadline,MultipartHttpServletRequest request){
		logger.info("TNewsHeadlineService update");
		Map<String, Object> map = new HashMap<String,Object>();
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				newsHeadline.setFimageUrl(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		try {
			newsHeadlineService.update(newsHeadline);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping("/delete")
	@ResponseBody
	@SystemControllerLog(description =" TNewsHeadlineService delete")
	public Map<String, Object> delete(String[] ftids){
		logger.info("TNewsHeadlineService delete");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			for(String id:ftids){
				TNewsHeadline newsHeadline=newsHeadlineService.getById(id);
				newsHeadlineService.delete(newsHeadline);
			}
			
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping("/findAll")
	@ResponseBody
	@SystemControllerLog(description =" TNewsHeadlineService findAll")
	public Map<String, Object> findAll(){
		logger.info("TNewsHeadlineService findAll");
		Map<String, Object> map = new HashMap<String,Object>();
		List<TNewsHeadline> list=null;
		try {
			list=newsHeadlineService.findAll();
			map.put("msg", "成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping("/getAllDynamicPagingList")
	@ResponseBody
	@SystemControllerLog(description =" TNewsHeadlineService getAllDynamicPagingList")
	public Map<String, Object> getAllDynamicPagingList(String fheadline,Integer start,Integer limit,String zsxs,String xwfl,String publicTime){
		logger.info("TNewsHeadlineService getAllDynamicPagingList");
		//store.load({params:{fheadline:searchCondition,start:0,limit:LIMIT,xwfl:xwfl,zsxs:zsxs,publicTime:publicTime}});
		PagingList<TNewsHeadline> pageList =null;
		Map<String, Object> map = new HashMap<String,Object>();
		
		try {
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(null==publicTime){
				pageList = newsHeadlineService.getAllDynamicPagingList(fheadline, start,limit,zsxs,xwfl,null);
			}else{
				String time=publicTime.replaceAll("T00:00:00", "");
				//Date date = sdf.parse(time);
				pageList = newsHeadlineService.getAllDynamicPagingList(fheadline, start,limit,zsxs,xwfl,time);
			}
			
			map.put("msg","成功");
			map.put("obj", pageList);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
		
	}
	@RequestMapping("/getAllDynamicPagingListByComment")
	@ResponseBody
	@SystemControllerLog(description =" commentService getAllDynamicPagingListByComment")
	public Map<String, Object> getAllDynamicPagingListByComment(String ftid,Integer start,Integer limit){
		logger.info("commentService getAllDynamicPagingListByComment");
		PagingList<YComment> pageList =null;
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			pageList = commentService.getAllDynamicPagingList(ftid, start,limit);
			map.put("msg","成功");
			map.put("obj", pageList);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;	
	} 
	
	@RequestMapping("/deleteByComment")
	@ResponseBody
	@SystemControllerLog(description =" commentService delete")
	public Map<String, Object> deleteByComment(String[] fids){
		logger.info("commentService delete");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			for(String id:fids){
				YComment comment=commentService.getById(id);
				commentService.delete(comment);
			}
			
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
}
