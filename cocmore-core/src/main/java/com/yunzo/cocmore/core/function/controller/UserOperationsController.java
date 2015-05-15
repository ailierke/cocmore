package com.yunzo.cocmore.core.function.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMembercompany;
import com.yunzo.cocmore.core.function.model.mysql.YVisitorsRecordNotlogin;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.MembercompanyServiceI;
import com.yunzo.cocmore.core.function.service.NotloginServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * Description: <用户运营controller>. <br>
 * @date:2015年3月5日 上午10:19:14
 * @author beck
 * @version V1.0
 */
@Controller
@RequestMapping("/useroc")
public class UserOperationsController {
	
	private static final Logger logger = Logger.getLogger(CharacterController.class);
	
	@Resource(name = "memberService")
	private MemberServiceI memberService;			//会员service
	
	@Resource(name = "companyService")
	private MembercompanyServiceI companyService;	//会员公司service
	
	@Resource(name = "notLoginService")
	private NotloginServiceI notLoginService;		//登录记录service
	
	/**
	 * 查询全部数据
	 * @param searchName
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping("/findAllLogin")
	@ResponseBody
	@SystemControllerLog(description="查询全部数据")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllLogin(@RequestParam(value="searchName", required=false)String searchName,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit){
		logger.info("findAllLogin");
		Map<String, Object> map = new HashMap<String,Object>();
		PagingList<YVisitorsRecordNotlogin> list = null;
		try {
			list = notLoginService.findAll(searchName,start,limit);
			map.put("msg", "查询成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 查询公司信息
	 * @return
	 */
	@RequestMapping("/selectCompanyInfo")
	@ResponseBody
	@SystemControllerLog(description="查询公司信息")
	public Map<String, Object> doNotNeedSessionAndSecurity_selectCompanyInfo(@RequestParam("fUserId")String fUserId){
		logger.info("UserOperationsController  selectCompanyInfo");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicMembercompany com = companyService.getByMemberId(fUserId);
			map.put("msg", "查询成功");
			map.put("success", true);
			map.put("obj", com);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		
		return map;
	}
	
	/**
	 * 查询个人信息
	 * @return
	 */
	@RequestMapping("/selectInfo")
	@ResponseBody
	@SystemControllerLog(description="查询个人信息")
	public Map<String, Object> doNotNeedSessionAndSecurity_selectInfo(@RequestParam("fUserId")String fUserId){
		logger.info("UserOperationsController  selectInfo");
		Map<String, Object> map = new HashMap<String,Object>();
		YBasicMember obj = null;
		try {
			obj = memberService.getById(fUserId);
			map.put("msg", "查询成功");
			map.put("obj", obj);
			map.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 登录状态
	 * @return
	 */
	@RequestMapping("/loginStatus")
	@ResponseBody
	@SystemControllerLog(description="登录状态")
	public Map<String, Object> doNotNeedSessionAndSecurity_loginStatus(@RequestParam("fUserId")String fUserId){
		logger.info("UserOperationsController  loginStatus");
		Map<String, Object> map = new HashMap<String,Object>();
		List<YVisitorsRecordNotlogin> list = null;
		try {
			list = notLoginService.getById(fUserId);
			map.put("msg", "查询成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 查询指定时间会员的活跃度
	 * @param fUserIds
	 * @param flag
	 * @return
	 */

	@RequestMapping("/selectActive")
	@ResponseBody
	@SystemControllerLog(description="查询指定时间会员的活跃度")
	public Map<String, Object> doNotNeedSessionAndSecurity_selectActive(@RequestParam("fUserIds")String fUserIds,@RequestParam("flag")Integer flag){
		logger.info("UserOperationsController  loginStatus");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			//调用service
			List list = notLoginService.getMemberAndActiveByIds(fUserIds, flag);
			map.put("msg", "查询成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
}
