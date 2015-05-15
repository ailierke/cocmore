package com.yunzo.cocmore.core.function.controller.appAPI.newsapi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.Informationview;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplyPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplycmentPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDynamicInfoPush;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLifePushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicShopingPushinfo;
import com.yunzo.cocmore.core.function.service.DemandSupplyComentPushService;
import com.yunzo.cocmore.core.function.service.DemandsupplyPushinfoServiceI;
import com.yunzo.cocmore.core.function.service.DynmicandInfoPushService;
import com.yunzo.cocmore.core.function.service.InformationviewServiceI;
import com.yunzo.cocmore.core.function.service.LifePushinfoSerciveI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.ShopingPushinfoServiceI;
import com.yunzo.cocmore.core.function.util.ResponseCode;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.thread.LoginUserGroupPushThread;
import com.yunzo.cocmore.core.thread.LoginUserPushThread;

/**
 * @author：jackpeng
 * @date：2015年3月20日上午9:20:59
 * 个推
 */
@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/mobileapi/getui")
public class PushinfoController {
	
	private static Logger logger = Logger.getLogger(PushinfoController.class);
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Resource(name = "dspService")
	private DemandsupplyPushinfoServiceI dspService;
	
	@Resource(name = "dynmicandInfoPushService")
	private DynmicandInfoPushService dynmicandInfoPushService;
	
	@Resource(name = "lifePushService")
	private LifePushinfoSerciveI lifePushService;
	
	@Resource(name = "demandSupplyComentPushService")
	private DemandSupplyComentPushService demandSupplyComentPushService;
	
	@Resource(name = "memberService")
	private MemberServiceI memberService;
	
	@Resource(name = "InformationviewService")
	private InformationviewServiceI InformationviewService;
	
	@Resource(name = "shopingPushinfoService")
	private ShopingPushinfoServiceI shopingPushinfoService;
	
	/**
	 * 个推透传回执
	 * @param infoMap
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/transmissionTemplateCallBack")
	@ResponseBody
	@SystemControllerLog(description = "APP 个推透传回执")
	public Map<String, Object> transmissionTemplateCallBack(String infoMap,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		String fid = null;
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap, Map.class);
			Integer fType = new Integer((String)jsonObj.get("fType"));//透传类型
			if(fType!=4 || fType != 5 || fType != 6){
				fid = (String)jsonObj.get("fid");//内容id
			}
			String mid = (String)jsonObj.get("mid");//电话号码
			String businessId = (String)jsonObj.get("businessId");//电话号码
			
			YBasicDynamicInfoPush dynamicInfoPush = null;
			YBasicLifePushinfo lifePushinfo = null;
			YBasicDemandsupplyPushinfo demandsupplyPushinfo = null;
			List<YBasicDemandsupplycmentPushinfo> demandSupplycmentPushinfolist = null;
			List<Informationview> informationviewlist = null;
			List<YBasicShopingPushinfo> shopingPushinfoList = null;
			
			//fType = 
			/**
			 * 1，发现未读数量
			 * 2，生活未读数量
			 * 3，商会未读数量
			 * 4，我的供需未读数量
			 * 5，预约管理未读数量
			 * 6，消息通知未读数量
			 */
			switch (fType) {
			case 1:
				demandsupplyPushinfo = dspService.findDemandsupplyPushinfo(fid, mid);
				if(demandsupplyPushinfo != null){
					demandsupplyPushinfo.setFstatu(Status.READ.value());
					dspService.update(demandsupplyPushinfo);
				}
				break;
			case 2:
				lifePushinfo = lifePushService.findLifePushinfo(fid, mid);
				if(lifePushinfo != null){
					lifePushinfo.setFstatu(Status.READ.value());
					lifePushService.update(lifePushinfo);
				}
				break;
			case 3:
				dynamicInfoPush = dynmicandInfoPushService.findDynamicInfoPush(fid, mid);
				if(dynamicInfoPush != null){
					dynamicInfoPush.setStatu(Status.READ.value());
					dynmicandInfoPushService.update(dynamicInfoPush);
				}
				break;
			case 4:
				demandSupplycmentPushinfolist = demandSupplyComentPushService.findByHql(mid);
				if(demandSupplycmentPushinfolist != null && demandSupplycmentPushinfolist.size() > 0){
					for(YBasicDemandsupplycmentPushinfo demandsupplycmentPushinfo:demandSupplycmentPushinfolist){
						demandsupplycmentPushinfo.setFstatu(Status.READ.value());
						demandSupplyComentPushService.update(demandsupplycmentPushinfo);
					}
				}
				break;
			case 5:
				shopingPushinfoList = shopingPushinfoService.findShopinPhone(mid);
				if(shopingPushinfoList != null && shopingPushinfoList.size() > 0){
					for(YBasicShopingPushinfo shopingPushinfo:shopingPushinfoList){
						shopingPushinfo.setFstatu(Status.READ.value());
						shopingPushinfoService.update(shopingPushinfo);
					}
				}
				break;
			case 6:
				informationviewlist = InformationviewService.findInformationviewPhone(mid);
				if(informationviewlist != null && informationviewlist.size() > 0){
					for(Informationview informationview:informationviewlist){
						informationview.setStatus("1");
						InformationviewService.update(informationview);
					}
				}
				break;
			default:
				break;
			}
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
//			dynmicandInfoPushService.push(fid, mid, businessId,dynmicandInfoPushService);
			/**
			 * 
			 * 用户所在商会未读消息
			 */
			String deviceId = "";//推送clientId
			Integer channelNo = null;//推送渠道号
			List<YAppdevice> devices  = dynmicandInfoPushService.push(mid);
			if(devices!=null&&devices.size()>0){
				deviceId = devices.get(0).getFclient();
				if(devices.get(0).getFappChannelNo()!=null){
					channelNo = new Integer(devices.get(0).getFappChannelNo());
				}
			}
			LoginUserGroupPushThread userGroupPush = new LoginUserGroupPushThread(mid,"未读消息", deviceId, businessId, channelNo, dynmicandInfoPushService);
			userGroupPush.start();
			
