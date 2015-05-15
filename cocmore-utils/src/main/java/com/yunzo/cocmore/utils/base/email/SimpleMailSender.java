package com.yunzo.cocmore.utils.base.email;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

/**
 * 
 * @author Terry
 * @see 2014-5-9 云筑科技有限公司深圳研发中心 邮件（带附件的邮件）发送器
 * @version 1.0
 */
public class SimpleMailSender {
	private static Logger logger = Logger.getLogger(SimpleMailSender.class
			.getName());

	/**
	 * @author Terry
	 * @see 以文本格式发送邮件
	 * @param mailInfo
	 *            待发送的邮件的信息
	 * 
	 */
	public boolean sendTextMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		CustomAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new CustomAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * @author Terry
	 * @param mailInfo
	 *            待发送的邮件信息 mainPart 邮件容器
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @see 添加附件
	 * 
	 */
	public static Multipart addAnnex(MailSenderInfo mailInfo, Multipart mainPart)
			throws MessagingException, UnsupportedEncodingException {
		// 添加附件
		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(mailInfo.getAffix());
		// 添加附件的内容
		messageBodyPart.setDataHandler(new DataHandler(source));

		/********************* 添加附件的标题 *******************/
		// Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
		// BASE64Encoder enc = new BASE64Encoder();
		messageBodyPart.setFileName(MimeUtility.encodeText(
				mailInfo.getAffixName(), "UTF-8", "B"));
		logger.info("Annex transcoding success.");
		// 附件添加到容器
		mainPart.addBodyPart(messageBodyPart);
		logger.info("Annex added successfully.");
		return mainPart;
	}

	/**
	 * @see以HTML格式发送邮件
	 * @param mailInfo
	 *            待发送的邮件信息 isAddAnnex 是否添加附件
	 * @author Terry
	 * @throws UnsupportedEncodingException
	 */
	public static boolean sendHtmlMail(MailSenderInfo mailInfo,
			boolean isAddAnnex) throws UnsupportedEncodingException {
		boolean isAcess = false;
		// 判断是否需要身份认证
		CustomAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// //如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new CustomAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		// 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
		// 用（你可以在控制台（console)上看到发送邮件的过程）
		sendMailSession.setDebug(true);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();

			// 判断是否需要发送附件
			if (isAddAnnex) {
				// 添加附件
				mainPart = SimpleMailSender.addAnnex(mailInfo, mainPart);
			}

			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage, mailMessage.getAllRecipients());
			logger.info("Mail success.");
			isAcess = true;
		} catch (MessagingException ex) {
			logger.info(" sendHtmlMail Send mail failed:" + ex);
			isAcess = false;
		}
		return isAcess;
	}
}