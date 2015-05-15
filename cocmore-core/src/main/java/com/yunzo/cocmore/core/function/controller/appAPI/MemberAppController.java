package com.yunzo.cocmore.core.function.controller.appAPI;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.SetFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.Informationview;
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicGetadress;
import com.yunzo.cocmore.core.function.model.mysql.YBasicIndustry;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMembercompany;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsabout;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupscontact;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.model.mysql.YCompanyproduct;
import com.yunzo.cocmore.core.function.model.mysql.YInitiationApply;
import com.yunzo.cocmore.core.function.model.mysql.YManshowinformation;
import com.yunzo.cocmore.core.function.model.mysql.YSupplyGroup;
import com.yunzo.cocmore.core.function.service.ApplyService;
import com.yunzo.cocmore.core.function.service.CompanyproductServiceI;
import com.yunzo.cocmore.core.function.service.DemandService;
import com.yunzo.cocmore.core.function.service.GetadressServiceI;
import com.yunzo.cocmore.core.function.service.GroupsAboutService;
import com.yunzo.cocmore.core.function.service.GroupsContactService;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.service.GroupsService;
import com.yunzo.cocmore.core.function.service.GuaranteeServiceI;
import com.yunzo.cocmore.core.function.service.InformationviewServiceI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.MembercompanyServiceI;
import com.yunzo.cocmore.core.function.service.SupplyService;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.ResponseCode;
import com.yunzo.cocmore.utils.base.ConfigUtil;
import com.yunzo.cocmore.utils.base.IMGSize;
import com.yunzo.cocmore.utils.base.IpUtil;
import com.yunzo.cocmore.utils.base.YunzoCocSignCode;

/**
 * 我
 * 
 * @author yunzo
 * 
 */
@RestController
@RequestMapping("/mobileapi/my")
public class MemberAppController {

	@Resource
	MemberServiceI memberService; // 会员接口

	@Resource
	MembercompanyServiceI companyService; // 公司接口

	@Resource
	CompanyproductServiceI companyproductService; // 公司主营产品

	@Resource
	DemandService demandService; // 需求接口

	@Resource
	SupplyService supplyService; // 供应接口

	@Resource
	GroupsService groupsService; // 社会团体信息接口

	@Resource
	GroupsAboutService groupsAboutService; // 关于社会团体接口

	@Resource
	GroupsContactService groupsContactService; // 社会团体联系方式接口

	@Resource
	GuaranteeServiceI guaranteeService; // 供应担保关系表

	@Resource
	GetadressServiceI getadressService; // 收货地址

	@Resource
	InformationviewServiceI InformationviewService; // 通知视图的接口

	@Resource
	GroupsInformService groupsInformService; // 商会通知

	@Resource
	ApplyService applyService; // 入会申请

	private ObjectMapper objectMapper = new ObjectMapper();

	private Logger logger = Logger.getLogger(MemberAppController.class);

