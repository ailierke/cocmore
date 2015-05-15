package com.yunzo.cocmore.core.function.controller.group;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicJoinActivity;
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;
import com.yunzo.cocmore.core.function.service.WallActivityService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月26日下午2:23:14
 * 上墙活动controller类
 */
@Controller
@RequestMapping("/wallActivity")
public class WallActivityConTroller {

	private static final Logger logger = Logger.getLogger(WallActivityConTroller.class);

	@Resource(name = "waService")
	private WallActivityService waService;

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
	 * 查询全部供应并分页
	 * @param start
	 * @param limit
	 * @param groupId
	 * @param headline
	 * @return
	 */
	@RequestMapping("/findActivityPage")
	@ResponseBody
	@SystemControllerLog(description = "查询全部供应并分页")
	public Map<String, Object> findActivityPage(Integer start,Integer limit,String groupId,String theme){
		logger.info("find YWallactivity");
		Map<String, Object> map = new HashMap<String, Object>();
		//查询页面显示和总条数
		PagingList<YWallactivity> pageList = null;
		try {
			pageList = waService.getAllActivityPagingList(start, limit, groupId, theme);
			map.put("count",pageList.getCount());//总条数
			map.put("list", pageList.getList());//分页类容
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
	 * 根据社会团体id查询
	 * @param groups
	 * @return
	 */
	@RequestMapping("/findActivityGroupsId")
	@ResponseBody
	@SystemControllerLog(description = "根据社会团体id查询")
	public Map<String, Object> findActivityGroupsId(String groups){
		logger.info("根据社会团体查询     不分页");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YWallactivity> list = null;
		String hql = null;
		try {
			hql = "from YWallactivity y where y.YBasicSocialgroups.fid = '"+groups+"'";
			list = waService.getByHql(hql);
			map.put("obj", list);
			map.put("msg", "查询成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("obj", null);
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return map;
	}

	/**
	 * 根据社会团体id和状态查询
	 * @param groups
	 * @return
	 */
	@RequestMapping("/findActivityGroupsIdAndState")
	@ResponseBody
	@SystemControllerLog(description = "根据社会团体id和状态查询")
	public Map<String, Object> findActivityGroupsIdAndState(String groupsId,String state){
		logger.info("根据社会团体查询     不分页");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YWallactivity> list = null;
		String hql = null;
		try {
			hql = "from YWallactivity y where y.YBasicSocialgroups.fid = '"+groupsId+"' and y.fstate = "+state+"";
			list = waService.getByHql(hql);
			map.put("obj", list);
			map.put("msg", "查询成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("obj", null);
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return map;
	}

	/**
	 * 查询全部上墙活动
	 * @return
	 */
	@RequestMapping("/findall")
	@ResponseBody
	@SystemControllerLog(description = "查询全部上墙活动")
	public Map<String, Object> findAllWallActivity(){
		logger.info("find YWallactivity");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YWallactivity> list = null;
		try {
			list = waService.findAll();
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
	 * 根据id查询上墙活动
	 * @param id
	 * @return
	 */
	@RequestMapping("/findWallActivityId")
	@ResponseBody
	@SystemControllerLog(description = "根据id查询上墙活动")
	public Map<String, Object> findWallActivityId(@RequestParam("id")String id){
		logger.info("find YWallactivity fid=="+id);
		Map<String, Object> map = new HashMap<String, Object>();
		YWallactivity wa = null;
		try {
			wa = waService.getById(id);
			map.put("obj", wa);
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
	 * 根据条件查询上墙活动
	 * @param theme
	 * @param theUrl
	 * @return
	 */
	@RequestMapping("/findWallActivityHql")
	@ResponseBody
	@SystemControllerLog(description = "根据条件查询上墙活动")
	public Map<String, Object> findWallActivityHql(@RequestParam("theme")String theme,@RequestParam("theUrl")String theUrl){
		logger.info("find YWallactivity ftheme=="+theme+"or ftheUrl"+theUrl);
		Map<String, Object> map = new HashMap<String, Object>();
		List<YWallactivity> list = null;
		String hql = null;
		try {
			hql = "from YWallactivity y where y.ftheme like'%"+theme+"%' or y.ftheUrl like '"+theUrl+"'";
			list = waService.getByHql(hql);
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
	 * 修改状态
	 * @param status
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateState")
	@ResponseBody
	@SystemControllerLog(description = "修改上墙活动状态")
	public Map<String, Object> updateState(@RequestParam("status")int status,@RequestParam("fids")String fids){
		logger.info("update YWallactivity State");
		Map<String, Object> map = new HashMap<String, Object>();
		YWallactivity wallActivity = null;
		try {
			for(String id:fids.split(",")){
				wallActivity = waService.getById(id);
				wallActivity.setFstate(status);
				waService.update(wallActivity);
			}
			map.put("msg", "修改成功！");
			map.put("success", true);
		} catch (Exception e) {
			map.put("msg", "修改失败！");
			map.put("success", false);
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
	@SystemControllerLog(description = "上墙活动作废")
	public Map<String, Object> doNotNeedSessionAndSecurity_Invalid(String fid,String fbillState,String comment){
		logger.info("Invalid YWallactivity");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YWallactivity wall=new YWallactivity();
			wall=waService.getById(fid);
			wall.setFstate(Integer.parseInt(fbillState));
			wall.setFcomment(comment);
			waService.update(wall);
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
	 * 新增上墙活动
	 * @param wa
	 * @return
	 */
	@RequestMapping("/saveWallActivity")
	@ResponseBody
	@SystemControllerLog(description = "新增上墙活动")
	public Map<String, Object> saveWallActivity(@ModelAttribute("wa")YWallactivity wa){
		logger.info("save YWallactivity");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			waService.save(wa);
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
	 * 删除上墙活动
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteWallActivity")
	@ResponseBody
	@SystemControllerLog(description = "删除上墙活动")
	public Map<String,Object> deleteWallActivity(@RequestParam("fids")String fids){
		logger.info("delete YWallactivity");
		Map<String, Object> map = new HashMap<String, Object>();
		YWallactivity wa = null;
		try {
			for(String id:fids.split(",")){
				wa = waService.getById(id);
				waService.delete(wa);
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
	 * 修改上墙活动
	 * @param wa
	 * @return
	 */
	@RequestMapping("/updateWallActivity")
	@ResponseBody
	@SystemControllerLog(description = "修改上墙活动")
	public Map<String, Object> updateWallActivity(@ModelAttribute("wa")YWallactivity wa){
		logger.info("update YWallactivity");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			waService.update(wa);
			map.put("msg", "修改成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败！");
			map.put("success", false);
		}
		return map;
	}

	/**
	 * 新增上墙活动参加人
	 * @param wa
	 * @return
	 */
	@RequestMapping("/saveJoinActivity")
	@ResponseBody
	@SystemControllerLog(description = "新增上墙活动参加人")
	public Map<String, Object> saveJoinActivity(YBasicJoinActivity yBasicJoinActivity){
		logger.info("save yBasicJoinActivity");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(yBasicJoinActivity.getSetNumber()==null){
				//如果没有座位号就随机生成座位号
				yBasicJoinActivity.setSetNumber(100);
			}
			waService.saveJoinActivity(yBasicJoinActivity);

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
	 * 删除上墙活动参加人
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteJoinActivity")
	@ResponseBody
	@SystemControllerLog(description = "删除上墙活动参加人")
	public Map<String,Object> deleteJoinActivity(String fids){
		logger.info("delete JoinActivity");
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicJoinActivity wa = null;
		try {
			String[] ids = fids.split(",");
			for(String id:ids){
				waService.deleteJoinActivity(id);
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
	 * 修改上墙活动参加人
	 * @param wa
	 * @return
	 */
	@RequestMapping("/updateJoinActivity")
	@ResponseBody
	@SystemControllerLog(description = "修改上墙活动参加人")
	public Map<String, Object> updateJoinActivity(YBasicJoinActivity yBasicJoinActivity){
		logger.info("update updateJoinActivity");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			waService.updateJoinActivity(yBasicJoinActivity);
			map.put("msg", "修改成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败！");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 批量的导入活动参与人
	 * @param file excel表格
	 * @param activityId 活动id
	 * @return
	 */
	@RequestMapping("/importJoinActivity")
	@ResponseBody
	@SystemControllerLog(description = "批量的导入活动参与人")
	public Map<String, Object> importBatchJoinActivity(MultipartFile file,String activityId){
		logger.info("update updateJoinActivity");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			waService.importJoinActivity(file,activityId);
			map.put("msg", "导入成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "导入失败！");
			map.put("success", false);
		}
		return map;
	}

	/**
	 *分页查询
	 * @param theme
	 * @param theUrl
	 * @return
	 */
	@RequestMapping("/findAllJionWallActivitypeople")
	@ResponseBody
	@SystemControllerLog(description = "分页查询")
	public Map<String, Object> findAllJionWallActivitypeople(Integer start,Integer limit, String searchCondition, String ztid){
		Map<String, Object> map = new HashMap<String, Object>();
		if(ztid!=null&&!ztid.equals("")){
			PagingList<YBasicJoinActivity> list = null;
			try {
				list = waService.getAllGroupsPagingList(start, limit, searchCondition, ztid);
				map.put("list", list.getList());
				map.put("count", list.getCount());
				map.put("msg", "查询成功！");
				map.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("msg", "查询失败！");
				map.put("success", false);
			}
		}else{
			map.put("msg", "查询成功！");
			map.put("success", true);
		}
		
		return map;
	}

	/**
	 *获取所有签到人员
	 * @param theme
	 * @param theUrl
	 * @return
	 */
	@RequestMapping("/changeSeatNumberForJionWallActivitypeople")
	@ResponseBody
	@SystemControllerLog(description = "获取所有签到人员")
	public Map<String, Object> changeSeatNumberForJionWallActivitypeople(Integer startNum,Integer endNum,String ztid){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			waService.changeSeatNumberForJionWallActivitypeople(startNum, endNum, ztid);
			map.put("msg", "设置成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "设置失败！");
			map.put("success", false);
		}
		return map;
	}


	//	/**
	//	 *填写随机座位号的范围，改变签到人员的座位号
	//	 * @param theme
	//	 * @param theUrl
	//	 * @return
	//	 */
	//	@RequestMapping("/changeSeatNumberForJionWallActivitypeople")
	//	@ResponseBody
	//	public Map<String, Object> findAllJionWallActivitypeople(String ztid){
	//		Map<String, Object> map = new HashMap<String, Object>();
	//		try {
	//			map = waService.findAllJionWallActivitypeople(ztid);
	//			map.put("msg", "设置成功！");
	//			map.put("success", true);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			map.put("msg", "设置失败！");
	//			map.put("success", false);
	//		}
	//		return map;
	//	}

	/**
	 *保存中奖人信息
	 * @param ztid 主题
	 * @param theUrl
	 * @return
	 */
	@RequestMapping("/saveWinActivitypeople")
	@ResponseBody
	@SystemControllerLog(description = "保存中奖人信息")
	public Map<String, Object> saveWinActivitypeople(String ztid,String[] tel,String settingId){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			waService.saveWinActivitypeople(ztid,tel,settingId);
			map.put("msg", "设置成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "设置失败！");
			map.put("success", false);
		}
		return map;
	}
	/**
	 *返回所有的奖项设置 和 为中奖的参与人  根据活动id
	 * @param theme
	 * @param theUrl
	 * @return
	 */
	@RequestMapping("/getAllWinAndJoinWallActivitypeople")
	@ResponseBody
	@SystemControllerLog(description = "返回所有的奖项设置 和 为中奖的参与人  根据活动id")
	public Map<String, Object> findAllJionWallActivitypeople(String ztid){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = waService.findAllJionWallActivitypeople(ztid);
			map.put("msg", "设置成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "设置失败！");
			map.put("success", false);
		}
		return map;
	}


	/**
	 *姓名，电话，团体)获取座位号 团体id
	 *同时满足以上要求才能查询出来
	 * @param theme
	 * @param theUrl
	 * @return
	 */
	@RequestMapping("/getWallActivitypeopleSeatNum")
	@ResponseBody
	@SystemControllerLog(description = "姓名，电话，团体)获取座位号 团体id同时满足以上要求才能查询出来")
	public Map<String, Object> getWallActivitypeopleSeatNum(String userName,String tel,String groupname,String ztid){
		Map<String, Object> map = new HashMap<String, Object>();
		Integer seatNum = null;
		try {
			seatNum = waService.getWallActivitypeopleSeatNum(userName,tel,groupname,ztid);
			map.put("msg", "设置成功！");
			map.put("success", true);
			map.put("seatNum", seatNum);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "设置失败！");
			map.put("success", false);
		}
		return map;
	}

	@RequestMapping("/download")
	@ResponseBody
	@SystemControllerLog(description = "上传文件")
	public void download(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		BufferedInputStream bis = null;  
		BufferedOutputStream bos = null;
		try {
			request.setCharacterEncoding("UTF-8");
			String filepath = request.getSession().getServletContext().getRealPath("/function/template/joinActivityPeople.xlsx");
			File file = new File(filepath);
			long fileLength = file.length();  
			response.setContentType("multipart/form-data");  
			response.setHeader("Content-disposition", "attachment; filename="  
					+ new String(file.getName().getBytes("utf-8"), "ISO8859-1"));  
			response.setHeader("Content-Length", String.valueOf(fileLength));  

			bis = new BufferedInputStream(new FileInputStream(file));  
			bos = new BufferedOutputStream(response.getOutputStream());  
			byte[] buff = new byte[2048];  
			int bytesRead;  
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
				bos.write(buff, 0, bytesRead);  
			}  
			bis.close();  
			bos.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
