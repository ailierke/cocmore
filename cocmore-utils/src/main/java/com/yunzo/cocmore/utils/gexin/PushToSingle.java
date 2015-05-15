package com.yunzo.cocmore.utils.gexin;

import java.util.ArrayList;
import java.util.List;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.PopupTransmissionTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
/**
 * 推送消息到个人
 * @author david
 *
 */
public class PushToSingle {
	private static String appId = "OXBi46P0zE7ipnd81ykRB4";
	private static String appkey = "Pvled0TUP88HfbYv0eS7p2";
	private static String master = "RhC0NsKtEz7VjZbyfsfSU9";
	private static String CID = "3a221842feefa4669d67b8e35e124904";
//	private static String CID = "d5cb3a602439acc395b92985b87f17b7";
	private static String Alias = "请输入别名"; // 根据别名推送，需要先绑定别名

	private static String host = "http://sdk.open.api.igexin.com/apiex.htm";

	public static void send() throws Exception {
		IGtPush push = new IGtPush(host, appkey, master);
		push.connect();

//		LinkTemplate template = linkTemplateDemo();
//		 LinkTemplate template = linkTemplateDemo();
		 NotificationTemplate template = NotificationTemplateDemo();
//       NotyPopLoadTemplate template =NotyPopLoadTemplateDemo();

		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		message.setOfflineExpireTime(1 * 1000 * 3600);
		message.setData(template);
		// message.setPushNetWorkType(1); //根据WIFI推送设置

		List<Target> targets = new ArrayList<Target>();
		Target target1 = new Target();
		Target target2 = new Target();
		target1.setAppId(appId);
		target1.setClientId(CID);
		// target1.setAlias(Alias); //根据别名推送设置，CID与Alias可二选一进行推送

		IPushResult ret = push.pushMessageToSingle(message, target1);
		System.out.println(ret.getResponse().toString());
	}

	public static PopupTransmissionTemplate PopupTransmissionTemplateDemo() {
		PopupTransmissionTemplate template = new PopupTransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		template.setText("");
		template.setTitle("");
		template.setImg("");
		template.setConfirmButtonText("");
		template.setCancelButtonText("");
		template.setTransmissionContent("111");
		template.setTransmissionType(1);

		return template;
	}
	/**
	 * 透传（payload）消息模板
	 * 数据经SDK传给您的客户端，由您写代码决定如何处理展现给用户
	 * @return
	 * @throws Exception
	 */
	public static TransmissionTemplate TransmissionTemplateDemo()
			throws Exception {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		template.setTransmissionType(1);
		template.setTransmissionContent("OS-TOSingle");
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
	public static LinkTemplate linkTemplateDemo() throws Exception {
		LinkTemplate template = new LinkTemplate();
		// 设置APPID与APPKEY
	    template.setAppId(appId);
	    template.setAppkey(appkey);
	    // 设置通知栏标题与内容
	    template.setTitle("请输入通知栏标题");
	    template.setText("请输入通知栏内容");
	    // 配置通知栏图标
	    template.setLogo("icon.png");
	    // 配置通知栏网络图标
	    template.setLogoUrl("");
	    // 设置通知是否响铃，震动，或者可清除
	    template.setIsRing(true);
	    template.setIsVibrate(true);
	    template.setIsClearable(true);
	    // 设置打开的网址地址
	    template.setUrl("http://www.baidu.com");
		// template.setPushInfo("actionLocKey", 1, "message", "sound",
		// "payload", "locKey", "locArgs", "launchImage");
		return template;
	}
	/**
	 * 激活应用
	 * @return
	 * @throws Exception
	 */
	public static NotificationTemplate NotificationTemplateDemo()
			throws Exception {
		NotificationTemplate template = new NotificationTemplate();
	    // 设置APPID与APPKEY
	    template.setAppId(appId);
	    template.setAppkey(appkey);
	    // 设置通知栏标题与内容
	    template.setTitle("请输入通知栏标题");
	    template.setText("请输入通知栏内容");
	    // 配置通知栏图标
	    template.setLogo("icon.png");
	    // 配置通知栏网络图标
	    template.setLogoUrl("");
	    // 设置通知是否响铃，震动，或者可清除
	    template.setIsRing(true);
	    template.setIsVibrate(true);
	    template.setIsClearable(true);
	    // 透传消息设置
	    template.setTransmissionType(1);
	    template.setTransmissionContent("请输入您要透传的内容");
		// template.setPushInfo("actionLocKey", 2, "message", "sound",
		// "payload", "locKey", "locArgs", "launchImage");
		return template;
	}
	/**
	 * 点击通知栏弹框下载模板
	 * 在通知栏显示一条含图标、标题等的通知，用户点击后弹出框，用户可以选择直接下载应用或者取消下载应用。
	 * @return
	 */
	public static NotyPopLoadTemplate NotyPopLoadTemplateDemo() {
		NotyPopLoadTemplate template = new NotyPopLoadTemplate();
		// 填写appid与appkey
		template.setAppId(appId);
		template.setAppkey(appkey);
		// 填写通知标题和内容
		template.setNotyTitle("标题");
		template.setNotyContent("内容");
		// template.setLogoUrl("");
		// 填写图标文件名称
		template.setNotyIcon("text.png");
		// 设置响铃，震动，与可清除
		// template.setBelled(false);
		// template.setVibrationed(false);
		// template.setCleared(true);

		// 设置弹框标题与内容
		template.setPopTitle("弹框标题");
		template.setPopContent("弹框内容");
		// 设置弹框图片
		template.setPopImage("http://www-igexin.qiniudn.com/wp-content/uploads/2013/08/logo_getui1.png");
		template.setPopButton1("打开");
		template.setPopButton2("取消");

		// 设置下载标题，图片与下载地址
		template.setLoadTitle("下载标题");
		template.setLoadIcon("file://icon.png");
		template.setLoadUrl("http://gdown.baidu.com/data/wisegame/c95836e06c224f51/weixinxinqing_5.apk");
		template.setActived(true);
		template.setAutoInstall(true);
		template.setAndroidMark("");
		return template;
	}

}
