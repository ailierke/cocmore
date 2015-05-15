package com.yunzo.cocmore.core.function.controller.group;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.Region;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicPosition;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesMemberdistribution;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.model.mysql.YPointlike;
import com.yunzo.cocmore.core.function.model.mysql.YSupplyGroup;
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;
import com.yunzo.cocmore.core.function.service.CityService;
import com.yunzo.cocmore.core.function.service.CommentService;
import com.yunzo.cocmore.core.function.service.CountyService;
import com.yunzo.cocmore.core.function.service.DemandsupplyPushinfoServiceI;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.service.GroupsService;
import com.yunzo.cocmore.core.function.service.GuaranteeServiceI;
import com.yunzo.cocmore.core.function.service.LabelServiceI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.ProvinceService;
import com.yunzo.cocmore.core.function.service.SupplyService;
import com.yunzo.cocmore.core.function.service.TradeServiceI;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.vo.GuaranteeVo;
import com.yunzo.cocmore.core.function.vo.PushVo;
import com.yunzo.cocmore.core.thread.PushThread;
import com.yunzo.cocmore.utils.base.IMGSize;
import com.yunzo.cocmore.utils.base.MD5Util;
import com.yunzo.cocmore.utils.number.EncryptionForTellPhone;

/**
 * @author：jackpeng
 * @date：2014年11月24日下午4:27:13
 * 供应controller类
 */
@Controller
@RequestMapping("/supply")
public class SupplyConttroller {
	
	private static final Logger logger = Logger.getLogger(SupplyConttroller.class);
	
	@Resource(name = "supplyService")
	private SupplyService supplyService;
	
	@Resource(name = "getNumberService")
	private GetNumberService getNumberService;
	
	@Resource(name = "guaranteeService")
	private GuaranteeServiceI guaranteeService;
	
	@Resource(name = "commentService")
	private CommentService commentService;
	
	@Resource(name = "cityService")
	private CityService cityService;

	@Resource(name = "countyService")
	CountyService countyService;

	@Resource(name = "groupsService")
	GroupsService groupsService;

	@Resource(name = "provinceService")
	ProvinceService provinceService;
	
	@Resource(name = "memberService")
	private MemberServiceI memberService;
	
	@Resource(name = "tradeService")
	private TradeServiceI tradeService;
	
	@Resource(name = "groupsInformService")
	private GroupsInformService groupsInformService;
	
	@Resource(name = "labelService")
	private LabelServiceI labelService;
	
