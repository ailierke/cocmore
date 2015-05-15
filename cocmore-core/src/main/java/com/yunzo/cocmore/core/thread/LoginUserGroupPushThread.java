package com.yunzo.cocmore.core.thread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.yunzo.cocmore.core.function.service.DynmicandInfoPushService;
import com.yunzo.cocmore.core.function.util.CheckBytes;
import com.yunzo.cocmore.utils.gexin.PushToListMessage;
import com.yunzo.cocmore.utils.gexin.TemplateType;

/**
 * Description: <>. <br>
 * @date:2015年3月20日 下午3:52:55
 * @author beck
 * @version V1.0
 */
public class LoginUserGroupPushThread extends Thread{
	private String tel;
	private String title;
	private String clientId;
	private String groupIds;
	private int channelno;
	private DynmicandInfoPushService dynmicandInfoPushService;	//商会动态推送service
	Gson gson = new Gson();
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
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
	
	public String getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}
	public DynmicandInfoPushService getDynmicandInfoPushService() {
		return dynmicandInfoPushService;
	}
	public void setDynmicandInfoPushService(
			DynmicandInfoPushService dynmicandInfoPushService) {
		this.dynmicandInfoPushService = dynmicandInfoPushService;
	}
	
	public LoginUserGroupPushThread(String tel,String title,String clientId,String groupIds,int channelno
			,DynmicandInfoPushService dynmicandInfoPushService){
		this.tel = tel;
		this.title = title;
		this.clientId = clientId;
		this.groupIds = groupIds;
		this.channelno = channelno;
		this.dynmicandInfoPushService = dynmicandInfoPushService;		
	}
	
	
	@Override
	public void run() {
		try {
			List<Map<String, Object>> value = new ArrayList<Map<String,Object>>();
			for(String groupId : groupIds.split(",")){
				int dynNum = dynmicandInfoPushService.unreadDynmicandMsgByGroupId(tel,groupId);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bId", groupId);
				map.put("unreaNum", dynNum);
				value.add(map);
			}
			
			String logo ="";
			String logoUrl="";
			Map<String,Object> transmissionContentMap = new HashMap<String,Object>();
			transmissionContentMap.put("codeType",2);
			transmissionContentMap.put("infoMap",value);
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
				System.out.println(clientId + "," + channelno);
				flag = PushToListMessage.sendDownLoadMessageToSingel(clientId, channelno, TemplateType.TRANSMISSION_TEMPLATE.toString(), "云筑", headLine, logoUrl, logo, transmissionContent, transmissionType, null, null, null, null, null, null, null);
			} catch (Exception e) {
				flag = false;
			}
			if(flag==true){
				System.out.println("ok...");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
}
