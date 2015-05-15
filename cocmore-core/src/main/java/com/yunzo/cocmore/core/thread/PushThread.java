package com.yunzo.cocmore.core.thread;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplyPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplycmentPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDynamicInfoPush;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLifePushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.service.DemandSupplyComentPushService;
import com.yunzo.cocmore.core.function.service.DemandsupplyPushinfoServiceI;
import com.yunzo.cocmore.core.function.service.DynmicandInfoPushService;
import com.yunzo.cocmore.core.function.service.LifePushinfoSerciveI;
import com.yunzo.cocmore.core.function.util.CheckBytes;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.function.vo.PushVo;
import com.yunzo.cocmore.utils.gexin.PushToListMessage;
import com.yunzo.cocmore.utils.gexin.TemplateType;
/**
 * type
 * @author ailierke
 *
 */
public class PushThread extends Thread{
	/**
	 * 1、供应 
	 * 2：需求
	 * 3：生活
	 * 4：商会商会动态
	 * 5：商会商会通知
	 * 6：预约管理
	 * 7：我的供应评论：
	 * 8、我的需求评论
	 */
	private int type;
	private Map<String,PushVo> pushVoMap;
	private String title;
	private String infoFid;
	private String groupId;
	private DemandSupplyComentPushService demandSupplyComentPushService;
	private static ObjectMapper objectMapper = new ObjectMapper();

	private DynmicandInfoPushService dynmicandInfoPushService;
	private DemandsupplyPushinfoServiceI dspService;

	private LifePushinfoSerciveI lifePushService;
	public PushThread(int type, Map<String, PushVo> pushVoMap, String title,
			String infoFid, String groupId,
			DemandsupplyPushinfoServiceI dspService) {
		super();
		this.type = type;
		this.pushVoMap = pushVoMap;
		this.title = title;
		this.infoFid = infoFid;
		this.groupId = groupId;
		this.dspService = dspService;
	}

	public PushThread(int type, Map<String, PushVo> pushVoMap, String title,
			String infoFid, String groupId,
			DemandSupplyComentPushService demandSupplyComentPushService) {
		super();
		this.type = type;
		this.pushVoMap = pushVoMap;
		this.title = title;
		this.infoFid = infoFid;
		this.groupId = groupId;
		this.demandSupplyComentPushService = demandSupplyComentPushService;
	}

	public PushThread(int type, Map<String, PushVo> pushVoMap, String title,
			String infoFid, String groupId,
			DynmicandInfoPushService dynmicandInfoPushService) {
		super();
		this.type = type;
		this.pushVoMap = pushVoMap;
		this.title = title;
		this.infoFid = infoFid;
		this.groupId = groupId;
		this.dynmicandInfoPushService = dynmicandInfoPushService;
	}
	public PushThread(int type, Map<String, PushVo> pushVoMap, String title,
			String infoFid, String groupId,
			LifePushinfoSerciveI lifePushService) {
		super();
		this.type = type;
		this.pushVoMap = pushVoMap;
		this.title = title;
		this.infoFid = infoFid;
		this.groupId = groupId;
		this.lifePushService = lifePushService;
	}
	public DemandSupplyComentPushService getDemandSupplyComentPushService() {
		return demandSupplyComentPushService;
	}
	public DemandsupplyPushinfoServiceI getDspService() {
		return dspService;
	}
	public DynmicandInfoPushService getDynmicandInfoPushService() {
		return dynmicandInfoPushService;
	}
	public String getGroupId() {
		return groupId;
	}

	public String getInfoFid() {
		return infoFid;
	}
	public LifePushinfoSerciveI getLifePushService() {
		return lifePushService;
	}
	public Map<String, PushVo> getPushVoMap() {
		return pushVoMap;
	}

	public String getTitle() {
		return title;
	}
	public int getType() {
		return type;
	}



