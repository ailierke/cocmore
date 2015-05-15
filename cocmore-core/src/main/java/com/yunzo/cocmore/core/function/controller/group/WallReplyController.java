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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YWallreply;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.WallreplyService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月27日下午5:05:35
 * 上墙回复controller类
 */
@Controller
@RequestMapping("/wallReply")
public class WallReplyController {
	
	private static final Logger logger = Logger.getLogger(WallReplyController.class);
	
	@Resource(name = "replyService")
	private WallreplyService replyService;
	
	@Resource(name = "memberService")
	private MemberServiceI memberService;
	
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
	 * 查询全部并分页
	 * @param start
	 * @param limit
	 * @param activityId
	 * @param state
	 * @return
	 */
	@RequestMapping("/findReplyPage")
	@ResponseBody
	@SystemControllerLog(description = "查询全部并分页")
	public Map<String, Object> findReplyPage(Integer start,Integer limit,String activityId,Integer state){
		logger.info("find YWallreply");
		Map<String, Object> map = new HashMap<String, Object>();
		PagingList<YWallreply> pagingList = null;
		try {
			pagingList = replyService.getAllReplyPagingList(start, limit, activityId, state);
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
	 * 根据上墙活动ID查询上墙回复
	 * @param activityId
	 * @return
	 */
	@RequestMapping("/findWallReplyId")
	@ResponseBody
	@SystemControllerLog(description = "根据上墙活动ID查询上墙回复")
	public Map<String, Object> findWallReplyId(@RequestParam("activityId")String activityId){
		logger.info("find YWallreply YWallactivity.fid=="+activityId);
		Map<String, Object> map = new HashMap<String, Object>();
		List<YWallreply> list = null;
		String hql = "";
		try {
			hql = "from YWallreply y where y.YWallactivity.fid = '"+activityId+"'";
			list = replyService.getByHql(hql);
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
	 * 根据上墙活动ID和时间判断查询上墙回复
	 * @param replyTime
	 * @param activityId
	 * @return
	 */
	@RequestMapping("/findReplyTime")
	@ResponseBody
	@SystemControllerLog(description = "")
	public Map<String, Object> findReplyTime(String replyTime,String activityId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<YWallreply> list = null;
		String hql = null;
		try {
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//小写的mm表示的是分钟
//			Date dateTime = sdf.parse(replyTime);
			hql = "from YWallreply y where y.YWallactivity.fid = '"+activityId+"'";
			if(null !=replyTime && replyTime.length()>0){
				hql+=" and y.freplyTime > '"+replyTime+"'";
			}		
			hql +=" and y.fstate = 16 order by y.freplyTime desc";
			list = replyService.getByHql(hql);
			if(list != null){
				map.put("obj", list);
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
	 * 根据状态查询上墙回复
	 * @param state
	 * @return
	 */
	@RequestMapping("/findWallReplyState")
	@ResponseBody
	@SystemControllerLog(description = "根据状态查询上墙回复")
	public Map<String, Object> findWallReplyState(@RequestParam("state")String state,@RequestParam("activityId")String activityId){
		logger.info("find YWallreply fstate=="+state);
		Map<String, Object> map = new HashMap<String, Object>();
		List<YWallreply> list = null;
		String hql = null;
		try {
			if(state!=null){
				hql = "from YWallreply y where y.YWallactivity.fid = '"+activityId+"' and y.fstate = '"+state+"'";
			}else{
				hql = "from YWallreply y where y.YWallactivity.fid = '"+activityId+"'";
			}
			list = replyService.getByHql(hql);
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
	 * 新增上墙回复
	 * @param userPhone	   用户电话
	 * @param reply
	 * @return
	 */
	@RequestMapping("/addWallReply")
	@ResponseBody
	@SystemControllerLog(description = "新增上墙回复")
	public Map<String, Object> saveReply(String userPhone,YWallreply reply){
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicMember> list = null;
		String hql = null;
		try {
			//根据用户电话获取用户
			hql = "from YBasicMember y where y.fmobilePhone = '"+userPhone+"'";
			list = memberService.findByHql(hql);
			YBasicMember member = list.get(0);
			reply.setYBasicMember(member);
			
			replyService.save(reply);
			
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
	 * 修改上墙回复状态
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateState")
	@ResponseBody
	@SystemControllerLog(description = "修改上墙回复状态")
	public Map<String, Object> updateState(@RequestParam("fids")String fids,@RequestParam("status")int status){
		logger.info("update YWallreply state");
		Map<String, Object> map = new HashMap<String, Object>();
		YWallreply wallReply = null;
		try {
			for(String id:fids.split(",")){
				wallReply = replyService.getById(id);
				wallReply.setFstate(status);
				if(status==16){
					wallReply.setFreplyTime(new Date());
				}
				replyService.update(wallReply);
			}
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
	 * 删除上墙回复
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteWallReply")
	@ResponseBody
	@SystemControllerLog(description = "删除上墙回复")
	public Map<String, Object> deleteWallReply(@RequestParam("fids")String fids){
		logger.info("delete YWallreply");
		Map<String, Object> map = new HashMap<String, Object>();
		YWallreply wallReply = null;
		try {
			for(String id:fids.split(",")){
				wallReply = replyService.getById(id);
				replyService.delete(wallReply);
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
