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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YWallsadvertising;
import com.yunzo.cocmore.core.function.service.WallsadvertisingService;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.IMGSize;

/**
 * @author：jackpeng
 * @date：2014年11月27日上午10:09:26
 * 上墙广告controller类
 */
@Controller
@RequestMapping("/sadvertising")
public class WallsadvertisingController {
	
	private static final Logger logger = Logger.getLogger(WallsadvertisingController.class);
	
	@Resource(name = "WallsadvertisingService")
	private WallsadvertisingService WallsadvertisingService;
	
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
	 * 根据上墙主题活动id查询对应的广告
	 * @param start
	 * @param limit
	 * @param activityId
	 * @return
	 */
	@RequestMapping("/findSadvertisingPage")
	@ResponseBody
	@SystemControllerLog(description = "根据上墙主题活动id查询对应的广告")
	public Map<String, Object> findSadvertisingPage(Integer start,Integer limit,String activityId){
		logger.info("find YWallsadvertising");
		Map<String, Object> map = new HashMap<String, Object>();
		PagingList<YWallsadvertising> pagingList = null;
		try {
			pagingList = WallsadvertisingService.getAllSadvertisingPagingList(start, limit, activityId);
			map.put("list", pagingList.getList());
			map.put("count", pagingList.getCount());
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
	 * 根据上墙活动ID查询对应上墙广告
	 * @param id
	 * @return
	 */
	@RequestMapping("/findWallAdHql")
	@ResponseBody
	@SystemControllerLog(description = "根据上墙活动ID查询对应上墙广告")
	public Map<String, Object> findWallAdHql(@RequestParam("activityId")String activityId){
		logger.info("find YWallsadvertising YWallactivity.fid=="+activityId);
		Map<String, Object> map = new HashMap<String, Object>();
		List<YWallsadvertising> list = null;
		String hql = null;
		try {
			hql = "from YWallsadvertising y where y.YWallactivity.fid = '"+activityId+"'";
			list = WallsadvertisingService.getByHql(hql);
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
	 * 新增上墙广告
	 * @param wallAd
	 * @return
	 */
	@RequestMapping("/saveWallAd")
	@ResponseBody
	@SystemControllerLog(description = "新增上墙广告")
	public Map<String, Object> saveWallAd(@ModelAttribute("wallAd")YWallsadvertising wallAd,MultipartHttpServletRequest request){
		logger.info("save YWallsadvertising");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				wallAd.setFimage(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		try {
			WallsadvertisingService.save(wallAd);
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
	 * 删除上墙广告
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteWallAd")
	@ResponseBody
	@SystemControllerLog(description = "删除上墙广告")
	public Map<String, Object> deleteWallAd(@RequestParam("fids")String fids){
		logger.info("delete YWallsadvertising");
		Map<String, Object> map = new HashMap<String, Object>();
		YWallsadvertising wallAd = null;
		try {
			for(String id:fids.split(",")){
				wallAd = WallsadvertisingService.getById(id);
				WallsadvertisingService.delete(wallAd);
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
}