	@Override
	public void run() {
		//如果存在此用户的clientId信息就推送
		if(pushVoMap!=null){
			Set<String>  clientIds = pushVoMap.keySet();
			if(clientIds!=null&&clientIds.size()>0){
				/**
				 * clientId进行循环推送
				 */
				String logo ="";
				String logoUrl="";
				Map<String,Object> transmissionContentMap = new HashMap<String,Object>();
				transmissionContentMap.put("t",type);
				transmissionContentMap.put("b",groupId);
				transmissionContentMap.put("c",infoFid);
				String transmissionContent ="";
				try {
					transmissionContent = objectMapper.writeValueAsString(transmissionContentMap);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				};
				int transmissionType =1;
				String headLine = title;
				//如果大于12个汉字就截取
				if(headLine.getBytes().length>12){
					try {
						headLine = CheckBytes.substring(headLine, 12)+"...";
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				YSystemconfigurationMessage message = new YSystemconfigurationMessage();
				message.setFcontent(headLine);
				message.setFbillState(new Integer(5));
				message.setFcreateTime(new java.sql.Date(new java.util.Date().getTime()));
				/**
				 * 增加系统推送记录
				 */
				try {
					if(type==4||type==5){
						message.setFmessageType("动态通知模块");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                				message.setFmessageType("商会动态通知模块");
						dynmicandInfoPushService.saveConfigurationMessage(message);
					}else if(type==2||type==1){
						message.setFmessageType("商会供需模块");
						dspService.saveConfigurationMessage(message);
					}else if(type==3){
						message.setFmessageType("生活模块");
						lifePushService.saveConfigurationMessage(message);
					}else if(type==6){
						message.setFmessageType("预约管理");
						
					}else if(type==7){
						message.setFmessageType("我的供需评论模块");
						demandSupplyComentPushService.saveConfigurationMessage(message);
					}					

				} catch (Exception e) {
					e.printStackTrace();
				}
				for(String  clientid:clientIds){
					PushVo pushVo = pushVoMap.get(clientid);
					List<String> tels =pushVo.getTels(); 
					//使用激活应用模板
					boolean flag =false;
					try {
						System.out.println(clientid + "," + new Integer(pushVo.getChannelno()));
						flag = PushToListMessage.sendDownLoadMessageToSingel(clientid, new Integer(pushVo.getChannelno()), TemplateType.NOTIFY_TEMPLATE.toString(), "云筑", headLine, logoUrl, logo, transmissionContent, transmissionType, null, null, null, null, null, null, null);
					} catch (Exception e) {
						flag = false;
					}
					if(flag==true){
						YBasicDynamicInfoPush pushInfo = null;
						YBasicLifePushinfo lifeInfo = null;
						YBasicDemandsupplycmentPushinfo commentPushInfo =null;//供需评论推送
						YBasicDemandsupplyPushinfo demandsupplyPushinfo = null;
						for(String tel : tels){
							/**
							 * 增加推送记录
							 */
							try {
								if(type==4||type==5){
									pushInfo =  dynmicandInfoPushService.findDynamicInfoPush(infoFid, tel);
									if(pushInfo==null){
										pushInfo = new YBasicDynamicInfoPush();
										pushInfo.setDynamicinformid(infoFid);
										pushInfo.setPushtitle(headLine);
										pushInfo.setStatu(Status.UNREAD.value());
										pushInfo.setTel(tel);
										pushInfo.setUpdatetime(new java.sql.Date(new java.util.Date().getTime()));
										pushInfo.setGroupId(groupId);
										if(type==4){
											pushInfo.setType(new Integer(4));
										}else{
											pushInfo.setType(new Integer(5));
										}
										dynmicandInfoPushService.savePushInfo(pushInfo);//推送消息表
									}else{
										pushInfo.setStatu(Status.UNREAD.value());
										dynmicandInfoPushService.update(pushInfo);
									}
								}else if(type==1 || type==2){
									demandsupplyPushinfo = dspService.findDemandsupplyPushinfo(infoFid, tel);
									if(demandsupplyPushinfo != null){
										demandsupplyPushinfo.setFstatu(Status.UNREAD.value());
										dspService.update(demandsupplyPushinfo);
									}else{
										demandsupplyPushinfo = new YBasicDemandsupplyPushinfo();
										demandsupplyPushinfo.setFdemandsupplyId(infoFid);
										demandsupplyPushinfo.setFpushTitle(headLine);
										demandsupplyPushinfo.setFstatu(Status.UNREAD.value());
										demandsupplyPushinfo.setFtel(tel);
										demandsupplyPushinfo.setFupdateTime(new java.sql.Date(new java.util.Date().getTime()));
										if(type==1){
											demandsupplyPushinfo.setFtype(new Integer(1));
										}else{
											demandsupplyPushinfo.setFtype(new Integer(2));
										}
										dspService.save(demandsupplyPushinfo);//供需推送消息表
									}
								}else if(type==3){
									lifeInfo = lifePushService.findLifePushinfo(infoFid, tel);
									if(lifeInfo != null){
										lifeInfo.setFstatu(Status.UNREAD.value());
										lifePushService.update(lifeInfo);
									}else{
										lifeInfo = new YBasicLifePushinfo();
										lifeInfo.setFlifeId(infoFid);
										lifeInfo.setFpushTitle(headLine);
										lifeInfo.setFstatu(Status.UNREAD.value());
										lifeInfo.setFtel(tel);
										lifeInfo.setFupdateTime(new java.sql.Date(new java.util.Date().getTime()));
										lifeInfo.setFtype(new Integer(3));
										lifePushService.save(lifeInfo);//推送消息表
									}
								}else if(type==6){
									
								}else if(type==7||type==8){
									commentPushInfo = demandSupplyComentPushService.findPushInfo(tel);
									if(commentPushInfo==null){
										commentPushInfo = new YBasicDemandsupplycmentPushinfo();
										commentPushInfo.setFmobilePhone(tel);
										commentPushInfo.setFremark("供应需求评论推送");
										commentPushInfo.setFstatu(Status.UNREAD.value());
										commentPushInfo.setFupdateTime(new java.sql.Date(new java.util.Date().getTime()));
										demandSupplyComentPushService.savePushInfo(commentPushInfo);
									}else{
										commentPushInfo.setFstatu(Status.UNREAD.value());
										demandSupplyComentPushService.upatePushInfo(commentPushInfo);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				}
			}
		}
	}


	public void setDemandSupplyComentPushService(
			DemandSupplyComentPushService demandSupplyComentPushService) {
		this.demandSupplyComentPushService = demandSupplyComentPushService;
	}
	public void setDspService(DemandsupplyPushinfoServiceI dspService) {
		this.dspService = dspService;
	}
	public void setDynmicandInfoPushService(
			DynmicandInfoPushService dynmicandInfoPushService) {
		this.dynmicandInfoPushService = dynmicandInfoPushService;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public void setInfoFid(String infoFid) {
		this.infoFid = infoFid;
	}
	public void setLifePushService(LifePushinfoSerciveI lifePushService) {
		this.lifePushService = lifePushService;
	}
	public void setPushVoMap(Map<String, PushVo> pushVoMap) {
		this.pushVoMap = pushVoMap;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(int type) {
		this.type = type;
	}
//	public static void main(String[] args) throws JsonProcessingException {
//		Map<String,Object> transmissionContentMap = new HashMap<String,Object>();
//		transmissionContentMap.put("t","type");
//		transmissionContentMap.put("b","groupId");
//		transmissionContentMap.put("c","infoFid");
//		String transmissionContent = objectMapper.writeValueAsString(transmissionContentMap);
//		System.out.println(transmissionContent);
//	}
}
