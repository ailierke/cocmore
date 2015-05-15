package com.yunzo.cocmore.utils.gexin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
/**
 * 推送消息到多个用户 根据设备表中 app渠道号(0表示android，1表示appstore，2表示苹果企业分发)来调用。
 * @author ailierke
 *
 */
public class PushToListMessage {
	/**
	 * 0表示android，1表示appstore
	 */
	private static String android_appstore_appId = "rxLIX0TCqb9QNQlvttSwB";
	private static String android_appstore_appkey = "pKXz7wsWtm8mdZ3nKbxdc2";
	private static String android_appstore_master = "JJFATWZNOv5mcfXxCpzPl8";
	/**
	 * 2表示苹果企业分发
	 */
	private static String apple_appId = "Dx3831cvM86E9uMidPFrF1";
	private static String apple_appkey = "kHG4hCQ6nr5keVek1woOB2";
	private static String apple_master = "omUyi4tIl47ACVB1MdVnx2";
	//	private static String CID = "3a221842feefa4669d67b8e35e124904";
	//	private static String CID = "d5cb3a602439acc395b92985b87f17b7";
	//	private static String Alias = "请输入别名"; // 根据别名推送，需要先绑定别名
	/**
	 * 3表示android，4表示appstore
	 */
	private static String android_appstore_appId_shizihui = "W6tpvNiHzy5ZCjJAy23vxA";
	private static String android_appstore_appkey_shizihui = "yfqnjM225P8dwNHFT5Inb9";
	private static String android_appstore_master_shizihui = "pQzIaCtTtd7K9eA0z8a0y6";
	/**
	 * 5表示苹果企业分发_狮子会
	 */
	private static String apple_appId_shizihui = "pwGPfIStgw9jiWoShVI142";
	private static String apple_appkey_shizihui = "T0XdXv9SuLArgBCfdGMkQ6";
	private static String apple_master_shizihui = "FoAOCCVbN89gdKNCwKo8c";
	private static String host = "http://sdk.open.api.igexin.com/apiex.htm";
	private static String url_host ="http://sdk.open.api.igexin.com/serviceex";
	/**
	 * 将渠道号（在设备表中）为0和1的用户的clientId放在一起，调用此方法传appChannelNo=1或者0都可以，将渠道号为2的用户的clientId放在一起,调用此方法传appChannelNo=2
	 * clientIds表示客户端id(要推送到的客户端)
	 * appChannelNo表示渠道号
	 * @param templateType模板类型（发送消息有4中推送类型） 在枚举类TmplateType中
	 * 激活应用模板
	 * 
	 * 模板共用参数
	 * @param title 通知栏标题
	 * @param text 通知栏内容
	 * @param logo 通知栏图标
	 * @param logoUrl通知栏网络图标
	 * 打开网页模板参数
	 * @param Url 打开网页地址
	 * 下载模板参数
	 * @param popTitle   设置弹框标题
	 * @param popContent 设置弹框内容
	 * @param popImage   设置弹框显示的图片
	 * @param downLoadTitle  设置下载标题    
	 * @param downLoadIcon   设置弹框显示的图片
	 * @param downLoadUrl    设置下载地址    
	 * 透传消息参数（传透消息可设置在通知打开应用模板中使用）
	 * @param transmissionType 收到消息是否立即启动应用，1为立即启动，2则广播等待客户端自启动
	 * @param transmissionContent 透传内容，不支持转义字符
	 * @throws Exception 
	 */
	public  void sendDownLoadMessageToList(String[] clientIds,int appChannelNo,String templateType, String title,String text,String logoUrl,String logo,String transmissionContent,int transmissionType,
			String url,String popTitle,String popContent,String popImage,String downLoadTitle,String downLoadIcon,String downLoadUrl
			) throws Exception{
		System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");
		String appkey="";String master="";String appId ="";
		/**
		 * 根据渠道号选择对应的发送渠道
		 */
		if(appChannelNo==0||appChannelNo==1){
			appkey = android_appstore_appkey;
			appId=android_appstore_appId;
			master = android_appstore_master;
		}else if(appChannelNo==2){
			appkey = apple_appkey;
			appId=apple_appId;
			master = apple_master;
		}else if(appChannelNo==3||appChannelNo==4){
			appkey = android_appstore_appkey_shizihui;
			appId=android_appstore_appId_shizihui;
			master = android_appstore_master_shizihui;
		}else if(appChannelNo==5){
			appkey = apple_appkey_shizihui;
			appId=apple_appId_shizihui;
			master = apple_master_shizihui;
		}
		if(appChannelNo==0||appChannelNo==3){
			final IGtPush push = new IGtPush(host, appkey, master);
			push.connect();
			AbstractTemplate template = null;
//			NotificationTemplate template = NotificationTemplateDemo(title, text, logo, logoUrl, transmissionContent, transmissionType);
			switch (templateType) {
			case "DOWN_LOAD_TEMPLATE"://下载模板
				template = NotyPopLoadTemplateDemo(title, text, logo, logoUrl, popTitle, popContent, popImage, downLoadTitle, downLoadIcon, downLoadUrl);
				break;
			case "NOTIFY_TEMPLATE"://激活应用
				template = NotificationTemplateDemo(title, text, logo, logoUrl, transmissionContent, transmissionType);
				break;	
			case "LINK_TEMPLATE"://打开网页
				template = linkTemplateDemo(title, text, logo, logoUrl, downLoadUrl);
				break;
			case "TRANSMISSION_TEMPLATE"://透传模板
				template = TransmissionTemplateDemo(transmissionContent, transmissionType);
				break;
			default:
				break;
			}
			// 设置APPID与APPKEY
			template.setAppId(appId);
			template.setAppkey(appkey);


			ListMessage message = new ListMessage();

			message.setData(template);

			message.setOffline(false);
			message.setOfflineExpireTime(24 * 1000 * 3600);
			message.setPushNetWorkType(0);

			List<Target> targets = new ArrayList<Target>();
			Target target =null;
			/**
			 * 循环加入推送的客户端id
			 */
			if(clientIds!=null){
				for(String clientId:clientIds){
					target = new Target();
					target.setAppId(appId);
					target.setClientId(clientId);
					//				target1.setAlias(Alias);
					targets.add(target);
				}
			}
			String taskId = push.getContentId(message, "toList_Alias_Push");
			//推送消息
			IPushResult ret = push.pushMessageToList(taskId, targets);
			System.out.println(ret.getResponse().toString());
		}else if(appChannelNo==1||appChannelNo==2||appChannelNo==4||appChannelNo==5){
			IGtPush push = new IGtPush(url_host, appkey, master);
		       APNTemplate t = new APNTemplate();
		       t.setPushInfo("ok", 5, text, "beep.wav", "get new message", "", "", "");  
		       ListMessage message = new ListMessage();
		       message.setData(t);
		       String contentId = push.getAPNContentId(appId, message);
		       System.out.println(contentId);
		       List<String> dtl = new ArrayList<String>();
		       dtl.toArray(clientIds);
		       System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");
		       IPushResult ret = push.pushAPNMessageToList(appId, contentId, dtl);
		       System.out.println(ret.getResponse());
		}
		
	}
	/**
	 * 推送给个人
	 * 参数和上面一致
	 * @throws Exception
	 */
	public static  boolean sendDownLoadMessageToSingel(String clientId,int appChannelNo,String templateType, String title,String text,String logoUrl,String logo,String transmissionContent,int transmissionType,
			String url,String popTitle,String popContent,String popImage,String downLoadTitle,String downLoadIcon,String downLoadUrl
			) throws Exception{
		boolean flag =true;
		String appkey="";String master="";String appId ="";
		/**
		 * 根据渠道号选择对应的发送渠道
		 */
		if(appChannelNo==0||appChannelNo==1){
			appkey = android_appstore_appkey;
			appId=android_appstore_appId;
			master = android_appstore_master;
		}else if(appChannelNo==2){
			appkey = apple_appkey;
			appId=apple_appId;
			master = apple_master;
		}else if(appChannelNo==3||appChannelNo==4){
			appkey = android_appstore_appkey_shizihui;
			appId=android_appstore_appId_shizihui;
			master = android_appstore_master_shizihui;
		}else if(appChannelNo==5){
			appkey = apple_appkey_shizihui;
			appId=apple_appId_shizihui;
			master = apple_master_shizihui;
		}
		
		if(appChannelNo==0||appChannelNo==3){
			
			AbstractTemplate template = null;
			switch (templateType) {
			case "DOWN_LOAD_TEMPLATE"://下载模板
				template = NotyPopLoadTemplateDemo(title, text, logo, logoUrl, popTitle, popContent, popImage, downLoadTitle, downLoadIcon, downLoadUrl);
				break;
			case "NOTIFY_TEMPLATE"://激活应用
				template = NotificationTemplateDemo(title, text, logo, logoUrl, transmissionContent, transmissionType);
				break;	
			case "LINK_TEMPLATE"://打开网页
				template = linkTemplateDemo(title, text, logo, logoUrl, downLoadUrl);
				break;
			case "TRANSMISSION_TEMPLATE"://透传模板
				template = TransmissionTemplateDemo(transmissionContent, transmissionType);
				break;
			default:
				break;
			}
			flag = templatePush(template, appId, appkey, clientId, master, flag);
		}else if(appChannelNo==1||appChannelNo==2||appChannelNo==4||appChannelNo==5){
			if(templateType.equals("TRANSMISSION_TEMPLATE")){
				 	TransmissionTemplate template = new TransmissionTemplate();
				    template.setAppId(appId);
				    template.setAppkey(appkey);
				    // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动。
				    template.setTransmissionType(2);
				    template.setTransmissionContent(transmissionContent);
				    flag = templatePush(template, appId, appkey, clientId, master, flag);
			}else{
					IGtPush push = new IGtPush(url_host, appkey, master); 
			         APNTemplate t = new APNTemplate();
			         t.setPushInfo("ok", 1, text, "beep.wav", transmissionContent, "", "", ""); 
			         SingleMessage sm = new SingleMessage();
			         sm.setData(t);
			         IPushResult ret0 = null;
			     	try {
			     		ret0 = push.pushAPNMessageToSingle(appId, clientId, sm);
						Map<String, Object> map = ret0.getResponse();
						System.out.println(map.toString());
						if(map!=null&&map.get("result")!=null&&map.get("result").equals("ok")){
							flag = true;
						}else{
							flag = false;
						}
					} catch (Exception e) {
						e.printStackTrace();
						flag = false;
					}
			}
		}
		     
		return flag;
	}
	
