package com.yunzo.cocmore.core.function.controller.appAPI.newsapi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLabel;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.model.mysql.YSupplyGroup;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessagerecord;
import com.yunzo.cocmore.core.function.service.AppdeviceServiceI;
import com.yunzo.cocmore.core.function.service.CityService;
import com.yunzo.cocmore.core.function.service.CommentService;
import com.yunzo.cocmore.core.function.service.CountyService;
import com.yunzo.cocmore.core.function.service.DemandService;
import com.yunzo.cocmore.core.function.service.DemandsupplyPushinfoServiceI;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.service.GuaranteeServiceI;
import com.yunzo.cocmore.core.function.service.LabelServiceI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.MessageServiceI;
import com.yunzo.cocmore.core.function.service.MessagerecordServiceI;
import com.yunzo.cocmore.core.function.service.ProvinceService;
import com.yunzo.cocmore.core.function.service.ReplytocommentServiceI;
import com.yunzo.cocmore.core.function.service.SupplyService;
import com.yunzo.cocmore.core.function.service.TradeServiceI;
import com.yunzo.cocmore.core.function.util.CheckBytes;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.LabelXMLToObject;
import com.yunzo.cocmore.core.function.util.ResponseCode;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.function.vo.PushVo;
import com.yunzo.cocmore.core.thread.PushThread;
import com.yunzo.cocmore.utils.base.IMGSize;
import com.yunzo.cocmore.utils.gexin.PushToListMessage;
import com.yunzo.cocmore.utils.gexin.TemplateType;

/**
 * @author：jackpeng
 * @date：2014年12月16日上午10:07:33
 * 供需接口
 */
@RestController
@RequestMapping("/mobileapi/sad")
public class SupplyDemandRestController {
	private static Logger logger =Logger.getLogger(SupplyDemandRestController.class);
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Resource(name = "supplyService")
	private SupplyService supplyService;
	
	@Resource(name = "demandService")
	private DemandService demandService;

	@Resource(name = "labelService")
	private LabelServiceI labelService;
	
	@Resource(name = "provinceService")
	private ProvinceService provinceService;
	
	@Resource(name = "cityService")
	private CityService cityService;
	
	@Resource(name = "countyService")
	private CountyService countyService;
	
	@Resource(name = "tradeService")
	private TradeServiceI tradeService;
	
	@Resource(name = "guaranteeService")
	private GuaranteeServiceI guaranteeService;
	
	@Resource(name = "commentService")
	private CommentService commentService;
	
	@Resource(name = "replyTocommentService")
	private ReplytocommentServiceI replyTocommentService;
	
	@Resource(name = "memberService")
	private MemberServiceI memberService;
	
	@Resource(name = "deviceService")
	private AppdeviceServiceI deviceService;
	
	@Resource(name = "msgService")
	private MessageServiceI msgService;
	
	@Resource(name = "messagerecordService")
	private MessagerecordServiceI messagerecordService;
	
	@Resource(name = "getNumberService")
	private GetNumberService getNumberService;
	
	@Resource(name = "dspService")
	private DemandsupplyPushinfoServiceI dspService;
	
	@Resource(name = "groupsInformService")
	private GroupsInformService groupsInformService;
	