	/**
	 * 获取我的个人信息
	 * 
	 * @param infoMap
	 * @return
	 */
	@RequestMapping("/getMyInfo")
	@SystemControllerLog(description="APP 获取我的个人信息")
	public void doNotNeedSessionAndSecurity_getMyInfo(String infoMap,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String mid = (node.get("mid") == null ? null : node.get("mid").textValue());
			
			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			YBasicMember member=new YBasicMember();
			List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				member=memberlist.get(0);
				
				if (member != null && !member.equals("")) {
					if (member.getFheadImage() != null) {
						String img[] = member.getFheadImage().split(",");
						resultMap.put("userImageUrl", img[0]); // userImageUrl
																// String
																// 用户头像地址
					}
					resultMap.put("userNickName", member.getFname()); // userNickName
																		// String
																		// 用户昵称
					// resultMap.put("consigneeAddress",
					// member.getFreceivingAddress()); //consigneeAddress String
					// 收货地址
					boolean flag = false;
					String nativePlace = "";
					if (member.getYBasicProvince() != null) {
						nativePlace += member.getYBasicProvince().getFname();
						flag = true;
					}
					if (member.getYBasicCity() != null) {
						nativePlace += member.getYBasicCity().getFname();
						flag = true;
					}
					if (member.getYBasicCounty() != null) {
						nativePlace += member.getYBasicCounty().getFname();
						flag = true;
					}
					if (flag) {
						resultMap.put("nativePlace", nativePlace);
					} else {
						resultMap.put("nativePlace", null); // nativePlace
					}

					// String
					// 籍贯
					resultMap.put("sex", member.getFsex()); // sex String 性别(0男，1女)
					Date d = member.getFbirthday();
					String birthday = null;
					if (null != d) {
						birthday = new SimpleDateFormat("YYYY-MM-dd").format(d);
					}
					resultMap.put("birthday", birthday); // birthday String 生日
					Map<String, Object> adressMap = new HashMap<String, Object>();
					List<YBasicGetadress> list = new ArrayList<YBasicGetadress>();
					list = getadressService
							.findByHql("from YBasicGetadress y where y.YBasicMember.fid='"
									+ member.getFid() + "' and y.fisDefault='1'");
					if (list.size() > 0) {
						YBasicGetadress adress = new YBasicGetadress();
						adress = list.get(0);
						adressMap.put("consigneeAddress", adress.getFadress());// consigneeAddress
																				// String
																				// 收货地址
						adressMap.put("consignee", adress.getFharvestingName());// consignee
																				// String
																				// 收货人
						adressMap.put("consigneePhone", adress.getFharvestPhone());// consigneePhone
																					// String
																					// 收货人电话
						adressMap.put("provincialId", adress.getYBasicProvince()
								.getFid());// provincialId String 省份ID
						adressMap.put("cityId", adress.getYBasicCity().getFid());// cityId
																					// String
																					// 城市ID
						adressMap.put("countryId", adress.getYBasicCounty()
								.getFid());// countryId String 县ID
						adressMap.put("provincialName", adress.getYBasicProvince()
								.getFname());// provincialName String 省份名
						adressMap
								.put("cityName", adress.getYBasicCity().getFname());// cityName
																					// String
																					// 城市名
						adressMap.put("countryName", adress.getYBasicCounty()
								.getFname());// countryName String 县名
						adressMap.put("consigneeAddressId", adress.getFid());// consigneeAddressId
																				// String
																				// 收货信息ID
						resultMap.put("consignee", adressMap);
						map.put(ResponseCode.MSGC.msg(),
								ResponseCode.SUCCESS.value());
						map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
						map.put(ResponseCode.MSGR.msg(), resultMap);
					} else {
						resultMap.put("consignee", adressMap);
						map.put(ResponseCode.MSGC.msg(),
								ResponseCode.SUCCESS.value());
						map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
						map.put(ResponseCode.MSGR.msg(), resultMap);
					}

				} else {
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				}
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
				COC_APPResponseResult.responseToGJson(map, response);
			}	
			 
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);

	}

	/**
	 * 获取我的公司信息（一个会员对应一个公司）
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getMyCompanyInfo")
	@SystemControllerLog(description="APP 获取我的公司信息（一个会员对应一个公司）")
	public void doNotNeedSessionAndSecurity_getMyCompanyInfo(String infoMap,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String mid = (node.get("mid") == null ? null : node.get("mid")
					.textValue());
			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
			
			
			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			YBasicMember member=new YBasicMember();
			List<YBasicMember> memberlist=new ArrayList<YBasicMember>();
			if(null==businessId||"".equals(businessId)){
				 memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			}else{
				 memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'  and  y.YBasicSocialgroups.fid='"+businessId+"'");
			}
//			YBasicMember member=new YBasicMember();
//			List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'  and  y.YBasicSocialgroups.fid='"+businessId+"'");
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				member=memberlist.get(0);
				YBasicMembercompany company = new YBasicMembercompany();
				List<YBasicMembercompany> list = (List<YBasicMembercompany>) companyService
						.findAllByIndAndTrade("from YBasicMembercompany as y where y.YBasicMember.fid='"
								+ member.getFid() + "'");
				if (list.size() > 0) {
					company = list.get(0);
					if (null != company) {
						String logo = company.getFcompanyLogo();
						if (logo != null) {
							String[] companyLogo = logo.split(",");
							resultMap.put("companyLogo", companyLogo[0]);// companyLogo
																			// String
																			// 公司logo
						}
						resultMap.put("companyName", company.getFcname());// companyName
																			// String
																			// 公司名字
						resultMap.put("companyProperty",
								company.getFcompanyNature());// companyProperty
																// String 公司性质
						resultMap.put("companySize", company.getFcompanyScale());// companySize
																					// String
																					// 公司规模
						resultMap.put("companyTrade",
								(company.getYBasicTrade() == null ? null : company
										.getYBasicTrade().getFname()));// companyTrade
																		// String
																		// 公司行业
						resultMap.put("companyTradeId",
								(company.getYBasicTrade() == null ? null : company
										.getYBasicTrade().getFid()));// companyTradeId
																		// String
																		// 公司行业Id
						resultMap.put("companyPhone", company.getFctelphone());// companyPhone
																				// String
																				// 公司电话
						resultMap.put("companyEmail", company.getFcemail());// companyEmail
																			// String
																			// 公司邮箱
						resultMap.put("companyUrl", company.getFcurl());// companyUrl
																		// String
																		// 公司网址
						resultMap.put("companyAddress", company.getFclocation());// companyAddress
																					// String
																					// 公司地址
						resultMap.put("companyLocation",
								company.getFcompanyLatitude());// companyLocation
																// String
																// 公司经纬度(百度地址经纬度11.11,22.22经度在前纬度在后)
						resultMap
								.put("companyProfile", company.getFcintroduction());// companyProfile
																					// String
																					// 公司简介
						resultMap.put("companyPosition",
								company.getFcompanyPosition());// companyPosition
																// String 公司职位
						map.put(ResponseCode.MSGC.msg(),
								ResponseCode.SUCCESS.value());
						map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
						map.put(ResponseCode.MSGR.msg(), resultMap);
					} else {
						map.put(ResponseCode.MSGC.msg(),
								ResponseCode.EXCEPTION.value());
						map.put(ResponseCode.MSGM.msg(),
								ResponseCode.EXCEPTION.msg());
					}
				} else {
					resultMap.put("companyName", null);// companyName String 公司名字
					resultMap.put("companyProperty", null);// companyProperty String
															// 公司性质
					resultMap.put("companySize", null);// companySize String 公司规模
					resultMap.put("companyTrade", null);// companyTrade String 公司行业
					resultMap.put("companyTradeId", null);// companyTradeId String
															// 公司行业Id
					resultMap.put("companyPhone", null);// companyPhone String 公司电话
					resultMap.put("companyEmail", null);// companyEmail String 公司邮箱
					resultMap.put("companyUrl", null);// companyUrl String 公司网址
					resultMap.put("companyAddress", null);// companyAddress String
															// 公司地址
					resultMap.put("companyLocation", null);// companyLocation String
															// 公司经纬度(百度地址经纬度11.11,22.22经度在前纬度在后)
					resultMap.put("companyProfile", null);// companyProfile String
															// 公司简介

					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
					map.put(ResponseCode.MSGR.msg(), resultMap);
				}
				
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), resultMap);
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);

	}

	/**
	 * 设置我的个人信息
	 * 
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/saveMySetInfo")
	@SystemControllerLog(description="APP 设置我的个人信息")
	public void doNotNeedSessionAndSecurity_saveMySetInfo(
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		MultipartFile file = null;
		String images = null;
		// 验证请求是否有效
		Map<String, Object> ImgInfoMap = null;
		try {
			request.setCharacterEncoding("utf-8");
			String infoMap = request.getParameter("infoMap");
			if (request instanceof DefaultMultipartHttpServletRequest) {
				DefaultMultipartHttpServletRequest rest = (DefaultMultipartHttpServletRequest) request;
				MultipartHttpServletRequest r=(MultipartHttpServletRequest)request;
				ImgInfoMap = ImgUploadUtil.imgUpload(r,IMGSize.X200.value());
				if (ImgInfoMap != null)
					if ((boolean) ImgInfoMap.get("success") == true) {
						if ((ImgInfoMap.get("imgsrc")) != null) {
							images = (ImgInfoMap.get("imgsrc")).toString();
						}
					}
			}

			map=memberService.updateByAPP(infoMap,images);
			
			 

			 
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}

		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 设置我的公司信息（一个会员对应一个公司）
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/saveMyCompanySetInfo")
	@SystemControllerLog(description="APP 设置我的公司信息（一个会员对应一个公司）")
	public void doNotNeedSessionAndSecurity_saveMyCompanySetInfo(
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		MultipartFile file = null;
		String images = null;
		// 验证请求是否有效
		Map<String, Object> ImgInfoMap = null;
		try {
			request.setCharacterEncoding("utf-8");
			String infoMap = request.getParameter("infoMap");

			if (request instanceof DefaultMultipartHttpServletRequest) {
				DefaultMultipartHttpServletRequest rest = (DefaultMultipartHttpServletRequest) request;
				MultipartHttpServletRequest r=(MultipartHttpServletRequest)request;
				ImgInfoMap = ImgUploadUtil.imgUpload(r,IMGSize.X200.value());
				if (ImgInfoMap != null)
					if ((boolean) ImgInfoMap.get("success") == true) {
						if ((ImgInfoMap.get("imgsrc")) != null) {
							images = (ImgInfoMap.get("imgsrc")).toString();
						}
					}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();

			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String mid = node.get("mid").textValue();
			String companyTradeId = (node.get("companyTradeId") == null ? null
					: node.get("companyTradeId").textValue());
			String companyName = (node.get("companyName") == null ? null : node
					.get("companyName").textValue());
			String companyProperty = (node.get("companyProperty") == null ? null
					: node.get("companyProperty").textValue());
			String companySize = (node.get("companySize") == null ? null : node
					.get("companySize").textValue());
			String companyPhone = (node.get("companyPhone") == null ? null
					: node.get("companyPhone").textValue());
			String companyEmail = (node.get("companyEmail") == null ? null
					: node.get("companyEmail").textValue());
			String companyUrl = (node.get("companyUrl") == null ? null : node
					.get("companyUrl").textValue());
			String companyProfile = (node.get("companyProfile") == null ? null
					: node.get("companyProfile").textValue());
			String companyAddress = (node.get("companyAddress") == null ? null
					: node.get("companyAddress").textValue());
			String companyLocation = (node.get("companyLocation") == null ? null
					: node.get("companyLocation").textValue());
			String companyPosition = (node.get("companyPosition") == null ? null
					: node.get("companyPosition").textValue());
			
			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
			
			
			if(null==mid||"".equals(mid)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			YBasicMember member=new YBasicMember();
			List<YBasicMember> memberlist=new ArrayList<YBasicMember>();
			if(null==businessId||"".equals(businessId)){
				memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			}else{
				memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'  and  y.YBasicSocialgroups.fid='"+businessId+"'");
			}
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				member=memberlist.get(0);
				YBasicMembercompany company = new YBasicMembercompany();
				List<YBasicMembercompany> list = new ArrayList<YBasicMembercompany>();
				list = (List<YBasicMembercompany>) companyService
						.findAllByIndAndTrade("from YBasicMembercompany as y where y.YBasicMember.fid='"
								+ member.getFid() + "'");

				if (list.size() > 0) {
					company = list.get(0);
					if (!"".equals(companyTradeId) && null != companyTradeId) {
						YBasicTrade trade = new YBasicTrade();
						trade.setFid(companyTradeId);
						company.setYBasicTrade(trade);// companyTrade String 否 公司行业
					}
					if (!"".equals(companyName) && null != companyName) {
						company.setFcname(companyName);// companyName String 否 公司名字
					}
					if (!"".equals(companyProperty) && null != companyProperty) {
						company.setFcompanyNature(companyProperty);// companyProperty
																	// String 否 公司性质
					}
					if (!"".equals(companySize) && null != companySize) {
						company.setFcompanyScale(companySize);// companySize String
																// 否 公司规模
					}
					if (!"".equals(companyPhone) && null != companyPhone) {
						company.setFctelphone(companyPhone);// companyPhone String 否
															// 公司电话
					}
					if (!"".equals(companyEmail) && null != companyEmail) {
						company.setFcemail(companyEmail);// companyEmail String 否
															// 公司邮箱
					}
					if (!"".equals(companyUrl) && null != companyUrl) {
						company.setFcurl(companyUrl);// companyUrl String 否 公司网址
					}
					if (!"".equals(companyProfile) && null != companyProfile) {
						company.setFcintroduction(companyProfile);// companyProfile
																	// String 否 公司简介
					}
					if (!"".equals(companyAddress) && null != companyAddress) {
						company.setFclocation(companyAddress);// companyAddress
																// String 否 公司地址
					}
					if (!"".equals(companyLocation) && null != companyLocation) {
						company.setFcompanyLatitude(companyLocation);// companyLocation
																		// String 否
																		// 公司经纬度(百度地址经纬度11.11,22.22经度在前纬度在后)
					}
					if (!"".equals(companyPosition) && null != companyPosition) {
						company.setFcompanyPosition(companyPosition);// companyPosition
																		// String 否
																		// 公司职位
					}
					if (images != null) {
						company.setFcompanyLogo(images);
					}
					companyService.updateByApp(company);
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				} else {

					if (!"".equals(companyTradeId) && null != companyTradeId) {
						YBasicTrade trade = new YBasicTrade();
						trade.setFid(companyTradeId);
						company.setYBasicTrade(trade);// companyTrade String 否 公司行业
					}
					if (!"".equals(companyName) && null != companyName) {
						company.setFcname(companyName);// companyName String 否 公司名字
					}
					if (!"".equals(companyProperty) && null != companyProperty) {
						company.setFcompanyNature(companyProperty);// companyProperty
																	// String 否 公司性质
					}
					if (!"".equals(companySize) && null != companySize) {
						company.setFcompanyScale(companySize);// companySize String
																// 否 公司规模
					}
					if (!"".equals(companyPhone) && null != companyPhone) {
						company.setFctelphone(companyPhone);// companyPhone String 否
															// 公司电话
					}
					if (!"".equals(companyEmail) && null != companyEmail) {
						company.setFcemail(companyEmail);// companyEmail String 否
															// 公司邮箱
					}
					if (!"".equals(companyUrl) && null != companyUrl) {
						company.setFcurl(companyUrl);// companyUrl String 否 公司网址
					}
					if (!"".equals(companyProfile) && null != companyProfile) {
						company.setFcintroduction(companyProfile);// companyProfile
																	// String 否 公司简介
					}
					if (!"".equals(companyAddress) && null != companyAddress) {
						company.setFclocation(companyAddress);// companyAddress
																// String 否 公司地址
					}
					if (!"".equals(companyLocation) && null != companyLocation) {
						company.setFcompanyLatitude(companyLocation);// companyLocation
																		// String 否
																		// 公司经纬度(百度地址经纬度11.11,22.22经度在前纬度在后)
					}
					if (!"".equals(companyPosition) && null != companyPosition) {
						company.setFcompanyPosition(companyPosition);// companyPosition
																		// String 否
																		// 公司职位
					}
					// company.set// companyTradeId String 否 公司行业Id
					// company.setFcproducts(node.get("companyMainProduct").textValue());//
					// companyMainProduct String 否 公司主营产品(多条信息)
					if (images != null) {
						company.setFcompanyLogo(images);
					}
					company.setYBasicMember(member);
					companyService.saveByApp(company);
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				}
				
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			 

		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}

		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 获取商会信息
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getBusinessInfo")
	@SystemControllerLog(description="APP 获取商会信息")
	public void doNotNeedSessionAndSecurity_getBusinessInfo(String infoMap,HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
		map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		map.put(ResponseCode.MSGR.msg(), request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/cocmore-web/webApp/gytt.html");
		// 需要模板页面
		COC_APPResponseResult.responseToGJson(map, response);

	}

	/**
	 * 获取我的公司主营产品列表
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getCompanyList")
	@SystemControllerLog(description="APP 获取我的公司主营产品列表")
	public void doNotNeedSessionAndSecurity_getCompanyList(String infoMap,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			Integer Size = (node.get("pageSize") == null ? 748 : node.get(
					"pageSize").intValue());
			String fid = (node.get("productId") == null ? null : node.get(
					"productId").textValue());
			String mid = (node.get("mid") == null ? null : node.get("mid")
					.textValue());
			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
			
			
			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (748 == Size) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "pageSize 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			YBasicMember member=new YBasicMember();
			List<YBasicMember> memberlist=new ArrayList<YBasicMember>();
			if(null==businessId||"".equals(businessId)){
				memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			}else{
				memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'  and  y.YBasicSocialgroups.fid='"+businessId+"'");
			}
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				member=memberlist.get(0);
				resultMap = companyproductService.findList(fid, Size, member.getFid());
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), resultMap);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), resultMap);
			}
			
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			e.printStackTrace();
		}
		COC_APPResponseResult.responseToGJson(map, response);

	}

	/**
	 * 编辑公司主营产品
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/saveCompanyProduct")
	@SystemControllerLog(description="APP 编辑公司主营产品")
	public void doNotNeedSessionAndSecurity_saveCompanyProduct(
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		MultipartFile file = null;
		String images = null;
		// 验证请求是否有效
		Map<String, Object> ImgInfoMap = null;
		try {
			request.setCharacterEncoding("utf-8");
			String infoMap = request.getParameter("infoMap");
			if (request instanceof DefaultMultipartHttpServletRequest) {
				DefaultMultipartHttpServletRequest rest = (DefaultMultipartHttpServletRequest) request;
				MultipartHttpServletRequest r=(MultipartHttpServletRequest)request;
				ImgInfoMap = ImgUploadUtil.imgUpload(r,IMGSize.X200.value());
				if (ImgInfoMap != null)
					if ((boolean) ImgInfoMap.get("success") == true) {
						if ((ImgInfoMap.get("imgsrc")) != null) {
							images = (ImgInfoMap.get("imgsrc")).toString();
						}
					}
			}

			Map<String, Object> resultMap = new HashMap<String, Object>();

			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String fid = (node.get("productId") == null ? null : node.get(
					"productId").textValue());
			String productName = (node.get("productName") == null ? null : node
					.get("productName").textValue());
			String productDetail = (node.get("productDetail") == null ? null
					: node.get("productDetail").textValue());

			if (null == fid || "".equals(fid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "productId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == productName || "".equals(productName)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "productName 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == productDetail || "".equals(productDetail)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "productDetail 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			YCompanyproduct companyProduct = companyproductService.getById(fid);
			if (companyProduct != null) {
				companyProduct.setFname(productName);
				companyProduct.setFdescription(productDetail);

				companyProduct.setFlogoImage(images);

				companyproductService.update(companyProduct);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.FORMWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.FORMWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}

		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 创建公司新主营产品
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/addCompanyProduct")
	@SystemControllerLog(description="APP 创建公司新主营产品")
	public void doNotNeedSessionAndSecurity_addCompanyProduct(
			HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		MultipartFile file = null;
		String images = null;
		// 验证请求是否有效
		Map<String, Object> ImgInfoMap = null;
		try {
			request.setCharacterEncoding("utf-8");
			String infoMap = request.getParameter("infoMap");
			if (request instanceof DefaultMultipartHttpServletRequest) {
				DefaultMultipartHttpServletRequest rest = (DefaultMultipartHttpServletRequest) request;
				MultipartHttpServletRequest r=(MultipartHttpServletRequest)request;
				ImgInfoMap = ImgUploadUtil.imgUpload(r,IMGSize.X200.value());
				if (ImgInfoMap != null)
					if ((boolean) ImgInfoMap.get("success") == true) {
						if ((ImgInfoMap.get("imgsrc")) != null) {
							images = (ImgInfoMap.get("imgsrc")).toString();
						}
					}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();

			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);

			if (images != null) {
				
				String mid = (node.get("mid") == null ? null : node.get(
						"mid").textValue());

				String productName = (node.get("productName") == null ? null
						: node.get("productName").textValue());
				String productDetail = (node.get("productDetail") == null ? null
						: node.get("productDetail").textValue());
				String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
				
				
				if (null == productName || "".equals(productName)) {
					map.put(ResponseCode.MSGC.msg(), 103);
					map.put(ResponseCode.MSGM.msg(), "productName 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if (null == productDetail || "".equals(productDetail)) {
					map.put(ResponseCode.MSGC.msg(), 103);
					map.put(ResponseCode.MSGM.msg(), "productDetail 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if (null == mid || "".equals(mid)) {
					map.put(ResponseCode.MSGC.msg(), 103);
					map.put(ResponseCode.MSGM.msg(), "mid 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				
				
				
				YBasicMember member=new YBasicMember();
				List<YBasicMember> memberlist=new ArrayList<YBasicMember>();
				if(null==businessId||"".equals(businessId)){
					memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
				}else{
					memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'  and  y.YBasicSocialgroups.fid='"+businessId+"'");
				}
				if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
					member=memberlist.get(0);
					
					YBasicMembercompany company = new YBasicMembercompany();
					List<YBasicMembercompany> list = (List<YBasicMembercompany>) companyService
							.findAllByIndAndTrade("from YBasicMembercompany as y where y.YBasicMember.fid='"
									+ member.getFid() + "'");
					company = list.get(0);
					if(company!=null&&!"".equals(company)){
						YCompanyproduct companyProduct = new YCompanyproduct();

						companyProduct.setFname(productName);
						companyProduct.setFdescription(productDetail);
						companyProduct.setYBasicMembercompany(company);
						if(images!=null&&!"".equals(images)){
							companyProduct.setFlogoImage(images);
						}
						companyproductService.save(companyProduct);
						map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
						map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
					}else{
						map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
						map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
					}
					
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}

		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 删除公司主营产品
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/delCompanyProduct")
	@SystemControllerLog(description="APP 删除公司主营产品")
	public void doNotNeedSessionAndSecurity_delCompanyProduct(String infoMap,
			HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		// productId String 是 产品Id
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String fid = (node.get("productId") == null ? null : node.get(
					"productId").textValue());

			if (null == fid || "".equals(fid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "fid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			YCompanyproduct companyProduct = companyproductService.getById(fid);

			if (companyProduct != null) {
				companyproductService.delete(companyProduct);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.FORMWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.FORMWRONG.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 获取我的供需列表
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getSupplyAndDemandList")
	@SystemControllerLog(description="APP 获取我的供需列表")
	public void doNotNeedSessionAndSecurity_getSupplyAndDemandList(
			String infoMap, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);

			String fid = (node.get("fId") == null ? null : node.get("fId")
					.textValue());// fId String 是 供需ID(如果为空字符串则表示获取最新的数据)
			Integer type = (node.get("supplyDemandType") == null ? 748 : node
					.get("supplyDemandType").intValue());// supplyDemandType Int
															// 是 供需类型（0供应,1需求）
 			Integer pageSize = (node.get("pageSize") == null ? 748 : node.get(
					"pageSize").intValue());// pageSize Int 是 分页大小
			String mid = (node.get("mid") == null ? null : node.get("mid")
					.textValue());
//			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
//			
//			if(null==businessId||"".equals(businessId)){
//				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
//				map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
//				 COC_APPResponseResult.responseToGJson(map, response);
//			}
			if (748 == type) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "type 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (748 == pageSize) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "pageSize 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}

			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				if (type == 0) {
					resultMap = supplyService.findByMyList(memberlist, pageSize, fid);
				} else {
					resultMap = demandService.findByMyList(memberlist, pageSize, fid);
				}
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), resultMap);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			e.printStackTrace();
		}

		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 隐藏某条发布的供需
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/saveSupplyAndDemandListState")
	@SystemControllerLog(description="APP 编辑我的供需")
	public void doNotNeedSessionAndSecurity_saveSupplyAndDemandListState(
			String infoMap, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			// fId String 是 供需ID(如果为空字符串则表示获取最新的数据)
			// supplyDemandType Int 是 供需类型（0供应,1需求）
			// isHide Int 是 是否隐藏该条供需（0显示，1影藏）
			JsonNode node = objectMapper.readTree(infoMap);
			Integer type = (node.get("supplyDemandType") == null ? 748 : node
					.get("supplyDemandType").intValue());
			Integer isHide = (node.get("isHide") == null ? 748 : node.get(
					"isHide").intValue());
			String fid = (node.get("fId") == null ? null : node.get("fId")
					.textValue());

			if (748 == type) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (748 == isHide) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "isHide 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}

			if (type == 0) {
				YBasicSocialgroupssupply supply = supplyService.getById(fid);
				supply.setFisHide(isHide);
				supplyService.update(supply);
			} else {
				YBasicSocialgroupsdemand demand = demandService.getById(fid);
				demand.setFisHide(isHide);
				demandService.update(demand);
			}
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 编辑我的供需
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/updateSupplyAndDemandInfo")
	@SystemControllerLog(description="APP 编辑我的供需")
	public void doNotNeedSessionAndSecurity_updateSupplyAndDemandInfo(
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效
		MultipartFile file = null;
		String images = null;
		YBasicSocialgroupsdemand demand = null;
		YBasicSocialgroupssupply supply = null;
		// 验证请求是否有效
		Map<String, Object> ImgInfoMap = null;
		try {
			request.setCharacterEncoding("utf-8");
			String infoMap = request.getParameter("infoMap");
			if (request instanceof DefaultMultipartHttpServletRequest) {
				DefaultMultipartHttpServletRequest rest = (DefaultMultipartHttpServletRequest) request;
				MultipartHttpServletRequest r=(MultipartHttpServletRequest)request;
				ImgInfoMap = ImgUploadUtil.imgUpload(r,IMGSize.X200.value());
				if (ImgInfoMap != null)
					if ((boolean) ImgInfoMap.get("success") == true) {
						if ((ImgInfoMap.get("imgsrc")) != null) {
							images = (ImgInfoMap.get("imgsrc")).toString();
						}
					}
			}

			Map<String, Object> resultMap = new HashMap<String, Object>();

			infoMap = URLDecoder.decode(infoMap, "UTF-8");

			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap,
					Map.class);

			String provincialId = (String) jsonObj.get("provincialId");
			String cityId = (String) jsonObj.get("cityId");
			String countryId = (String) jsonObj.get("countryId");
			String tradeId = (String) jsonObj.get("tradeId");
			String contactsPhone = (String) jsonObj.get("contactsPhone");
			String contactsPerson = (String) jsonObj.get("contactsPerson");

			String expDateStop = (String) jsonObj.get("expDateStop");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date stopDate = null;
			if (null != expDateStop && !"".equals(expDateStop)) {
				stopDate = sdf.parse(expDateStop);
			}

			String expDateStart = (String) jsonObj.get("expDateStart");
			SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date startDate = null;
			if (null != expDateStart && !"".equals(expDateStart)) {
				startDate = sdfs.parse(expDateStart);
			}

			String fTitle = (String) jsonObj.get("fTitle");
			String fContent = (String) jsonObj.get("fContent");

			// String file = (String) jsonObj.get("file");

			int isWarrant = (jsonObj.get("isWarrant") == null ? 748 : (Integer
					.parseInt(jsonObj.get("isWarrant").toString())));

			// int type = (int) jsonObj.get("type");

			String fId = (String) jsonObj.get("fId");
			JsonNode warrantBusiness = objectMapper.readTree(infoMap);

			// String businessId =
			// jsonObjNode.path("businessId").textValue();
			JsonNode warrantBusinessObject = warrantBusiness
					.path("warrantBusiness");
			JsonNode warrantTypeList = warrantBusinessObject
					.path("warrantType");

			if (null == provincialId || "".equals(provincialId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "provincialId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == cityId || "".equals(cityId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "cityId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == countryId || "".equals(countryId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "countryId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == tradeId || "".equals(tradeId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "tradeId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == contactsPhone || "".equals(contactsPhone)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "contactsPhone 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == contactsPerson || "".equals(contactsPerson)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "contactsPerson 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == expDateStop || "".equals(expDateStop)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "expDateStop 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == expDateStart || "".equals(expDateStart)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "expDateStart 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}

			if (null == fTitle || "".equals(fTitle)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "fTitle 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == fContent || "".equals(fContent)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "fContent 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (748 == isWarrant) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "isWarrant 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}

			if (null == warrantBusinessObject || "".equals(warrantBusiness)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "warrantBusiness 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}

			if (null == warrantTypeList || "".equals(warrantTypeList)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "warrantType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}

			demand = new YBasicSocialgroupsdemand();
			supply = new YBasicSocialgroupssupply();
			supply = supplyService.getById(fId);
			demand = demandService.getById(fId);

			if (null == supply) {
				// demand = new YBasicSocialgroupsdemand();
				YBasicProvince province = new YBasicProvince();
				province.setFid(provincialId);
				demand.setYBasicProvince(province);
				YBasicCity city = new YBasicCity();
				city.setFid(cityId);
				demand.setYBasicCity(city);
				YBasicCounty county = new YBasicCounty();
				county.setFid(countryId);
				demand.setYBasicCounty(county);
				YBasicTrade trade = new YBasicTrade();
				trade.setFid(tradeId);
				demand.setYBasicTrade(trade);
				demand.setFtel(contactsPhone);
				demand.setFcontacts(contactsPerson);
				demand.setFfinishTime(stopDate);
				demand.setFstartTime(startDate);
				demand.setFheadline(fTitle);
				demand.setFmessage(fContent);

				demand.setFimages(images);

				demandService.update(demand);
			} else {
				// supply = new YBasicSocialgroupssupply();
				YBasicProvince province = new YBasicProvince();
				province.setFid(provincialId);
				supply.setYBasicProvince(province);
				YBasicCity city = new YBasicCity();
				city.setFid(cityId);
				supply.setYBasicCity(city);
				YBasicCounty county = new YBasicCounty();
				county.setFid(countryId);
				supply.setYBasicCounty(county);
				YBasicTrade trade = new YBasicTrade();
				trade.setFid(tradeId);
				supply.setYBasicTrade(trade);
				supply.setFtel(contactsPhone);
				supply.setFcontacts(contactsPerson);
				supply.setFexpireTime(stopDate);
				supply.setFpublisherTime(startDate);
				supply.setFheadline(fTitle);
				supply.setFmessage(fContent);

				supply.setFimages(images);

				supply.setFareGuarantee(isWarrant);
				supplyService.update(supply);

			}

			List<YSupplyGroup> supplyGroups = guaranteeService
					.findByHql("from YSupplyGroup as y where y.YBasicSocialgroupssupply.fid='"
							+ fId + "'");

			if (warrantTypeList.isArray()) {
				for (JsonNode node : warrantTypeList) {
					logger.info("供应的担保信息：" + node.textValue());
					boolean bl = false;
					for (YSupplyGroup sg : supplyGroups) {
						if (node.asText().equals(
								sg.getYBasicAssurancecontent().getFid())) {
							bl = true;
						}
					}
					if (!bl) {
						YSupplyGroup supplyGroup = new YSupplyGroup();
						YBasicAssurancecontent yBasicAssurancecontent = new YBasicAssurancecontent();
						yBasicAssurancecontent.setFid(node.asText());
						YBasicSocialgroupssupply yBasicSocialgroupssupply = new YBasicSocialgroupssupply();
						yBasicSocialgroupssupply.setFid(fId);
						supplyGroup.setYBasicAssurancecontent(yBasicAssurancecontent);
						supplyGroup.setYBasicSocialgroupssupply(yBasicSocialgroupssupply);
						guaranteeService.save(supplyGroup);
					}

				}
			}
			map.put(ResponseCode.MSGC.msg(), 100);
			map.put(ResponseCode.MSGM.msg(), "操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), 103);
			map.put(ResponseCode.MSGM.msg(), "操作失败！");
		}

		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 获取我的收货地址列表
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/consigneeAddress")
	@SystemControllerLog(description="APP  获取我的收货地址列表")
	public void doNotNeedSessionAndSecurity_consigneeAddress(String infoMap,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String mid = (node.get("mid") == null ? null : node.get("mid")
					.textValue());
//			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
//			
//			if(null==businessId||"".equals(businessId)){
//				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
//				map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
//				 COC_APPResponseResult.responseToGJson(map, response);
//			}
			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				
				List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
				
				resultMap = getadressService.findByMemberList(memberlist);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), resultMap);
				
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 新增我的收货地址
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/addConsigneeAddress")
	@SystemControllerLog(description="APP 新增我的收货地址")
	public void addConsigneeAddress(String infoMap, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String mid = (node.get("mid") == null ? null : node.get("mid")
					.textValue());
			String consigneeAddress = (node.get("consigneeAddress") == null ? null
					: node.get("consigneeAddress").textValue());// consigneeAddress
																// String 是 收货地址
			String consignee = (node.get("consignee") == null ? null : node
					.get("consignee").textValue());// consignee String 是 收货人
			String consigneePhone = (node.get("consigneePhone") == null ? null
					: node.get("consigneePhone").textValue());// consigneePhone
																// String 是
																// 收货人电话
			String provincialId = (node.get("provincialId") == null ? null
					: node.get("provincialId").textValue());// provincialId
															// String 是 省份ID
			String cityId = (node.get("cityId") == null ? null : node.get(
					"cityId").textValue());// cityId String 是 城市ID
			String countryId = (node.get("countryId") == null ? null : node
					.get("countryId").textValue());// countryId String 是 县ID
//			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
//			
//			if(null==businessId||"".equals(businessId)){
//				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
//				map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
//				 COC_APPResponseResult.responseToGJson(map, response);
//			}
			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == consigneeAddress || "".equals(consigneeAddress)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "consigneeAddress 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == consignee || "".equals(consignee)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "consignee 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == consigneePhone || "".equals(consigneePhone)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "consigneePhone 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == provincialId || "".equals(provincialId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "provincialId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == cityId || "".equals(cityId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "cityId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == countryId || "".equals(countryId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "countryId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			YBasicMember member=new YBasicMember();
			List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				member=memberlist.get(0);
				if (null != member && null != provincialId && null != cityId
						&& null != countryId) {
					map = getadressService.saveAdress(member.getFid(), consigneeAddress,
							consignee, consigneePhone, provincialId, cityId,
							countryId);
				} else {
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				}
				
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 修改我的收货地址
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/updateConsigneeAddress")
	@SystemControllerLog(description="APP 修改我的收货地址")
	public void updateConsigneeAddress(String infoMap,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String consigneeAddressId = (node.get("consigneeAddressId") == null ? null
					: node.get("consigneeAddressId").textValue());// consigneeAddressId
																	// String 是
																	// 收货地址ID
			String consigneeAddress = (node.get("consigneeAddress") == null ? null
					: node.get("consigneeAddress").textValue());// consigneeAddress
																// String 是 收货地址
			String consignee = (node.get("consignee") == null ? null : node
					.get("consignee").textValue());// consignee String 是 收货人
			String consigneePhone = (node.get("consigneePhone") == null ? null
					: node.get("consigneePhone").textValue());// consigneePhone
																// String 是
																// 收货人电话
			String provincialId = (node.get("provincialId") == null ? null
					: node.get("provincialId").textValue());// provincialId
															// String 是 省份ID
			String cityId = (node.get("cityId") == null ? null : node.get(
					"cityId").textValue());// cityId String 是 城市ID
			String countryId = (node.get("countryId") == null ? null : node
					.get("countryId").textValue());// countryId String 是 县ID
			if (null == consigneeAddressId || "".equals(consigneeAddressId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "consigneeAddressId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == consigneeAddress || "".equals(consigneeAddress)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "consigneeAddress 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == consignee || "".equals(consignee)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "consignee 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == consigneePhone || "".equals(consigneePhone)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "consigneePhone 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == provincialId || "".equals(provincialId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "provincialId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == cityId || "".equals(cityId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "cityId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == countryId || "".equals(countryId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "countryId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}

			YBasicCity city = new YBasicCity();
			city.setFid(cityId);
			YBasicProvince province = new YBasicProvince();
			province.setFid(provincialId);
			YBasicCounty county = new YBasicCounty();
			county.setFid(countryId);
			YBasicGetadress getadress = new YBasicGetadress();
			getadress = getadressService.getById(consigneeAddressId);
			getadress.setFadress(consigneeAddress);
			getadress.setFharvestingName(consignee);
			getadress.setFharvestPhone(consigneePhone);
			getadress.setFupdatetime(new Date());
			getadress.setYBasicProvince(province);
			getadress.setYBasicCity(city);
			getadress.setYBasicCounty(county);
			getadressService.update(getadress);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 删除我的收货地址
	 * 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/delegateConsigneeAddress")
	@SystemControllerLog(description="APP 删除我的收货地址")
	public void delegateConsigneeAddress(String infoMap,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String consigneeAddressId = (node.get("consigneeAddressId") == null ? null
					: node.get("consigneeAddressId").textValue());// consigneeAddressId
																	// String 是
																	// 收货地址ID
			if (null == consigneeAddressId || "".equals(consigneeAddressId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "consigneeAddressId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}

			YBasicGetadress getadress = new YBasicGetadress();
			getadress = getadressService.getById(consigneeAddressId);
			getadressService.delete(getadress);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 获取我的消息通知
	 * 
	 * @param infoMap
	 * @param cocSign
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/messageList")
	@SystemControllerLog(description="APP 获取我的消息通知")
	public void messageList(String infoMap, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String mid = (node.get("mid") == null ? null : node.get("mid")
					.textValue());
			String messageId = (node.get("messageId") == null ? null : node
					.get("messageId").textValue());
			int pageSize = (node.get("pageSize") == null ? 748 : node.get(
					"pageSize").intValue());
//			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
//			
//			if(null==businessId||"".equals(businessId)){
//				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
//				map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
//				 COC_APPResponseResult.responseToGJson(map, response);
//			}
			  
			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (748 == pageSize) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "pageSize 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
				resultMap = InformationviewService.findByMemberList(memberlist,messageId, pageSize);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), resultMap);
				
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 删除消息通知
	 * 
	 * @param infoMap
	 * @param cocSign
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/deleteMessage")
	@SystemControllerLog(description="APP 删除消息通知")
	public void deleteMessage(String infoMap, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String messageId = (node.get("messageId") == null ? null : node
					.get("messageId").textValue());// messageId String 是 消息ID
			if (null == messageId || "".equals(messageId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "messageId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			Informationview view = new Informationview();
			view = InformationviewService.getById(messageId);
			switch (view.getType()) {
			case 1:
				groupsInformService.updateBycordId(messageId,"1");
				break;
			case 2:
				YSupplyGroup guarantee = new YSupplyGroup();
				guarantee = guaranteeService.getById(messageId);
				guarantee.setFisHide("1");
				guaranteeService.update(guarantee);
				break;
			case 3:
				YInitiationApply apply = new YInitiationApply();
				apply = applyService.getById(messageId);
				apply.setFisHide("1");
				applyService.update(apply);
				break;
			}
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);

	}

	/**
	 * 修改我的供需-商会认证
	 * 
	 * @param infoMap
	 * @param cocSign
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/updateBusinessAuthentication")
	@SystemControllerLog(description="APP 修改我的供需-商会认证")
	public void updateBusinessAuthentication(String infoMap,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String mid = (node.get("mid") == null ? null : node.get("mid")
					.textValue());
			String fId = (node.get("fId") == null ? null : node.get("fId")
					.textValue());// fId String 是 供需ID
			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
			
			if(null==businessId||"".equals(businessId)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
				 COC_APPResponseResult.responseToGJson(map, response);
			}
			
			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == fId || "".equals(fId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "fId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			JsonNode warrantnode = node.path("warrantBusiness");

			// for (JsonNode n : warrantnode) {
			// n.path("warrantType");
			// }

			JsonNode warrantTypeList = warrantnode.path("warrantType");

			if (null == warrantnode || "".equals(warrantnode)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "warrantBusiness 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == warrantTypeList || "".equals(warrantTypeList)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "warrantType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}

			List<String> list = new ArrayList<String>();
			for (JsonNode n : warrantTypeList) {
				list.add(n.asText());
			}
			map = guaranteeService.updateByGroup(fId, list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 设置默认收货地址
	 * 
	 * @param infoMap
	 * @param cocSign
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/defaultConsignee")
	@SystemControllerLog(description="APP 设置默认收货地址")
	public void defaultConsignee(String infoMap, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String mid = (node.get("mid") == null ? null : node.get("mid")
					.textValue());
			String fId = (node.get("consigneeAddressId") == null ? null : node
					.get("consigneeAddressId").textValue());// 收货地址ID
//			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
//			
//			if(null==businessId||"".equals(businessId)){
//				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
//				map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
//				 COC_APPResponseResult.responseToGJson(map, response);
//			}
			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if (null == fId || "".equals(fId)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "consigneeAddressId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			
			List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				YBasicGetadress getadress = new YBasicGetadress();
				getadress = getadressService.getById(fId);
				List<YBasicGetadress> list = new ArrayList<YBasicGetadress>();
				list = getadressService.findByMember(memberlist);//查询会员的所有的收货地址
				for (YBasicGetadress adress : list) {
					if (!fId.equals(adress.getFid())) {
						if ("1".equals(adress.getFisDefault())) {
							adress.setFisDefault("0");
							getadressService.update(adress);
						}
					}
				}
				getadress.setFisDefault("1");
				getadressService.update(getadress);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取未读消息通知条数
	 * 
	 * @param infoMap
	 * @param cocSign
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getNotReadMessageSum")
	@SystemControllerLog(description="APP  获取未读消息通知条数")
	public void getNotReadMessageSum(String infoMap, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证请求是否有效

		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String mid = (node.get("mid") == null ? null : node.get("mid").textValue());
//			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
//			
//			if(null==businessId||"".equals(businessId)){
//				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
//				map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
//				 COC_APPResponseResult.responseToGJson(map, response);
//			}
			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				
				List<Informationview> views=new ArrayList<Informationview>();
				views=InformationviewService.getListByUserId(memberlist);
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("notreadmsum", views.size());//notreadmsum	Int	未读消息总数（如果没有就默认为0）
				map.put(ResponseCode.MSGR.msg(), resultMap);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);

	}

}
