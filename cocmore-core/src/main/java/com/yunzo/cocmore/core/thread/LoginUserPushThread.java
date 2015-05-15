package com.yunzo.cocmore.core.thread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.google.gson.Gson;
import com.yunzo.cocmore.core.function.service.DemandSupplyComentPushService;
import com.yunzo.cocmore.core.function.service.DemandsupplyPushinfoServiceI;
import com.yunzo.cocmore.core.function.service.DynmicandInfoPushService;
import com.yunzo.cocmore.core.function.service.InformationviewServiceI;
import com.yunzo.cocmore.core.function.service.LifePushinfoSerciveI;
import com.yunzo.cocmore.core.function.service.ShopingPushinfoServiceI;
import com.yunzo.cocmore.core.function.util.CheckBytes;
import com.yunzo.cocmore.utils.gexin.PushToListMessage;
import com.yunzo.cocmore.utils.gexin.TemplateType;

/**
 * Description: <登录推送线程>. <br>
 * @date:2015年3月20日 上午11:57:51
 * @author beck
 * @version V1.0
 */
public class LoginUserPushThread extends Thread{
	private String title;
	private String clientId;
	private int channelno;
	private String tel;
	private DemandSupplyComentPushService demandSupplyComentPushService;	//供需评论推送service
	private DynmicandInfoPushService dynmicandInfoPushService;	//商会动态推送service
	private DemandsupplyPushinfoServiceI dspService;	//发现供需推送service
	private LifePushinfoSerciveI lifePushService;	//生活推送service
	private InformationviewServiceI informationviewService; // 通知视图的接口
	private ShopingPushinfoServiceI shopingPushinfoService;	//订单接口
	Gson gson = new Gson();
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public int getChannelno() {
		return channelno;
	}
	public void setChannelno(int channelno) {
		this.channelno = channelno;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public DemandSupplyComentPushService getDemandSupplyComentPushService() {
		return demandSupplyComentPushService;
	}
	public void setDemandSupplyComentPushService(
			DemandSupplyComentPushService demandSupplyComentPushService) {
		this.demandSupplyComentPushService = demandSupplyComentPushService;
	}
	public DynmicandInfoPushService getDynmicandInfoPushService() {
		return dynmicandInfoPushService;
	}
	public void setDynmicandInfoPushService(
			DynmicandInfoPushService dynmicandInfoPushService) {
		this.dynmicandInfoPushService = dynmicandInfoPushService;
	}
	public DemandsupplyPushinfoServiceI getDspService() {
		return dspService;
	}
	public void setDspService(DemandsupplyPushinfoServiceI dspService) {
		this.dspService = dspService;
	}
	public LifePushinfoSerciveI getLifePushService() {
		return lifePushService;
	}
	public void setLifePushService(LifePushinfoSerciveI lifePushService) {
		this.lifePushService = lifePushService;
	}
	public InformationviewServiceI getInformationviewService() {
		return informationviewService;
	}
	public void setInformationviewService(
			InformationviewServiceI informationviewService) {
		this.informationviewService = informationviewService;
	}
	public LoginUserPushThread(String title,String clientId,int channelno,String tel,DemandSupplyComentPushService demandSupplyComentPushService
			,DynmicandInfoPushService dynmicandInfoPushService,DemandsupplyPushinfoServiceI dspService
			,LifePushinfoSerciveI lifePushService,InformationviewServiceI informationviewService,ShopingPushinfoServiceI shopingPushinfoService){
		this.title = title;
		this.clientId = clientId;
		this.channelno = channelno;
		this.tel = tel;
		this.demandSupplyComentPushService = demandSupplyComentPushService;
		this.dynmicandInfoPushService = dynmicandInfoPushService;
		this.dspService = dspService;
		this.lifePushService = lifePushService;
		this.informationviewService = informationviewService;
		this.shopingPushinfoService = shopingPushinfoService;
	}
	
	@Override
	public void run() {
		try {
			//获取推送未读数量
			//int dspNum = dspService.unreadDemandSupplyMsg();
			int dspNum = 0;													//发现未读数量默认返回0
			int lifeNum = lifePushService.unreadLifeMsg(tel);
			int dynNum = dynmicandInfoPushService.unreadDynmicandMsg(tel); 
			int dsc = demandSupplyComentPushService.unreadDemandSupplyComentMsg(tel);
			int orderNum = shopingPushinfoService.unreadOrderMsg(tel);
			
			//int msgNum = informationviewService.getListByUserTel(tel);
			int msgNum =0;
			
			List<Map<String, Object>> infoMap = new ArrayList<Map<String,Object>>();
			//个推infoMap内容
			Map<String, Object> m1 = new HashMap<String, Object>();
			m1.put("fType", 1);
			m1.put("unreaNum", dspNum);
			
			Map<String, Object> m2 = new HashMap<String, Object>();
			m2.put("fType", 2);
			m2.put("unreaNum", lifeNum);
			
			Map<String, Object> m3 = new HashMap<String, Object>();
			m3.put("fType", 3);
			m3.put("unreaNum", dynNum);
			
			Map<String, Object> m4 = new HashMap<String, Object>();
			m4.put("fType", 4);
			m4.put("unreaNum", dsc > 0 ? 1 : 0);
			
			//消息未读数，如果有未读消息就返回 1，否则返回 0
			Map<String, Object> m5 = new HashMap<String, Object>();
			m5.put("fType", 5);
			m5.put("unreaNum", orderNum > 0 ? 1 : 0);
			
			//消息未读数，如果有未读消息就返回 1，否则返回 0
			Map<String, Object> m6 = new HashMap<String, Object>();
			m6.put("fType", 6);
			m6.put("unreaNum", msgNum);
			
			infoMap.add(m1);
			infoMap.add(m2);
			infoMap.add(m3);
			infoMap.add(m4);
			infoMap.add(m5);
			infoMap.add(m6);
			
			String logo ="";
			String logoUrl="";
			Map<String,Object> transmissionContentMap = new HashMap<String,Object>();
			transmissionContentMap.put("codeType",1);
			transmissionContentMap.put("infoMap",infoMap);
			String transmissionContent=gson.toJson(transmissionContentMap);
			System.out.println(transmissionContent);
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
			//使用激活应用模板
			boolean flag =false;
			try {
				flag = PushToListMessage.sendDownLoadMessageToSingel(clientId, channelno, TemplateType.TRANSMISSION_TEMPLATE.toString(), "云筑", headLine, logoUrl, logo, transmissionContent, transmissionType, null, null, null, null, null, null, null);
			} catch (Exception e) {
				flag = false;
			}
			if(flag==true){
				System.out.println("ok...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
}