	@Resource(name = "dspService")
	private DemandsupplyPushinfoServiceI dspService;
	
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
	 * 运维后台推送供应
	 * @param suppluIds 多个供应id，用逗号分隔
	 * @param memberIds 多个会员id，用逗号分隔
	 * @return
	 */
	@RequestMapping("/pushSupply")
	@ResponseBody
	@SystemControllerLog(description = "运维后台推送供应")
	public Map<String, Object> pushSupply(String supplyIds,String memberIds){
		Map<String, Object> map = new HashMap<String, Object>();
		String[] supplyStrs = supplyIds.split(",");
		try {
			for(String supplyId:supplyStrs){
				YBasicSocialgroupssupply supply = supplyService.getById(supplyId);
				if(supply != null){
					String[] memberStrs = memberIds.split(",");
					for(String memberId:memberStrs){
						String hql = "select y.fmobilePhone from YBasicMember y where y.fid = '"+memberId+"'";
						List<String> listTels = memberService.getByHql(hql);
						if(listTels != null && listTels.size() > 0){
							Set<String> setTels = new HashSet<String> (listTels);
							Map<String,PushVo> deviceIdMap  =  groupsInformService.getOutRepeat(setTels);
							
							//判断是是否有团体id
							YBasicSocialgroups group = supply.getYBasicSocialgroups();
							String groupId = null;
							if(group != null){
								groupId = group.getFid();
							}
							/**
							 * 开启推送线程，进行推送行为的记录和系统日志的记录  app端的推送
							 */
							PushThread pushThread = new PushThread(new Integer(1), deviceIdMap, supply.getFheadline(), supply.getFid(), groupId, dspService);
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
	 * 查询全部供应并分页
	 * @param start
	 * @param limit
	 * @param groupId
	 * @param headline
	 * @return
	 */
	@RequestMapping("/findall")
	@ResponseBody
	@SystemControllerLog(description = "查询全部供应并分页")
	public Map<String, Object> findAllSupply(Integer start,Integer limit,String groupId,String headline){
		logger.info("find YBasicSocialgroupssupply");
		Map<String, Object> map = new HashMap<String, Object>();
		//查询页面显示和总条数
		PagingList<YBasicSocialgroupssupply> pageList = null;
		try {
			pageList = supplyService.getAllSupplyPagingList(start, limit, groupId,headline);
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
	 * 查询全部不分页
	 * @return
	 */
	@RequestMapping("/findSupply")
	@ResponseBody
	@SystemControllerLog(description = "查询全部不分页")
	public Map<String, Object> findSupply(){
		logger.info("find YBasicSocialgroupssupply");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicSocialgroupssupply> list = null;
		try {
			list = supplyService.findAll();
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
	 * 根据ID查询供应
	 * @param id
	 * @return
	 */
	@RequestMapping("/findSupplyId")
	@ResponseBody
	@SystemControllerLog(description = "根据ID查询供应")
	public Map<String, Object> findSupplyId(@RequestParam("id")String id){
		logger.info("find YBasicSocialgroupssupply fid ==" + id);
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicSocialgroupssupply supply = null;
		try {
			supply = supplyService.getById(id);
			map.put("obj", supply);
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
	 * 根据供应id查询供应担保分配信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/findSAList")
	@ResponseBody
	@SystemControllerLog(description = "根据供应id查询供应担保分配信息")
	public Map<String, Object> findSAList(String id){
		logger.info("dind YSupplyGroup");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YSupplyGroup> list = null;
		try {
			list = supplyService.findSASupplyId(id);
			if(list != null){
				map.put("obj", list);
				map.put("", "查询成功！");
				map.put("success", true);
			}else{
				map.put("obj", null);
				map.put("", "查询失败！");
				map.put("success", false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("", "查询失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 根据条件查询供应
	 * @param name
	 * @return
	 */
	@RequestMapping("/findSupplyHql")
	@ResponseBody
	@SystemControllerLog(description = "根据条件查询供应")
	public Map<String, Object> findSupplyHql(@RequestParam("name")String name){
		logger.info("find YBasicSocialgroupssupply fheadline like "+name);
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicSocialgroupssupply> list = null;
		String hql = null;
		try {
			hql = "from YBasicSocialgroupssupply y where y.fheadline like '%"+name+"%'";
			list = supplyService.getByHql(hql);
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
	 * 新增供应
	 * @param supply
	 * @return
	 */
	@RequestMapping("/saveSupply")
	@ResponseBody
	@SystemControllerLog(description = "新增供应")
	public Map<String, Object> saveSupply(@ModelAttribute("supply")YBasicSocialgroupssupply supply,MultipartHttpServletRequest request){
		logger.info("save YBasicSocialgroupssupply");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X640.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				supply.setFimages(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		try {
			supplyService.saveSupply(supply);
			map.put("msg", "新增成功！");
			map.put("success", true);
			getNumberService.addSerialNumber("FX-GYGL");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "新增失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 删除供应
	 * @param supply
	 * @return
	 */
	@RequestMapping("/deleteSupply")
	@ResponseBody
	@SystemControllerLog(description = "删除供应")
	public Map<String, Object> deleteSupply(@RequestParam("fids")String fids){
		logger.info("delete YBasicSocialgroupssupply");
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicSocialgroupssupply supply = null;
		try {
			for(String id:fids.split(",")){
				supply = supplyService.getById(id);
				supplyService.delete(supply);
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
	 * 修改供应
	 * @param supply
	 * @return
	 */
	@RequestMapping("/updateSupply")
	@ResponseBody
	@SystemControllerLog(description = "修改供应")
	public Map<String, Object> updateSupply(@ModelAttribute("supply")YBasicSocialgroupssupply supply,MultipartHttpServletRequest request){
		logger.info("update YBasicSocialgroupssupply");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X640.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				supply.setFimages(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		try {
			supplyService.updateSupply(supply);
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
	 * 修改供应状态(审核、反审核、生效、失效)
	 * @param page
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateState")
	@ResponseBody
	@SystemControllerLog(description = "修改供应状态(生效、失效)")
	public Map<String, Object> updateState(@RequestParam("status")int status,@RequestParam("fids")String fids){
		logger.info("update YBasicSocialgroupssupply State");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YSupplyGroup> listsg1 = null;
		List<YSupplyGroup> listsg2 = null;
		List<YSupplyGroup> listsg3 = null;
		String hqlsg1 = null;
		String hqlsg2 = null;
		String hqlsg3 = null;
		YBasicSocialgroupssupply supply = null;
		try {
			if(fids != null && !fids.equals("")){
				for(String id:fids.split(",")){
					supply = supplyService.getById(id);
					if(supply.getFareGuarantee() == 1){
						//查询认证关系状态为待审核的数据
						hqlsg1 = "from YSupplyGroup y where y.YBasicSocialgroupssupply.fid = '"+id+"' and y.ispass = 15";
						listsg1 = guaranteeService.findByHql(hqlsg1);
						//查询认证关系状态为通过的数据
						hqlsg2 = "from YSupplyGroup y where y.YBasicSocialgroupssupply.fid = '"+id+"' and y.ispass = 16";
						listsg2 = guaranteeService.findByHql(hqlsg2);
						//查询认证关系状态为拒绝的数据
						hqlsg3 = "from YSupplyGroup y where y.YBasicSocialgroupssupply.fid = '"+id+"' and y.ispass = 17";
						listsg3 = guaranteeService.findByHql(hqlsg3);
						if(status == 5){
							if(listsg2.size() > 0){
								supply.setFbillState(status);
								supplyService.update(supply);
								map.put("msg", "生效成功！");
								map.put("success", true);
							}else{
								map.put("msg", "生效失败，认证未通过或正在审核！");
								map.put("success", false);
							}
						}else if(status == 6){
							if(listsg3.size() > 0 && listsg1.size() <= 0 && listsg2.size() <= 0){
								supply.setFbillState(status);
								supplyService.update(supply);
								map.put("msg", "失效成功！");
								map.put("success", true);
							}else{
								map.put("msg", "失效失败，认证正在审核或通过审核！");
								map.put("success", false);
							}
						}else{
							logger.info("状态参数不符...");
						}
					}else{
						supply.setFbillState(status);
						supplyService.update(supply);
					}
				}
				
			}else{
				map.put("msg", "fids 为空！");
				map.put("success", false);
			}
			
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
	@SystemControllerLog(description = "供应作废")
	public Map<String, Object> doNotNeedSessionAndSecurity_Invalid(String fid,String fbillState,String comment){
		logger.info("Invalid YBasicSocialgroupssupply");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicSocialgroupssupply supply=new YBasicSocialgroupssupply();
			supply=supplyService.getById(fid);
			supply.setFbillState(Integer.parseInt(fbillState));
			supply.setFcomment(comment);
			supplyService.update(supply);
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
	 * update by ailierke
	 * 获取担保列表所需信息
	 * @param groupId  需要担保的商会groupId
	 * @return
	 */
	@RequestMapping("/getYBasicAssurancecontentVo")
	@ResponseBody
	@SystemControllerLog(description = "获取担保列表所需信息")
	public Map<String, Object> getYBasicAssurancecontentVo(String start,String limit,String groupId,String ispass){
		Map<String, Object> map = new HashMap<String, Object>();
		List<GuaranteeVo> guaranteeVos =null;
		Integer count = null;
		try {
			guaranteeVos=supplyService.getYBasicAssurancecontentVo(start,limit,groupId,ispass);
			count = supplyService.getCount(start, limit, groupId, ispass);
			map.put("object", guaranteeVos);
			map.put("count", count);
			map.put("msg", "获取成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "获取失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * update by ailierke
	 * 商会确定担保或者拒绝担保
	 * @param supplygroupId  供应担保中间关系表id
	 * @return
	 */
	@RequestMapping("/updateYBasicAssurancecontent")
	@ResponseBody
	@SystemControllerLog(description = "商会确定担保或者拒绝担保")
	public Map<String, Object> updateYBasicAssurancecontent(String supplygroupId,String ispass){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			supplyService.updateYBasicAssurancecontent(supplygroupId,ispass);
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
	 * 获取点赞和踩的数量
	 * @param supplyDemandId
	 * @return
	 */
	@RequestMapping("/getNumById")
	@ResponseBody
	@SystemControllerLog(description = "获取供应点赞和踩的数量")
	public Map<String, Object> getNumById(String supplyDemandId){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String,Object> num=new HashMap<String, Object>();
			num=supplyService.getNumById(supplyDemandId);
			map.put("msg", "修改成功！");
			map.put("success", true);
			map.put("num", num);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 根据评论数排序查供应。
	 * @param start
	 * @param limit
	 * @param groupId
	 * @param headline
	 * @return
	 */
	@RequestMapping("/findSupplyBySequence")
	@ResponseBody
	@SystemControllerLog(description = "根据评论数排序查供应")
	public Map<String, Object> findSupplyBySequence(Integer start,Integer limit,String type){
		logger.info("find YBasicSocialgroupssupply");
		Map<String, Object> map = new HashMap<String, Object>();
		//查询页面显示和总条数
		PagingList<YBasicSocialgroupssupply> pageList = null;
		try {
			if(type.equals("0")){
				pageList = supplyService.findSupplyByPointLike(start, limit);
			}else if(type.equals("1")){
				pageList = supplyService.findSupplyByComment(start, limit);
			}else{
				pageList = supplyService.findSupplyBytime(start, limit);
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
	
	/**
	 * 新增评论
	 * @param fforeignIds 供需id数组
	 * @param fcontents 评论类容
	 * @param type 判断是供应还是需求 0 供应 1需求
	 * @param memberids 评论会员id数组
	 * @return
	 */
	@RequestMapping("/saveComment")
	@ResponseBody
	@SystemControllerLog(description = "新增供应评论")
	public Map<String, Object> saveComment(String[] fforeignIds,String fcontents,String type,String[] memberIds){
		logger.info("save YComment");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			YBasicType basictype=new YBasicType();
			if(type.equals("0")){
				basictype.setFid("3103");
			}else{
				basictype.setFid("3104");
			}
			for(String fforeignId:fforeignIds){
				for(String memberid:memberIds){
					YComment comment=new YComment();
					YBasicMember member=new YBasicMember();
					member.setFid(memberid);
					comment.setFcontents(fcontents);
					comment.setFforeignId(fforeignId);
					comment.setFtime(new Date());
					comment.setYBasicMember(member);
					comment.setYBasicType(basictype);
					commentService.save(comment);
				}
			}
			
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
	 * 供需点赞
	 * @param fmanShowIds 供需id集合
	 * @param type 判断供需 0供应 1需求
	 * @param memberids 会员id
	 * @return
	 */
	@RequestMapping("/savePotlike")
	@ResponseBody
	@SystemControllerLog(description = "供需点赞")
	public Map<String, Object> savePotlike(String[] fmanShowIds,String type,String[] memberids){
		logger.info("save YPointlike");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			YBasicType basictype=new YBasicType();
			if(type.equals("0")){
				basictype.setFid("3003");
			}else{
				basictype.setFid("3004");
			}
			//3003	供应信息	30	0	表态模块	33
			//3004	需求信息	30	0	表态模块	34
			for(String fmanShowId:fmanShowIds){
				for(String memberid:memberids){
					
					YPointlike pointlike=new YPointlike();
					YBasicMember member=new YBasicMember();
					member.setFid(memberid);
					pointlike.setYBasicType(basictype);
					pointlike.setYBasicMember(member);
					pointlike.setFmanShowId(fmanShowId);
					pointlike.setFarePointLike(0);//0表示赞
					pointlike.setFpointLikeType(0);//0表示点赞
					pointlike.setFpointLikeTime(new Date());
					
					supplyService.savepointlike(pointlike);
				}
			}
			
			map.put("msg", "新增成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "新增失败！");
			map.put("success", false);
		}
		return map;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 下载模板
	 * 
	 * @return
	 */
	@RequestMapping("/download")
	@SystemControllerLog(description = "供应下载模板")
	public void doNotNeedSessionAndSecurity_Download(
			HttpServletRequest request, HttpServletResponse response) {
		String fileName = "供应信息.xls";
		try {
			// 处理不同浏览器对文件名的解析问题
			String agent = (String) request.getHeader("USER-AGENT");
			if (agent != null && agent.indexOf("MSIE") == -1) { // FF
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			} else { // IE
				fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
			}
			response.setContentType("application/x-msdownload; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		HSSFWorkbook excel = new HSSFWorkbook();

		HSSFCellStyle style = excel.createCellStyle(); // 样式对象

		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		HSSFFont font = excel.createFont();// 设置字体
		//font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
		style.setFont(font);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 

		
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // 填充单元格
		style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		
		HSSFCellStyle style2 = excel.createCellStyle(); // 样式对象
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 垂直
		style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
		style2.setWrapText(true); // 自动换行
		HSSFFont font2 = excel.createFont();// 设置字体
		font2.setColor(HSSFFont.COLOR_RED);// 设置字体颜色
		style2.setFont(font2);
		
		HSSFSheet sheet = excel.createSheet();
		excel.setSheetName(0, "供应信息");
		HSSFRow firstRow = sheet.createRow(0);

		HSSFCell cells[] = new HSSFCell[15];

		String[] titles = new String[] { "标题", "行业", "发布人","供应信息", "省份","城市","区县","开始时间", "到期时间", "等级", "备注","联系人","联系电话","国家认证","审核意见"};

		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 7000);
		sheet.setColumnWidth(3, 7000);
		sheet.setColumnWidth(4, 7000);
		sheet.setColumnWidth(5, 7000);
		sheet.setColumnWidth(6, 7000);
		sheet.setColumnWidth(7, 7000);
		sheet.setColumnWidth(8, 7000);
		sheet.setColumnWidth(9, 7000);
		sheet.setColumnWidth(10, 7000);
		sheet.setColumnWidth(11, 7000);
		sheet.setColumnWidth(12, 7000);
		sheet.setColumnWidth(13, 7000);
		sheet.setColumnWidth(14, 7000);

		int rowNumber = 0;
		HSSFRow infoRow = sheet.createRow(rowNumber);
		rowNumber++;
		infoRow.setHeight((short) 0x280);// 设置行高
		HSSFFont infoFont = excel.createFont();// 设置字体
		infoFont.setBoldweight((short) 1000);// 设置字体类型
		infoFont.setFontHeightInPoints((short) 20);// 设置字体大小
		HSSFCellStyle infoStyle = excel.createCellStyle();// 添加字体样式
		infoStyle.setFont(infoFont);
		infoStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置对齐方式

		for (int i = 0; i < 15; i++) {
			cells[0] = firstRow.createCell(i);
			cells[0].setCellValue(titles[i]);
			cells[0].setCellStyle(style);
		}

		HSSFCellStyle cellStyle = excel.createCellStyle();
		cellStyle.setWrapText(true);

		for (int i = 0; i < 1; i++) {
			HSSFRow row = sheet.createRow(i + 1);

			HSSFCell cell = row.createCell(0);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellValue("");
			cell = row.createCell(2);
			cell.setCellValue("");
			cell = row.createCell(3);
			cell.setCellValue("");
			cell = row.createCell(4);
			cell.setCellValue("");
			cell = row.createCell(5);
			cell.setCellValue("");
			cell = row.createCell(6);
			cell.setCellValue("");
			cell = row.createCell(7);
			cell.setCellValue("");
			cell = row.createCell(8);
			cell.setCellValue("");
			cell = row.createCell(9);
			cell.setCellValue("");
			cell = row.createCell(10);
			cell.setCellValue("");
			cell = row.createCell(11);
			cell.setCellValue("");
			cell = row.createCell(12);
			cell.setCellValue("");
			cell = row.createCell(13);
			cell.setCellValue("");
			cell = row.createCell(14);
			cell.setCellValue("");
			
		}
		HSSFRow row = sheet.createRow(2);

		HSSFCell cell = row.createCell(0);
		//cell.setCellValue("名称、昵称、姓名、出生年月、手机 为必填项，");
		cell.setCellValue("标题、 行业、发布人、供应信息为必填项，等级只能是1-5如果不填默认为1，注意！本条提示信息不能删除否则最后一条信息无法导入，如果要添加一行请选择插入一行。");
		cell.setCellStyle(style2);
		sheet.addMergedRegion(new Region(2, (short) 0, 2, (short) 14));
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			excel.write(out);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导入
	 * 
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/import")
	@SystemControllerLog(description = "导入供应")
	public Map<String,Object> doNotNeedSessionAndSecurity_import(String groupId,HttpServletRequest request,HttpServletResponse response) throws ParseException{
		Map<String,Object> map=new HashMap<String, Object>();
		
		String filepath = request.getSession().getServletContext().getRealPath("/function/template/supplyImporting.lock");
		File lockFile = new File(filepath);
		System.out.println("******************************************");
		System.out.println(filepath);
		System.out.println("*******************************************");
		if(lockFile.exists()){
			map.put("msg", "已经有程序在进行导入,请稍后.....");
			map.put("success", false);
			return map;
		}else{
			try {
				lockFile.createNewFile();
			} catch (IOException e) {
				lockFile.delete();
				e.printStackTrace();
			}
		}
		writeStatus(request, "*********批量导入会员Excel状态日志文件***************", lockFile,false);
		try {
			//MultipartFile file,MultipartHttpServletRequest request,HttpServletRequest request,HttpServletResponse response
			
			MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;  
		    MultipartFile excelfile = mulRequest.getFile("file");  
		    
//			String filepath = request.getSession().getServletContext().getRealPath("/function/template/importStatus.lock");
//		    System.out.println(filepath);
		    
			String filename = excelfile.getOriginalFilename();  
			InputStream input = excelfile.getInputStream();  
			
			
			HSSFWorkbook wookbook = new HSSFWorkbook(input);
			
			// 在Excel文档中，第一张工作表的缺省索引是0
			// 其语句为：HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFSheet sheet = wookbook.getSheetAt(0);
			// 获取到Excel文件中的所有行数
			int rows = sheet.getPhysicalNumberOfRows();
			
			
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 
			if (rows >= 3) {
				// 遍历行
				for (int i = 1; i < rows-1; i++) {
					// 读取左上端单元格
					HSSFRow row = sheet.getRow(i);
					
					if (row != null) {
						// 获取到Excel文件中的所有的列
						//int cells = row.getPhysicalNumberOfCells();
						int cells = 16;
						String value = "";
						// 遍历列
						for (int j = 0; j <= cells; j++) {
							// 获取到列的值
							HSSFCell cell = row.getCell(j);							
							
							if (cell != null && !cell.equals("")&& !cell.toString().equals("")) {
								switch (cell.getCellType()) {
									case HSSFCell.CELL_TYPE_FORMULA:
										value += cell.getCellFormula()+",";
										break;
									case HSSFCell.CELL_TYPE_NUMERIC:
										if (HSSFDateUtil.isCellDateFormatted(cell)){ 
						                    Date date = cell.getDateCellValue();
						                   
						                    String cellvalue = sdf.format(date);
											value += cellvalue+",";
										}else{
											CellStyle cs = cell.getCellStyle();
											 double d = cell.getNumericCellValue();
											 if (cs.getDataFormat() == 31){
											     Date date2 = DateUtil.getJavaDate(d);
											     String cellvalue = sdf.format(date2);
													value += cellvalue+",";
											 }else{
												 DecimalFormat df = new DecimalFormat("0");  
												    String strCell = df.format(cell.getNumericCellValue());
													value += strCell + ",";
											 }
											
										}
										break;
									case HSSFCell.CELL_TYPE_STRING:
										value += cell.getStringCellValue().trim()
													+ ",";
										break;
									case HSSFCell.CELL_TYPE_BLANK:
										value += "null" + ",";
										break;
									default:
										value += "输入格式不对";
										break;
								}
								//value +=cell.toString()+",";
							} else {
								value += "null" + ",";
							}
							
						}
						// 将数据插入到mysql数据库中
						String[] val = value.split(",");
						//System.out.println("vvvvvvvvvvvv     "+value);
						
						//
						
						if (val.length < 4 || val[0].equals("")
								|| val[1].equals("") || val[2].equals("")
								|| val[3].equals("") ) {
							lockFile.delete();
							writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止",lockFile,true);
							map.put("msg","您上传的文件第" + (i + 1) + "行输入的数据不对，上传终止");
							return map;
						} else {
							if (val[0].equals("输入格式不对")
									|| val[1].equals("输入格式不对")
									|| val[2].equals("输入格式不对")
									|| val[3].equals("输入格式不对")
									||val[0].equals("null")
									|| val[1].equals("null")
									|| val[2].equals("null")
									|| val[3].equals("null")
									) {
								lockFile.delete();
								writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的必填项数据不对，上传终止",lockFile,true);
								map.put("msg","您上传的文件第" + (i + 1) + "行输入的数据不对，上传终止");
								return map;
							} else {
								//"标题", "行业", "发布人","供应信息", "省份","城市","区县","开始时间", "到期时间", "等级", "备注","联系人","联系电话","国家认证","审核意见"
								//   0       1     2      3        4     5     6     7        8        9     10    11      12     13        14 
								YBasicSocialgroupssupply supply = new YBasicSocialgroupssupply();
								try {
									supply.setFareGuarantee(0);//是否担保 默认为否
									supply.setFpublisherTime(new Date());//发布时间
									supply.setFbillState(0);//状态
									supply.setFisHide(0);//是否隐藏
									YBasicSocialgroups group=new YBasicSocialgroups();
									group.setFid(groupId);
									
									supply.setFheadline(val[0]);//标题
									
									List<YBasicTrade> trades=new ArrayList<YBasicTrade>();
									trades=tradeService.findByHql("from YBasicTrade as y where y.fname like '%"+val[1]+"%'");
									if(trades.size()==1){
										supply.setYBasicTrade(trades.get(0));//行业
									}else{
										lockFile.delete();
										writeStatus(request,"您上传的文件第" + (i + 1)+"行输入行业的数据不对，上传终止",lockFile,true);
										map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
										return map;
									}
									
									List<YBasicMember> members=new ArrayList<YBasicMember>();
									members=memberService.findByHql("from YBasicMember as y where y.YBasicSocialgroups.fid='"+groupId+"' and y.fname like '%"+val[2]+"%'");
									if(members.size()==1){
										supply.setYBasicMember(members.get(0));//发布人
									}else{
										lockFile.delete();
										writeStatus(request,"您上传的文件第" + (i + 1)+"行输入发布人的数据不对，上传终止",lockFile,true);
										map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
										return map;
									}
									
									supply.setFmessage(val[3]);//供应信息
									
									if(null!=val[4]&&!"".equals(val[4])&&!"null".equals(val[4])){
										//supply.setYBasicProvince(val[4]);//省份
										List<YBasicProvince> provinces=new ArrayList<YBasicProvince>();
										provinces=provinceService.findByHql("from YBasicProvince as y where y.fname like '%"+val[4]+"%'");
										if(provinces.size()==1){
											supply.setYBasicProvince(provinces.get(0));
										}else{
											lockFile.delete();
											writeStatus(request,"您上传的文件第" + (i + 1)+"行输入省份的数据不对，上传终止",lockFile,true);
											map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
											return map;
										}
									}
									if(null!=val[5]&&!"".equals(val[5])&&!"null".equals(val[5])){
										//supply.setYBasicCity(val[5]);//城市
										List<YBasicCity> citys=new ArrayList<YBasicCity>();
										citys=cityService.getByHql("from YBasicCity as y where y.fname like '%"+val[5]+"%'");
										if(citys.size()==1){
											supply.setYBasicCity(citys.get(0));
										}else{
											lockFile.delete();
											writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的城市数据不对，上传终止",lockFile,true);
											map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
											return map;
										}
										
									}
									if(null!=val[6]&&!"".equals(val[6])&&!"null".equals(val[6])){
										//supply.setYBasicCounty(val[6]);//区县
										List<YBasicCounty> countys=new ArrayList<YBasicCounty>();
										countys=countyService.getByHql("from YBasicCounty as y where y.fname like '%"+val[6]+"%'");
										if(countys.size()==1){
											supply.setYBasicCounty(countys.get(0));
										}else{
											lockFile.delete();
											writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的区县数据不对，上传终止",lockFile,true);
											map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
											return map;
										}
									}
									
									if(null!=val[7]&&!"".equals(val[7])&&!"null".equals(val[7])){
										Date auditTime=sdf.parse(val[7]);
										supply.setFauditTime(auditTime);//开始时间
									}
									if(null!=val[8]&&!"".equals(val[8])&&!"null".equals(val[8])){
										Date expireTime=sdf.parse(val[8]);
										supply.setFexpireTime(expireTime);//到期时间
									}
									if(null!=val[9]&&!"".equals(val[9])&&!"null".equals(val[9])){
										int level=Integer.parseInt(val[9]);
										if(level>0&&level<6){
											supply.setFlevel(level);//等级
										}else{
											lockFile.delete();
											writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的等级信息不对，上传终止",lockFile,true);
											map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
											return map;
										}
									}else{
										supply.setFlevel(1);//等级
									}
									if(null!=val[10]&&!"".equals(val[10])&&!"null".equals(val[10])){
										supply.setFcomment(val[10]);//备注
									}
									if(null!=val[11]&&!"".equals(val[11])&&!"null".equals(val[11])){
										supply.setFcontacts(val[11]);//联系人
									}
									if(null!=val[12]&&!"".equals(val[12])&&!"null".equals(val[12])){
										supply.setFtel(val[12]);//联系电话
									}
									if(null!=val[13]&&!"".equals(val[13])&&!"null".equals(val[13])){
										supply.setFnationalCertification(val[13]);//国家认证
									}
									if(null!=val[14]&&!"".equals(val[14])&&!"null".equals(val[14])){
										supply.setFauditIdea(val[14]);//审核意见
									}
									try {										
										supplyService.save(supply);
										map.put("msg", "成功");
										lockFile.delete();
										writeStatus(request,"数据导入成功！",lockFile,true);
										
									} catch (Exception e) {
										lockFile.delete();
										e.printStackTrace();
										writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止",lockFile,true);
										map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
										return map;
									}
								} catch (Exception e) {
									lockFile.delete();
									e.printStackTrace();
									writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止",lockFile,true);
									map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
									return map;
								}
							}
						}
					}
				}
			} else {
				lockFile.delete();
				writeStatus(request,"请按照模板填写数据！",lockFile,true);
				map.put("msg", "请按照模板填写数据！");
				return map;
			}
			
		} catch (FileNotFoundException e) {
			lockFile.delete();
			writeStatus(request,"异常！",lockFile,true);
			e.printStackTrace();
			map.put("msg", "异常");
			return map;
		} catch (IOException e) {
			lockFile.delete();
			e.printStackTrace();
			writeStatus(request,"出错！",lockFile,true);
			map.put("msg", "出错");
			return map;
		}
		return map;
	}

	
	public void writeStatus(HttpServletRequest request,String message,File lockFile,boolean flag){
		String filepath = request.getSession().getServletContext().getRealPath("/function/template/supplyImport.txt");
		System.out.println("******************************************");
		System.out.println(filepath);
		System.out.println("*******************************************");
		FileWriter wt = null;
		try {
			wt = new FileWriter(filepath,flag);
			wt.write(message+"\r\n");
			wt.flush();
			wt.close();
		} catch (IOException e) {
			lockFile.delete();
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param file
	 * @param activityId
	 * @return
	 */
	@RequestMapping("/downloadImportStatus")
	@ResponseBody
	@SystemControllerLog(description = "上传文件")
	public void downloadImportStatus(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		BufferedInputStream bis = null;  
		BufferedOutputStream bos = null;
		try {
			request.setCharacterEncoding("UTF-8");
			String filepath = request.getSession().getServletContext().getRealPath("/function/template/supplyImport.txt");
			File file = new File(filepath);
			long fileLength = file.length();  
			response.setContentType("multipart/form-data");  
			response.setHeader("Content-disposition", "attachment; filename=" + new String(file.getName().getBytes("utf-8"), "ISO8859-1"));  
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
