package com.yunzo.cocmore.utils.base;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yunzo.cocmore.utils.http.HttpClientUtil;

/**
 * 即时通 短信发送接口
 * 
 * @author star
 *
 */
public class SMSUtils {
	/** 即时通 接口地址 */
	// 提交
	private static final String Opration = "http://userinterface.vcomcn.com/Opration.aspx";
	// 查询
	private static final String GetResult = "http://userinterface.vcomcn.com/GetResult.aspx";
	// 账户
	private static final String LoginName = "szyzkj";
	// 密码
	private static final String LoginPwd = "182FE825DEB0437931A848578CE853CE";

	/**
	 * 批量发送短信
	 * 
	 * @param mobiles
	 *            多个手机号码, 以英文逗号分割, 不要超过50个
	 * @param msgText
	 *            短信内容
	 * @return 短信发送返回状态码: 00: 成功; 01: 账号或密码错误; 02: 已欠费; 09: 无效的接收方号码; 10:
	 *         网络传输故障，发生这种异常请再次重试提交 其他:上传的xml片断格式与定义不相符，请检查xml片断格式
	 */
	public static String sendSMS(String mobiles, String msgText) {
		String responseCode = null;
		try {
			String XMLStr = createXML(mobiles, msgText);// 获取body
			responseCode = HttpClientUtil.httpPost(Opration, XMLStr,
					"application/x-www-form-urlencoded", "GB2312");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseCode;
	}

	/**
	 * 获取账户余额
	 * 
	 * @return JSON文本
	 */
	public static String getBalance() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		try {
			/*
			 * <Root Service_Type="0"> <Item> <Account_Name> szyzkj
			 * </Account_Name><Account_Name> test2 </Account_Name> </Item>
			 * </Root>
			 */
			String xml = "<Root Service_Type='0'><Item><Account_Name>"
					+ LoginName + "</Account_Name></Item></Root>";
			String resultXML = HttpClientUtil.httpPost(GetResult, xml,
					"application/x-www-form-urlencoded", "GB2312");
			Document document = DocumentHelper.parseText(resultXML);
			Element group = document.getRootElement();// 获取根节点
			Element result = group.element("Result");
			String inBalance = result.elementTextTrim("InBalance");// 已花费的金额 单位厘
			String balance = result.elementTextTrim("Balance");// 账户的账户余额。
			double ib = Double.parseDouble(inBalance) * 0.001;
			double b = Double.parseDouble(balance) * 0.001;

			map.put("inBalance", ib);
			map.put("balance", b);
			map.put("msg", "inBalance:已花费的金额; balance: 此账户的账户余额");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return YZJSON.map2json(map);
	}

	/**
	 * 获取XML
	 * 
	 * @param mobiles
	 *            手机号, 逗号分隔, 不能超过50个
	 * @param msgText
	 *            短信内容
	 * @return 发送body
	 */
	private static String createXML(String mobiles, String msgText) {
		/*
		 * <Group Login_Name="账号" Login_Pwd=式"密码（MD5方加密）" OpKind="51"
		 * InterFaceID=""SerType=”短信类型”> <E_Time>发送时间(YYYY-MM-DD
		 * HI24:MM:SS)</E_Time> <Mobile>139xxxxxxxx,139xxxxxxxx</Mobile>
		 * <Content><![CDATA[短信内容]]></Content> <ClientID>任务流水号(调用方定义)</ClientID>
		 * </Group>
		 */
		String E_Time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
				.format(new Date());
		StringBuffer sp = new StringBuffer();
		sp.append(String.format("<Group Login_Name='%s' ", LoginName));
		sp.append(String.format(
				"Login_Pwd='%s' OpKind='51' InterFaceID='' SerType='消息类型'>",
				LoginPwd));
		sp.append(String.format("<E_Time>%s</E_Time>", E_Time));
		sp.append(String.format("<Mobile>%s</Mobile>", mobiles));
		sp.append(String.format("<Content>%s</Content>", msgText));
		sp.append(String.format("<ClientID>%s</ClientID>", "1000"));
		sp.append("</Group>");

		return sp.toString();
	}

	
	public static void main(String args[]) throws IOException {
		System.out.println(getBalance());
		System.out.println(sendSMS("15198003587" ,"欢迎使用云筑商会社交网络APP,点击以下链接下载http://114.215.201.200:8080/coc-mana"));
		}
}