	/**
	 * 是否设置过供需兴趣标签
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getSupplyAndDemandFlag")
	@ResponseBody
	@SystemControllerLog(description = "APP 是否设置过供需兴趣标签")
	public void getSupplyAndDemandFlag(String infoMap,HttpServletResponse response) throws UnsupportedEncodingException{
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
			String userName = (String) jsonObj.get("userName");
			int nums = labelService.getLabelNum(userName);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			map.put(ResponseCode.MSGR.msg(), nums);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 设置行业，地域标签
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/saveSupplyAndDemandTradeFlag")
	@ResponseBody
	@SystemControllerLog(description = "APP 设置行业，地域标签")
	public void saveSupplyAndDemandTradeFlag(String infoMap,HttpServletResponse response) throws UnsupportedEncodingException{
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		try {
			YBasicLabel label = null;
			try {
				Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
				
				String userPhone = (String) jsonObj.get("userName");
				
				JsonNode jsonObjNode = objectMapper.readTree(infoMap);
				
				JsonNode regions = jsonObjNode.path("regions");
				JsonNode trade = jsonObjNode.path("trade");
				
				String hqldy = "from YBasicLabel y where y.fprovincialId != '' or y.fcityId != '' and y.fuserPhone = '"+userPhone+"'";
				String hqlhy = "from YBasicLabel y where y.ftrades != '' and y.fuserPhone = '"+userPhone+"'";
				List<YBasicLabel> listdy = labelService.getLabelHql(hqldy);
				List<YBasicLabel> listhy = labelService.getLabelHql(hqlhy);
				
				if(regions != null && !regions.equals("") || trade != null && !trade.equals("")){
					//判断行业地域标签个数
					if(regions.size() <= 5 && trade.size() <= 5){
						
						//判断该用户是否存在地域标签，如果存在就删除
						if(listdy.size() > 0){
							for(int i = 0;i < listdy.size();i++){
								YBasicLabel labels = listdy.get(i);
								labelService.delete(labels);
							}
						}
						//判断该用户是否存在行业标签，如果存在就删除
						if(listhy.size() > 0){
							for(int j = 0;j < listhy.size();j++){
								YBasicLabel labels = listhy.get(j);
								labelService.delete(labels);
							}
						}
						
						//设置地域标签
						for(int i = 0;i < regions.size();i++){
							label = new YBasicLabel();
							JsonNode node = regions.get(i);
							String provincialId = node.get("provincialId").textValue();
							String cityId = node.get("cityId").textValue();
							Integer orderIndex = node.get("orderIndex").intValue();
							
							label.setFprovincialId(provincialId);
							label.setFcityId(cityId);
							label.setForderIndex(orderIndex);
							label.setFtype(0);
							label.setFuserPhone(userPhone);
							labelService.save(label);
						}
						//设置行业标签
						for(int i = 0;i < trade.size();i++){
							label = new YBasicLabel();
							JsonNode nodet = trade.get(i);
							String tradeValue = nodet.get("tradeId").textValue();
							Integer orderIndex = nodet.get("orderIndex").intValue();
							
							label.setFtrades(tradeValue);
							label.setFuserPhone(userPhone);
							label.setForderIndex(orderIndex);
							label.setFtype(1);
							labelService.save(label);
						}
						map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
						map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
					}else{
						map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
						map.put(ResponseCode.MSGM.msg(), "操作失败！(最多只能设置5个地域标签和5个行业标签)");
					}
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "regions 或 trade 为空！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取兴趣标签
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> getUsrLabel(List<YBasicLabel> list){
		YBasicProvince province = null;
		YBasicCity city = null;
		YBasicTrade trade = null;
		Map<String, Object> mapRegions = null;
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		
		for (int i = 0; i < list.size(); i++) {
			mapRegions = new HashMap<String, Object>();
			YBasicLabel label = list.get(i);
			
			String provincialId = label.getFprovincialId();//获取省份
			String cityId = label.getFcityId();//获取城市
			String tradeId = label.getFtrades();//获取行业
			Integer fType = label.getFtype();//获取标签类型（0：地域标签，1：行业标签）
			Integer orderIndex = label.getForderIndex();//获取排序号
			
			mapRegions.put("fType", fType);
			mapRegions.put("orderIndex", orderIndex);
			if(tradeId != null && !"".equals(tradeId)){
				trade = tradeService.getById(tradeId);
				mapRegions.put("tradeId", tradeId);
				if(trade != null && !trade.equals("")){
					mapRegions.put("tradeName", trade.getFname());
				}else{
					mapRegions.put("tradeName", "");
				}
			}
			
			if(provincialId != null && !"".equals(provincialId)){
				province = provinceService.getById(provincialId);
				mapRegions.put("provincialId", provincialId);
				if(province != null && !province.equals("")){
					mapRegions.put("provincialName", province.getFname());
				}else{
					mapRegions.put("provincialName", "");
				}
			}
			
			if(cityId != null && !"".equals(cityId)){
				city = cityService.getById(cityId);
				mapRegions.put("cityId", cityId);
				if(city != null && !city.equals("")){
					mapRegions.put("cityName", city.getFname());
				}else{
					mapRegions.put("cityName", "");
				}
			}
			listMap.add(mapRegions);
		}
		
		return listMap;
	}
	/**
	 * 获取我的行业，地域标签
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/getSupplyAndDemandTradeFlag")
	@ResponseBody
	@SystemControllerLog(description = "APP 获取我的行业，地域标签")
	public void getSupplyAndDemandTradeFlag(String infoMap,HttpServletResponse response) throws UnsupportedEncodingException{
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		List<YBasicLabel> list = null;
		List<Map<String, Object>> listMapR = new ArrayList<Map<String,Object>>();
		String result="";
		Boolean flag=false;
		String hql = null;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
			String userPhone = (String) jsonObj.get("userName");
			hql = "from YBasicLabel y where y.fuserPhone = '"+userPhone+"' order by y.forderIndex desc";
			list = labelService.getLabelHql(hql);
			//判断用户是否存在标签，如果不存在标签，就为用户新增默认标签
			if(list.size() > 0){
				listMapR=getUsrLabel(list);
				result+= ResponseCode.SUCCESS.msg();
				flag=true;
			}else{
				List<YBasicLabel> listabel = LabelXMLToObject.labelObject(userPhone);
				for(YBasicLabel labals: listabel){
					labelService.save(labals);
				}
				listMapR=getUsrLabel(listabel);
				flag=true;
			}
			if(flag)
			{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), result);
				map.put(ResponseCode.MSGR.msg(), listMapR);
			}else
			{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取供应需求列表
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getSupplyAndDemandList")
	@ResponseBody
	@SystemControllerLog(description = "APP 获取供应需求列表")
	public void getSupplyAndDemandList(String infoMap,HttpServletResponse response) throws UnsupportedEncodingException{
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		List<Map<String, Object>> listMap = null;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
			String fId = (String) jsonObj.get("fId");//供需id
			Integer supplyDemandType = (Integer) jsonObj.get("supplyDemandType");//供需类型
			Integer pageSize = (Integer)jsonObj.get("pageSize");//分页大小
			Integer IsWarrant = (Integer)jsonObj.get("IsWarrant");//是否按商会认证排序（0是1否）
			Integer IsCredit = (Integer)jsonObj.get("IsCredit");//是否按照信用等级（0是1否）
			Integer IsTrade = (Integer)jsonObj.get("IsTrade");//是否按行业分类（0是1否）
			Integer IsRegion = (Integer)jsonObj.get("IsRegion");//是否按地域分类（0是1否）
			String tradeId = (String) jsonObj.get("tradeId");//行业ID（当IsTrade为0的时候需要填写）
			String provincialId = (String) jsonObj.get("provincialId");//省份ID（当IsRegion为0的时候必填写）
			String cityId = (String) jsonObj.get("cityId");//城市Id（当IsRegion为0的时候可选填写）
			String mid = (String) jsonObj.get("mid");//会员电话
			
			if(null==supplyDemandType||"".equals(supplyDemandType)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==pageSize||"".equals(pageSize)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "pageSize 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(supplyDemandType == 0){
				listMap = supplyService.getSupplyList(fId, IsWarrant, IsCredit, IsTrade, IsRegion, pageSize, tradeId, provincialId, cityId, supplyDemandType,mid);
			}else if(supplyDemandType == 1){
				listMap = demandService.getDemandList(fId, IsWarrant, IsCredit, IsTrade, IsRegion, pageSize, tradeId, provincialId, cityId, supplyDemandType,mid);
			}else if(supplyDemandType == 2){
				listMap = supplyService.getSupAndDemList(fId, IsWarrant, IsCredit, IsTrade, IsRegion, pageSize, tradeId, provincialId, cityId,mid);
			}else if(supplyDemandType == 3){
				listMap = supplyService.getRecommendationList(fId, IsWarrant, IsCredit, IsTrade, IsRegion, pageSize, tradeId, provincialId, cityId,mid);
			}else{
				logger.info("供需类型参数不符...");
			}
			if(listMap != null){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), listMap);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				map.put(ResponseCode.MSGR.msg(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取供需详情
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/getSupplyAndDemandInfo")
	@ResponseBody
	@SystemControllerLog(description = "APP 获取供需详情")
	public void getSupplyAndDemandInfo(String infoMap,HttpServletResponse response) throws UnsupportedEncodingException{
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		Map<String,Object> resultList = new HashMap<String,Object>();
		try{
			JsonNode node =  objectMapper.readTree(infoMap);
			String fid = node.get("fId")==null?null:node.get("fId").textValue();//动态id
			String userId = node.get("mid")==null?null:node.get("mid").textValue();//动态id
			String businessId = node.get("businessId")==null?null:node.get("businessId").textValue();//获取商会id
			int supplyDemandType = (node.get("supplyDemandType")==null?748:node.get("supplyDemandType").intValue());
			String userName = node.get("userName")==null?null:node.get("userName").textValue();
			if(userName != null){
				if(null==fid||"".equals(fid)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "fId 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(null==userId||"".equals(userId)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "mid 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(748==supplyDemandType){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(supplyDemandType==0){//供应
					resultList = supplyService.getSupplyInfo(fid,userId,businessId);
				}else if(supplyDemandType==1){//需求
					resultList=demandService.getDemandInfo(fid,userId,businessId);
				}else{
					logger.info("参数不符合格式......");
				}
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), resultList);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				map.put(ResponseCode.MSGR.msg(), "未登录游客无权限！");
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			map.put(ResponseCode.MSGR.msg(), "");
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 发布供需
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/saveSupplyAndDemandInfo")
	@ResponseBody
	@SystemControllerLog(description = "APP 发布供需")
	public void saveSupplyAndDemandInfo(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> ImgInfoMap = null;
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		MultipartFile file = null;
		String images = null;
		YBasicSocialgroupssupply supply = null;
		YBasicSocialgroupsdemand demand = null;
		YSupplyGroup guarantee = null;
		String hqlMember = null;
		try {
			request.setCharacterEncoding("utf-8");
			String infoMap = request.getParameter("infoMap");
			if (request instanceof DefaultMultipartHttpServletRequest) {
				DefaultMultipartHttpServletRequest rest = (DefaultMultipartHttpServletRequest) request;
				int i = 0;
				while(i < 9){
					file = rest.getFile("file" + i);
					if(file!=null){
						files.add(file);
						i++;
					}else{
						break;
					}
				}
				if (files != null) {
					ImgInfoMap = ImgUploadUtil.imgUpload(files,IMGSize.X200.value());
				}
				if (ImgInfoMap != null)
					if ((boolean) ImgInfoMap.get("success") == true) {
						if ((ImgInfoMap.get("imgsrc")) != null) {
							images = (ImgInfoMap.get("imgsrc")).toString();
						}
					}
			}
			
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
			String provincialId = (String) jsonObj.get("provincialId");
			String cityId = (String) jsonObj.get("cityId");
			String countryId = (String) jsonObj.get("countryId");
			String tradeId = (String) jsonObj.get("tradeID");
			String contactsPhone = (String) jsonObj.get("contactsPhone");
			String contactsPerson = (String) jsonObj.get("contactsPerson");
			String expDateStop = (String) jsonObj.get("expDateStop");
			String expDateStart = (String) jsonObj.get("expDateStart");
			String fTitle = (String) jsonObj.get("fTitle");
			String fContent = (String) jsonObj.get("fContent");
			//String file = (String) jsonObj.get("file");
			Integer isWarrant = (Integer) jsonObj.get("isWarrant");
			Integer type = (Integer) jsonObj.get("type");
			String memberId = (String) jsonObj.get("mid");
			String businessId = (String)jsonObj.get("businessId");
			
			if(null==tradeId||"".equals(tradeId)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "tradeId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==contactsPhone||"".equals(contactsPhone)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "contactsPhone 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==contactsPerson||"".equals(contactsPerson)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "contactsPerson 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==expDateStop||"".equals(expDateStop)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "expDateStop 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==expDateStart||"".equals(expDateStart)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "expDateStart 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==fTitle||"".equals(fTitle)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "fTitle 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==fContent||"".equals(fContent)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "fContent 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==isWarrant||"".equals(isWarrant)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "isWarrant 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==type||"".equals(type)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "type 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==memberId||"".equals(memberId)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = sdfs.parse(expDateStart);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date stopDate = sdf.parse(expDateStop);
			String groupId = null;
			
			//获取会员id
			boolean flag =memberService.isMember(memberId);
			if(flag==false){
				hqlMember = "from YBasicMember y where y.fmobilePhone = '"+memberId+"'";
			}else{
				hqlMember = "from YBasicMember y where y.fmobilePhone = '"+memberId+"' and y.YBasicSocialgroups.fid = '"+businessId+"'";
			}
			List<YBasicMember> listMember = (List<YBasicMember>) memberService.findByHql(hqlMember);
			YBasicMember ymem = listMember.get(0);
			if(ymem.getYBasicSocialgroups() != null && !ymem.getYBasicSocialgroups().equals("")){
				groupId = ymem.getYBasicSocialgroups().getFid();
			}
			
			YBasicProvince province = new YBasicProvince();
			province.setFid(provincialId);
			
			YBasicCity city = new YBasicCity();
			city.setFid(cityId);
			
			YBasicCounty county = new YBasicCounty();
			county.setFid(countryId);
			
			YBasicTrade trade = new YBasicTrade();
			trade.setFid(tradeId);
			
			YBasicSocialgroups groups = new YBasicSocialgroups();
			groups.setFid(groupId);
			
			YBasicMember mem = new YBasicMember();
			mem.setFid(ymem.getFid());
			
			String fid = null;
			if(type == 1){
				demand = new YBasicSocialgroupsdemand();
				Map<String, Object> mapNumber = getNumberService.checkExist("FX-XQGL");//获取编号方法
				String demandNumber = (String)mapNumber.get("serialNumber");//获取需求编号
				demand.setFnumber(demandNumber);
				if(groupId != null && !groupId.equals("")){
					demand.setYBasicSocialgroups(groups);
				}
				if(provincialId != null && !provincialId.equals("")){
					demand.setYBasicProvince(province);
				}
				if(provincialId != null && !provincialId.equals("") && cityId != null && !cityId.equals("")){
					demand.setYBasicCity(city);
				}
				if(provincialId != null && !provincialId.equals("") && cityId != null && !cityId.equals("") && countryId != null && !countryId.equals("")){
					demand.setYBasicCounty(county);
				}
				demand.setYBasicMember(mem);
				demand.setYBasicTrade(trade);
				demand.setFtel(contactsPhone);
				demand.setFcontacts(contactsPerson);
				demand.setFfinishTime(stopDate);//结算时间
				demand.setFstartTime(startDate);//开始时间
				demand.setFpublisherTime(new Date());//发布时间
				demand.setFheadline(fTitle);
				demand.setFmessage(fContent);
				demand.setFisHide(0);
				if(null!=images&&!"".equals(images)){
					demand.setFimages(images);
				}
				
				demand.setFbillState(5);
				demandService.save(demand);
				getNumberService.addSerialNumber("FX-XQGL");//调用编码规则流水号叠加方法
				//调用推送需求的方法
//				saveLabelDemand2(demand);
			}else if(type == 0){
				if(isWarrant == 0){
					supply = new YBasicSocialgroupssupply();
					Map<String, Object> mapNumber = getNumberService.checkExist("FX-GYGL");//获取编号方法
					String supplyNumber = (String)mapNumber.get("serialNumber");//获取供应编号
					supply.setFnumber(supplyNumber);
					if(groupId != null && !groupId.equals("")){
						supply.setYBasicSocialgroups(groups);
					}
					if(provincialId != null && !provincialId.equals("")){
						supply.setYBasicProvince(province);
					}
					if(provincialId != null && !provincialId.equals("") && cityId != null && !cityId.equals("")){
						supply.setYBasicCity(city);
					}
					if(provincialId != null && !provincialId.equals("") && cityId != null && !cityId.equals("") && countryId != null && !countryId.equals("")){
						supply.setYBasicCounty(county);
					}
					supply.setYBasicMember(mem);
					supply.setYBasicTrade(trade);
					supply.setFtel(contactsPhone);
					supply.setFcontacts(contactsPerson);
					supply.setFexpireTime(stopDate);//结算时间
					supply.setFauditTime(startDate);//开始时间
					supply.setFpublisherTime(new Date());//发布时间
					supply.setFheadline(fTitle);
					supply.setFmessage(fContent);
					supply.setFisHide(0);
					if(null!=images&&!"".equals(images)){
						supply.setFimages(images);
					}
					
					supply.setFareGuarantee(isWarrant);
					supply.setFbillState(5);
					supplyService.save(supply);
					getNumberService.addSerialNumber("FX-GYGL");//调用编码规则流水号叠加方法
					//调用推送供应的方法
//					saveLabelSupply2(supply);
				}else if(isWarrant == 1){
					supply = new YBasicSocialgroupssupply();
					Map<String, Object> mapNumber = getNumberService.checkExist("FX-GYGL");//获取编号方法
					String supplyNumber = (String)mapNumber.get("serialNumber");//获取供应编号
					supply.setFnumber(supplyNumber);
					if(groupId != null && !groupId.equals("")){
						supply.setYBasicSocialgroups(groups);
					}
					if(provincialId != null && !provincialId.equals("")){
						supply.setYBasicProvince(province);
					}
					if(provincialId != null && !provincialId.equals("") && cityId != null && !cityId.equals("")){
						supply.setYBasicCity(city);
					}
					if(provincialId != null && !provincialId.equals("") && cityId != null && !cityId.equals("") && countryId != null && !countryId.equals("")){
						supply.setYBasicCounty(county);
					}
					supply.setYBasicMember(mem);
					supply.setYBasicTrade(trade);
					supply.setFtel(contactsPhone);
					supply.setFcontacts(contactsPerson);
					supply.setFexpireTime(stopDate);//结算时间
					supply.setFauditTime(startDate);//开始时间
					supply.setFpublisherTime(new Date());//发布时间
					supply.setFheadline(fTitle);
					supply.setFmessage(fContent);
					supply.setFisHide(0);
					if(null!=images&&!"".equals(images)){
						supply.setFimages(images);
					}
					
					supply.setFareGuarantee(isWarrant);
					supply.setFbillState(11);
					supplyService.save(supply);
					getNumberService.addSerialNumber("FX-GYGL");//调用编码规则流水号叠加方法
					fid = supply.getFid();//获取fid
					
					JsonNode warrantBusiness = objectMapper.readTree(infoMap);
					/**
					 * update by ailierke 接口写的是guaranteeIds 少了s
					 */
					JsonNode guaranteeId = warrantBusiness.path("guaranteeIds");
					