			//推送用户未读消息
			LoginUserPushThread userPush = new LoginUserPushThread("未读消息",deviceId,channelNo,mid,demandSupplyComentPushService,dynmicandInfoPushService,dspService,lifePushService,InformationviewService,shopingPushinfoService);
			userPush.start();
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		return map;
	}
	
	/**
	 * 个推获取详情基础参数
	 * @param infoMap
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/getPageInfoBaseParam",method=RequestMethod.POST)
	@SystemControllerLog(description = "APP 个推获取详情基础参数")
	public Map<String, Object> getPageInfoBaseParam(String infoMap,HttpServletResponse response,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
//		String url = request.getScheme() + "://" + request.getServerName()
//				+ ":" + request.getServerPort()+""
//				+ request.getContextPath() + "/";
//		String url1 = request.getScheme() + "://" + request.getServerName()
//				+ ":" + request.getServerPort()+""
//				+ "admanagesystem-web" + "/";
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			Integer type=(node.get("type")==null?null:node.get("type").intValue());//	
			String fid=(node.get("fid")==null?null:node.get("fid").textValue());//	
			String tel=(node.get("mid")==null?null:node.get("mid").textValue());
			YBasicMember  member = memberService.getByMobilePhone(tel);
			String yuyue = "";
			//生活后面参数加密
			if(member!=null){
				String name = member.getFname();
				String groupId = member.getYBasicSocialgroups().getFid();
				yuyue= "id="+fid+"&member="+tel+"&groupId="+groupId+"&name="+name;
				yuyue = URLEncoder.encode(yuyue);
			}
			
			//1发现供应2发现需求3生活4商会商会动态5商会商会通知6预约管理7我的供需-供应评论8我的供需-需求评论
			Map<String,Object> resultMap = new HashMap<String,Object>();
			Map<String,Object> urlMap =null;
			if(type!=null&&fid!=null&&!fid.equals("")){
				if(type == 1||type == 2||type == 7||type == 8){
					resultMap.put("type", type);
					resultMap.put("param", fid);
				}else if(type == 3){
					resultMap.put("type", type);
					urlMap = new HashMap<String,Object>();
					urlMap.put("url", "webApp/shhdxq.html?id="+fid);
					urlMap.put("fid", fid);
					resultMap.put("param", urlMap);
				}else if(type == 4){
					resultMap.put("type", type);
					urlMap = new HashMap<String,Object>();
					urlMap.put("url", "webApp/dynamicDetail.html?id="+fid);
					urlMap.put("fid", fid);
					resultMap.put("param", urlMap);
				}else if(type == 5){
					resultMap.put("type", type);
					urlMap = new HashMap<String,Object>();
					urlMap.put("url", "webApp/informDetail.html?id="+fid);
					urlMap.put("fid", fid);
					resultMap.put("param", urlMap);
				}else if(type == 6){
					resultMap.put("type", type);
					urlMap = new HashMap<String,Object>();
					urlMap.put("url","App/yuyue.htm?"+yuyue);
					urlMap.put("fid", fid);
					resultMap.put("param", urlMap);
				}
			}
			map.put("result", resultMap);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			map.put("result", "");
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		return map;
	}
	
}