	public static boolean templatePush(AbstractTemplate template,String appId,String appkey,String clientId,String master,boolean flag) throws IOException{
					IGtPush push = new IGtPush(host, appkey, master);
				 	push.connect();
				 	// 设置APPID与APPKEY
					template.setAppId(appId);
					template.setAppkey(appkey);


					SingleMessage message = new SingleMessage();
					message.setOffline(true);
					message.setOfflineExpireTime(24 * 1000 * 3600);
					message.setData(template);
					// message.setPushNetWorkType(1); //根据WIFI推送设置  
					message.setPushNetWorkType(0);

					Target target = new Target();
					target.setAppId(appId);
					target.setClientId(clientId);
					// target1.setAlias(Alias); //根据别名推送设置，CID与Alias可二选一进行推送
					IPushResult ret = null;
					//推送消息
					try {
						ret = push.pushMessageToSingle(message, target);
						Map<String, Object> map = ret.getResponse();
						System.out.println(map.toString());
						if(map!=null&&map.get("result")!=null&&map.get("result").equals("ok")){
							flag = true;
						}else{
							flag = false;
						}
					} catch (Exception e) {
						flag = false;
					}
					return flag;
	}
	/**
	 * 透传（payload）消息模板
	 * 数据经SDK传给您的客户端，由您写代码决定如何处理展现给用户
	 * @return
	 * @throws Exception
	 */
	public static TransmissionTemplate TransmissionTemplateDemo(String transmissionContent,int transmissionType)
			throws Exception {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setTransmissionType(transmissionType);
		template.setTransmissionContent(transmissionContent);
		// template.setPushInfo("dd", 1, "ddd", "com.gexin.ios.silence", "", "",
		// "", "");
		return template;
	}
	/**
	 * 点击通知打开网页模板
	 * 在通知栏显示一条含图标、标题等的通知，用户点击可打开您指定的网页
	 * @return
	 * @throws Exception
	 */
	public static LinkTemplate linkTemplateDemo(String title,String text,String logo,String logoUrl,String url) throws Exception {
		LinkTemplate template = new LinkTemplate();

		// 设置通知栏标题与内容
		template.setTitle(title);
		template.setText(text);
		// 配置通知栏图标
		template.setLogo(logo);
		// 配置通知栏网络图标
		template.setLogoUrl(logoUrl);
		// 设置通知是否响铃，震动，或者可清除
		template.setIsRing(true);
		template.setIsVibrate(true);
		template.setIsClearable(true);
		// 设置打开的网址地址
		template.setUrl(url);
		// template.setPushInfo("actionLocKey", 1, "message", "sound",
		// "payload", "locKey", "locArgs", "launchImage");
		return template;
	}
	/**
	 * 点击通知打开应用模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public static NotificationTemplate NotificationTemplateDemo(String title,String text,String logo,String logoUrl,String transmissionContent,int transmissionType)
			throws Exception {
		NotificationTemplate template = new NotificationTemplate();
		// 设置通知栏标题与内容
		template.setTitle(title);
		template.setText(text);
		// 配置通知栏图标
		template.setLogo(logo);
		// 配置通知栏网络图标
		template.setLogoUrl(logoUrl);
		// 设置通知是否响铃，震动，或者可清除
		template.setIsRing(true);
		template.setIsVibrate(true);
		template.setIsClearable(true);
		// 透传消息设置
		template.setTransmissionType(transmissionType);
		template.setTransmissionContent(transmissionContent);
		// template.setPushInfo("actionLocKey", 2, "message", "sound",
		// "payload", "locKey", "locArgs", "launchImage");
		return template;
	}
	/**
	 * 点击通知栏弹框下载模板
	 * 在通知栏显示一条含图标、标题等的通知，用户点击后弹出框，用户可以选择直接下载应用或者取消下载应用。
	 * @return
	 */
	public static  NotyPopLoadTemplate NotyPopLoadTemplateDemo(String title,String text,String logo,String logoUrl,String popTitle,String popContent,String popImage,String downLoadTitle,String downLoadIcon,String downLoadUrl) {
		NotyPopLoadTemplate template = new NotyPopLoadTemplate();
		// 设置通知栏标题与内容
		template.setNotyTitle(title);
		template.setNotyContent(text);
		// 配置通知栏图标
		template.setNotyIcon(logo);
		// 配置通知栏网络图标
		template.setLogoUrl(logoUrl);
		// 设置通知是否响铃，震动，或者可清除
		template.setBelled(true);
		template.setVibrationed(true);
		template.setCleared(true);

		// 设置弹框标题与内容
		template.setPopTitle(popTitle);
		template.setPopContent(popContent);
		// 设置弹框显示的图片
		template.setPopImage(popImage);
		template.setPopButton1("下载");//下載
		template.setPopButton2("取消");//取消

		// 设置下载标题
		template.setLoadTitle(downLoadTitle);
		template.setLoadIcon(downLoadIcon);//圖片

		//设置下载地址       
		template.setLoadUrl(downLoadUrl);
		return template;
	}
      
         
	public static void main(String[] a) throws Exception{
		PushToListMessage m = new PushToListMessage();
		String logo ="";
		String logoUrl="";
		int transmissionType =1;
//		m.sendDownLoadMessageToSingel("9b34918589c6ebac437a12067453f6d9284971ba99df249d2fced89cd6693e95", 2, TemplateType.NOTIFY_TEMPLATE.toString(), "云筑", "商会通知", logoUrl, logo, transmissionContent, transmissionType, null, null, null, null, null, null, null);
//		m.sendDownLoadMessageToSingel("26b4ab9fd9c68f61f7a39707a7d891f2", 1, TemplateType.NOTIFY_TEMPLATE.toString(), "云筑", "商会通知1", logoUrl, logo, "透传消息112313", transmissionType, null, null, null, null, null, null, null);
//		Map<String,Object> transmissionContentMap = new HashMap<String,Object>();
//		transmissionContentMap.put("t",2);
//		transmissionContentMap.put("b","1233sdfasdfsd2345sdfsffasdf");
//		transmissionContentMap.put("c","fdsfasdfqwebdfbhdfgdfgdfgger");
		String transmissionContent = "{\"t\":5,\"b\":\"ewrqwerwegf2312312312\",\"c\":\"n2312312312x1221s\"}";
//		boolean flag = m.sendDownLoadMessageToSingel("90b52b30a6b1ce4fd31a0ad33e1e5d225915e74c723307aa34f8ef64a26485c8", 2, TemplateType.NOTIFY_TEMPLATE.toString(), "云筑11``", "商会通111知", logoUrl, logo, transmissionContentMap.toString(), transmissionType, null, null, null, null, null, null, null);
		boolean flag = m.sendDownLoadMessageToSingel("59c9664fb05e7e0165e56955392539e60db642047e32a1f92efa5b64c59d2cdf", 2, TemplateType.NOTIFY_TEMPLATE.toString(), "云筑11``", "商会通111知", logoUrl, logo, transmissionContent, transmissionType, null, null, null, null, null, null, null);
		System.out.println(flag);
	}
}
