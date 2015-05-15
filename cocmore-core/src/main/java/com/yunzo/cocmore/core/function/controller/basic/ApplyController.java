package com.yunzo.cocmore.core.function.controller.basic;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YInitiationApply;
import com.yunzo.cocmore.core.function.service.ApplyService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年12月9日上午11:08:49
 * 入会申请controller类
 */
@Controller
@RequestMapping("/apply")
public class ApplyController {
	
	private static final Logger logger = Logger.getLogger(ApplyController.class);
	
	@Resource(name = "applyService")
	private ApplyService applyService;
	
	@RequestMapping("/findApplyPage")
	@ResponseBody
	@SystemControllerLog(description="查询申请列表")
	public Map<String, Object> findApplyPage(Integer start,Integer limit,String groupId,String name){
		logger.info("find YInitiationApply");
		Map<String, Object> map = new HashMap<String, Object>();
		PagingList<YInitiationApply> list = null;
		try {
			list = applyService.getAllApplyPagingList(start, limit, groupId, name);
			map.put("list", list.getList());
			map.put("count", list.getCount());
			map.put("msg", "查询成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/findApplyId")
	@ResponseBody
	@SystemControllerLog(description="查询单个申请")
	public Map<String, Object> findApplyId(String id){
		logger.info("find YInitiationApply id="+id);
		Map<String, Object> map = new HashMap<String, Object>();
		YInitiationApply apply = null;
		try {
			apply = applyService.getById(id);
			map.put("obj", apply);
			map.put("msg", "查询成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/saveApply")
	@ResponseBody
	@SystemControllerLog(description="保存入会申请")
	public Map<String, Object> saveApply(YInitiationApply apply){
		logger.info("save YInitiationApply");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			applyService.save(apply);
			map.put("msg", "新增成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "新增失败！");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/deleteApply")
	@ResponseBody
	@SystemControllerLog(description="删除入会申请")
	public Map<String, Object> deleteApply(String fids){
		logger.info("delete YInitiationApply");
		Map<String, Object> map = new HashMap<String, Object>();
		YInitiationApply apply = null;
		try {
			for(String id:fids.split(",")){
				apply = applyService.getById(id);
				applyService.delete(apply);
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
	
	@RequestMapping("/updateState")
	@ResponseBody
	@SystemControllerLog(description="APP检查通讯录信息是否有改动")
	public Map<String, Object> updateState(String fids,int state,String fcompanyNames,String fcompanyPositions,String groupId){
		logger.info("update YInitiationApply");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 执行操作
			applyService.update(fids,state,fcompanyNames,fcompanyPositions,groupId);
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
