package com.yunzo.cocmore.core.function.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.model.mysql.YImage;
import com.yunzo.cocmore.core.function.model.mysql.YManshowinformation;
import com.yunzo.cocmore.core.function.service.CommentService;
import com.yunzo.cocmore.core.function.service.ManshowinformationService;
import com.yunzo.cocmore.core.function.util.PagingList;


/**
 * 个人秀
 * @author yunzo
 *
 */
@Controller
@RequestMapping("/manshow")
public class ManShowController {
	private static final Logger logger = Logger.getLogger(ManShowController.class);
	
	
	@Resource(name = "ManshowinformationService")
	private ManshowinformationService ManshowinformationService;
	
	@Resource
	CommentService commentService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {  
	    //注册自定义的属性编辑器 (日期)  
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
	    //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
	    binder.registerCustomEditor(Date.class, dateEditor);     
	}
	
	@ResponseBody
	@RequestMapping("/findAllDynamicPagingList")
	@SystemControllerLog(description =" 查询findAllDynamic")
	public Map<String,Object> findAllDynamic(Integer start,Integer limit,String groupId,String memberId){
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<YManshowinformation> pageList=new ArrayList<YManshowinformation>();
		if(null!=groupId&&!"".equals(groupId)){
			pageList=ManshowinformationService.getAllDynamicPagingList(start, limit, groupId, memberId);
			map.put("success", true);
			map.put("msg","成功");
			map.put("count",pageList.size());//总条数
			map.put("list", pageList);//分页类容
		}else{
			map.put("success", false);
			map.put("msg","数据出错");
		}
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	@SystemControllerLog(description =" 删除manshow")
	public Map<String,Object> delete(String[] fids){
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(fids.length>0){
			for(String fid:fids){
				YManshowinformation show=ManshowinformationService.getById(fid);
				ManshowinformationService.delete(show);
			}
			map.put("success", true);
			map.put("msg","成功");
		}else{
			map.put("success", false);
			map.put("msg","数据出错");
		}	
		return map;
	}
	
	
	/**
	 * 删除评论
	 * @param fids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteComment")
	@SystemControllerLog(description ="删除评论")
	public Map<String,Object> deleteComment(String[] fids){
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(fids.length>0){
			for(String fid:fids){
				YComment com=commentService.getById(fid);
				commentService.delete(com);
			}
			map.put("success", true);
			map.put("msg","成功");
		}else{
			map.put("success", false);
			map.put("msg","数据出错");
		}	
		return map;
	}
	/**
	 * 查询图片
	 * @param fids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getimgByshowId")
	@SystemControllerLog(description =" 查询图片")
	public Map<String,Object> getimgByshowId(String showId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			List<YImage> list=new ArrayList<YImage>();
			if(null!=showId&&!"".equals(showId)){
				list=ManshowinformationService.getByshowId(showId);
				map.put("success", true);
				map.put("msg","成功");
				map.put("list", list);
			}else{
				map.put("success", false);
				map.put("msg","数据出错");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg","数据出错");
		}
		return map;
	}
	/**
	 * 查看点赞数量
	 * @param fids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getNumByshowId")
	@SystemControllerLog(description =" 查看点赞数量")
	public Map<String,Object> getNumByshowId(String showId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			
			if(null!=showId&&!"".equals(showId)){
				Integer num=0;
				num=ManshowinformationService.getNumByshowId(showId);
				map.put("success", true);
				map.put("msg","成功");
				map.put("list", num);
			}else{
				map.put("success", false);
				map.put("msg","数据出错");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg","数据出错");
		}
		return map;
	}
}
