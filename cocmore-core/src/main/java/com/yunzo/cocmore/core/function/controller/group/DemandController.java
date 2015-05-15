package com.yunzo.cocmore.core.function.controller.group;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply;
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;
import com.yunzo.cocmore.core.function.service.DemandService;
import com.yunzo.cocmore.core.function.service.DemandsupplyPushinfoServiceI;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.service.LabelServiceI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.vo.PushVo;
import com.yunzo.cocmore.core.thread.PushThread;
import com.yunzo.cocmore.utils.base.IMGSize;

/**
 * @author：jackpeng
 * @date：2014年11月27日下午3:34:54
 * 需求controller类
 */
@Controller
@RequestMapping("/demand")
public class DemandController {
	
	private static final Logger logger = Logger.getLogger(AssurancecontentController.class);
	
	@Resource(name = "demandService")
	private DemandService demandService;
	
	@Resource(name = "getNumberService")
	private GetNumberService getNumberService;
	
	@Resource(name = "groupsInformService")
	private GroupsInformService groupsInformService;
	
	@Resource(name = "labelService")
	private LabelServiceI labelService;
	
	@Resource(name = "dspService")
	private DemandsupplyPushinfoServiceI dspService;
	
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
	 * 运维后台推送需求
	 * @param suppluIds 多个需求id，用逗号分隔
	 * @param memberIds 多个会员id，用逗号分隔
	 * @return
	 */
	@RequestMapping("/pushDemand")
	@ResponseBody
	@SystemControllerLog(description = "运维后台推送需求")
	public Map<String, Object> pushDemand(String demandIds,String memberIds){
		Map<String, Object> map = new HashMap<String, Object>();
		String[] demandStrs = demandIds.split(",");
		try {
			for(String demandId:demandStrs){
				YBasicSocialgroupsdemand demand = demandService.getById(demandId);
				if(demand != null){
					String[] memberStrs = memberIds.split(",");
					for(String memberId:memberStrs){
						String hql = "select y.fmobilePhone from YBasicMember y where y.fid = '"+memberId+"'";
						List<String> listTels = memberService.getByHql(hql);
						if(listTels != null && listTels.size() > 0){
							Set<String> setTels = new HashSet<String> (listTels);
							Map<String,PushVo> deviceIdMap  =  groupsInformService.getOutRepeat(setTels);
							//判断是是否有团体id
							YBasicSocialgroups group = demand.getYBasicSocialgroups();
							String groupId = null;
							if(group != null){
								groupId = group.getFid();
							}
							/**
							 * 开启推送线程，进行推送行为的记录和系统日志的记录  app端的推送
							 */
							PushThread pushThread = new PushThread(new Integer(2), deviceIdMap, demand.getFheadline(), demand.getFid(), groupId, dspService);
							pushThread.start();
							
							map.put("msg", "推送成功！");
							map.put("success", true);
						}else{
							logger.info("没有登录过的设备信息....");
							map.put("msg", "没有登录过的设备信息！");
							map.put("success", false);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "推送失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 查询全部需求并分页
	 * @param start
	 * @param limit
	 * @param groupId
	 * @param headline
	 * @return
	 */
	@RequestMapping("/findDemandPage")
	@ResponseBody
	@SystemControllerLog(description = "查询全部需求并分页")
	public Map<String, Object> findDemandPage(Integer start,Integer limit,String groupId,String headline){
		logger.info("find YBasicSocialgroupsdemand");
		Map<String, Object> map = new HashMap<String, Object>();
		//查询页面显示和总条数
		PagingList<YBasicSocialgroupsdemand> pageList = null;
		try {
			pageList = demandService.getAllDemandPagingList(start, limit, groupId,headline);
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
	 * 查询全部需求
	 * @return
	 */
	@RequestMapping("/findall")
	@ResponseBody
	@SystemControllerLog(description = "查询全部需求")
	public Map<String, Object> findAllDemand(){
		logger.info("find YBasicSocialgroupsdemand");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicSocialgroupsdemand> list = null;
		try {
			list = demandService.findAll();
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
	 * 根据ID查询需求
	 * @param id
	 * @return
	 */
	@RequestMapping("/findDemandId")
	@ResponseBody
	@SystemControllerLog(description = "根据ID查询需求")
	public Map<String, Object> findDemandId(@RequestParam("id")String id){
		logger.info("find YBasicSocialgroupsdemand fid=="+id);
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicSocialgroupsdemand demand = null;
		try {
			demand = demandService.getById(id);
			map.put("obj", demand);
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
	 * 根据条件查询需求
	 * @param name
	 * @return
	 */
	@RequestMapping("/findDemandHql")
	@ResponseBody
	@SystemControllerLog(description = "根据条件查询需求")
	public Map<String, Object> findDemandHql(@RequestParam("name")String name){
		logger.info("find YBasicSocialgroupsdemand fheadline like "+name);
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicSocialgroupsdemand> list = null;
		String hql = null;
		try {
			hql = "from YBasicSocialgroupsdemand y where y.fheadline like '%"+name+"%'";
			list = demandService.getByHql(hql);
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
	 * 新增需求
	 * @param demand
	 * @return
	 */
	@RequestMapping("/saveDemand")
	@ResponseBody
	@SystemControllerLog(description = "新增需求")
	public Map<String, Object> saveDemand(@ModelAttribute("demand")YBasicSocialgroupsdemand demand,MultipartHttpServletRequest request){
		logger.info("save YBasicSocialgroupsdemand");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X640.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				demand.setFimages(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		try {
			demandService.save(demand);
			map.put("msg", "新增成功！");
			map.put("success", true);
			getNumberService.addSerialNumber("FX-XQGL");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "新增失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 删除需求
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteDemand")
	@ResponseBody
	@SystemControllerLog(description = "删除需求")
	public Map<String, Object> deleteDemand(@RequestParam("fids")String fids){
		logger.info("delete YBasicSocialgroupsdemand");
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicSocialgroupsdemand demand = null;
		try {
			for(String id:fids.split(",")){
				demand = demandService.getById(id);
				demandService.delete(demand);
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
	 * 修改需求
	 * @param demand
	 * @return
	 */
	@RequestMapping("/updateDemand")
	@ResponseBody
	@SystemControllerLog(description = "修改需求")
	public Map<String, Object> updateDemand(@ModelAttribute("demand")YBasicSocialgroupsdemand demand,MultipartHttpServletRequest request){
		logger.info("update YBasicSocialgroupsdemand");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X640.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				demand.setFimages(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		try {
			demandService.update(demand);
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
	 * 修改状态
	 * @param status
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateState")
	@ResponseBody
	@SystemControllerLog(description = "修改状态")
	public Map<String, Object> updateState(@RequestParam("status")int status,@RequestParam("fids")String fids){
		logger.info("update YBasicSocialgroupsdemand state");
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicSocialgroupsdemand demand = null;
		try {
			for(String id:fids.split(",")){
				demand = demandService.getById(id);
				demand.setFbillState(status);
				demandService.update(demand);
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
	 * 作废
	 * @param fid
	 * @param fbillState
	 * @param comment
	 * @return
	 */
	@RequestMapping("/Invalid")
	@ResponseBody
	@SystemControllerLog(description = "需求作废")
	public Map<String, Object> doNotNeedSessionAndSecurity_Invalid(String fid,String fbillState,String comment){
		logger.info("Invalid YBasicSocialgroupsdemand");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicSocialgroupsdemand demand=new YBasicSocialgroupsdemand();
			demand=demandService.getById(fid);
			demand.setFbillState(Integer.parseInt(fbillState));
			demand.setFcomment(comment);
			demandService.update(demand);
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
	 * 根据评论数排序查需求。
	 * @param start
	 * @param limit
	 * @param groupId
	 * @param headline
	 * @return
	 */
	@RequestMapping("/findDemand")
	@ResponseBody
	@SystemControllerLog(description = "根据评论数排序查需求")
	public Map<String, Object> findDemandByComment(Integer start,Integer limit,String type){
		logger.info("find YBasicSocialgroupsdemand");
		Map<String, Object> map = new HashMap<String, Object>();
		//查询页面显示和总条数
		PagingList<YBasicSocialgroupsdemand> pageList = null;
		try {
			if(type.equals("1")){
				pageList = demandService.findDemandByComment(start, limit);
			}else if(type.equals("0")){
				pageList = demandService.findDemandByPointLike(start, limit);
			}else{
				pageList = demandService.findDemandBytime(start, limit);
			}
//			map.put("d", pageList.getList().size());
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
}
