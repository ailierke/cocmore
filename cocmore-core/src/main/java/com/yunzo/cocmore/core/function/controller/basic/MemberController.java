package com.yunzo.cocmore.core.function.controller.basic;

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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.controller.basic.PositionServiceController.Tree;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicEmployee;
import com.yunzo.cocmore.core.function.model.mysql.YBasicImaccount;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLabel;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicPosition;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesEmployeedistribution;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesMemberdistribution;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.service.CityService;
import com.yunzo.cocmore.core.function.service.CountyService;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.GroupsService;
import com.yunzo.cocmore.core.function.service.IMService;
import com.yunzo.cocmore.core.function.service.LabelServiceI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.MemberdistributionServiceI;
import com.yunzo.cocmore.core.function.service.PositionServiceI;
import com.yunzo.cocmore.core.function.service.ProvinceService;
import com.yunzo.cocmore.core.function.service.UserService;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.LabelXMLToObject;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.utils.base.CRC32Util;
import com.yunzo.cocmore.utils.base.IMGSize;
import com.yunzo.cocmore.utils.base.IMUtils;
import com.yunzo.cocmore.utils.base.MD5Util;
import com.yunzo.cocmore.utils.number.EncryptionForTellPhone;

/** 
 *Description: <会员控制层>. <br>
 * @date:2014年11月26日 下午1:37:40
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/member")
public class MemberController {
	private static final Logger logger = Logger.getLogger(MemberController.class);
	
	@Resource(name = "memberService")
	private MemberServiceI memberService;
	
	@Resource(name = "userInfo")
	private UserService userInfo;
	
	@Resource(name = "getNumberService")
	private GetNumberService getNumberService;
	
	@Resource(name = "cityService")
	private CityService cityService;

	@Resource(name = "countyService")
	CountyService countyService;

	@Resource(name = "groupsService")
	GroupsService groupsService;

	@Resource(name = "provinceService")
	ProvinceService provinceService;
	
	@Resource(name = "memdisService")
	private MemberdistributionServiceI memdisService;
	
	@Resource(name = "posService")
	private PositionServiceI posService;
	
	@Resource(name = "imService")
	private IMService imService;
	
	@Resource(name = "labelService")
	private LabelServiceI labelService;
	
	private File methohdspath;

	// myFileContentType属性用来封装上传文件的类型
	private String methohdspathContentType;

	// myFileFileName属性用来封装上传文件的文件名
	private String methohdspathFileName;
	
	//加密前段
	private static final String BEGINSTRING="yunzo";
	
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
	 * 查询单个
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findMemberById")
	@ResponseBody
	@SystemControllerLog(description="查找单个会员")
	public Map<String, Object> doNotNeedSessionAndSecurity_findMemberById(@RequestParam("fid")String fid){
		logger.info("YBasicMember  findMemberById");
		Map<String, Object> map = new HashMap<String,Object>();
		YBasicMember obj = null;
		try {
			obj = memberService.getById(fid);
			map.put("msg", "查询成功");
			map.put("obj", obj);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 查询全部
	 * @return
	 */
	@RequestMapping("/findAllMember")
	@ResponseBody
	@SystemControllerLog(description="查找全部会员")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllMember(@RequestParam(value="searchName", required=false)String searchName,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit,String groupId,String sort,String dir){
		
			logger.info("YBasicMember  handleList");
			Map<String, Object> map = new HashMap<String,Object>();
			PagingList<YBasicMember> list = null;
			try {
				if(sort!=null&&dir!=null&&!"".equals(sort)&&!"".equals(dir)){
					list = memberService.findAll(searchName,start,limit,groupId,sort,dir);
				}else{
					list = memberService.findAll(searchName,start,limit,groupId);
				}
				
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
	 * 保存
	 * @param form
	 * @return
	 */
	@RequestMapping("/saveMember")
	@ResponseBody
	@SystemControllerLog(description="保存会员")
	public void doNotNeedSessionAndSecurity_saveMember(@ModelAttribute("form")YBasicMember form
			,MultipartHttpServletRequest request){
		logger.info("save YBasicMember");
		Map<String, Object> map = new HashMap<String,Object>();
		map = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
		
		try {
			if((boolean)map.get("success")==true){
				if(((StringBuffer)map.get("imgsrc"))!=null){
					form.setFheadImage(((StringBuffer)map.get("imgsrc")).toString());
					logger.info("保存图片url:"+((StringBuffer)map.get("imgsrc")).toString());
				}
			}
			
			memberService.save(form);
			getNumberService.addSerialNumber("HY-HYXX");
			
			//修改该会员所有的基本信息
			List<YBasicMember> members = memberService.findByHql("from YBasicMember y where y.fmobilePhone = '" + form.getFmobilePhone()  + "'");
			//批量更新
			this.updateMembers(members,form);
//			map.put("msg", "保存成功");
//			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
//			map.put("msg", "保存失败");
//			map.put("success", false);
		}
	//	return map;
	}
	
	/**
	 * @param form
	 * @return
	 */
	@RequestMapping("/updateMember")
	@ResponseBody
	@SystemControllerLog(description="修改会员")
	public void doNotNeedSessionAndSecurity_updateMember(@ModelAttribute("form")YBasicMember form
			,MultipartHttpServletRequest request){
		logger.info("update YBasicMember");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			//如果申请为管理员
			if(form.getIsAdmin() == 1){
				YSystemUsers oldUser = (YSystemUsers) userInfo.getById(form.getYSystemUsers().getFid());
				//判断是否已经关联了一个管理员，如果为空则要创建新的管理员设置给它
				if(oldUser == null){
					YSystemUsers user = new YSystemUsers(UUID.randomUUID().toString(), form.getFmobilePhone(), MD5Util.md5("888888"), 0, "0");
					userInfo.save(user);
					form.setYSystemUsers(user);
				}
			}else{
				form.setYSystemUsers(null);
			}
			//图片上传
			map = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
			if((boolean)map.get("success")==true){
				if(((StringBuffer)map.get("imgsrc"))!=null){
					form.setFheadImage(((StringBuffer)map.get("imgsrc")).toString());
					logger.info("保存图片url:"+((StringBuffer)map.get("imgsrc")).toString());
				}
				memberService.update(form);
			}
			
			//批量更新
			List<YBasicMember> members = memberService.findByHql("from YBasicMember y where y.fmobilePhone = '" + form.getFmobilePhone()  + "'");
			this.updateMembers(members,form);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存
	 * @param form
	 * @return
	 */
	@RequestMapping("/saveMemberTwo")
	@ResponseBody
	@SystemControllerLog(description="保存会员")
	public void doNotNeedSessionAndSecurity_saveMemberTwo(@ModelAttribute("form")YBasicMember form
			,MultipartHttpServletRequest request){
		logger.info("save YBasicMember");
		Map<String, Object> map = new HashMap<String,Object>();
		map = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
		
		try {
			if((boolean)map.get("success")==true){
				if(((StringBuffer)map.get("imgsrc"))!=null){
					form.setFheadImage(((StringBuffer)map.get("imgsrc")).toString());
					logger.info("保存图片url:"+((StringBuffer)map.get("imgsrc")).toString());
				}
			}
			
			memberService.save(form);
			getNumberService.addSerialNumber("HY-HYXX");
			
			//修改该会员所有的基本信息
//			List<YBasicMember> members = memberService.findByHql("from YBasicMember y where y.fmobilePhone = '" + form.getFmobilePhone()  + "'");
			//批量更新
//			this.updateMembers(members,form);
//			map.put("msg", "保存成功");
//			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
//			map.put("msg", "保存失败");
//			map.put("success", false);
		}
	//	return map;
	}
	
	/**
	 * 修改
	 * @param form
	 * @return
	 */
	@RequestMapping("/updateMemberTwo")
	@ResponseBody
	@SystemControllerLog(description="修改会员")
	public void doNotNeedSessionAndSecurity_updateMemberTwo(@ModelAttribute("form")YBasicMember form
			,MultipartHttpServletRequest request){
		logger.info("update YBasicMember");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			//如果申请为管理员
			if(form.getIsAdmin() == 1){
				YSystemUsers oldUser = (YSystemUsers) userInfo.getById(form.getYSystemUsers().getFid());
				//判断是否已经关联了一个管理员，如果为空则要创建新的管理员设置给它
				if(oldUser == null){
					YSystemUsers user = new YSystemUsers(UUID.randomUUID().toString(), form.getFmobilePhone(), MD5Util.md5("888888"), 0, "0");
					userInfo.save(user);
					form.setYSystemUsers(user);
				}
			}else{
				form.setYSystemUsers(null);
			}
			//图片上传
			map = ImgUploadUtil.imgUpload(request,IMGSize.X88.value());
			if((boolean)map.get("success")==true){
				if(((StringBuffer)map.get("imgsrc"))!=null){
					form.setFheadImage(((StringBuffer)map.get("imgsrc")).toString());
					logger.info("保存图片url:"+((StringBuffer)map.get("imgsrc")).toString());
				}
				memberService.update(form);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 用于批量更新会员信息
	 * @param members
	 */
	public void updateMembers(List<YBasicMember> members,YBasicMember form){
		for (YBasicMember o : members) {
			if(!o.getFid().equals(form.getFid())){
				o.setFname(form.getFname());
				o.setFnickName(form.getFnickName());
				o.setFsex(form.getFsex());
				o.setFbirthday(form.getFbirthday());
				o.setFemail(form.getFemail());
				o.setFpreviousEmail(form.getFpreviousEmail());
				o.setYBasicProvince(form.getYBasicProvince());
				o.setYBasicCity(form.getYBasicCity());
				o.setYBasicCounty(form.getYBasicCounty());
				o.setFnativePlace(form.getFnativePlace());
				o.setFsite(form.getFsite());
				o.setFcomment(form.getFcomment());
				o.setFheadImage(form.getFheadImage());
				o.setFhomePhone(form.getFhomePhone());
				o.setFsecondPhone(form.getFsecondPhone());
				o.setFbillState(Status.CHANGE.value());
				memberService.updateStatus(o);
			}
		}
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@RequestMapping("/deleteMember")
	@ResponseBody
	@SystemControllerLog(description="删除会员")
	public Map<String, Object> doNotNeedSessionAndSecurity_deleteMember(String fid){
		logger.info("delete YBasicMember");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicMember obj = memberService.getById(fid);
			memberService.delete(obj);
			map.put("msg", "删除成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "删除失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 根据手机，姓名，邮箱，职位等条件查询
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findMemberByCondition")
	@ResponseBody
	@SystemControllerLog(description="根据手机，姓名，邮箱，职位等条件查询")
	public Map<String, Object> doNotNeedSessionAndSecurity_findMemberByCondition(String fmobilePhone,String fname,String femail,String positionFid){
		logger.info("findMemberByCondition");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String sql = "select y.* from y_basic_member y inner join y_basic_memberdetail d on d.FMemberID = y.fid "
					+ "inner join y_basicentries_memberdistribution mp on mp.FMemberID = y.fid "
					+ "where y.fmobilePhone = '" + fmobilePhone + "' and y.fname = '" + fname + "' and d.FEmail = '" + femail + "' and mp.FPositionID = '" + positionFid + "'";
			List<YBasicMember> list = memberService.findAllBySql(sql);
			map.put("msg", "删除成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "删除失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 审核、反审核、生效和失效操作
	 * @param fid
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateMemStatus")
	@ResponseBody
	@SystemControllerLog(description="审核、反审核、生效和失效操作")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateMemStatus(@RequestParam("fids")String fids,@RequestParam("status")int status){
		logger.info("updateEmpStatus YBasicMember");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			for(String id : fids.split(",")){
				YBasicMember obj = memberService.getById(id);
				if(status == 6){
					obj.setFisHidePhone(0);
				}
				obj.setFbillState(status);
				memberService.updateStatus(obj);
			}
			map.put("msg", "状态修改成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "状态修改失败");
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
	@SystemControllerLog(description="作废会员")
	public Map<String, Object> doNotNeedSessionAndSecurity_Invalid(String fid,String fbillState,String comment){
		logger.info("Invalid YBasicMember");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicMember mem=new YBasicMember();
			mem=memberService.getById(fid);
			mem.setFbillState(Integer.parseInt(fbillState));
			mem.setFcomment(comment);
			mem.setFisHidePhone(0);
			memberService.updateStatus(mem);
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
	 * 修改会员密码
	 * @param fid
	 * @return
	 */
	@RequestMapping("/updateMemberPassWord")
	@ResponseBody
	@SystemControllerLog(description="修改会员密码")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateMemberPassWord(String fid,String newpwd){
		logger.info("updateMemberPassWord");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicMember obj = memberService.getById(fid);
			obj.setFpassword(MD5Util.md5(newpwd));
			memberService.updateStatus(obj);
			map.put("msg", "密码修改成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "密码修改失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 根据会员ID查询会员职位分配的数据
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findMDByMemID")
	@ResponseBody
	@SystemControllerLog(description="根据会员ID查询会员职位分配的数据")
	public Map<String, Object> doNotNeedSessionAndSecurity_findMDByMemID(String fid){
		logger.info("findEDByPosID YBasicentriesMemberdistribution");
		Map<String, Object> map = new HashMap<String,Object>();
		List<YBasicentriesMemberdistribution> list = null;
		try {
			list = memberService.findMDByMemID(fid);
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
	@RequestMapping("/getByUserId")
	@ResponseBody
	@SystemControllerLog(description="getByUserId")
	public Map<String, Object> doNotNeedSessionAndSecurity_getByUserId(String adminId){
		logger.info("findEDByPosID YBasicentriesMemberdistribution");
		Map<String, Object> map = new HashMap<String,Object>();
		
		List<YBasicMember> memlist=new ArrayList<YBasicMember>();
		YBasicMember yb=new YBasicMember();
		try {
			memlist=memberService.findByHql("from YBasicMember as y where y.YSystemUsers.fid='"+adminId+"'");
			yb=memlist.get(0);
			
			map.put("memberId", yb.getFid());
			map.put("groupId", yb.getYBasicSocialgroups().getFid());
			map.put("orgId", yb.getYBasicSocialgroups().getYBasicOrganization().getFid());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 根据社会团体id查询会员
	 * @param groupId
	 * @return
	 */
	@RequestMapping("/findMemgerHql")
	@ResponseBody
	@SystemControllerLog(description="根据社会团体id查询会员")
	public Map<String, Object> findMemgerHql(String groupId){
		logger.info("find YBasicMember hql");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicMember> list = null;
		String hql = null;
		try {
			hql = "from YBasicMember y where y.YBasicSocialgroups.fid = '"+groupId+"' and y.fbillState = 5";
			list = memberService.findByHql(hql);
			map.put("obj", list);
			map.put("msg", "查询成功！");
			map.put("success", true);
		} catch (Exception e) {
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 批量更新会员是否隐藏状态
	 * @param String fids,int status
	 * @return
	 */
	@RequestMapping("/updateMembersHideStatus")
	@ResponseBody
	@SystemControllerLog(description="批量更新会员是否隐藏状态")
	public Map<String, Object> updateMembersHideStatus(String fids,int status){
		logger.info("find YBasicMember hql");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			memberService.updateMembersHideStatus(fids, status);
			map.put("msg", "更新成功！");
			map.put("success", true);
		} catch (Exception e) {
			map.put("msg", "更新失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 下载模板
	 * 
	 * @return
	 */
	@RequestMapping("/download")
	public void doNotNeedSessionAndSecurity_Download(
			HttpServletRequest request, HttpServletResponse response) {
		String fileName = "会员信息.xls";
		try {

			// 处理不同浏览器对文件名的解析问题
			String agent = (String) request.getHeader("USER-AGENT");
			if (agent != null && agent.indexOf("MSIE") == -1) { // FF
				// fileName = new String(fileName.getBytes("ISO-8859-1"),
				// "UTF-8");
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
				// fileName = "=?UTF-8?B?" + (new
				// String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) +
				// "?=";
			} else { // IE
				fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
			}
			response.setContentType("application/x-msdownload; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
//		style.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
		style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		
		HSSFCellStyle style2 = excel.createCellStyle(); // 样式对象
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 垂直
		style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
		style2.setWrapText(true); // 自动换行
		HSSFFont font2 = excel.createFont();// 设置字体
		font2.setColor(HSSFFont.COLOR_RED);// 设置字体颜色
		style2.setFont(font2);
		
		HSSFSheet sheet = excel.createSheet();
		excel.setSheetName(0, "会员信息");
		HSSFRow firstRow = sheet.createRow(0);

		HSSFCell cells[] = new HSSFCell[17];

		String[] titles = new String[] { "姓名", "昵称", "手机","性别","职位","出生年月", "来源", "籍贯", "省份", 
		"城市", "区县", "邮箱", "备用邮箱", "家庭电话", "备用手机","会员地址","备注"};

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
		sheet.setColumnWidth(15, 7000);
		sheet.setColumnWidth(16, 7000);

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

		for (int i = 0; i < 17; i++) {
			cells[0] = firstRow.createCell(i);
			cells[0].setCellValue(titles[i]);
			cells[0].setCellStyle(style);
		}

//		HSSFRow titleRow = sheet.createRow(rowNumber);
//		rowNumber++;
//		titleRow.setHeight((short) 0x180);// 设置行高
//		HSSFFont titlefont = excel.createFont();// 设置字体
//		titlefont.setColor(HSSFFont.COLOR_NORMAL);// 设置字体颜色
//		titlefont.setBoldweight((short) 1000);// 设置字体类型
//
//		HSSFCellStyle titleStyle = excel.createCellStyle();
//		titleStyle.setFont(titlefont);// 添加字体样式
//		titleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 设置对齐方式
//
		HSSFCellStyle cellStyle = excel.createCellStyle();
		cellStyle.setWrapText(true);
//
//		// 设置单元格边框
//		titleStyle.setBorderBottom((short) 1);
//		titleStyle.setBorderLeft((short) 1);
//		titleStyle.setBorderRight((short) 1);
//		titleStyle.setBorderTop((short) 1);

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
			cell = row.createCell(15);
			cell.setCellValue("");
			cell = row.createCell(16);
			cell.setCellValue("");
		}
		HSSFRow row = sheet.createRow(2);

		HSSFCell cell = row.createCell(0);
		//cell.setCellValue("名称、昵称、姓名、出生年月、手机 为必填项，");
		cell.setCellValue("姓名、昵称、手机、性别 、职位为必填项,性别只能是男或者女，注意，本条提示信息不能删除否则最后一条信息无法导入，如果要添加一行请选择插入一行。");
		cell.setCellStyle(style2);
		sheet.addMergedRegion(new Region(2, (short) 0, 2, (short) 16));
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
	@SystemControllerLog(description="导入")
	public Map<String,Object> doNotNeedSessionAndSecurity_import(String groupId,HttpServletRequest request,HttpServletResponse response) throws ParseException{
		Map<String,Object> map=new HashMap<String, Object>();
		
		String filepath = request.getSession().getServletContext().getRealPath("/function/template/ImportingDetails.lock");
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
								YBasicMember mem = new YBasicMember();
								try {
									mem.setFid(UUID.randomUUID().toString());
									mem.setFpassword(MD5Util.md5(BEGINSTRING + "888888"));
									mem.setFbillState(0);
									Map<String, Object> num = getNumberService.checkExist("HY-HYXX");
									mem.setFnumber((String)num.get("serialNumber"));
									
									mem.setFcreaterId((String)request.getSession().getAttribute("userId"));

									mem.setFcreateTime(new Date());
														
									mem.setIsAdmin(0);

									YBasicSocialgroups group=new YBasicSocialgroups();
									group.setFid(groupId);
									
									mem.setYBasicSocialgroups(group);
									mem.setFname(val[0]);
									mem.setFnickName(val[1]);
									String phone=EncryptionForTellPhone.encryptToABC(val[2]);
									List<YBasicMember> memberlist=new ArrayList<YBasicMember>();
									memberlist=memberService.findByHql("from YBasicMember as y where y.fmobilePhone='"+phone+"' and y.YBasicSocialgroups.fid='"+groupId+"' and y.fbillState !="+Status.ABOLISH.value());
									if(memberlist.size()==0){
										mem.setFmobilePhone(phone);
									}else{
										lockFile.delete();
										writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的手机号码本商会已存在，上传终止",lockFile,true);
										map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
										return map;
									}
									
//									mem.setFpassword(val[3]);
									if(val[3].equals("男")){
										mem.setFsex(0);
									}else{
										mem.setFsex(1);
									}
									
									
									if(null!=val[5]&&!"".equals(val[5])&&!"null".equals(val[5])){
//										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										Date birthday=sdf.parse(val[5]);
										mem.setFbirthday(birthday);
									}
									if(null!=val[6]&&!"".equals(val[6])&&!"null".equals(val[6])){
										mem.setFsource(val[6]);//来源
									}
									if(null!=val[7]&&!"".equals(val[7])&&!"null".equals(val[7])){
										mem.setFnativePlace(val[7]);//籍贯									
									}
									if(null!=val[8]&&!"".equals(val[8])&&!"null".equals(val[8])){
										List<YBasicProvince> provinces=new ArrayList<YBasicProvince>();
										provinces=provinceService.findByHql("from YBasicProvince as y where y.fname like '%"+val[8]+"%'");
										if(provinces.size()==1){
											mem.setYBasicProvince(provinces.get(0));
										}else{
											lockFile.delete();
											writeStatus(request,"您上传的文件第" + (i + 1)+"行输入省份的数据不对，上传终止",lockFile,true);
											map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
											return map;
										}
										
									}
									if(null!=val[9]&&!"".equals(val[9])&&!"null".equals(val[9])){
										List<YBasicCity> citys=new ArrayList<YBasicCity>();
										citys=cityService.getByHql("from YBasicCity as y where y.fname like '%"+val[9]+"%'");
										if(citys.size()==1){
											mem.setYBasicCity(citys.get(0));
										}else{
											lockFile.delete();
											writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的城市数据不对，上传终止",lockFile,true);
											map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
											return map;
										}
									}
									if(null!=val[10]&&!"".equals(val[10])&&!"null".equals(val[10])){
										List<YBasicCounty> countys=new ArrayList<YBasicCounty>();
										countys=countyService.getByHql("from YBasicCounty as y where y.fname like '%"+val[10]+"%'");
										if(countys.size()==1){
											mem.setYBasicCounty(countys.get(0));
										}else{
											lockFile.delete();
											writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的区县数据不对，上传终止",lockFile,true);
											map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
											return map;
										}
									}
									Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
									if(null!=val[11]&&!"".equals(val[11])&&!"null".equals(val[11])){
										Matcher m = p.matcher(val[11]);
										boolean b = m.matches();
										if(b){
											mem.setFemail(val[11]);// 邮箱
										}else{
											lockFile.delete();
											map.put("msg","您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止");
											writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的邮箱数据不对，上传终止",lockFile,true);
											return map;
										}
									}
									if(null!=val[12]&&!"".equals(val[12])&&!"null".equals(val[12])){
										Matcher h = p.matcher(val[12]);
										boolean s = h.matches();
										if(s){
											mem.setFpreviousEmail(val[12]);
										}else{
											lockFile.delete();
											writeStatus(request,"您上传的文件第" + (i + 1)+"行输入的数据不对，上传终止",lockFile,true);
											map.put("msg","您上传的文件第" + (i + 1)+"行输入的备用邮箱数据不对，上传终止");
											return map;
										}
									}
									if(null!=val[13]&&!"".equals(val[13])&&!"null".equals(val[13])){
										mem.setFhomePhone(val[13]);
									}
									if(null!=val[14]&&!"".equals(val[14])&&!"null".equals(val[14])){
										mem.setFsecondPhone(val[14]);
									}
									if(null!=val[15]&&!"".equals(val[15])&&!"null".equals(val[15])){
										mem.setFsite(val[15]);
									}
									if(null!=val[16]&&!"".equals(val[16])&&!"null".equals(val[16])){
										mem.setFcomment(val[16]);
									}
									mem.setFisHidePhone(1);//默认不隐藏电话号码
									mem.setFtypeId("1");
									try {										
										if(null!=val[4]&&!"".equals(val[4])&&!"null".equals(val[4])){
											
											/**
											 * 如果这个IM已经存在了就不在去添加，也不去增加
											 */
											if(imService.findaccount(mem.getFmobilePhone())==true){
												//保存生成的IM账号
												YBasicImaccount im = new YBasicImaccount();
												
												Map<String,String> userMap = new HashMap<String,String>();
												userMap.put("username", CRC32Util.getCRC32(mem.getFmobilePhone()));
												userMap.put("password", MD5Util.md5("888888"));//默认密码888888
												IMUtils.createUser(userMap);//新增
												im.setFimkey(CRC32Util.getCRC32(mem.getFmobilePhone()));
												im.setFimpassword(MD5Util.md5("888888"));
												im.setFimtel(phone);
												imService.sava(im);
											}
											
											
											//获取默认的十个标签
											List<YBasicLabel> list = LabelXMLToObject.labelObject(mem.getFmobilePhone());
											labelService.saveOrUpdateList(list);
										

											mem.setFbillState(Status.EFFECT.value());//状态为生效
											mem.setFcreateTime(new java.sql.Date(new java.util.Date().getTime()));
											memberService.memberSave(mem);
											getNumberService.addSerialNumber("HY-HYXX");
											
											
											int maxVersion = memdisService.getMaxVersion(groupId) + 1;
											List<YBasicPosition> poslist=new ArrayList<YBasicPosition>();
											poslist=posService.findByHql("from YBasicPosition as y where y.fsocialGroupsId ='"+groupId+"' and  y.fname='"+val[4]+"'");
											
											YBasicPosition pos=new YBasicPosition();
											if(poslist.size()==0){	
												Integer seq=0;
												List<YBasicPosition> positioncount=new ArrayList<YBasicPosition>();
												positioncount=posService.findByHql("from YBasicPosition as y where y.fsocialGroupsId ='"+groupId+"' order by y.fseq desc");
												if(positioncount.size()>0){
													seq=positioncount.get(0).getFseq()+1;
												}
												
												pos.setFid(UUID.randomUUID().toString());
												pos.setFsocialGroupsId(groupId);
												pos.setFname(val[4]);
												pos.setFcreaterId((String)request.getSession().getAttribute("userId"));
												pos.setPcreateTime(new Date());
												pos.setFbillState(5);
												pos.setFseq(seq);
												pos.setVersion(maxVersion);
												posService.save(pos);
											}else{
												pos=poslist.get(0);
												pos.setVersion(maxVersion);
												posService.update(pos);
											}
											YBasicentriesMemberdistribution obj=new YBasicentriesMemberdistribution();
											obj.setFid(UUID.randomUUID().toString());
											obj.setYBasicPosition(pos);
											obj.setYBasicMember(mem);
											obj.setFversion(maxVersion);
											obj.setFkeyPost("是");
											memdisService.save(obj);
											
										
											
											map.put("msg", "成功");
											lockFile.delete();
											writeStatus(request,"数据导入成功！",lockFile,true);
										}else{
											writeStatus(request,"您上传的文件第" + (i + 1)+"行职位为空跳过本条数据，请重新填写后上传",lockFile,true);
										}
										
										
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
	/**
	 * 根据团体查询会员
	 * @return
	 */
	@RequestMapping("/findByGroupId")
	@ResponseBody
	@SystemControllerLog(description="根据团体查询会员")
	public Object[] doNotNeedSessionAndSecurity_findByGroupId(String groupId){
		Map<String, Object> map = new HashMap<String,Object>();
		List list = new ArrayList();
		try {
			list = memberService.findByGroupId(groupId);
			map.put("msg", "查询成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return list.toArray();
	}
	
	
	public void writeStatus(HttpServletRequest request,String message,File lockFile,boolean flag){
		String filepath = request.getSession().getServletContext().getRealPath("/function/template/ImportingDetails.txt");
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
	@SystemControllerLog(description="downloadImportStatus")
	public void downloadImportStatus(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		BufferedInputStream bis = null;  
		BufferedOutputStream bos = null;
		try {
			request.setCharacterEncoding("UTF-8");
			String filepath = request.getSession().getServletContext().getRealPath("/function/template/ImportingDetails.txt");
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
	

	/**
	 * 生成树的方法
	 */
	@RequestMapping(value = "/getPositionTree")
	@ResponseBody
	@SystemControllerLog(description="posService getPositionTree")
	public JSONArray getPositionTree(String groupId){
		logger.info("posService getPositionTree");
		Map<String, Object> map = new HashMap<String,Object>();
		JSONArray arrayJson = new JSONArray();
		logger.info("************#########################"+groupId);
		try {
			List<Tree> treelist = new ArrayList<Tree>();
			createTree(treelist,groupId);
			arrayJson = treeToJson(treelist);
//			map.put("msg", "成功");
//			map.put("obj", arrayJson);
//			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			//map.put("msg", "失败");
			//map.put("success", false);
		}
		return arrayJson;
	}
	/**
	 * 创建树干
	 * @Title: createTree 
	 * @Description: TODO 
	 * @param     
	 * @return void   
	 * @throws
	 */
	private void createTree(List<Tree> treelist,String groupId){
		List<YBasicPosition> poslist = posService.findByHql("from YBasicPosition as y where y.fsocialGroupsId='"+groupId+"' and y.fbillState=5 order by  y.fseq");
		for(YBasicPosition pos:poslist){
			if(null==pos.getFsuperPositonId()||"".equals(pos.getFsuperPositonId())){
					Tree tree = new Tree();
					tree.setId(pos.getFid());
					tree.setText(pos.getFname());
					createTreechild(pos.getFid(),tree,groupId);
					treelist.add(tree);
				
			}
		}
	}
	/**
	 * 
	 * @Title: createTreechild 
	 * @Description: TODO 创建子树
	 * @param @param id
	 * @param @param tree    
	 * @return void   
	 * @throws
	 */
	private void createTreechild(String id,Tree tree,String groupId){
		List<YBasicPosition> list = posService.findByHql("from YBasicPosition as y where y.fsuperPositonId='"+id+"' and y.fsocialGroupsId='"+groupId+"'");
		Treechild(id,tree);
		if(list!=null&&list.size()>0){
			tree.setLeaf(false);
			for(YBasicPosition me : list){
				Tree child = new Tree();
				child.setId(me.getFid());
				child.setText(me.getFname());
				
				createTreechild(me.getFid(),child,groupId);
				
				tree.getChildren().add(child);
				
			}
		}
	}
	private void Treechild(String id,Tree tree){
		List<YBasicentriesMemberdistribution> poslist=memdisService.findPosByEdID(id);
		if(poslist !=null&& poslist.size()>0){
			tree.setLeaf(false);
			for(YBasicentriesMemberdistribution ye:poslist){
				if(ye.getYBasicMember().getFbillState()==5)
				{
					Tree child = new Tree();
					child.setId(ye.getYBasicMember().getFid());
					child.setText(ye.getYBasicMember().getFname());
					child.setLeaf(true);
					
					tree.getChildren().add(child);
				}
			}
		}else{
			tree.setLeaf(true);
		}
	}
	
	/**
	 * 
	 * @Title: treeToJson 
	 * @Description: TODO 迭代生成 Tree Json
	 * @param @param tree
	 * @param @return    
	 * @return JSONArray   
	 * @throws
	 */
	private JSONArray treeToJson(List<Tree> tree){
		JSONArray array = new JSONArray();
		for(Tree childrenTree : tree){
			JSONObject object = new JSONObject();
			object.put("cls", childrenTree.getCls());
			object.put("id", childrenTree.getId());
			object.put("leaf", childrenTree.getLeaf());
			if(childrenTree.getChildren().size() > 0){
				object.put("text", childrenTree.getText()+"("+childrenTree.getChildren().size()+")");
				object.put("children", treeToJson(childrenTree.getChildren()));
			}else
				object.put("text", childrenTree.getText());
			array.add(object);
		}
		return array;
	}
	class Tree {
		private String cls = "folder";
		private String id;

		private boolean leaf = false;
		private String text = "";
		private List<Tree> children = new ArrayList<Tree>();
		private int level;
		private String pid;

		public String getCls() {
			return cls;
		}

		public void setCls(String cls) {
			this.cls = cls;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public boolean getLeaf() {
			return leaf;
		}

		public void setLeaf(boolean leaf) {
			this.leaf = leaf;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public List<Tree> getChildren() {
			return children;
		}

		public void setChildren(List<Tree> children) {
			this.children = children;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}
	}
	
	
}