					if(null==guaranteeId||"".equals(guaranteeId)){
						map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
						map.put(ResponseCode.MSGM.msg(), "guaranteeId 为空！");
						COC_APPResponseResult.responseToGJson(map, response);
					}
					
					for (int i = 0; i < guaranteeId.size(); i++) {
						guarantee = new YSupplyGroup();
						JsonNode node = guaranteeId.get(i);
						String guaranId = node.textValue();
						YBasicSocialgroupssupply supp = new YBasicSocialgroupssupply();
						supp.setFid(fid);
						guarantee.setYBasicSocialgroupssupply(supp);
						YBasicAssurancecontent tent = supplyService.getYBasicAssurancecontentById(guaranId);
						guarantee.setGroupid(tent.getYBasicSocialgroups().getFid());//添加组织
						guarantee.setIspass(Status.PENDING.value()+"");//添加状态
						guarantee.setUpdatetime(new Date());
						guarantee.setYBasicAssurancecontent(tent);
						guaranteeService.save(guarantee);
					}
					
				}else{
					logger.info("是否担保参数不符...");
				}
			}else{
				logger.info("供需类型参数不符...");
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
	 * 推送供应2
	 * @param supply
	 */
//	public void saveLabelSupply2(YBasicSocialgroupssupply supply){
//		try {
//			String hqlLable = "select y.fuserPhone from YBasicLabel y where y.ftrades = '"+supply.getYBasicTrade().getFid()+"' or y.fprovincialId = '"+supply.getYBasicProvince().getFid()+"' or y.fcityId = '"+supply.getYBasicCity().getFid()+"' and ";
//			List<String> liteTels = (List<String>)labelService.getByHql(hqlLable);
//			if(liteTels != null && liteTels.size() > 0){
//				Set<String> setTels = new HashSet<String> (liteTels);
//				Map<String,PushVo> deviceIdMap  =  groupsInformService.getOutRepeat(setTels);
//				/**
//				 * 开启推送线程，进行推送行为的记录和系统日志的记录  app端的推送
//				 */
//				PushThread pushThread = new PushThread(new Integer(1), deviceIdMap, supply.getFheadline(), supply.getFid(), supply.getYBasicSocialgroups().getFid(), dspService);
//				pushThread.start();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 推送需求2
	 * @param demand
	 */
//	public void saveLabelDemand2(YBasicSocialgroupsdemand demand){
//		try {
//			String hqlLable = "select y.fuserPhone from YBasicLabel y where y.ftrades = '"+demand.getYBasicTrade().getFid()+"' or y.fprovincialId = '"+demand.getYBasicProvince().getFid()+"' or y.fcityId = '"+demand.getYBasicCity().getFid()+"'";
//			List<String> liteTels = (List<String>)labelService.getByHql(hqlLable);
//			if(liteTels != null && liteTels.size() > 0){
//				Set<String> setTels = new HashSet<String> (liteTels);
//				Map<String,PushVo> deviceIdMap  =  groupsInformService.getOutRepeat(setTels);
//				/**
//				 * 开启推送线程，进行推送行为的记录和系统日志的记录  app端的推送
//				 */
//				PushThread pushThread = new PushThread(new Integer(2), deviceIdMap, demand.getFheadline(), demand.getFid(), demand.getYBasicSocialgroups().getFid(), dspService);
//				pushThread.start();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	/**
	 * 推送供应
	 * @param supply
	 */
//	public void saveLabelSupply(YBasicSocialgroupssupply supply){
//		StringBuffer condition = null;
//		String hqlLable = "select y.fuserPhone from YBasicLabel y where y.ftrades = '"+supply.getYBasicTrade().getFid()+"' or y.fprovincialId = '"+supply.getYBasicProvince().getFid()+"' or y.fcityId = '"+supply.getYBasicCity().getFid()+"'";
//		List<String> tels = (List<String>)labelService.getByHql(hqlLable);
//		//同样拼接查询用户设备表的in 条件，根据id来查询
//		if(tels!=null&&tels.size()>0){
//			condition =new StringBuffer("(");
//			for(String record:tels){
//				condition.append("'"+record+"', ");
//			}
//			//去掉最后的","
//			condition.replace(condition.lastIndexOf(","),condition.length()-1, "");
//			condition.append(")");
//			List<YAppdevice> memberAppDeviceList = (List<YAppdevice>) deviceService.getByHql("from YAppdevice device where device.fuserName in "+condition);
//			//如果存在此用户的clientId信息就推送
//			if(memberAppDeviceList!=null&&memberAppDeviceList.size()>0){
//				String logo ="";
//				String logoUrl="";
//				String transmissionContent="";
//				int transmissionType =1;
//				String headLine = supply.getFheadline();
//				//如果大于10个汉字就截取
//				if(headLine.getBytes().length>20){
//					try {
//						headLine = CheckBytes.substring(headLine, 20)+"...";
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				YSystemconfigurationMessage message = new YSystemconfigurationMessage();
//				message.setFmessageType("供应信息表态");
//				message.setFcontent(headLine);
//				msgService.saveOrUpdate(message);//推送消息表
//				YSystemconfigurationMessagerecord messageRecord = null;
//				List<YSystemconfigurationMessagerecord> messagerecords = new ArrayList<YSystemconfigurationMessagerecord>();
//				for(YAppdevice yAppdevice:memberAppDeviceList){
//					//使用激活应用模板
//					try {
//						PushToListMessage.sendDownLoadMessageToSingel(yAppdevice.getFdeviceId(), new Integer(yAppdevice.getFappChannelNo()), TemplateType.NOTIFY_TEMPLATE.toString(), "云筑", headLine, logoUrl, logo, transmissionContent, transmissionType, null, null, null, null, null, null, null);
//					} catch (NumberFormatException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					messageRecord = new YSystemconfigurationMessagerecord();
//					messageRecord.setFuserId(yAppdevice.getFuserName());
//					messageRecord.setYSystemconfigurationMessage(new YSystemconfigurationMessage(message.getFid()));
//					messageRecord.setFdate(new java.sql.Date(new java.util.Date().getTime()));
//					messagerecords.add(messageRecord);
//				}
//				messagerecordService.saveOrUpdateAll(messagerecords);//推送消息记录表
//			}
//		}else{
//			logger.info("没有登录过的设备信息....");
//		}
//	}
		
	/**
	 * 推送需求
	 * @param demand
	 */
//	public void saveLabelDemand(YBasicSocialgroupsdemand demand){
//		StringBuffer condition = null;
//		String hqlLable = "select y.fuserPhone from YBasicLabel y where y.ftrades = '"+demand.getYBasicTrade().getFid()+"' or y.fprovincialId = '"+demand.getYBasicProvince().getFid()+"' or y.fcityId = '"+demand.getYBasicCity().getFid()+"'";
//		List<String> tels = (List<String>)labelService.getByHql(hqlLable);
//		//同样拼接查询用户设备表的in 条件，根据id来查询
//		if(tels!=null&&tels.size()>0){
//			condition =new StringBuffer("(");
//			for(String record:tels){
//				condition.append("'"+record+"', ");
//			}
//			//去掉最后的","
//			condition.replace(condition.lastIndexOf(","),condition.length()-1, "");
//			condition.append(")");
//			List<YAppdevice> memberAppDeviceList = (List<YAppdevice>) deviceService.getByHql("from YAppdevice device where device.fuserName in "+condition);
//			//如果存在此用户的clientId信息就推送
//			if(memberAppDeviceList!=null&&memberAppDeviceList.size()>0){
//				String logo ="";
//				String logoUrl="";
//				String transmissionContent="";
//				int transmissionType =1;
//				String headLine = demand.getFheadline();
//				//如果大于10个汉字就截取
//				if(headLine.getBytes().length>20){
//					try {
//						headLine = CheckBytes.substring(headLine, 20)+"...";
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				YSystemconfigurationMessage message = new YSystemconfigurationMessage();
//				message.setFmessageType("需求信息表态");
//				message.setFcontent(headLine);
//				msgService.saveOrUpdate(message);//推送消息表
//				YSystemconfigurationMessagerecord messageRecord = null;
//				List<YSystemconfigurationMessagerecord> messagerecords = new ArrayList<YSystemconfigurationMessagerecord>();
//				for(YAppdevice yAppdevice:memberAppDeviceList){
//					//使用激活应用模板
//					try {
//						PushToListMessage.sendDownLoadMessageToSingel(yAppdevice.getFdeviceId(), new Integer(yAppdevice.getFappChannelNo()), TemplateType.NOTIFY_TEMPLATE.toString(), "云筑", headLine, logoUrl, logo, transmissionContent, transmissionType, null, null, null, null, null, null, null);
//					} catch (NumberFormatException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					messageRecord = new YSystemconfigurationMessagerecord();
//					messageRecord.setFuserId(yAppdevice.getFuserName());
//					messageRecord.setYSystemconfigurationMessage(new YSystemconfigurationMessage(message.getFid()));
//					messageRecord.setFdate(new java.sql.Date(new java.util.Date().getTime()));
//					messagerecords.add(messageRecord);
//				}
//				messagerecordService.saveOrUpdateAll(messagerecords);//推送消息记录表
//			}
//		}else{
//			logger.info("没有登录过的设备信息....");
//		}
//	}
	
	/**
	 * 供需点赞
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/saveSupplyAndDemandForSupport")
	@ResponseBody
	@SystemControllerLog(description = "APP 供需点赞")
	public void saveSupplyAndDemandForSupport(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> resultList = new HashMap<String,Object>();
		try{
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node =  objectMapper.readTree(infoMap);
			String fid = (node.get("fId")==null?null:node.get("fId").textValue());//动态id
			String userId = (node.get("mid")==null?null:node.get("mid").textValue());//用户id
			String businessId = (node.get("businessId")==null?null:node.get("businessId").textValue());//商户id
			Integer isSupport = (node.get("isSupport")==null?null:node.get("isSupport").intValue());//点赞0  取消1
			Integer supplyDemandType = (node.get("supplyDemandType")==null?null:node.get("supplyDemandType").intValue());//供需类型
			if(null==userId||"".equals(userId)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==isSupport||"".equals(isSupport)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "isSupport 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==supplyDemandType||"".equals(supplyDemandType)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			if(supplyDemandType==0){//供应
				supplyService.saveSupplyForSupport(fid,userId,isSupport,businessId);
			}else if(supplyDemandType==1){//需求
				demandService.saveDemandForSupport(fid,userId,isSupport,businessId);
			}else{
				logger.info("参数不符合格式......");
			}
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			map.put(ResponseCode.MSGR.msg(), resultList);
		}catch(Exception e){
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			map.put(ResponseCode.MSGR.msg(), "");
			e.printStackTrace();
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 投诉某条供需
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/addSupplyAndDemandInfoFlag")
	@ResponseBody
	@SystemControllerLog(description = "APP 投诉某条供需")
	public void addSupplyAndDemandInfoFlag(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			
			String fId=(node.get("fId")==null?null:node.get("fId").textValue());//			fId	String	是	供需ID(如果为空字符串则表示获取最新的数据)
			Integer supplyDemandType=(node.get("supplyDemandType")==null?null:node.get("supplyDemandType").intValue()); //			supplyDemandType	Int	是	供需类型（0供应,1需求）
			String businessId = node.get("businessId")==null?null:node.get("businessId").textValue();
			String memberId=node.get("mid").textValue();
			if(null==memberId||"".equals(memberId)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==supplyDemandType||"".equals(supplyDemandType)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(supplyDemandType==0){
				supplyService.saveComplaint(fId,memberId,businessId);
			}else if(supplyDemandType == 1){
				demandService.saveComplaint(fId,memberId,businessId);
			}else{
				logger.info("供需类型参数不符...");
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
	 * 评论供需
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/saveSupplyAndDemandInfoReview")
	@ResponseBody
	@SystemControllerLog(description = "APP 评论供需")
	public void saveSupplyAndDemandInfoReview(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> comMap= null;
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String fId=(node.get("fId")==null?null:node.get("fId").textValue());//			fId	String	是	供需ID(如果为空字符串则表示获取最新的数据)
			Integer supplyDemandType=(node.get("supplyDemandType")==null?null:node.get("supplyDemandType").intValue()); //			supplyDemandType	Int	是	供需类型（0供应,1需求）
			String fContent=(node.get("fContent")==null?null:node.get("fContent").textValue());//			fContent	String	是	评论内容
			String businessId = (node.get("businessId")==null?null:node.get("businessId").textValue());
			String memberId=(node.get("mid")==null?null:node.get("mid").textValue());
			if(null==supplyDemandType||"".equals(supplyDemandType)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==fContent||"".equals(fContent)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "fContent 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==memberId||"".equals(memberId)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			comMap = commentService.savesupplyDemand(memberId,fContent,supplyDemandType,fId,businessId);
			if(comMap != null){
				map.put(ResponseCode.MSGR.msg(), comMap);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			}else{
				map.put(ResponseCode.MSGR.msg(), null);
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
	 * 获取供需回复列表
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getSupplyAndDemandInfoReviewList")
	@ResponseBody
	@SystemControllerLog(description = "APP 获取供需回复列表")
	public void getSupplyAndDemandInfoReviewList(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
			String fId = (String) jsonObj.get("fId");
			Integer supplyDemandType = (Integer) jsonObj.get("supplyDemandType");
			Integer pageSize = (Integer) jsonObj.get("pageSize");
			String commentID = (String) jsonObj.get("commentID");
			if(null==supplyDemandType||"".equals(supplyDemandType)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==pageSize||"".equals(pageSize)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "pageSize 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			if(supplyDemandType == 0){
				list = commentService.getByHql(commentID, fId, pageSize);
			}else if(supplyDemandType == 1){
				list = commentService.getByHql(commentID, fId, pageSize);
			}else{
				logger.info("供需类型参数不符...");
			}
			if(list != null){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), list);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				map.put(ResponseCode.MSGR.msg(), null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 删除供需评论
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/delSupplyAndDemandInfoReview")
	@ResponseBody
	@SystemControllerLog(description = "APP 删除供需评论")
	public void delSupplyAndDemandInfoReview(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			boolean bool=false;
			JsonNode node = objectMapper.readTree(infoMap);
			Integer supplyDemandType=(node.get("supplyDemandType")==null?null:node.get("supplyDemandType").intValue()); //			supplyDemandType	Int	是	供需类型（0供应,1需求）
			
			String commentID=(node.get("commentID")==null?null:node.get("commentID").textValue());
			if(null==supplyDemandType||"".equals(supplyDemandType)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==commentID||"".equals(commentID)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "commentID 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			bool=commentService.delsupplyDemand(commentID);
			if(bool){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			}else{
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
	 * 网页分享供需
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getSupplyAndDemandInfoForUrl")
	@ResponseBody
	@SystemControllerLog(description = "APP 网页分享供需")
	public void getSupplyAndDemandInfoForUrl(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
			Integer supplyDemandType = (Integer) jsonObj.get("supplyDemandType");
			if(null==supplyDemandType||"".equals(supplyDemandType)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(supplyDemandType == 0){
				map.put(ResponseCode.MSGR.msg(), "cocmore-web/webApp/gyxx.html");
			}else if(supplyDemandType == 1){
				map.put(ResponseCode.MSGR.msg(), "cocmore-web/webApp/xqxx.html");
			}else{
				logger.info("供需类型参数不符...");
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
	 * 我的商会列表是否有变动
	 * @param infoMap
	 * @return
	 */
//	@SuppressWarnings("restriction")
//	@RequestMapping("/getBusinessVersions")
//	@ResponseBody
//	public Map<String, Object> getBusinessVersions(String infoMap){
//		
//			Map<String, Object> map = new HashMap<String, Object>();
//			
//			return map;
//		}else{
//			return null;
//		}
//		
//	}
	
	/**
	 * 获取我的商会列表，包括担保类型
	 * @return
	 */
	@RequestMapping("/getBusinessListAndEnsureType")
	@ResponseBody
	@SystemControllerLog(description = "APP 获取我的商会列表，包括担保类型")
	public void getBusinessListAndEnsureType(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			
			String telphone=node.get("userName").textValue();//			根据用户的电话号码去查member表，查看用户所属哪几个商会
			resultList = supplyService.getBusinessListAndEnsureType(telphone);
			map.put(ResponseCode.MSGR.msg(), resultList);
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
	 * 获取供需评论列表
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getSupplyAndDemandInfoCommentList")
	@ResponseBody
	@SystemControllerLog(description = "APP 获取供需评论列表")
	public void getSupplyAndDemandInfoCommentList(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapValue = null;
		YComment comment = null;
		String hql = null;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
			String fId = (String) jsonObj.get("fId");
			Integer supplyDemandType = (Integer) jsonObj.get("supplyDemandType");
			Integer pageSize = (Integer) jsonObj.get("pageSize");
			
			if(null==supplyDemandType||"".equals(supplyDemandType)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==pageSize||"".equals(pageSize)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "pageSize 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			if(supplyDemandType == 0){
				if(fId != null && !fId.equals("")){
					hql = "from YComment y where y.fforeignId = '"+fId+"' order by y.flag desc";
				}else{
					hql = "from YComment y order by y.flag desc";
				}
			}else if(supplyDemandType == 1){
				if(fId != null && !fId.equals("")){
					hql = "from YComment y where y.fforeignId = '"+fId+"' order by y.flag desc";
				}else{
					hql = "from YComment y order by y.flag desc";
				}
			}else{
				logger.info("供需类型参数不符...");
			}
			
			List<YComment> listCom = commentService.findByHql(hql, pageSize);
			for(int i = 0;i < listCom.size();i++){
				comment = listCom.get(i);
				mapValue = new HashMap<String, Object>();
				mapValue.put("fid", comment.getFid());
				mapValue.put("fforeignID", comment.getFforeignId());
				mapValue.put("fmemberID", comment.getYBasicMember().getFid());
				mapValue.put("fmemberNickName", comment.getYBasicMember().getFname());
				mapValue.put("fmemberHeadImage", comment.getYBasicMember().getFheadImage());
				mapValue.put("fcontents ", comment.getFcontents());
				mapValue.put("ftime", new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(comment.getFtime()));
				mapValue.put("ftype", comment.getYBasicType().getFid());
				listMap.add(mapValue);
			}
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			map.put(ResponseCode.MSGR.msg(), listMap);

		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			map.put(ResponseCode.MSGR.msg(), null);
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取推荐的供需列表(广告栏位置)
	 * @param infoMap
	 * @param cocSign
	 * @return
	 */
	@RequestMapping("/getrecommendSupplyAndDemandList")
	@ResponseBody
	@SystemControllerLog(description = "APP 获取推荐的供需列表(广告栏位置)")
	public void getrecommendSupplyAndDemandList(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listMap = null;
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
			Integer supplyDemandType = (Integer) jsonObj.get("supplyDemandType");
			
			if(null==supplyDemandType||"".equals(supplyDemandType)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			if(supplyDemandType == 0){
				listMap = supplyService.findSupplyAndDemandList();
			}else if(supplyDemandType == 1){
				listMap = demandService.findSupplyAndDemandList();
			}
			if(listMap != null){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), listMap);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), listMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 获取行业列表
	 * @param infoMap
	 * @param cocSign
	 * @return
	 */
	@RequestMapping("/radeList")
	@ResponseBody
	@SystemControllerLog(description = "APP 获取行业列表")
	public void radeList(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listMap = null;
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			listMap = tradeService.getTradeList();
			if(listMap != null){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), listMap);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				map.put(ResponseCode.MSGR.msg(), listMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	
	/**
	 * 查看指定用户的供需列表
	 * @param infoMap
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getUserSupplyAndDemandList")
	@ResponseBody
	@SystemControllerLog(description = "APP 查看指定用户的供需列表")
	public void getUserSupplyAndDemandList(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listMap = null;
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
			String fId = (String)jsonObj.get("fId");//供需id
			Integer supplyDemandType = (Integer)jsonObj.get("supplyDemandType");//供需类型
			Integer pageSize = (Integer)jsonObj.get("pageSize");//分页大小
			String userId = (String)jsonObj.get("userId");//用户id
			
			if(null==supplyDemandType||"".equals(supplyDemandType)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "supplyDemandType 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==pageSize||"".equals(pageSize)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "pageSize 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==userId||"".equals(userId)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "userId 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
			if(supplyDemandType == 0){
				listMap = supplyService.getMySupplyList(fId, pageSize, userId);
			}else if(supplyDemandType == 1){
				listMap = demandService.getMyDemandList(fId, pageSize, userId);
			}else{
				logger.info("供需类型参数不符...");
			}
			if(listMap.size()>0){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), listMap);
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), "该用户未发布供需！");
				map.put(ResponseCode.MSGR.msg(), listMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * update by ailierke
	 */
	/**
	 * 供需评分
	 * @param infoMap
	 * @param response
	 */
	@RequestMapping("/saveSupplyAndDemandForGradeNum")
	@ResponseBody
	@SystemControllerLog(description = "APP 供需评分")
	public void saveSupplyAndDemandForGradeNum(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listMap = null;
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String fId=node.get("fId")==null?null:node.get("fId").textValue();//		供需id	
			String mid=node.get("mid")==null?null:node.get("mid").textValue();//			
			Integer supplyDemandType=node.get("supplyDemandType")==null?null:node.get("supplyDemandType").intValue();//		供需类型	
			Integer gradeNum=node.get("gradeNum")==null?null:node.get("gradeNum").intValue();//评分
			if(fId==null||supplyDemandType==null||gradeNum==null){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.FORMWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.FORMWRONG.msg());
				map.put(ResponseCode.MSGR.msg(), listMap);
			}else{
				supplyService.commentScore(fId,supplyDemandType,gradeNum,mid);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), "评分成功..");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * 根据关键字查询
	 * @param infoMap
	 * @param response
	 */
	@RequestMapping("/searchSupplyAndDemandList")
	@ResponseBody
	@SystemControllerLog(description = "APP 根据关键字查询")
	public void searchSupplyAndDemandList(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String searchName=node.get("searchName")==null?null:node.get("searchName").textValue();//	关键词
			String fid=node.get("fId")==null?null:node.get("fId").textValue();//	关键词	
			Integer pageSize=node.get("pageSize")==null?null:node.get("pageSize").intValue();//		分页大小	
			Integer supplyDemandType=node.get("supplyDemandType")==null?null:node.get("supplyDemandType").intValue();//供需类型	
			String userId=node.get("userId")==null?null:node.get("userId").textValue();//用户电话号码
			if(pageSize==null||searchName==null||fid==null){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.FORMWRONG.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.FORMWRONG.msg());
				map.put(ResponseCode.MSGR.msg(), "");
			}else{
				list = supplyService.getsupplyAnddemandByType(userId,pageSize,searchName,supplyDemandType,fid);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), list);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
}
