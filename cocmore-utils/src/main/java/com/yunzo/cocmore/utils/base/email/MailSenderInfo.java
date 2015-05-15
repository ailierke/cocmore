package com.yunzo.cocmore.utils.base.email;

/**   
 * @see 云筑科技有限公司深圳研发中心 
 * 送邮件需要使用的基本信息 
 * @author Terry
 * @time 2014-5-9
 */
import java.util.Properties;

public class MailSenderInfo {
	// 发送邮件的服务器的IP和端口
	private String mailServerHost;
	private String mailServerPort = "465";
	// 邮件发送者的地址
	private String fromAddress;
	// 邮件接收者的地址
	private String toAddress;
	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
	// 是否需要身份验证
	private boolean validate = false;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	private String content;
	// 邮件附件的文件名
	private String[] attachFileNames;
	// 附件地址
	private String affix;
	// 附件名称
	private String affixName;

	/**
	 * 获得邮件会话属性
	 */
	public Properties getProperties() {
		Properties p = new Properties();
		// Properties p = System.getProperties();
		p.put("mail.smtp.host", this.mailServerHost);
		p.put("mail.transport.protocol", "smtp");
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.auth", validate ? "true" : "false");
		p.put("mail.smtp.socketFactory.port", this.mailServerPort);
		p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		p.put("mail.smtp.socketFactory.fallback", "false");
		p.setProperty("mail.imap.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		p.setProperty("mail.imap.socketFactory.fallback", "false");
		p.setProperty("mail.imap.port", "993");
		p.setProperty("mail.imap.socketFactory.port", "993");
		return p;
	}

	public String getAffix() {
		return affix;
	}

	public void settAffix(String affix) {
		this.affix = affix;
	}

	public String getAffixName() {
		return affixName;
	}

	public void setAffixName(String affixName) {
		this.affixName = affixName;
	}

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String[] getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(String[] fileNames) {
		this.attachFileNames = fileNames;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String textContent) {
		this.content = textContent;
	}
}