package com.yunzo.cocmore.core.thread;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.yunzo.cocmore.core.function.util.CheckBytes;
import com.yunzo.cocmore.utils.gexin.PushToListMessage;
import com.yunzo.cocmore.utils.gexin.TemplateType;


/**
 * Description: <>. <br>
 * @date:2015年3月24日 上午11:12:41
 * @author beck
 * @version V1.0
 */
public class LoginIsUpdatePushThread extends Thread{
	private String title;
	private String clientId;
	private int channelno;
	private int isUpdate;
	
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
	
	public int getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(int isUpdate) {
		this.isUpdate = isUpdate;
	}
	public LoginIsUpdatePushThread(String title,String clientId,int channelno,int isUpdate){
		this.title = title;
		this.clientId = clientId;
		this.channelno = channelno;	
		this.isUpdate = isUpdate;
	}
	@Override
	public void run() {
		try {
			
			String logo ="";
			String logoUrl="";
			Map<String,Object> transmissionContentMap = new HashMap<String,Object>();
			transmissionContentMap.put("codeType",3);
			transmissionContentMap.put("infoMap",isUpdate);
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
